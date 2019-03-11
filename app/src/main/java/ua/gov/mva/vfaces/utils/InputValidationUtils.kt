package ua.gov.mva.vfaces.utils

import java.util.regex.Pattern

object InputValidationUtils {

    private const val EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    private const val NAME_PATTERN = "^[a-zA-Z-\\s]+"
    private const val NAME_MINIMUM_LENGTH = 1
    private const val NAME_MAXIMUM_LENGTH = 30
    private const val PASSWORD_MINIMUM_LENGTH = 6
    private const val PASSWORD_MAXIMUM_LENGTH = 30

    fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        return pattern.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length in PASSWORD_MINIMUM_LENGTH..PASSWORD_MAXIMUM_LENGTH
    }

    fun isNameValid(name: String): Boolean {
        val pattern = Pattern.compile(NAME_PATTERN)
        return pattern.matcher(name).matches() && name.length >= NAME_MINIMUM_LENGTH && name.length <= NAME_MAXIMUM_LENGTH
    }
}
