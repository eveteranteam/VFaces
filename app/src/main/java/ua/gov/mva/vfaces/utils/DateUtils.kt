package ua.gov.mva.vfaces.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val TAG = "DateUtils"

    private val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("dd MMM YYYY hh:mm", Locale("ua", "UA"))
    }

    fun format(inputTime: Long) : String {
        if (inputTime <= 0) {
            Log.e(TAG, "Wrong inputTime. inputTime == $inputTime")
            return Strings.EMPTY
        }
        return dateFormat.format(Date(inputTime))
    }
}