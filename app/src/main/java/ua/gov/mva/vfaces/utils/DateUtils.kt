package ua.gov.mva.vfaces.utils

import android.content.Context
import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val TAG = "DateUtils"

    fun format(inputTime: Long, locale: Locale): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", locale)
        if (inputTime <= 0) {
            Log.e(TAG, "Wrong inputTime. inputTime == $inputTime")
            return Strings.EMPTY
        }
        return dateFormat.format(Date(inputTime))
    }

    fun getLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }
}