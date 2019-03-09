package ua.gov.mva.vfaces.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun showKeyboard(context: Context?) {
        if (context == null) {
            return
        }
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyboard(context: Context?, view: View?) {
        if (context == null || view == null) {
            return
        }
        val inputMethod = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val binder = view.windowToken
        if (binder != null) {
            inputMethod.hideSoftInputFromWindow(binder, 0)
        }
    }
}
