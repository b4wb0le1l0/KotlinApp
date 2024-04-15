package commands

import requestManager
import utility.CommandResult

class Exit: AbstractCommand("exit", "завершает программу (без сохранения в файл)") {

    override fun execute(str: String): CommandResult {
        return requestManager.exitClientRequest(str)
    }
}