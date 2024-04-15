package utility

import java.io.Serializable

data class CommandResult(
    val commandComplicated: StateOfResponse,
    val message: String? = null,
    var serverToken: String? = null
) : Serializable