package ua.gov.mva.vfaces

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import ua.gov.mva.vfaces.utils.Preferences

class VFacesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Preferences.init(applicationContext)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    companion object {
        const val TAG = "VFacesApp"
        const val PREFS_FIRST_LAUNCH_KEY = "first_launch_key"
    }
}