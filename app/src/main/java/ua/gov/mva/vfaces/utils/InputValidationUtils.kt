package ua.gov.mva.vfaces.utils

import java.util.regex.Pattern

object InputValidationUtils {

    private const val EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    private const val UA_PHONE_PATTERN = "^(0|\\+380)\\d{9}\$"

    private const val NAME_MINIMUM_LENGTH = 1
    private const val NAME_MAXIMUM_LENGTH = 30
    private const val PASSWORD_MINIMUM_LENGTH = 6
    private const val PASSWORD_MAXIMUM_LENGTH = 30
    private const val PHONE_MINIMUM_LENGTH = 10

    fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        return pattern.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length in PASSWORD_MINIMUM_LENGTH..PASSWORD_MAXIMUM_LENGTH
    }

    fun isNameValid(name: String): Boolean {
        return name.length in NAME_MINIMUM_LENGTH..NAME_MAXIMUM_LENGTH
    }

    fun isPhoneValid(phone: String) : Boolean {
        val pattern = Pattern.compile(UA_PHONE_PATTERN)
        return pattern.matcher(phone).matches()
    }

    fun isPhoneTooShort(phone: String) : Boolean {
        return phone.length < PHONE_MINIMUM_LENGTH
    }
}