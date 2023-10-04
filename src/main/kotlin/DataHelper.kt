import com.google.common.hash.HashCode
import com.google.common.hash.Hashing.sha256
import net.openhft.chronicle.core.values.IntValue
import net.openhft.chronicle.map.ChronicleMap
import net.openhft.chronicle.values.Values
import java.nio.charset.StandardCharsets

class DataHelper(private val numberOfRecords: Int) {

    private var index = 0

    fun generateRandomCustomerId(): IntValue {
        val randomId = index++
        return Values.newHeapInstance(IntValue::class.java).apply {
            value = randomId
        }
    }

    fun generateRandomCustomerIdString(): String {
        val randomId = index++
        return randomId.toString()
    }


    fun generateRandomEmailHash(email: String): HashCode {
        return sha256()
            .hashString(email, StandardCharsets.UTF_8)
    }

    fun createChronicleMap(): ChronicleMap<IntValue, ByteArray> {

        val keySize = 2
        val valueSize = generateRandomEmailHash("some_user@domain.com").asBytes().size
        val overhead = 13
        val estimatedTotalSize = ((keySize + valueSize + overhead) * numberOfRecords) / (1024 * 1024)

        println("Creating ChronicleMap, estimated size: ${estimatedTotalSize.toInt()} MB")

        return ChronicleMap
            .of(IntValue::class.java, ByteArray::class.java)
            .entries(numberOfRecords.toLong())
            .constantKeySizeBySample(generateRandomCustomerId())
            .constantValueSizeBySample(generateRandomEmailHash("some_user@domain.com").asBytes())
            .create()
    }
}

inline fun measureTimeNanos(block: () -> Unit): Long {
    val startTime = System.nanoTime()
    block()
    val endTime = System.nanoTime()
    return endTime - startTime
}
