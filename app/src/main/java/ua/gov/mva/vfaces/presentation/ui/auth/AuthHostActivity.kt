package ua.gov.mva.vfaces.presentation.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.register.profile.ProfileFragment
import ua.gov.mva.vfaces.presentation.ui.auth.signin.SignInFragment
import ua.gov.mva.vfaces.presentation.ui.base.IFragmentTransaction
import ua.gov.mva.vfaces.presentation.ui.base.OnBackPressedCallback

class AuthHostActivity : AppCompatActivity(), IFragmentTransaction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        // TODO
        if (FirebaseAuth.getInstance().currentUser == null) {
            replaceFragment(SignInFragment.newInstance())
        } else {
            replaceFragment(ProfileFragment.newInstance())
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

    companion object {
        const val TAG = "AuthHostActivity"
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthHostActivity::class.java))
        }
    }
}
