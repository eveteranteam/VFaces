package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R

class QuestionnaireListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire_list)
        findViewById<View>(R.id.text_view_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, QuestionnaireListActivity::class.java))
        }
    }
}
