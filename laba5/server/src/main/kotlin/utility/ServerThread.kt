package utility

import commands.AbstractCommand
import commands.Save
import commands.ServerExit
import java.util.*

class ServerThread(private val scan: Scanner): Thread() {

    private val serverCommands = HashMap<String, AbstractCommand>()
    private lateinit var command: String

    init {
        serverCommands["exit"] = ServerExit()
        serverCommands["save"] = Save()
    }

    override fun run() {
        while (true) {
            if (scan.hasNext()) {
                command = scan.nextLine()
                if (checkServerCommand(command)) {
                    serverCommands[command]?.execute("").let { if (it?.message != null) println(it.message)}
                }
            }
        }
    }

    private fun checkServerCommand(str: String): Boolean {
        return serverCommands.containsKey(str)
    }
}