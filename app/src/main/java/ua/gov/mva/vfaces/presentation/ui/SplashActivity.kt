package ua.gov.mva.vfaces.presentation.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ua.gov.mva.vfaces.VFacesApp.Companion.PREFS_FIRST_LAUNCH_KEY
import ua.gov.mva.vfaces.presentation.ui.auth.AuthActivity
import ua.gov.mva.vfaces.utils.Preferences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedule()
    }

    private fun schedule() {
        val duration = getSplashDuration()
        Handler().postDelayed({
            navigateNext()
            finish()
        }, duration)
    }

    private fun navigateNext() {
        // TODO check if logged in
        AuthActivity.start(this)
    }

    private fun getSplashDuration(): Long {
        val res = Preferences.getBoolean(PREFS_FIRST_LAUNCH_KEY, true)
        return when(res) {
            true -> 2500L
            false -> 1000L
        }
    }
}