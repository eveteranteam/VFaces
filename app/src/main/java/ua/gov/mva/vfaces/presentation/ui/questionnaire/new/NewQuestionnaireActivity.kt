package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.ActionBarActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.view.LockableViewPager

class NewQuestionnaireActivity : ActionBarActivity() {

    override val TAG = "NewQuestionnaireActivity"
    override var dialog: AlertDialog? = null

    private lateinit var viewPager: LockableViewPager
    private lateinit var backButton : Button
    private lateinit var currentPage : TextView
    private lateinit var nextButton : Button
    private lateinit var backToListButton : Button

    private lateinit var adapter: QuestionnairePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_questionnaire)
        initUi()
        adapter = QuestionnairePagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        updateViewCounter(0) // Set initial counter value
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

    private fun updateViewCounter(index: Int) {
        currentPage.text = String.format(getString(R.string.new_questionnaire_current_page), index + 1, adapter.count)
    }

    private fun onBackClick() {
        var index = viewPager.currentItem
        if (index <= 0 ) {
            Log.e(TAG, "currentItem == $index. Can't get previous Fragment")
            return
        }
        viewPager.setCurrentItem(--index, true)
        val shouldShow = index != 0
        showBackButton(shouldShow)
        updateViewCounter(index)
    }

    private fun onNextClick() {
        var index = viewPager.currentItem
        if (index > adapter.count - 1) {
            Log.e(TAG, "currentItem == $index. ${adapter.count} Fragments at all. Can't get next Fragment")
            return
        }
        viewPager.setCurrentItem(++index, true)
        updateViewCounter(index)
        showBackButton(true)
        if (index == adapter.count - 1) {
            showBackToListButton()
        }
    }

    private fun showBackButton(show: Boolean) {
        backButton.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showActionButtons() {
        showBackButton(true)
        nextButton.visibility = View.VISIBLE
    }

    private fun showBackToListButton() {
        backButton.visibility = View.GONE
        nextButton.visibility = View.GONE
        backToListButton.visibility = View.VISIBLE
    }

    private fun initUi() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setBackIcon()
        setTitle(getString(R.string.new_questionnaire_title))
        viewPager = findViewById(R.id.view_pager)
        currentPage = findViewById(R.id.text_view_page)
        backButton = findViewById(R.id.button_back)
        nextButton = findViewById(R.id.button_next)
        backToListButton = findViewById(R.id.button_back_to_list)
        showBackButton(false)
        viewPager.isSwipeEnabled = false // Disable swipe
        viewPager.addOnPageChangeListener(PageChangeListener())
        backButton.setOnClickListener { onBackClick() }
        nextButton.setOnClickListener { onNextClick() }
        backToListButton.setOnClickListener { finish() }
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
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, NewQuestionnaireActivity::class.java))
        }
    }
}
