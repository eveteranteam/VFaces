package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.data.mapper.QuestionnaireMapper
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.presentation.ui.base.activity.ActionBarActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter.QuestionnairePagerAdapter
import ua.gov.mva.vfaces.utils.KeyboardUtils
import ua.gov.mva.vfaces.utils.RawResourceReader
import ua.gov.mva.vfaces.utils.Strings
import ua.gov.mva.vfaces.view.LockableViewPager

class NewQuestionnaireActivity : ActionBarActivity(), QuestionnaireNavigationListener {

    override val TAG = "NewQuestionnaireActivity"
    override var dialog: AlertDialog? = null

    private lateinit var viewPager: LockableViewPager
    private lateinit var backButton: Button
    private lateinit var currentPage: TextView
    private lateinit var nextFinishButton: Button

    private lateinit var adapter: QuestionnairePagerAdapter
    private lateinit var data: Questionnaire

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_questionnaire)
        initUi()
        loadQuestionnaire() // TODO
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
        val fragment = adapter.currentFragment
        if (fragment is OnBackPressedCallback && fragment.onBackPressed()) {
            return
        }
        if (supportFragmentManager.backStackEntryCount > 1) {
            //super.onBackPressed()
        }
        // TODO
        super.onBackPressed()
    }

    /**
     * Overridden to clear also custom title counter.
     */
    override fun clear() {
        super.clear()
        findViewById<TextView>(R.id.text_view_page).text = Strings.EMPTY
    }

    override fun navigateBack() {
        var index = viewPager.currentItem
        if (index <= 0) {
            Log.e(TAG, "currentItem == $index. Can't get previous Fragment")
            return
        }
        KeyboardUtils.hideKeyboard(this)
        viewPager.setCurrentItem(--index, true)
        val shouldShow = index != 0
        showBackButton(shouldShow)
        showNextButton()
        updateViewCounter(index)
    }

    override fun navigateNext() {
        var index = viewPager.currentItem
        if (index > adapter.count - 1) {
            Log.e(TAG, "currentItem == $index. ${adapter.count} Fragments at all. Can't get next Fragment")
            return
        }
        viewPager.setCurrentItem(++index, true)
        updateViewCounter(index)
        showBackButton(true)
        if (isLastItem()) {
            showFinishButton()
        }
    }

    private fun trySave() {
        if (!isQuestionnaireCompleted()) {
            Log.d(TAG, "Questionnaire is not completed. Can't save")
            showNotCompletedAlertDialog()
            return
        }
        KeyboardUtils.hideKeyboard(this)
        saveCurrentBlock()
    }

    private fun updateViewCounter(index: Int) {
        currentPage.text = String.format(getString(R.string.new_questionnaire_current_page), index + 1, adapter.count)
    }

    // TODO remove
    private fun loadQuestionnaire() {
        // TODO Should be performed on worker thread
        val entity = Gson().fromJson(
            RawResourceReader
                .readTextFileFromRawResource(R.raw.questionnaire, this),
            ua.gov.mva.vfaces.data.entity.Questionnaire::class.java
        )

        data = QuestionnaireMapper().entityToModel(entity)
        adapter = QuestionnairePagerAdapter(data, supportFragmentManager)
        viewPager.adapter = adapter
        updateViewCounter(0) // Set initial counter value
    }

    private fun isQuestionnaireCompleted(): Boolean {
        val fragment = adapter.currentFragment
        return if (fragment is CompletionCallback) {
            val result = fragment.isQuestionnaireCompleted()
            Log.d(TAG, "isQuestionnaireCompleted = $result")
            return result
        } else {
            Log.e(TAG, "Fragment does not implement CompletionCallback")
            false
        }
    }

    private fun saveCurrentBlock() {
        val fragment = adapter.currentFragment
        if (fragment is SaveCallback) {
            val result = fragment.save()
            Log.d(TAG, "isSaved = $result")
        } else {
            Log.e(TAG, "Fragment does not implement SaveCallback")
        }
    }

    private fun showNotCompletedAlertDialog() {
        if (isFinishing) {
            Log.w(TAG, "isFinishing. Skipping...")
            return
        }
        dialog = AlertDialog.Builder(this)
            .setTitle(R.string.alert_dialog_exit_questionnaire_not_completed_title)
            .setMessage(R.string.alert_dialog_exit_questionnaire_not_completed_msg)
            .setPositiveButton(R.string.action_ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        dialog!!.show()
    }

    private fun showReturnBackAlertDialog() {
        if (isFinishing) {
            Log.w(TAG, "isFinishing. Skipping...")
            return
        }
        dialog = AlertDialog.Builder(this)
            .setTitle(R.string.alert_dialog_back_questionnaire_title)
            .setMessage(R.string.alert_dialog_back_questionnaire_msg)
            .setPositiveButton(R.string.action_yes) { dialog, _ ->
                dialog.dismiss()
                navigateBack()
            }
            .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        dialog!!.show()
    }

    private fun onFragmentSelected() {
        val fragment = adapter.currentFragment
        if (fragment is SelectCallback) {
            fragment.onSelected()
            Log.d(TAG, "onSelected")
        } else {
            Log.e(TAG, "Fragment does not implement SelectCallback")
        }
    }

    /**
     * @return true - if [viewPager] is currently showing his last Fragment.
     */
    private fun isLastItem(): Boolean {
        return viewPager.currentItem == adapter.count - 1
    }

    private fun showBackButton(show: Boolean) {
        backButton.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showNextButton() {
        nextFinishButton.text = getString(R.string.new_questionnaire_action_next)
    }

    private fun showFinishButton() {
        nextFinishButton.text = getString(R.string.new_questionnaire_action_finish)
    }

    private fun initUi() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setBackIcon()
        setTitle(getString(R.string.new_questionnaire_title))
        viewPager = findViewById(R.id.view_pager)
        currentPage = findViewById(R.id.text_view_page)
        backButton = findViewById(R.id.button_back)
        nextFinishButton = findViewById(R.id.button_next)
        showBackButton(false)
        viewPager.isSwipeEnabled = false // Disable swipe
        viewPager.addOnPageChangeListener(PageChangeListener())
        backButton.setOnClickListener {
            if (viewPager.currentItem != 0) { // if not first item
                showReturnBackAlertDialog()
                return@setOnClickListener
            }
        }
        nextFinishButton.setOnClickListener {
            if (isLastItem()) {
                findViewById<View>(R.id.actions).visibility = View.GONE
            }
            trySave()
        }
    }

    private inner class PageChangeListener : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
            // do nothing
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            // do nothing
        }

        override fun onPageSelected(position: Int) {
            updateViewCounter(position)
            onFragmentSelected()
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NewQuestionnaireActivity::class.java))
        }
    }
}

interface QuestionnaireNavigationListener {
    fun navigateNext()
    fun navigateBack()
}
