package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.gov.mva.vfaces.R

class NewQuestionnaireActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_questionnaire)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NewQuestionnaireActivity::class.java))
        }
    }
}
