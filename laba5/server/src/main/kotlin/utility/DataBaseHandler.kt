package utility

import entity.*
import hasher
import java.io.FileNotFoundException
import java.io.FileReader
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import kotlin.collections.LinkedHashSet
import kotlin.system.exitProcess

class DataBaseHandler {
    private lateinit var connection: Connection
    private val pepper = "2jd*1ld?wp@"
    private val requestFindUsername = "SELECT COUNT(*) AS times FROM USERS WHERE username = ?"
    private val requestAddUsername = "INSERT INTO USERS (username, password, salt) VALUES (?, ?, ?)"
    private val requestValidateUser = "SELECT COUNT(*) AS times FROM USERS WHERE username = ? and password = ?"
    private val requestFindSalt = "SELECT salt FROM USERS WHERE username = ?"
    private val requestSelectCollection = "SELECT * FROM FLAT"
    private val requestAddFlat = "INSERT INTO FLAT (flat_id, flat_name, coordinates_x, coordinates_y, creation_date, area, number_of_rooms, furnish, view, transport, house_name, house_year, house_number_of_lifts, username_creation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    private val requestClear = "DELETE FROM FLAT WHERE username_creation = ?"
    private val requestRemoveById = "DELETE FROM FLAT WHERE flat_id = ?"
    private val requestUpdateById = "UPDATE FLAT SET flat_name = ?, coordinates_x = ?, coordinates_y = ?, creation_date = ?, area = ?, numbers_of_roooms = ?, furnish = ?, view = ?, transport = ?, house_name = ?, house_year = ?, house_number_of_lifts = ?, username_creation = ? WHERE flat_id = ?"
    private val username: String
    private val password: String
    private val url = "jdbc:postgresql://localhost:5432/studs"

    init {
        val loginScanner: Scanner

        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            println("PostgreSQL драйвер не найден")
            exitProcess(-1)
        }

        /*try { loginScanner = Scanner(FileReader(".\\login.txt"))
        } catch (e: FileNotFoundException) {
            println("Потерялся файл с данными для входа на БД")
            exitProcess(-1)
        }*/

        try {
            /*username = loginScanner.nextLine().trim()
            password = loginScanner.nextLine().trim()*/
            username = "username"
            password = "password"
        } catch (e: NoSuchElementException) {
            println("Данные для входа некорректно записаны в файл")
            exitProcess(-1)
        }
    }
    fun connectToDataBase() {
        try {
            connection = DriverManager.getConnection(url, username, password)
            println("Подключение к БД успешно завершено")
        } catch (e: SQLException) {
            println("Не удалось подключиться к БД")
            exitProcess(-1)
        }
    }

    fun loadCollectionFromBD(): LinkedHashSet<Flat> {
        val collection = LinkedHashSet<Flat>()
        try {
            val selectCollection = connection.prepareStatement(requestSelectCollection)
            val result = selectCollection.executeQuery()
            while (result.next()) {
                collection.add(selectFlatFromBD(result))
            }
            selectCollection.close()
            println("Коллекция успешно загружена. Её размер: " + collection.size)
        } catch (e: SQLException) {
            println("Ошибка! Ошибка при загрузке коллекци из БД")
            e.printStackTrace()
            exitProcess(-1)
        }
        return collection
    }

    private fun selectFlatFromBD(result: ResultSet): Flat {
        return Flat(result.getInt("flat_id"),
            result.getString("flat_name"),
            Coordinates(result.getFloat("coordinates_x"),
                result.getInt("coordinates_y")),
            result.getString("creation_date"),
            result.getInt("area"),
            result.getInt("number_of_rooms"),
            Furnish.valueOf(result.getString("furnish")),
            View.valueOf(result.getString("view")),
            Transport.valueOf(result.getString("transport")),
                House(result.getString("house_name"),
                result.getLong("house_year"),
                    result.getInt("house_number_of_lifts")),
            result.getString("username_creation")
        )
    }

    fun registerUserToBD(username: String, password: String): Boolean {
        if (usernameExist(username)) return false
        val addUser = connection.prepareStatement(requestAddUsername)
        val salt = getRandomString()
        addUser.setString(1, username)
        addUser.setString(2, hasher.encryptStringToSHA384(salt + password + pepper))
        addUser.setString(3, salt)
        addUser.executeUpdate()
        addUser.close()
        return true
    }

    private fun usernameExist(username: String): Boolean {
        val findUsername = connection.prepareStatement(requestFindUsername)
        findUsername.setString(1, username)
        val result = findUsername.executeQuery()
        result.next()
        return if (result.getInt(1) == 1) {
            findUsername.close()
            true
        } else {
            findUsername.close()
            false
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val findSalt = connection.prepareStatement(requestFindSalt)
        findSalt.setString(1, username)
        val test = findSalt.executeQuery()
        test.next()
        val salt = test.getString(1)
        val validateUser = connection.prepareStatement(requestValidateUser)
        validateUser.setString(1, username)
        validateUser.setString(2, hasher.encryptStringToSHA384(salt + password + pepper))
        val result = validateUser.executeQuery()
        result.next()
        return if (result.getInt(1) == 1) {
            validateUser.close()
            true
        } else {
            validateUser.close()
            false
        }
    }

    fun addFLatToBD(flat: Flat) {
        connection.autoCommit = false
        connection.setSavepoint()
        val addFLat = connection.prepareStatement(requestAddFlat)
        addFLat.setInt(1, flat.getId())
        addFLat.setString(2, flat.getName())
        addFLat.setFloat(3, flat.getCoordinates().getX())
        addFLat.setInt(4, flat.getCoordinates().getY())
        addFLat.setString(5, flat.getCreationDate())
        addFLat.setInt(6, flat.getArea())
        addFLat.setInt(7, flat.getNumberOfRooms())
        addFLat.setString(8, flat.getFurnish().toString())
        addFLat.setString(9, flat.getVIew().toString())
        addFLat.setString(10, flat.getTransport().toString())
        addFLat.setString(11, flat.getHouse().getName())
        addFLat.setLong(12, flat.getHouse().getYear())
        addFLat.setInt(13, flat.getHouse().getNumberOfLifts())
        addFLat.setString(14, flat.getUserName())
        addFLat.executeUpdate()
        addFLat.close()
        connection.commit()
        connection.autoCommit = true
    }

    fun clearBD(username: String) {
        val clearBD = connection.prepareStatement(requestClear)
        clearBD.setString(1, username)
        clearBD.executeUpdate()
        clearBD.close()
    }

    fun removeById(id: Int) {
        val removeById = connection.prepareStatement(requestRemoveById)
        removeById.setInt(1, id)
        removeById.executeUpdate()
        removeById.close()
    }

    fun updateById(flat: Flat) {
        val updateById = connection.prepareStatement(requestUpdateById)
        updateById.setString(1, flat.getName())
        updateById.setFloat(2, flat.getCoordinates().getX())
        updateById.setInt(3, flat.getCoordinates().getY())
        updateById.setString(4, flat.getCreationDate())
        updateById.setInt(5, flat.getArea())
        updateById.setInt(6, flat.getNumberOfRooms())
        updateById.setString(7, flat.getFurnish().toString())
        updateById.setString(8, flat.getVIew().toString())
        updateById.setString(9, flat.getTransport().toString())
        updateById.setString(10, flat.getHouse().getName())
        updateById.setLong(11, flat.getHouse().getYear())
        updateById.setInt(12, flat.getHouse().getNumberOfLifts())
        updateById.setString(13, flat.getUserName())
        updateById.setInt(14, flat.getId())
        updateById.executeUpdate()
        updateById.close()
    }

    fun getRandomString(): String{
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { charset.random() }
            .joinToString("")
    }
}