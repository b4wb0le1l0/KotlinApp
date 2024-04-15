package utility

import app
import commandExecuter
import dataBaseHandler
import entity.*
import logger
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.system.exitProcess

class RequestManager (private var entityManager: EntityManager, private var fileManager: FileManager){

    fun helpRequest(str: String): CommandResult {
        return if (str.split(Regex(" ")).toTypedArray().size == 1) {
            CommandResult(StateOfResponse.GOOD, commandExecuter.help())
        }else {
            println(str.split(Regex(" ")).toTypedArray().joinToString())
            CommandResult(StateOfResponse.ERROR, "Ошибка! После команды help не должно быть аргументов")}
    }

    fun historyRequest(str: String):CommandResult {
        return if (str.isEmpty()) {
            CommandResult(StateOfResponse.GOOD, commandExecuter.history())
        }else return CommandResult(StateOfResponse.ERROR, "Ошибка! После команды history не должно быть аргументов")
    }

    fun exitClientRequest(str: String): CommandResult {
        return if (str.split(Regex(" ")).toTypedArray().size == 1) {
            CommandResult(StateOfResponse.CLIENT_EXIT, "Выключение клиентского модуля")
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды exit не должно быть аргументов")
    }

    fun exitServerRequest(str: String){
        saveRequest("")
        logger.info("Завершение работы сервера...")
        try {
            app.getServerSocket()!!.close()
        } catch (e: IOException) {
            logger.error("Ошибка при завершении сервера!")
        } catch (_: NullPointerException) {
        }
        logger.info("Работа сервера завершена")
        exitProcess(0)
    }

    fun infoRequest(str: String): CommandResult {
        return if (str.split(Regex(" ")).toTypedArray().size == 1) {
            CommandResult(StateOfResponse.GOOD, "Информация о коллекции:\n" +
                    " Тип коллекции: " + entityManager.getCollectionType() + "\n" +
                    " Размер коллекции: " + entityManager.getCollectionSize() + "\n" +
                    " Дата и время последнего сохранения: " + entityManager.getLastSaveTime() + "\n" +
                    " Дата и время последней иницилизации: " + entityManager.getLastInitTime())
        } else CommandResult(StateOfResponse.ERROR,"Ошибка! После команды info не должно быть аргументов")
    }

    fun showRequest(str: String): CommandResult {
        return if (str.split(Regex(" ")).toTypedArray().size == 1) {
            CommandResult(StateOfResponse.GOOD, entityManager.toString())
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды show не должно быть аргументов")
    }

    fun addRequest(id: Int?, str: String): CommandResult {
        val listOfArguments = str.split(Regex(" ")).toTypedArray()
        return if(listOfArguments.size == 13) {
            if (id == null) {
                val flat = Flat(
                    entityManager.generateId(),
                    listOfArguments[0],
                    Coordinates(listOfArguments[1].toFloat(), listOfArguments[2].toInt()),
                    Date.from(Instant.now()).toString(),
                    listOfArguments[3].toInt(),
                    listOfArguments[4].toInt(),
                    Furnish.valueOf(listOfArguments[5]),
                    View.valueOf(listOfArguments[6]),
                    Transport.valueOf(listOfArguments[7]),
                    House(listOfArguments[8], listOfArguments[9].toLong(), listOfArguments[10].toInt()),
                    listOfArguments[11]
                )
                entityManager.addObjectToCollection(flat)
                dataBaseHandler.addFLatToBD(flat)
                CommandResult(StateOfResponse.GOOD, "Квартира создана и добавлена в коллекцию")
            } else {
                val flat = Flat(
                    id,
                    listOfArguments[0],
                    Coordinates(listOfArguments[1].toFloat(), listOfArguments[2].toInt()),
                    Date.from(Instant.now()).toString(),
                    listOfArguments[3].toInt(),
                    listOfArguments[4].toInt(),
                    Furnish.valueOf(listOfArguments[5]),
                    View.valueOf(listOfArguments[6]),
                    Transport.valueOf(listOfArguments[7]),
                    House(listOfArguments[8], listOfArguments[9].toLong(), listOfArguments[10].toInt()),
                    listOfArguments[11]
                )
                entityManager.addObjectToCollection(flat)
                dataBaseHandler.updateById(flat)
                CommandResult(StateOfResponse.GOOD, "Квартира обновлена")
            }
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды add не должно быть аргументов")
    }

    fun clearRequest(str: String): CommandResult {
        val listOfArguments = str.split(Regex(" ")).toTypedArray()
        val collectionSize = entityManager.getCollectionSize()
        return if (listOfArguments.size == 1) {
            if (entityManager.getCollectionSize() == 0) {
                CommandResult(StateOfResponse.ERROR, "Коллекция пустая")
            } else {
                entityManager.clearCollection(listOfArguments[0])
                dataBaseHandler.clearBD(listOfArguments[0])
                if (collectionSize == entityManager.getCollectionSize()) {
                    CommandResult(StateOfResponse.GOOD, "Ваших квартир не было в коллекции")
                } else CommandResult(StateOfResponse.GOOD, "Коллекция очищена от ваших квартир")
            }
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды clear не должно быть аргументов")
    }

    fun checkIfMinArea(str: String): CommandResult {
        val listOfArguments = str.split(Regex(" ")).toTypedArray()
        return if(listOfArguments.size == 11) {
            println(listOfArguments[3])
            if (entityManager.areaMin(listOfArguments[3].toInt())) {
                addRequest(null, str)
            } else CommandResult(StateOfResponse.GOOD, "Есть элемент с меньшей площадью.")
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды add_if_min не должно быть аргументов")
    }

    fun checkIfMaxArea(str: String): CommandResult {
        val listOfArguments = str.split(Regex(" ")).toTypedArray()
        return if(listOfArguments.size == 11) {
            println(listOfArguments[3])
            if (entityManager.areaMax(listOfArguments[3].toInt())) {
                addRequest(null, str)
            } else CommandResult(StateOfResponse.GOOD, "Есть элемент с большей площадью.")
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды add_if_max не должно быть аргументов")
    }

    fun saveRequest(str: String): CommandResult {
        return if (str.split(Regex(" ")).toTypedArray().size == 1) {
            fileManager.writeCollection(entityManager.getFlatCollection())
            entityManager.setLastSaveTime(LocalDateTime.now())
            CommandResult(StateOfResponse.GOOD)
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды save не должно быть аргументов")
    }

    fun removeByIdRequest(str: String): CommandResult {
        val listOfArguments = str.split(Regex(" ")).toTypedArray()
        if (listOfArguments.size == 2) {
            try {
                if (entityManager.getCollectionSize() == 0) {
                    return CommandResult(StateOfResponse.GOOD, "Коллекция пустая")
                } else {
                    val flat = entityManager.getById(listOfArguments[0].toInt()) ?: return CommandResult(StateOfResponse.GOOD,"Нет квартиры с таким id")
                    return if (flat.getName() == listOfArguments[1]) {
                        entityManager.removeFromCollection(flat)
                        dataBaseHandler.removeById(listOfArguments[0].toInt())
                        CommandResult(StateOfResponse.GOOD, "Квартира удалена")
                    } else CommandResult(StateOfResponse.ERROR, "Ошибка! Нельзя модифицировать чужие объекты!")
                }
            } catch (e: NumberFormatException) { return CommandResult(StateOfResponse.ERROR, "Ошибка! Неправильный ввод данных")
            }
        } else return CommandResult(StateOfResponse.ERROR, "Ошибка! После команды remove_by_id должно быть указано id")
    }

    fun updateByIdRequest(str: String): CommandResult {
        if (str.split(Regex(" ")).toTypedArray().size == 17) {
            try {
                if (entityManager.getCollectionSize() == 0) {
                    return CommandResult(StateOfResponse.GOOD, "Коллекция пустая")
                } else {
                    var listOfArguments = str.split(Regex(" ")).toTypedArray()
                    val flat = entityManager.getById(listOfArguments[0].toInt()) ?: return CommandResult(StateOfResponse.GOOD, "Нет квартир с таким id")
                    val id = listOfArguments[0].toInt()
                    return if (flat.getUserName() == listOfArguments[16]) {
                        entityManager.removeFromCollection(flat)

                        val list = listOfArguments.toMutableList()
                        list.removeAt(0)
                        listOfArguments = list.toTypedArray()

                        addRequest(id, listOfArguments.joinToString(separator = " "))
                    } else CommandResult(StateOfResponse.ERROR, "Ошибка! Нельзя модифицировать чужие объекты!")
                }
            }catch (e: NumberFormatException) { return CommandResult(StateOfResponse.ERROR, "Ошибка! Неправильный ввод данных") }
        } else return CommandResult(StateOfResponse.ERROR, "Ошибка! После команды update должно быть указано id")
    }


    fun countLessThanHouseRequest(str: String): CommandResult {
        return if (str.isEmpty()) {
            if (entityManager.getCollectionSize() == 0) {
                CommandResult(StateOfResponse.GOOD, "Коллекция пуста")
            } else CommandResult(StateOfResponse.GOOD, "Количество элементов у которых значение поля house меньше заданного по годам(year): "+entityManager.getCountLessThanHouse())
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды count_less_than_house должно быть указано id")
    }

    fun printDescendingRequest(str: String): CommandResult {
        return if (str.isEmpty()) {
            if (entityManager.getCollectionSize() == 0) {
                CommandResult(StateOfResponse.GOOD, "Коллекция пуста")
            } else CommandResult(StateOfResponse.GOOD, "Квартир в порядке убывания по id: " + entityManager.getInDescending())
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды print_descending не должно быть аргументов")
    }

    fun printFieldDescendingViewRequest(str: String): CommandResult {
        return if (str.isEmpty()) {
            if (entityManager.getCollectionSize() == 0) {
                CommandResult(StateOfResponse.GOOD, "Коллекция пуста")
            } else CommandResult(StateOfResponse.GOOD, "Знаечения поля view всех существующих квартир в порядке убывания.: " + entityManager.getViewInDescending())
        } else CommandResult(StateOfResponse.ERROR, "Ошибка! После команды print_field_descending_view не должно быть аргументов")
    }
}