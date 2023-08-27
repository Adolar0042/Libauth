import java.lang.reflect.UndeclaredThrowableException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * This is an implementation of the OATH TOTP algorithm.
 * Visit www.openauthentication.org for more information.
 *
 * Original Java code by
 * @author Johan Rydell, PortWise, Inc.
 * Converted to Kotlin and adjusted by
 * @author Adolar0042, Some Guy on the Internet
 * @author Mr Toby, Some Guy on the Internet
 */
object TOTP {
    private const val DEFAULT_TIME_STEP_SECONDS = 30L
    private const val DEFAULT_RETURN_DIGITS = 6
    private const val DEFAULT_UNIX_EPOCH = 0L

    fun String.toHex(): String {
        val result = StringBuilder()

        for (char in this.toCharArray()) {
            val hex = Integer.toHexString(char.toInt())
            result.append(hex)
        }

        return result.toString()
    }

    // Seed for HMAC-SHA1 - 20 bytes
    private val exampleSeed = "YOURSECRETKEYHERE".toHex()//"3132333435363738393031323334353637383930"

    // Seed for HMAC-SHA256 - 32 bytes
    private const val exampleSeed32 =
        "3132333435363738393031323334353637383930" + "313233343536373839303132"

    // Seed for HMAC-SHA512 - 64 bytes
    private const val exampleSeed64 =
        "3132333435363738393031323334353637383930" + "3132333435363738393031323334353637383930" + "3132333435363738393031323334353637383930" + "31323334"

    enum class Crypto {
        HmacSHA1,
        HmacSHA256,
        HmacSHA512
    }

    /**
     * This method uses the JCE to provide the crypto algorithm.
     * HMAC computes a Hashed Message Authentication Code with the
     * crypto hash algorithm as a parameter.
     *
     * @param crypto: the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes: the bytes to use for the HMAC key
     * @param text: the message or text to be authenticated
     */
    private fun hmacSHA(
        crypto: String, keyBytes: ByteArray, text: ByteArray
    ): ByteArray {
        return try {
            val hmac: Mac = Mac.getInstance(crypto)
            val macKey = SecretKeySpec(keyBytes, "RAW")
            hmac.init(macKey)
            hmac.doFinal(text)
        } catch (gse: GeneralSecurityException) {
            throw UndeclaredThrowableException(gse)
        }
    }

    /**
     * This method converts a HEX string to Byte[]
     *
     * @param hex: the HEX string
     *
     * @return: a byte array
     */
    private fun hexStr2Bytes(hex: String): ByteArray {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        val bArray = BigInteger("10$hex", 16).toByteArray()

        // Copy all the REAL bytes, not the "first"
        val ret = ByteArray(bArray.size - 1)
        for (i in ret.indices) ret[i] = bArray[i + 1]
        return ret
    }

    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key: the shared secret, HEX encoded
     * @param time: a value that reflects a time
     * @param returnDigits: number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     * [truncationDigits] digits
     */
    @JvmOverloads
    fun generateTOTP(
        key: String, time: String, returnDigits: Int = 6, crypto: Crypto = Crypto.HmacSHA1
    ): String {
        var time = time
        var result: String? = null

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length < 16) time = "0$time"

        // Get the HEX in a Byte[]
        val msg = hexStr2Bytes(time)
        val k = hexStr2Bytes(key)
        val hash = hmacSHA(crypto.name, k, msg)

        // put selected bytes into result int
        val offset = hash[hash.size - 1].toInt() and 0xf
        val binary =
            hash[offset].toInt() and 0x7f shl 24 or (hash[offset + 1].toInt() and 0xff shl 16) or (hash[offset + 2].toInt() and 0xff shl 8) or (hash[offset + 3].toInt() and 0xff)
        val otp = binary % 10.toDouble().pow(returnDigits.toDouble())
        result = otp.toInt().toString()
        while (result!!.length < returnDigits) {
            result = "0$result"
        }
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val T0: Long = DEFAULT_UNIX_EPOCH
        val X: Long = DEFAULT_TIME_STEP_SECONDS
        val returnDigits = DEFAULT_RETURN_DIGITS

        val testTime = longArrayOf(
            //59L,
            //1111111109L,
            //1111111111L,
            //1234567890L,
            //2000000000L,
            //20000000000L,
            //20000000000000000L,
            System.currentTimeMillis() / 1000L
        )

        var steps: String
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df.timeZone = TimeZone.getTimeZone("UTC")
        try {
            println(
                "+---------------+-----------------------+" + "------------------+--------+--------+"
            )
            println(
                "|  Time(sec)    |   Time (UTC format)   " + "| Value of T(Hex)  |  TOTP  | Mode   |"
            )
            println(
                "+---------------+-----------------------+" + "------------------+--------+--------+"
            )
            for (i in testTime.indices) {
                val T = (testTime[i] - T0) / X
                steps = java.lang.Long.toHexString(T).uppercase(Locale.getDefault())
                while (steps.length < 16) steps = "0$steps"
                val fmtTime = String.format("%1$-11s", testTime[i])
                val utcTime = df.format(Date(testTime[i] * 1000))
                print(
                    "|  $fmtTime  |  $utcTime  | $steps |"
                )
                println(
                    generateTOTP(
                        exampleSeed, steps, returnDigits, Crypto.HmacSHA1
                    ) + "| SHA1   |"
                )
                print(
                    "|  $fmtTime  |  $utcTime  | $steps |"
                )
                println(
                    generateTOTP(
                        exampleSeed32, steps, returnDigits, Crypto.HmacSHA256
                    ) + "| SHA256 |"
                )
                print(
                    "|  $fmtTime  |  $utcTime  | $steps |"
                )
                println(
                    generateTOTP(
                        exampleSeed64, steps, returnDigits, Crypto.HmacSHA512
                    ) + "| SHA512 |"
                )
                println(
                    "+---------------+-----------------------+" + "------------------+--------+--------+"
                )
            }
        } catch (e: Exception) {
            println("Error : $e")
        }
    }
}