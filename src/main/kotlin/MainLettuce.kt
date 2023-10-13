import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    /* Connect to your Redis server using Lettuce */
    val redisClient = RedisClient.create("redis://localhost:6379")
    val connection: StatefulRedisConnection<String, String> = redisClient.connect()

    println("Connected to Redis server")

    val numberOfRecords = 1_000_000
    val dataHelper = DataHelper(numberOfRecords)
    println("Loading Redis database with $numberOfRecords records")

    val loadElapsedTime = measureTimeNanos {
        val commands = connection.sync()
        (1..numberOfRecords).asFlow().collect { i ->
            val key = dataHelper.generateRandomCustomerIdString()
            val value = dataHelper.generateRandomEmailHash("some_user@domain.com").toString()
            commands.set(key, value)
        }
    }

    println("Loaded Redis with $numberOfRecords records. Elapsed time: ${loadElapsedTime / 1_000_000} ms.")
    println("Average write time ${loadElapsedTime / numberOfRecords} ns.")

    val step = 5
    val numRecordsToRead = numberOfRecords / step
    println("Reading $numRecordsToRead records...")

    val readElapsedTime = measureTimeNanos {
        val commands = connection.sync()
        (1..numRecordsToRead step step).asFlow().collect { i ->
            val key = i.toString()
            val readValue = commands.get(key)
        }
    }

    println("Read $numRecordsToRead records. Elapsed time: ${readElapsedTime / 1_000_000} ms")
    println("Average entry read time: ${readElapsedTime / numRecordsToRead} ns")

    // Use the INFO command to get server statistics
    val commands = connection.sync()
    val info = commands.info()

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

    connection.close()
    redisClient.shutdown()

    println("Done")
}
