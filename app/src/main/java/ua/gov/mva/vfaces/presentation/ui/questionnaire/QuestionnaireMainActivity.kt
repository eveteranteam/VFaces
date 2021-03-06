package ua.gov.mva.vfaces.presentation.ui.questionnaire

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.domain.model.QuestionnaireType
import ua.gov.mva.vfaces.presentation.ui.auth.profile.ProfileFragment
import ua.gov.mva.vfaces.presentation.ui.base.activity.ActionBarActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListFragment

class QuestionnaireMainActivity : ActionBarActivity(), NavigationItemSelectListener {

    override val TAG = "QuestionnaireActivity"
    override var dialog: AlertDialog? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    /**
     * Listener to notify about changes in Firebase auth state.
     */
    private var authListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_questionnaire)
        initUi()
        replaceFragment(QuestionnaireListFragment.newInstance(QuestionnaireType.MAIN), addToBackStack = false)
        subscribeToFirebaseAuthStateChanges()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is OnBackPressedCallback && fragment.onBackPressed()) {
                    return true
                }
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

    override fun selectItemWith(id: Int) {
        navView.setCheckedItem(id)
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

    /**
     * Method handles auth state change.
     * If user == null - sign out user.
     */
    private fun subscribeToFirebaseAuthStateChanges() {
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            Log.d(TAG, "onAuthStateChanged. user == $user")
            if (user == null) {
                signOut()
            }
        }
    }

    private fun initUi() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setMenuIcon()
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(NavigationItemSelectListener())
    }

    inner class NavigationItemSelectListener : NavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            drawerLayout.closeDrawers()
            handleSelection(item)
            return true
        }

        private fun handleSelection(menu: MenuItem) {
            when (menu.itemId) {
                R.id.nav_questionnaire_main ->
                    replaceFragment(QuestionnaireListFragment.newInstance(QuestionnaireType.MAIN),
                        addToBackStack = false)
                R.id.nav_questionnaire_additional ->
                    replaceFragment(QuestionnaireListFragment.newInstance(QuestionnaireType.ADDITIONAL),
                        addToBackStack = false)

                R.id.nav_profile -> {
                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (fragment is ProfileFragment) {
                        Log.e(TAG, "ProfileFragment is in stack. Will not replace. Skipping...")
                    } else {
                        replaceFragment(ProfileFragment.newInstance(isFromMainScreen = true))
                    }
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
internal interface NavigationItemSelectListener {
    fun selectItemWith(id : Int)
}