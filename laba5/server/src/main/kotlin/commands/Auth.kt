package commands

import authManager
import utility.CommandResult

class Auth: AbstractCommand("auth", "зарегистрировать пользователя") {

    override fun execute(str: String): CommandResult {
        return authManager.registerUser(str)
    }
}