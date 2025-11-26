package com.example.app_basic.utils

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    /**
     * Mã hóa mật khẩu bằng PBKDF2
     */
    fun hashPassword(password: String): String {
        // Tạo salt ngẫu nhiên (64 bytes)
        val random = SecureRandom()
        val salt = ByteArray(64)
        random.nextBytes(salt)

        // Mã hóa bằng PBKDF2WithHmacSHA256
        val hash = pbkdf2(password.toCharArray(), salt, 10000, 256)

        // Kết hợp salt + hash (để verify sau này)
        return toHex(salt) + ":" + toHex(hash)
    }

    /**
     * Kiểm tra mật khẩu có khớp với hash không
     * @param password Mật khẩu người dùng nhập
     * @param storedHash Hash đã lưu trong database
     * @return true nếu khớp, false nếu không
     */
    fun verifyPassword(password: String, storedHash: String): Boolean {
        return try {
            // Tách salt và hash
            val parts = storedHash.split(":")
            if (parts.size != 2) return false

            val salt = fromHex(parts[0])
            val hash = fromHex(parts[1])

            // Hash lại password với salt cũ
            val testHash = pbkdf2(password.toCharArray(), salt, 10000, 256)

            // So sánh constant-time (chống timing attack)
            hash.contentEquals(testHash)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * PBKDF2 với HmacSHA256
     */
    private fun pbkdf2(
        password: CharArray,
        salt: ByteArray,
        iterations: Int,
        keyLength: Int
    ): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    /**
     * Chuyển ByteArray sang Hex string
     */
    private fun toHex(array: ByteArray): String {
        return array.joinToString("") { "%02x".format(it) }
    }

    /**
     * Chuyển Hex string sang ByteArray
     */
    private fun fromHex(hex: String): ByteArray {
        return hex.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }

    /**
     * Hash đơn giản bằng SHA-256
     */
    @Deprecated("Dùng hashPassword() thay vì SHA-256 đơn giản")
    fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}