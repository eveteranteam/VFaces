package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.questionnaire.create.CreateQuestionnaireActivity

class QuestionnaireListActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var fab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire_list)
        initUi()
    }

    private fun initUi() {
        recyclerView = findViewById(R.id.recycler_view_questionnaires)
        fab = findViewById(R.id.fab_new_questionnaire)
        fab.setOnClickListener { CreateQuestionnaireActivity.start(this) }
        findViewById<View>(R.id.text_view_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            AuthHostActivity.start(this)
            finish()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, QuestionnaireListActivity::class.java))
        }
    }
}
