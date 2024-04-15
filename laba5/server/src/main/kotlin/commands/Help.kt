package commands

import requestManager
import utility.CommandResult

class Help: AbstractCommand("help", "выводит справку по доступным командам") {

    override fun execute(str: String): CommandResult {
        return requestManager.helpRequest(str)
    }
}