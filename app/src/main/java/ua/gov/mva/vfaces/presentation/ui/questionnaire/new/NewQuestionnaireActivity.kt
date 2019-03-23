package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.BaseActivity

class NewQuestionnaireActivity : BaseActivity() {

    override val TAG = "NewQuestionnaireActivity"
    override var dialog: AlertDialog? = null

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
