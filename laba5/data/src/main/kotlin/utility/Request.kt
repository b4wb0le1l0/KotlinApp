package utility

import java.io.Serializable

data class Request(
    val commandName: String,
    val commandArguments: String,
    val serverToken: String? = null
) : Serializable