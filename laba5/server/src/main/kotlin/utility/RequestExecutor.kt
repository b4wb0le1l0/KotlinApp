package utility

import commandExecuter
import java.util.concurrent.Callable

class RequestExecutor(private val request: Request,
                      private val authManager: AuthManager
): Callable<CommandResult> {

    override fun call(): CommandResult? {
        val commandName = request.commandName
        val commandArguments = request.commandArguments
        val serverToken = request.serverToken
        if (authManager.tokenExist(request.serverToken)) {
            return commandExecuter.executeCommand(commandName, (commandArguments + " " + serverToken?.substringBefore(".")).trim())
        }
        if (commandName == "auth" || commandName == "login") {
            return commandExecuter.executeCommand(commandName, "$commandArguments $serverToken".trim())
        }
        return CommandResult(StateOfResponse.ERROR, "Для работы требуется авторизация")
    }
}