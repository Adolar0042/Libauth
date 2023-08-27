package dev.adolar0042.libauth.utility.alogrithm

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.time.Instant
import kotlin.experimental.and
import kotlin.math.pow

class TOTP(private val secretKey: ByteArray, private val timeStep: Long = 30, private val digits: Int = 6) {

    private fun hmacSha1(key: ByteArray, data: ByteArray): ByteArray {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(SecretKeySpec(key, "HmacSHA1"))
        return hmac.doFinal(data)
    }

    private fun generateTOTP(key: ByteArray, timestamp: Long): Int {
        val timeSteps = timestamp / timeStep
        val counter = ByteArray(8)
        for (i in 0 until 8) {
            counter[7 - i] = (timeSteps ushr (i * 8)).toByte()
        }
        val hmacResult = hmacSha1(key, counter)
        val offset = hmacResult[hmacResult.size - 1] and 0xf.toByte()
        val truncatedHash = ((hmacResult[offset.toInt()] and 0x7f.toByte()).toInt() shl 24) or
                ((hmacResult[offset.toInt() + 1] and 0xff.toByte()).toInt() shl 16) or
                ((hmacResult[offset.toInt() + 2] and 0xff.toByte()).toInt() shl 8) or
                (hmacResult[offset.toInt() + 3] and 0xff.toByte()).toInt()
        return truncatedHash % (10.0.pow(digits.toDouble())).toInt()
    }

    fun generateCode(): String {
        val currentTime = System.currentTimeMillis()
        val code = generateTOTP(secretKey, currentTime)
        return "%0${digits}d".format(code)
    }
}

fun main() {
    val secretKey = "YOURSECRETKEYHERE".toByteArray()
    val totp = TOTP(secretKey)
    val code = totp.generateCode()
    println("Generated TOTP code: $code")
}
