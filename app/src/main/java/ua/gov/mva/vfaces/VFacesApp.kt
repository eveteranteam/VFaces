package ua.gov.mva.vfaces

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.utils.Preferences

class VFacesApp : Application() {

    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate() {
        super.onCreate()
        Preferences.init(applicationContext)
        subscribeToFirebaseAuthStateChanges()
    }

    private fun subscribeToFirebaseAuthStateChanges() {
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            Log.d(TAG, "onAuthStateChanged. user == $user")
            if (user == null) {
                AuthHostActivity.start(this)
                // TODO finish current activity
            }
        }
    }

    companion object {
        const val TAG = "VFacesApp"
        const val PREFS_FIRST_LAUNCH_KEY = "first_launch_key"
    }
}