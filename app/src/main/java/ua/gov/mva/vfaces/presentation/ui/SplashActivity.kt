package ua.gov.mva.vfaces.presentation.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.VFacesApp.Companion.PREFS_FIRST_LAUNCH_KEY
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListActivity
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
        if (FirebaseAuth.getInstance().currentUser == null) {
            AuthHostActivity.start(this)
        } else {
            // TODO check if user has filled his profile data
            QuestionnaireListActivity.start(this)
        }
    }

    private fun getSplashDuration(): Long {
        val res = Preferences.getBoolean(PREFS_FIRST_LAUNCH_KEY, true)
        return when(res) {
            true -> 2500L
            false -> 1000L
        }
    }
}