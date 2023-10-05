import com.github.luben.zstd.ZstdInputStream
import com.github.luben.zstd.ZstdOutputStream
import com.google.common.hash.HashCode
import com.google.common.hash.Hashing.sha256
import net.openhft.chronicle.core.values.ByteValue
import net.openhft.chronicle.core.values.IntValue
import net.openhft.chronicle.map.ChronicleMap
import net.openhft.chronicle.values.Values
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

class DataHelper(private val numberOfRecords: Int) {

    private var index = 0

    fun generateRandomCustomerId(): IntValue {
        val randomId = index++
        return Values.newHeapInstance(IntValue::class.java).apply {
            value = randomId
        }
    }

    fun generateRandomCustomerIdByte(): ByteValue {
        val randomId = index++
        return Values.newHeapInstance(ByteValue::class.java).apply {
            value = randomId.toByte()
        }
    }

    fun generateCostumerId(idx: Int): ByteValue {
        return Values.newHeapInstance(ByteValue::class.java).apply {
            value = idx.toByte()
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

    fun createChronicleMap(): ChronicleMap<ByteValue, ByteArray> {
        val keySize = 2
        val valueSize = generateRandomEmailHash("some_user@domain.com").asBytes().size
        val overhead = 13
        val estimatedTotalSize = (keySize + valueSize + overhead) * numberOfRecords / (1024 * 1024)

        println("Creating ChronicleMap, estimated size: ${estimatedTotalSize.toInt()} MB")

        return ChronicleMap
            .of(ByteValue::class.java, ByteArray::class.java)
            .entries(numberOfRecords.toLong())
            .constantKeySizeBySample(generateRandomCustomerIdByte())
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

fun compressData(input: String): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val zstdOutputStream = ZstdOutputStream(outputStream)
    zstdOutputStream.write(input.toByteArray(Charsets.UTF_8))
    zstdOutputStream.close()
    return outputStream.toByteArray()
}

fun decompressData(input: ByteArray): String {
    val inputStream = ByteArrayInputStream(input)
    val zstdInputStream = ZstdInputStream(inputStream)
    val byteArray = zstdInputStream.readBytes()
    return String(byteArray, Charsets.UTF_8)
}
