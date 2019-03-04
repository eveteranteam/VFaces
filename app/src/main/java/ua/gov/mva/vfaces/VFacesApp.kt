package ua.gov.mva.vfaces

import android.app.Application
import ua.gov.mva.vfaces.utils.Preferences

class VFacesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Preferences.init(applicationContext)
    }

    companion object {
        const val PREFS_FIRST_LAUNCH_KEY = "first_launch_key"
    }
}