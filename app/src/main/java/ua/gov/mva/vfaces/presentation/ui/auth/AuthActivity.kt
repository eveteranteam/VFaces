package ua.gov.mva.vfaces.presentation.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.gov.mva.vfaces.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    companion object {
        @JvmStatic fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }
}
