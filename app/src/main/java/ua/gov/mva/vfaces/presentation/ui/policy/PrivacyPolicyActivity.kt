package ua.gov.mva.vfaces.presentation.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.auth.profile.ProfileFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.QuestionnaireMainActivity
import ua.gov.mva.vfaces.utils.Preferences

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        initUi()
    }

    private fun initUi() {
        val webView = findViewById<WebView>(R.id.web_view)
        webView.loadData(getString(R.string.privacy_policy_msg), "text/html",
                "utf-8")
        findViewById<View>(R.id.button_decline).setOnClickListener { finish() }
        findViewById<View>(R.id.button_accept).setOnClickListener {
            Preferences.putBoolean(PRIVACY_POLICY_ACCEPTED_KEY, true)
            navigateNext() }
    }

    private fun navigateNext() {
        if (Preferences.getBoolean(ProfileFragment.PROFILE_SAVED_KEY, false)) {
            QuestionnaireMainActivity.start(this)
        } else {
            AuthHostActivity.start(this)
        }
    }

    companion object {
        const val PRIVACY_POLICY_ACCEPTED_KEY = "privacy_policy_accepted_key"

        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }
    }
}
