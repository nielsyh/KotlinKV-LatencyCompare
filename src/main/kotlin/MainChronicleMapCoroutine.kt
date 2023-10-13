import kotlinx.coroutines.*
import net.openhft.chronicle.core.values.ByteValue
import net.openhft.chronicle.map.ChronicleMap
import net.openhft.chronicle.values.Values
import java.lang.Integer.min

suspend fun write(chronicleMap: ChronicleMap<ByteValue, ByteArray>, keys: List<ByteValue>, values: List<ByteArray>) {
//    println("Started on ${Thread.currentThread().name}")
    keys.forEachIndexed { index, key ->
        chronicleMap[key] = values[index]
    }
}

suspend fun read(chronicleMap: ChronicleMap<ByteValue, ByteArray>, keys: List<ByteValue>): List<ByteArray?> {
    return keys.map { key -> chronicleMap[key] }
}

fun main(args: Array<String>) = runBlocking(Dispatchers.Default) {
    val numberOfRecords = 25_000_000
//    val batchSizes = intArrayOf(1000)
    val batchSizes = intArrayOf(50, 100, 500, 1000, 5000)

    for (batchSize in batchSizes) {
        println("Loading Chronicle Map of size: $numberOfRecords, with BatchSize $batchSize")

        val dataHelper = DataHelper(numberOfRecords)
        val chronicleMap = dataHelper.createChronicleMap()

        val jobs = List(numberOfRecords / batchSize) {
            val keys = (0..<batchSize).map { dataHelper.generateRandomCustomerIdByte() }
            val values = (0..<batchSize).map { dataHelper.generateRandomEmailHash("user_name@somedomain.com").asBytes() }

            launch {
                write(chronicleMap, keys, values)
            }
        }

        val loadElapsedTime = measureTimeNanos {
            jobs.forEach { it.join() }
        }

        val memoryFootprint = chronicleMap.offHeapMemoryUsed() / (1024 * 1024)
        println("Loaded Chronicle Map with $numberOfRecords entries. Elapsed time: ${loadElapsedTime / 1_000_000} ms, Memory footprint: $memoryFootprint MB")
        println("Average write time ${loadElapsedTime.toDouble() / numberOfRecords} ns")

        println("Reading $numberOfRecords records...")

        val readJobs = (0..<numberOfRecords step batchSize).map { startIndex ->
            val endIndex = min(startIndex + batchSize, numberOfRecords)
            val readKeys = (startIndex..<endIndex).map { i ->
                Values.newHeapInstance(ByteValue::class.java).apply {
                    value = i.toByte()
                }
            }
            async {
                val readValues = read(chronicleMap, readKeys)
                readValues
            }
        }

        val readElapsedTime = measureTimeNanos {
            readJobs.awaitAll()
        }

        println("Read $numberOfRecords records. Elapsed time: ${readElapsedTime / 1_000_000} ms")
        println("Average entry read time: ${readElapsedTime / numberOfRecords} ns")

        chronicleMap.close()
        println("Done with BatchSize $batchSize")
    }
}
