import redis.clients.jedis.Jedis

fun main() {
    /* Connect to your Redis server */
    val jedis = Jedis("localhost", 6379)

    println("Connected to Redis server")

    val numberOfRecords = 25_000_000
    val dataHelper = DataHelper(numberOfRecords)
    println("Loading Redis database with $numberOfRecords records")

    val loadElapsedTime = measureTimeNanos {
        for (i in 0 until numberOfRecords) {
            val key = compressData(dataHelper.generateRandomCustomerIdString())
            val value = compressData(dataHelper.generateRandomEmailHash("some_user@domain.com").toString())
            jedis.set(key, value)
        }
    }

    println("Loaded Redis with $numberOfRecords records. Elapsed time: ${loadElapsedTime / 1_000_000} ms.")
    println("Average write time ${loadElapsedTime / numberOfRecords} ns.")

    val step = 5
    val numRecordsToRead = numberOfRecords / step
    println("Reading $numRecordsToRead records...")

    val readElapsedTime = measureTimeNanos {
        for (i in 1 until numRecordsToRead step step) {
            val key = compressData(i.toString())
            val readValue = decompressData(jedis.get(key))
        }
    }

    println("Read $numRecordsToRead records. Elapsed time: ${readElapsedTime / 1_000_000} ms")
    println("Average entry read time: ${readElapsedTime / numRecordsToRead} ns")

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
