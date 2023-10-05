import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val client = newClient(Endpoint.from("localhost:6379"))

    println("Connected to Redis server")

    val numberOfRecords = 1_000_000
    val dataHelper = DataHelper(numberOfRecords)
    println("Loading Redis database with $numberOfRecords records")

//    val threadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val threadDispatcher = Dispatchers.Default

    // Load records into Redis
    val loadElapsedTime = measureTimeNanos {
        launch(threadDispatcher) {
            for (i in 0..<numberOfRecords) {
                val key = dataHelper.generateRandomCustomerIdString()
                val value = dataHelper.generateRandomEmailHash("some_user@domain.com").toString()
                client.set(key, value)
            }
        }.join()
    }

    println("Loaded Redis with $numberOfRecords records. Elapsed time: ${loadElapsedTime / 1_000_000} ms.")
    println("Average write time ${loadElapsedTime / numberOfRecords} ns")

    // Read records from Redis
    val step = 5
    val numRecordsToRead = numberOfRecords / step
    println("Reading $numRecordsToRead records...")

    // Use the same singleThreadDispatcher for reading
    val readElapsedTime = measureTimeNanos {
        launch(threadDispatcher) {
            for (i in 1 until numRecordsToRead step step) {
                val key = i.toString()
                val readValue = client.get(key)
            }
        }.join()
    }

    println("Read $numRecordsToRead records. Elapsed time: ${readElapsedTime / 1_000_000} ms")
    println("Average entry read time: ${readElapsedTime / numRecordsToRead} ns")

    println("Done")
}
