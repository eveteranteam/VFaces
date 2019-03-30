package ua.gov.mva.vfaces.presentation.ui.base.activity

import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ua.gov.mva.vfaces.R

abstract class BaseActivity : AppCompatActivity(), IFragmentTransaction {

    protected abstract val TAG: String
    protected abstract var dialog : AlertDialog?

    override fun onPause() {
        dialog?.dismiss()
        dialog = null
        super.onPause()
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    override fun popBackStack() {
        val count = supportFragmentManager.backStackEntryCount
        if (count <= 0) {
            Log.d(TAG, "Can't pop back stack. Entry count = $count")
            return
        }
        supportFragmentManager.popBackStack()
    }
}