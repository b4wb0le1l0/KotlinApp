package commands

import utility.CommandResult
import utility.StateOfResponse

class ExecuteScript: AbstractCommand("execute_script file_name", "считает и исполняет скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме") {

    override fun execute(str: String): CommandResult {
        /*val clientManager = ConsoleManager(scan, commandManager, asker)
        clientManager.scriptMode(str)*/
        return CommandResult(StateOfResponse.GOOD, "")
    }

}