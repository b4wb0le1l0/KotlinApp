package utility

import java.io.*
import java.net.Socket
import java.sql.DriverManager

class ClientManager(private var asker: BychaAsker) {


    private var listOfCommands = ArrayList<String>()
    private val port = 9494
    private lateinit var sock: Socket
    private lateinit var outputStream: ObjectOutputStream
    private lateinit var inputStream: ObjectInputStream
    private var serverToken: String? = null
    private val last14 = mutableListOf<String>()

    init {
        fillCollection()
    }

    private fun fillCollection() {
        listOfCommands.add("help")
        listOfCommands.add("info")
        listOfCommands.add("show")
        listOfCommands.add("add")
        listOfCommands.add("update")
        listOfCommands.add("remove_by_id")
        listOfCommands.add("clear")
        listOfCommands.add("save")
        listOfCommands.add("execute_script")
        listOfCommands.add("exit")
        listOfCommands.add("add_if_min")
        listOfCommands.add("add_if_max")
        listOfCommands.add("history")
        listOfCommands.add("count_less_than_house")
        listOfCommands.add("print_descending")
        listOfCommands.add("print_field_descending_view")
        listOfCommands.add("auth")
        listOfCommands.add("login")
    }

    fun getUsername(): String? {
        return serverToken?.let { Regex("^\\w+").find(it)?.value }
    }

    fun sendCommandToServer(str1: String, str2: String): StateOfResponse {

        val listOfArguments =  asker.askForCommandArguments(str1)
        var maxTimesOfReconnection = 0
        while (maxTimesOfReconnection <= 6) {
            try {
                connectToServer()
                outputStream.writeObject(Request(str1, ("$str2 $listOfArguments").trim(), serverToken))
                (inputStream.readObject() as CommandResult).let {
                    if (it.message != null) println(it.message)
                    serverToken = it.serverToken
                    return it.commandComplicated
                }
            } catch (e: NotSerializableException) {
                println("Ошибка! Произошла ошибка при откправке данных на сервер!")
                return StateOfResponse.ERROR
            } catch (e: IOException) {
                println("Ошибка! Соединение с сервером разорвано!")
                println("Осталось " +  (6-maxTimesOfReconnection)*5 + " секунд на переподключение.")
                Thread.sleep(5000)
                maxTimesOfReconnection++
            }
        }
        return StateOfResponse.ERROR
    }

    fun checkCommand(str: String): Boolean {
        return listOfCommands.contains(str)
    }

    fun checkSymbols(s1: String, s2: String): Boolean {
        if (s1 in listOf("help", "show", "info", "clear", "save", "exit", "history", "print_descending", "print_field_descending_view")) {
            return if (s2.isEmpty()) true
            else {
                DriverManager.println("Ошибка! У команды $s1 нет аргументов! Команда должна быть введена без них.")
                false
            }
        }
        if (s1 in listOf("update", "remove_by_id")) {
            return try {
                s2.toInt()
                true
            } catch (e: NumberFormatException) {
                DriverManager.println("Ошибка! id - это целочисленное значение.")
                false
            }
        }
        if (s1 in listOf("count_less_than_house")) {

        }
        if (s1 in listOf("execute_script")) {
            return if (File(s1).exists()) true
            else {
                DriverManager.println("Ошибка! Файл $s2 не найден!")
                false
            }
        }
        return false
    }

    private fun connectToServer() {
        try {
            sock = Socket("localhost", port)
            outputStream = ObjectOutputStream(sock.getOutputStream())
            inputStream = ObjectInputStream(sock.getInputStream())
        } catch (e: IOException) {
            println("Ошибка! Произошла ошибка при подключении к серверу!")
        }
    }
}