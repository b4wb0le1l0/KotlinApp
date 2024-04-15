package commands

import requestManager
import utility.CommandResult

class Show: AbstractCommand("show", "выводит в стандартный поток вывода все элементы коллекции в строковом представлении"){

    override fun execute(str: String): CommandResult {
        return requestManager.showRequest(str)
    }
}