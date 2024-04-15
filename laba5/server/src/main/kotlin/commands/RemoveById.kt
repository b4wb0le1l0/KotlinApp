package commands

import requestManager
import utility.CommandResult

class RemoveById: AbstractCommand("remove_by_id id", "удаляет элемент из коллекции по его id") {

    override fun execute(str: String): CommandResult {
        return requestManager.removeByIdRequest(str)
    }
}