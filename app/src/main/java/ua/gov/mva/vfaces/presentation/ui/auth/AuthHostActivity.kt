package ua.gov.mva.vfaces.presentation.ui.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.profile.ProfilePromptFragment
import ua.gov.mva.vfaces.presentation.ui.auth.signin.SignInFragment
import ua.gov.mva.vfaces.presentation.ui.base.activity.BaseActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback

class AuthHostActivity : BaseActivity() {

    override val TAG = "AuthHostActivity"
    override var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        // TODO
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null || !user.isEmailVerified) {
            replaceFragment(SignInFragment.newInstance())
        } else {
            replaceFragment(ProfilePromptFragment.newInstance())
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is OnBackPressedCallback && fragment.onBackPressed()) {
            return
        }
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        }
    }

    override fun popBackStack() {
        val count = supportFragmentManager.backStackEntryCount
        if (count <= 0) {
            Log.d(TAG, "Can't pop back stack. Entry count = $count")
            return
        }
        supportFragmentManager.popBackStack()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthHostActivity::class.java))
        }
    }
}
