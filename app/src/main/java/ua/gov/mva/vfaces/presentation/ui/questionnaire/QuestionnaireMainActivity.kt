package ua.gov.mva.vfaces.presentation.ui.questionnaire

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.ActionBarActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListFragment

class QuestionnaireMainActivity : ActionBarActivity() {

    override val TAG = "QuestionnaireActivity"
    override var dialog: AlertDialog? = null

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_questionnaire)
        initUi()
        replaceFragment(QuestionnaireListFragment.newInstance())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "Drawer is open. Closing...")
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is OnBackPressedCallback && fragment.onBackPressed()) {
            return
        }
        // TODO fix when single fragment
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        }
    }

    private fun showExitAlertDialog() {
        if (isFinishing) {
            Log.w(TAG, "isFinishing(). Skipping...")
            return
        }
        dialog = AlertDialog.Builder(this)
                .setTitle(R.string.alert_dialog_exit_title)
                .setMessage(R.string.alert_dialog_exit_msg)
                .setPositiveButton(R.string.action_yes) { dialog, _ ->
                    dialog.dismiss()
                    signOut()
                }
                .setNegativeButton(R.string.action_no) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .create()
        dialog!!.show()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        AuthHostActivity.start(this@QuestionnaireMainActivity)
        finish()
    }

    private fun initUi() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setMenuIcon()
        drawerLayout = findViewById(R.id.drawer_layout)
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(NavigationItemSelectListener())
    }

    inner class NavigationItemSelectListener : NavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            drawerLayout.closeDrawers()
            handleSelection(item)

            return true
        }

        private fun handleSelection(menu: MenuItem) {
            // item.isChecked = true // TODO
            when (menu.itemId) {
                R.id.nav_profile -> {
                    // TODO
                }
                R.id.nav_exit -> {
                    showExitAlertDialog()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, QuestionnaireMainActivity::class.java))
        }
    }
}
