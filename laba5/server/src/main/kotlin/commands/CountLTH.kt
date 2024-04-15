package commands

import requestManager
import utility.CommandResult

class CountLTH: AbstractCommand("count_less_that_house", "выводит количество элементов, значение поля house которых меньше заданного") {

    override fun execute(str: String): CommandResult {
        return requestManager.countLessThanHouseRequest(str)
    }
}