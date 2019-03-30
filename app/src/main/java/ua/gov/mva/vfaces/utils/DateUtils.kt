package ua.gov.mva.vfaces.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("dd MMM YYYY hh:mm", Locale("ua", "UA"))
    }

    fun format(longDate: Long) : String {
        return dateFormat.format(Date(longDate))
    }
}