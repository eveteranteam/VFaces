package ua.gov.mva.vfaces.utils

// TODO
object Preconditions {

    fun checkArgument(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalArgumentException(errorMessage.toString())
        }
    }
}