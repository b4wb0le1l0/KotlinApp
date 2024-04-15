package commands

import requestManager
import utility.CommandResult

class Update: AbstractCommand("update id {element}", "обновляет значение элемента коллекции, id которого равен заданному"){

    override fun execute(str: String): CommandResult {
        return requestManager.updateByIdRequest(str)
    }
}