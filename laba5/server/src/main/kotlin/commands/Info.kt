package commands

import requestManager
import utility.CommandResult

class Info : AbstractCommand("info", "выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.") {

    override fun execute(str: String): CommandResult {
        return requestManager.infoRequest(str)
    }
}