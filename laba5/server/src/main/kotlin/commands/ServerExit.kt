package commands

import requestManager
import utility.CommandResult
import utility.StateOfResponse

class ServerExit: AbstractCommand("server_exit", "выключает серверный модуль") {

    override fun execute(str: String): CommandResult {
        requestManager.exitServerRequest(str)
        return CommandResult(StateOfResponse.GOOD)
    }
}