package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.ActionBarActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback

class NewQuestionnaireActivity : ActionBarActivity() {

    override val TAG = "NewQuestionnaireActivity"
    override var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_questionnaire)
        initUi()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isFinishing) {
            Log.e(TAG, "isFinishing(). Skipping...")
            return
        }
        if (dialog != null && dialog!!.isShowing) {
            Log.d(TAG, "Dialog is showing. Dismissing...")
            dialog!!.dismiss()
            return
        }
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is OnBackPressedCallback && fragment.onBackPressed()) {
            return
        }
        if (supportFragmentManager.backStackEntryCount > 1) {
            //super.onBackPressed()
        }
        // TODO
        super.onBackPressed()
    }

    private fun initUi() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setBackIcon()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NewQuestionnaireActivity::class.java))
        }
    }
}
