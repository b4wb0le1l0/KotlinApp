
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import utility.*
import java.util.*

val scan = Scanner(System.`in`)
val fileManager: FileManager = FileManager()
val dataBaseHandler = DataBaseHandler()
val entityManager = EntityManager()
val commandExecuter = CommandExecuter()
val requestManager = RequestManager(entityManager, fileManager)
val serverThread = ServerThread(scan)
val hasher = PasswordHasher()
val logger: Logger = LogManager.getLogger()
val app = ServerManager(scan, commandExecuter)
val authManager = AuthManager()

fun main(args: Array<String>) {
    serverThread.start()
    app.run()

}