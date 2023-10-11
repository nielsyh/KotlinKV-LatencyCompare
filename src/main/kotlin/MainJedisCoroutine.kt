import kotlinx.coroutines.*
import redis.clients.jedis.Jedis

suspend fun writeJedis(jedis: Jedis, keys: List<ByteArray>, values: List<ByteArray>) {
    keys.forEachIndexed { index, key ->
        jedis.set(key, values[index])
    }
}

suspend fun readJedis(jedis: Jedis, keys: List<ByteArray>): List<ByteArray?> {
    return keys.map { key ->
        jedis.get(key)
    }
}

fun main() {
    /* Connect to your Redis server */
    val jedis = Jedis("localhost", 6379)

    println("Connected to Redis server")

    val numberOfRecords = 25_000_000
    val dataHelper = DataHelper(numberOfRecords)

    val batchSizes = intArrayOf(1000)
    for (batchSize in batchSizes) {
        println("Loading Redis database with $numberOfRecords records with batchSize: $batchSize")
        runBlocking {
            val jobs = List(numberOfRecords / batchSize) {
                val keys = (0..<batchSize).map { i -> compressData(dataHelper.generateRandomCustomerIdString()) }
                val values = (0..<batchSize).map { i -> compressData(dataHelper.generateRandomEmailHash("some_user@domain.com").toString()) }
                launch {
                    writeJedis(jedis, keys, values)
                }
            }

            val loadElapsedTime: Long = measureTimeNanos {
                jobs.forEach { it.join() }
            }
            println("Loaded Redis with $numberOfRecords records. Elapsed time: ${loadElapsedTime / 1_000_000} ms.")
            println("Average write time ${loadElapsedTime / numberOfRecords} ns.")
        }

        println("Reading $numberOfRecords records...")

        runBlocking {
            val readJobs = (0..<numberOfRecords step batchSize).map { startIndex ->
                val endIndex = startIndex + batchSize.coerceAtMost(numberOfRecords)
                val readKeys = (startIndex..<endIndex).map { i -> compressData(i.toString()) }
                async {
                    val readValues = readJedis(jedis, readKeys)
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
        val info = jedis.info()

        // Split the output into lines
        val lines = info.split("\r\n")

        // Initialize a variable to store used_memory_human
        var usedMemoryHuman: String? = null

        // Iterate through lines to find used_memory_human
        for (line in lines) {
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

        jedis.close()

        println("Done")
    }
}
