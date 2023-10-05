import kotlinx.coroutines.*
import net.openhft.chronicle.core.values.ByteValue
import net.openhft.chronicle.values.Values

fun main(args: Array<String>) = runBlocking {
    val numberOfRecords = 25_000_000

    val BatchSize = intArrayOf(1, 5, 20, 50, 100, 500, 1000, 5000, 10_000)
    for (i in BatchSize) {
        println("Loading Chronicle Map of size: $numberOfRecords, with BatchSize $i")

        val dataHelper = DataHelper(numberOfRecords)
        val chronicleMap = dataHelper.createChronicleMap()
        val loadElapsedTime = measureTimeNanos {
            val jobs = (0..<numberOfRecords step i).map { start ->
                launch(Dispatchers.IO) {
                    for (i in start..<minOf(start + i, numberOfRecords)) {
                        val key = dataHelper.generateRandomCustomerIdByte()
                        chronicleMap[key] = dataHelper.generateRandomEmailHash("user_name@somedomain.com").asBytes()
                    }
                }
            }
            jobs.forEach { it.join() }
        }

        val memoryFootprint = chronicleMap.offHeapMemoryUsed() / (1024 * 1024)
        println("Loaded Chronicle Map with $numberOfRecords entries. Elapsed time: ${loadElapsedTime / 1_000_000} ms, Memory footprint: $memoryFootprint MB")
        println("Average write time ${loadElapsedTime / numberOfRecords} ns")

        val step = 5
        val numRecordsToRead = numberOfRecords / step
        println("Reading $numRecordsToRead records...")

        val readElapsedTime = measureTimeNanos {
            val jobs = (0..<numRecordsToRead step i).map { start ->
                launch(Dispatchers.IO) {
                    for (i in start..<minOf(start + i, numRecordsToRead)) {
                        val k = Values.newHeapInstance(ByteValue::class.java).apply {
                            value = (i * step).toByte()
                        }
                        val readValue = chronicleMap[k]
                    }
                }
            }
            jobs.forEach { it.join() }
        }

        println("Read $numRecordsToRead records. Elapsed time: ${readElapsedTime / 1_000_000} ms")
        println("Average entry read time: ${readElapsedTime / numRecordsToRead} ns")

        chronicleMap.close()
        println("Done with $i")
    }
}
