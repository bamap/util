package ir.bamap.blu.util

import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

open class BluEncryptor(
    protected open val password: String = "blu.bamap.ir",
    protected open val salt: String = RandomUtil.nextString(8),
    protected open val separator: String = "|"
) {

    protected open val secretKey: SecretKeySpec by lazy { initSecretKey() }
    protected open val ivParameterSpec: IvParameterSpec by lazy { initIvParameterSpec() }
    protected open val encryptedCipher: Cipher by lazy { initCipher(secretKey, Cipher.ENCRYPT_MODE, ivParameterSpec) }
    protected open val decryptedCipher: Cipher by lazy { initCipher(secretKey, Cipher.DECRYPT_MODE, ivParameterSpec) }

    open fun encrypt(text: Any): String = encrypt(listOf(text))

    open fun encrypt(entries: Collection<Any>): String {
        val byteArray = entries.joinToString(separator).toByteArray()
        return Base64.getEncoder().encodeToString(encryptedCipher.doFinal(byteArray))
    }

    open fun decrypt(encryptedText: String): List<String> {
        val base64 = Base64.getDecoder().decode(encryptedText)
        return String(decryptedCipher.doFinal(base64)).split(separator)
    }

    protected open fun initIvParameterSpec(): IvParameterSpec {
        val iv = ByteArray(16)
        for (i in 0..15)
            iv[i] = 1
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    protected open fun initSecretKey(): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    protected open fun initCipher(secretKey: SecretKeySpec, operationMode: Int, iv: IvParameterSpec): Cipher {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(operationMode, secretKey, iv)

        return cipher
    }
}
