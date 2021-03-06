package ua.gov.mva.vfaces.presentation.ui.base.activity

import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.auth.profile.ProfileFragment
import ua.gov.mva.vfaces.utils.Preferences

abstract class BaseActivity : AppCompatActivity(), IFragmentTransaction {

    protected abstract val TAG: String
    protected abstract var dialog: AlertDialog?

    override fun onPause() {
        dialog?.dismiss()
        dialog = null
        super.onPause()
    }

    override fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val tag = fragment::class.java.simpleName
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
        }
        transaction.commit()
    }

    override fun popBackStack() {
        val count = supportFragmentManager.backStackEntryCount
        if (count <= 0) {
            Log.d(TAG, "Can't pop back stack. Entry count = $count")
            return
        }
        supportFragmentManager.popBackStack()
    }

    protected fun signOut() {
        FirebaseAuth.getInstance().signOut()
        Preferences.putBoolean(ProfileFragment.PROFILE_SAVED_KEY, false)
        AuthHostActivity.start(this)
        finish()
    }
}