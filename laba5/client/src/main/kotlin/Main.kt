import utility.BychaAsker
import utility.ClientManager
import utility.ConsoleManager
import java.util.*

val scan = Scanner(System.`in`)
val asker: BychaAsker = BychaAsker(scan)
val clientManager = ClientManager(asker)
val app = ConsoleManager(scan, clientManager, asker)

fun main(args: Array<String>) {
    app.run()
}