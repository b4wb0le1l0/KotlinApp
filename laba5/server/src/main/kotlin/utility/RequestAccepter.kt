package utility

import java.io.ObjectInputStream
import java.util.concurrent.Callable

class RequestAccepter(private val inputStream: ObjectInputStream
) : Callable<Request> {

    override fun call(): Request {
        return inputStream.readObject() as Request
    }
}