package utility

import java.security.MessageDigest

class PasswordHasher {

    fun encryptStringToSHA384(str: String): String {
        val bytes = str.toByteArray()
        val md = MessageDigest.getInstance("SHA-384")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}