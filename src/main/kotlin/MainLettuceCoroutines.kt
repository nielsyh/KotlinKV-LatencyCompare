import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import kotlinx.coroutines.*
import redis.clients.jedis.exceptions.JedisConnectionException

suspend fun writeLettuce(redisCommands: RedisCommands<String, String>, keys: List<String>, values: List<String>) {
    keys.forEachIndexed { index, key ->
        redisCommands.set(key, values[index])
    }
}

suspend fun readLettuce(redisCommands: RedisCommands<String, String>, keys: List<String>): List<String?> {
    return keys.map { key ->
        val value = redisCommands.get(key)
        value
    }
}

fun main() {
    val redisClient = RedisClient.create("redis://localhost:6379")
    val connection: StatefulRedisConnection<String, String> = redisClient.connect()
    val redisCommands: RedisCommands<String, String> = connection.sync()

    println("Connected to Redis server")

    val numberOfRecords = 25_000_000
    val dataHelper = DataHelper(numberOfRecords)

    val batchSizes = intArrayOf(50)
    for (batchSize in batchSizes) {
        println("Loading Redis database with $numberOfRecords records with batchSize: $batchSize")
        runBlocking(Dispatchers.IO) {
            val jobs = List(numberOfRecords / batchSize) {
                val keys = (0 until batchSize).map { i -> dataHelper.generateRandomCustomerIdString() }
                val values = (0 until batchSize).map { i -> dataHelper.generateRandomEmailHash("some_user@domain.com").toString() }
                launch {
                    writeLettuce(redisCommands, keys, values)
                }
            }

            val loadElapsedTime: Long = measureTimeNanos {
                jobs.forEach { it.join() }
            }
            println("Loaded Redis with $numberOfRecords records. Elapsed time: ${loadElapsedTime / 1_000_000} ms.")
            println("Average write time ${loadElapsedTime / numberOfRecords} ns.")

            println("Reading $numberOfRecords records...")

            val readJobs = (0 until numberOfRecords step batchSize).map { startIndex ->
                val endIndex = startIndex + batchSize.coerceAtMost(numberOfRecords)
                val readKeys = (startIndex until endIndex).map { i -> i.toString() }
                async {
                    val readValues = readLettuce(redisCommands, readKeys)
                    readValues
                }
            }

            val readElapsedTime = measureTimeNanos {
                readJobs.awaitAll()
            }

            println("Read $numberOfRecords records. Elapsed time: ${readElapsedTime / 1_000_000} ms")
            println("Average entry read time: ${readElapsedTime / numberOfRecords} ns")

            println("Done with BatchSize $batchSize")
        }

        // Use the INFO command to get server statistics
        try {
            val info = redisCommands.info("memory")

            // Initialize a variable to store used_memory_human
            var usedMemoryHuman: String? = null

            // Parse the INFO output
            val infoLines = info.split("\r\n")
            for (line in infoLines) {
                if (line.startsWith("used_memory_human:")) {
                    val parts = line.split(":")
                    if (parts.size == 2) {
                        usedMemoryHuman = parts[1].trim()
                        break
                    }
                }
            }

            if (usedMemoryHuman != null) {
                println("Used Memory: $usedMemoryHuman")
            } else {
                println("Used Memory not found in INFO output.")
            }
        } catch (e: JedisConnectionException) {
            println("Redis connection exception: ${e.message}")
        }

        println("Done")
    }

    connection.close()
    redisClient.shutdown()
}
