package utility

import clientManager
import entity.*
import exceptions.IsEmptyException
import exceptions.NotInLimitException
import exceptions.NotInTrueFormatException
import java.io.FileNotFoundException
import java.util.*
import java.util.regex.Pattern

class BychaAsker(scan: Scanner, private var scriptMode: Boolean = false){

    private var scan: Scanner = scan
    private var coordinates: Coordinates = Coordinates(0F, 0)
    private var area: Int = 50
    private var numberOfRooms: Long = 0
    private var furnish: Furnish = Furnish.NONE
    private var view: View = View.NORMAL
    private var transport: Transport = Transport.NONE
    private var house: House = House("", 0, 1)
    private val minAreaCount = 0

    fun setScan(scan: Scanner) {
        this.scan = scan
    }

    fun getScan() : Scanner {
        return this.scan
    }

    fun setScriptNotInProgress() {
        this.scriptMode = false
    }

    fun setScriptInProgress() {
        this.scriptMode = true
    }

    private fun askUserName(): String {
        var username: String
        while (true) {
            try {
                print("Введите ваш никнейм\n>")
                username = scan.nextLine().trim()
                if(!Pattern.matches("[0-9A-Za-z]{3,12}", username)) throw NotInTrueFormatException()
                break
            } catch (e: NotInTrueFormatException) {
                println("Ошибка! Неправильный никнейм. Никнейм должен быть от 3 до 12 и содержать только английские буквы и цифры")
            }
        }
        return username
    }

    fun askPassword(): String {
        var password: String
        while (true) {
            try {
                print("Введите пароль\n>")
                password = scan.nextLine()
                if (!Pattern.matches("[0-9A-Za-z]{3,12}", password)) throw NotInTrueFormatException()
                break
            } catch (e: NotInTrueFormatException) {
                println("error: Неправильный пароль. Пароль должен быть от 3 до 12 и содержать только английские буквы и цифры")
            }
        }
        return password
    }

    fun askForFileName(): String{
        var fileName: String
        while(true){
            try {
                print("Введите название файла:\n>")
                fileName = scan.nextLine().trim()
                if(fileName == "") throw IsEmptyException()
                if(fileName.contains(Regex("[^a-z^A-Z0-9]"))) throw NotInTrueFormatException()
                fileName += ".json"
                break
            } catch (e: IsEmptyException) {
                println("Ошибка! Название не может быть пустым!")
            } catch (e: NotInTrueFormatException){
                println("Ошибка! Название должно содержать только буквы!")
            } catch (e: FileNotFoundException) {
                println("Ошибка! Файла с таким названием нет!")
            }
        }
        return fileName
    }

    fun askForCommandArguments(str1: String): String {
        if (str1 in listOf("add", "add_if_min", "update")) {
            println(clientManager.getUsername())
            return askNameOfFlat()+ " " + askCoordinateX() + " " + askCoordinateY() + " " + askArea() + " "+ askNumberOfRooms() + " " + askFurnish() + " " + askView() + " " + askTransport() + " " + askHouseName() + " " + askHouseYear() + " " + askHouseNumberOfLifts() + " " + clientManager.getUsername()
        }
        if (str1 in listOf("remove_greater", "remove_lower")) {
            return askAreaCount().toString()
        }
        if (str1 in listOf("auth", "login")) {
            return askUserName() + " " + askPassword()
        }
        return ""
    }

    private fun askAreaCount(): Int{
        var areaStr: String
        var area: Int
        while (true) {
            try {
                if (!scriptMode) print("Введите площадь квартиры:\n>")
                areaStr = scan.nextLine().trim()
                if (areaStr == "") throw IsEmptyException()
                area = areaStr.toInt()
                if (area < minAreaCount) throw NotInLimitException()
                break
            } catch (e: IsEmptyException) {
                println("Ошибка! Количетво не может быть пустым!")
            } catch (e: NotInLimitException) {
                println("Ошибка! минимальное площадь квартиры 1!")
            } catch (e: NumberFormatException) {
                println("Ошибка! Количество должно быть числом")
            }
        }
        return area
    }

    fun askYesOrNot(argument: String): Boolean {
        val question = "$argument (да/нет):\n>"
        var answer: String
        while (true) {
            try {
                print(question)
                answer = scan.nextLine().trim()
                if (answer != "да" && answer != "нет") throw NotInLimitException()
                break
            }catch (e: NotInLimitException) {
                println("Ошибка! Ответ должен быть либо да либо нет")
            }
        }
        return answer == "да"
    }

    private fun askNameOfFlat(): String {
        var flatName: String
        while(true){
            try {
                if (!scriptMode) print("Введите название квартиры:\n>")
                flatName = scan.nextLine().trim()
                if(flatName == "") throw IsEmptyException()
                if(flatName.contains(Regex("[^a-z^A-Z]"))) throw NotInTrueFormatException()
                break
            } catch (e: IsEmptyException) {
                println("Ошибка! Название не может быть пустым!")
            } catch (e: NotInTrueFormatException){
                println("Ошибка! Название должно содержать только буквы!")
            }
        }
        return flatName
    }

    private fun askCoordinateX(): Float {
        var strX: String
        var x: Float
        while (true) {
            try {
                if (!scriptMode) print("Введите координату x:\n>")
                strX = scan.nextLine().trim()
                if (strX == "") throw IsEmptyException()
                x = strX.toFloat()
                if (x > 430) throw NotInLimitException()
                this.coordinates.setX(x)
                break
            } catch (e: NumberFormatException) {
                println("Ошибка! Координата x должна быть числом!")
            } catch (e: IsEmptyException) {
                println("Ошибка! Координата не может быть пустой!")
            } catch (e: NotInLimitException) {
                println("Ошибка! Максимальное значение x: 430!")
            }
        }
        this.coordinates.setX(x)
        return this.coordinates.getX()
    }


    private fun askCoordinateY(): Int {
        var strY: String
        while(true){
            try {
                if (!scriptMode) print("Введите координату y:\n>")
                strY = scan.nextLine().trim()
                if (strY == "") throw IsEmptyException()
                this.coordinates.setY(strY.toInt())
                break
            } catch (e: NumberFormatException) {
                println("Ошибка! Координата y должна быть числом!")
            } catch (e: IsEmptyException) {
                println("Ошибка! Поле координат не может быть пустым!")
            }
        }
        return this.coordinates.getY()
    }

    private fun askArea(): Int {
        var strArea: String
        var area: Int
        while(true){
            try {
                if (!scriptMode) print("Введите площадь квартиры x:\n>")
                strArea = scan.nextLine().trim()
                if (strArea == "") throw IsEmptyException()
                area = strArea.toInt()
                if(area > 560 || area <= 0) throw NotInLimitException()
                this.area = area
                break
            } catch (e: NumberFormatException) {
                println("Ошибка! Координата x должна быть числом!")
            } catch (e: IsEmptyException) {
                println("Ошибка! Координата не может быть пустой!")
            } catch (e: NotInLimitException) {
                println("Ошибка! Неккоректное значение площади!")
            }
        }
        return this.area
    }

    private fun askNumberOfRooms(): Long {
        var strNofR: String
        var NofR: Long

        while(true){
            try {
                if (!scriptMode) print("Укажите кол-во комнат:\n>")
                strNofR = scan.nextLine().trim()
                if (strNofR == "") throw IsEmptyException()
                NofR = strNofR.toLong()
                if(NofR > 7 || NofR <= 0) throw NotInLimitException()
                this.numberOfRooms = NofR
                break
            } catch (e: NumberFormatException) {
                println("Ошибка! Координата y должна быть числом!")
            } catch (e: IsEmptyException) {
                println("Ошибка! Поле координат не может быть пустым!")
            } catch (e: NotInLimitException) {
                println("Ошибка! Неккоректное значение комнат!")
            }
        }
        return this.numberOfRooms
    }

    private fun askHouseName(): String {
        println("Выберите парраметры дома:")
        var houseName: String
        while(true){
            try {
                if (!scriptMode) print("Введите название дома:\n>")
                houseName = scan.nextLine().trim()
                if(houseName == "") throw IsEmptyException()
                if(houseName.contains(Regex("[^a-z^A-Z]"))) throw NotInTrueFormatException()
                this.house.setName(houseName)
                break
            } catch (e: IsEmptyException) {
                println("Ошибка! Название не может быть пустым!")
            } catch (e: NotInTrueFormatException){
                println("Ошибка! Название должно содержать только буквы!")
            }
        }
        return this.house.getName()
    }

    private fun askHouseYear(): Long {
        var year: String
        var yearl: Long
        while (true) {
            println("введите возраст дома")
            year = scan.nextLine().trim()
            yearl = year.toLong()
            if (yearl <= 0 || yearl > 974) {
                println("Некореткное значение возраста дома.")
            } else break
        }
        this.house.setYear(yearl)
        return this.house.getYear()
    }

    private fun askHouseNumberOfLifts(): Int {
        var number: String
        while (true) {
            println("введите кол-во лифтов в доме.")
            number = scan.nextLine().trim()
            if (this.house.getNumberOfLifts() <= 0) {
                println("Некорректное значение кол-ва лифтов.")
            } else break
        }
        this.house.setNumberOfLifts(number.toInt())
        return this.house.getNumberOfLifts()
    }

    private fun askFurnish(): Furnish {
        while (true) {
            println("Выберите furnish из списка: DESIGNER, NONE, FINE, BAD.")
            when (scan.nextLine().trim()) {
                "DESIGNER" -> {
                    this.furnish = Furnish.DESIGNER
                    break
                }
                "NONE" -> {
                    this.furnish = Furnish.NONE
                    break
                }
                "FINE" -> {
                    this.furnish = Furnish.FINE
                    break
                }
                "BAD" -> {
                    this.furnish = Furnish.BAD
                    break
                }
                else -> println("Некорректное значение Furnish!")
            }
        }
        return this.furnish
    }

    private fun askTransport(): Transport {
        while (true) {
            println("Выберите Transport из списка: FEW, NONE, NORMAL, ENOUGH")
            when (scan.nextLine().trim()) {
                "FEW" -> {
                    this.transport = Transport.FEW
                    break
                }
                "NONE" -> {
                    this.transport = Transport.NONE
                    break
                }
                "NORMAL" -> {
                    this.transport = Transport.NORMAL
                    break
                }
                "ENOUGH" -> {
                    this.transport = Transport.ENOUGH
                    break
                }
                else -> println("Некорректное значение Transport!")
            }
        }
        return this.transport
    }

    private fun askView(): View {
        while (true) {
            println("Выберите View из списка: STREET, YARD, NORMAL, TERRIBLE")
            when (scan.nextLine().trim()) {
                "STREET" -> {
                    this.view = View.STREET
                    break
                }
                "YARD" -> {
                    this.view = View.YARD
                    break
                }
                "NORMAL" -> {
                    this.view = View.NORMAL
                    break
                }
                "TERRIBLE" -> {
                    this.view = View.TERRIBLE
                    break
                }
                else -> println("Некорректное значение View!")
            }
        }
        return this.view
    }
}