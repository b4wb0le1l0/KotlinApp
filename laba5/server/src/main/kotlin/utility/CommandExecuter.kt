package utility

import commands.*

class CommandExecuter{

    private val last14 = mutableListOf<String>()
    private var commandList = LinkedHashMap<String, AbstractCommand>()

    init {
        makeListOfCommands()
    }

    private fun makeListOfCommands() {
        commandList["help"] = Help()
        commandList["info"] = Info()
        commandList["show"] = Show()
        commandList["history"] = History()
        commandList["add"] = Add()
        commandList["update"] = Update()
        commandList["remove_by_id"] = RemoveById()
        commandList["clear"] = Clear()
        commandList["execute_script"] = ExecuteScript()
        commandList["exit"] = Exit()
        commandList["add_if_min"] = AddIfMin()
        commandList["add_if_max"] = AddIfMax()
        commandList["remove_by_id"] = RemoveById()
        commandList["count_less_than_house"] = CountLTH()
        commandList["print_field_descending"] = PrintDescending()
        commandList["auth"] = Auth()
        commandList["login"] = Login()
    }

    fun executeCommand(str1: String, str2: String): CommandResult?{
        return if (checkCommand(str1)) {
            commandList[str1]?.execute(str2)
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! Команды $str1 не существует")
    }

    private fun checkCommand(str1: String): Boolean {
        return commandList.containsKey(str1)
    }

    fun help(): String {
        var listOfCommands = ""
        for (command in commandList) {
            listOfCommands += command.value.getName() + ": " + command.value.getExplanationOfCommand() + "\n"
        }
        return listOfCommands.substring(0, listOfCommands.length - 2)
    }

    fun queue(temp: String) {
        if (last14.size < 13) {
            last14.add(temp)
        }
        else {
            for (i in 1..12) {
                last14[i-1] = last14[i]
            }
            last14.add(temp)
        }
    }

    fun history(): String {
        var temp = ""
        for (i in last14) {
            temp += i + "\t\n"
        }
        return temp.substring(0, temp.length - 2)
    }
}