package commands

import requestManager
import utility.CommandResult

class History: AbstractCommand("history", "выводит последние 14 команд(без их аргументов)") {

    override fun execute(str: String): CommandResult {
        return requestManager.historyRequest(str)
    }
}