package commands

import requestManager
import utility.CommandResult

class Clear: AbstractCommand("clear", "очищает коллекцию") {

    override fun execute(str: String): CommandResult {
        return requestManager.clearRequest(str)
    }
}