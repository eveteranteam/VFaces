package ua.gov.mva.vfaces.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun showKeyboard(activity: Activity?) {
        if (activity == null) {
            return
        }
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity == null) {
            return
        }
        val focus = activity.currentFocus
        if (focus != null) {
            val inputMethod = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }
}