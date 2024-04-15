package utility

import logger
import java.io.IOException
import java.io.NotSerializableException
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.concurrent.RecursiveTask

class ResponseSender(private val response: CommandResult,
                     private val outputStream: ObjectOutputStream,
                     private val request: Request,
                     private val clientSocket: Socket
) : RecursiveTask<Unit>() {

    override fun compute() {
        try {
            if (response.commandComplicated == StateOfResponse.CLIENT_EXIT) logger.info("Клиент " + clientSocket.inetAddress.hostName + " отключился")
            if (response.serverToken == null) response.serverToken = request.serverToken
            outputStream.writeObject(response)
        } catch (e: NotSerializableException) {
            logger.error("Ошибка при отправлении данных клиенту")
        } catch (e: IOException) {
            logger.error("Разрыв соединения с клиентом")
        }
    }
}