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

    val numberOfRecords = 10_000
    val dataHelper = DataHelper(numberOfRecords)

    val batchSizes = intArrayOf(50, 100, 500, 1000, 5000)
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
            println("Loaded Redis with $numberOfRecords records. Elapsed time: ${loadElapsedTime/ 1_000_000} ms.")
            println("Average write time ${loadElapsedTime / numberOfRecords} ns.")
        }

        println("Reading $numberOfRecords records...")

        runBlocking {
            val readJobs = (0 until numberOfRecords step batchSize).map { startIndex ->
                val endIndex = startIndex + batchSize.coerceAtMost(numberOfRecords)
                val readKeys = (startIndex until endIndex).map { i -> compressData(i.toString()) }
                async {
                    val readValues = readJedis(jedis, readKeys)
                    readValues
                }
            }

            val readElapsedTime = measureTimeNanos {
                readJobs.awaitAll()
            }

            println("Read $numberOfRecords records. Elapsed time: ${readElapsedTime/1_000_000} ms")
            println("Average entry read time: ${readElapsedTime / numberOfRecords} ns")

            println("Done with BatchSize $batchSize")
        }

        jedis.close()

        println("Done")
    }
}
