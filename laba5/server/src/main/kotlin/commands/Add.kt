    package commands

    import requestManager
    import utility.CommandResult

    class Add: AbstractCommand("add {element}", "добавляет новый элемент в коллекцию") {

        override fun execute(str: String): CommandResult {
            return requestManager.addRequest(null, str)
        }
    }