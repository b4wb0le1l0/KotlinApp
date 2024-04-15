package commands

import requestManager
import utility.CommandResult

class Save: AbstractCommand("save", "сохраняет коллекцию в файл") {

    override fun execute(str: String): CommandResult {
        return requestManager.saveRequest(str)
    }
}