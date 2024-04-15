package commands

import utility.CommandResult

abstract class AbstractCommand (private val name: String, private val explanationOfCommand: String){

    fun getName(): String {
        return name
    }

    fun getExplanationOfCommand(): String {
        return explanationOfCommand
    }

    abstract fun execute(str: String): CommandResult

    override fun toString(): String {
        return "AbstractCommand(name='$name', explanationOfCommand='$explanationOfCommand')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractCommand

        if (name != other.name) return false
        if (explanationOfCommand != other.explanationOfCommand) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + explanationOfCommand.hashCode()
        return result
    }
}