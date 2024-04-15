package commands

import requestManager
import utility.CommandResult

class PrintDescending: AbstractCommand("print_descending", "выводит элементы коллекции в порядке убывания") {

    override fun execute(str: String): CommandResult {
        return requestManager.printDescendingRequest(str)
    }
}