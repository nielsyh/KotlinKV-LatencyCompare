import net.openhft.chronicle.core.values.IntValue
import net.openhft.chronicle.values.Values

fun main(args: Array<String>) {
    val numberOfRecords = 25_000_000
    println("Loading Chronicle Map of size: $numberOfRecords")

    val dataHelper = DataHelper(numberOfRecords)
    val chronicleMap = dataHelper.createChronicleMap()

    val loadElapsedTime = measureTimeNanos {
        repeat(numberOfRecords) {
            val key = dataHelper.generateRandomCustomerId()
            chronicleMap[key] = dataHelper.generateRandomEmailHash("user_name@somedomain.com").asBytes()
        }
    }

    val memoryFootprint = chronicleMap.offHeapMemoryUsed() / (1024 * 1024)
    println("Loaded Chronicle Map with $numberOfRecords entries. Elapsed time: ${loadElapsedTime / 1_000_000} ms, Memory footprint: $memoryFootprint MB")
    println("Average write time ${loadElapsedTime/numberOfRecords} ns")

    val step = 5
    val numRecordsToRead = numberOfRecords / step
    println("Reading $numRecordsToRead records...")

    val readElapsedTime = measureTimeNanos {
        repeat(numRecordsToRead) { i ->
            val k = Values.newHeapInstance(IntValue::class.java).apply {
                value = i * step
            }
            val readValue = chronicleMap[k]
        }
    }

    println("Read $numRecordsToRead records. Elapsed time: ${readElapsedTime/1_000_000} ms")
    println("Average entry read time: ${readElapsedTime / numRecordsToRead} ns")

    chronicleMap.close()
    println("Done")
}

