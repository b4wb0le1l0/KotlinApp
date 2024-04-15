package commands

import requestManager
import utility.CommandResult

class PrintFDV: AbstractCommand("print_field_descending_view", "выводит значения поля view всех элементов в порядке убывания") {

    override fun execute(str: String): CommandResult {
        return requestManager.printFieldDescendingViewRequest(str)
    }
}