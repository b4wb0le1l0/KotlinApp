package utility

import entity.Flat
import exceptions.IsEmptyException
import exceptions.NotInTrueFormatException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import logger
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*
import kotlin.collections.LinkedHashSet

class FileManager {

    private var name = ""

    private fun setName(name: String) {
        this.name = name
    }

    fun writeCollection(collection: LinkedHashSet<Flat>) {
        try {
            val list = ArrayList(collection)
            FileWriter(name).use { it.write(Json.encodeToString(list)) }
            logger.info("Коллекция успешно записана на файл")
        } catch (e: IOException) {
            logger.error("Ошибка! Невозможно сохранить!")
        }
    }

    fun readCollection(): LinkedHashSet<Flat> {
        var fileReader: FileReader
        while (true) {
            try {
                fileReader = FileReader(name)
                break
            } catch (e: FileNotFoundException) {
                logger.error("Ошибка! Файла с таким названием нету!")
            }
        }
        var char: Int
        var input = ""
        do {
            char = fileReader.read()
            if (char == -1) {
                break
            }
            input += char.toChar()
        } while (true)
        return LinkedHashSet(Json.decodeFromString<List<Flat>>(input))
    }

    private fun askForFileName(): String {
        var fileName: String
        val scan = Scanner(System.`in`)
        while (true) {
            try {
                print("Введите название файла:\n>")
                fileName = scan.nextLine().trim()
                if (fileName == "") throw IsEmptyException()
                if (fileName.contains(Regex("[^a-z^A-Z0-9]"))) throw NotInTrueFormatException()
                fileName += ".json"
                break
            } catch (e: IsEmptyException) {
                println("Ошибка! Название не может быть пустым!")
            } catch (e: NotInTrueFormatException) {
                println("Ошибка! Название должно содержать только буквы!")
            } catch (e: FileNotFoundException) {
                println("Ошибка! Файла с таким названием нет!")
            }
        }
        return fileName
    }
}