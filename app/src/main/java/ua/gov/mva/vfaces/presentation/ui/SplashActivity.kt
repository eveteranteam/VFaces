package ua.gov.mva.vfaces.presentation.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ua.gov.mva.vfaces.VFacesApp
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.auth.profile.ProfileFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.QuestionnaireMainActivity
import ua.gov.mva.vfaces.utils.Preferences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedule()
        Preferences.putBoolean(VFacesApp.PREFS_FIRST_LAUNCH_KEY, false)
    }

    private fun schedule() {
        val duration = getSplashDuration()
        Handler().postDelayed({
            navigateNext()
            finish()
        }, duration)
    }

    /**
     * Method will navigate to next screen.
     * If user has filled his profile - Questionnaire list will be shown.
     * Otherwise user will be prompted to fill in his profile.
     */
    private fun navigateNext() {
        if (Preferences.getBoolean(ProfileFragment.PROFILE_SAVED_KEY, false)) {
            QuestionnaireMainActivity.start(this)
        } else {
            AuthHostActivity.start(this)
        }
    }

    private fun getSplashDuration(): Long {
        val res = Preferences.getBoolean(VFacesApp.PREFS_FIRST_LAUNCH_KEY, true)
        return when(res) {
            true -> 1000L
            false -> 500L
        }
    }
}