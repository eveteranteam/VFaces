package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ua.gov.mva.vfaces.data.db.FirebaseDbChild
import ua.gov.mva.vfaces.data.db.child.UserDao
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel
import ua.gov.mva.vfaces.utils.ConnectionUtils

class ProfileViewModel : BaseViewModel() {

    private val db = FirebaseDatabase.getInstance().reference
    private val resultLiveData = MutableLiveData<ResultType>()

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    // TODO remove context!
    fun save(name: String, phone: String, work: String, context: Context) {
        showProgress()
        val authUser = FirebaseAuth.getInstance().currentUser
        val id = authUser!!.uid
        // Create own user and store him in Firebase Database
        val user = UserDao(id, name, authUser.email!!, phone, work)

        // TODO should use better solution
        if (ConnectionUtils.isNetworkConnected(context)) {
            db.child(FirebaseDbChild.USERS)
                    .child(id)
                    .setValue(user)
                    .addOnCompleteListener { task ->
                        hideProgress()
                        if (task.isSuccessful) {
                            resultLiveData.value = ResultType.SUCCESS
                        } else {
                            Log.e(TAG, "Error while saving profile. ${task.exception}")
                            resultLiveData.value = ResultType.ERROR
                        }
                    }
        } else {
            db.child(FirebaseDbChild.USERS).child(id)
                    .setValue(user)

            // User will be saved
            hideProgress()
            resultLiveData.value = ResultType.SUCCESS
        }
    }

    enum class ResultType {
        SUCCESS,
        NO_INTERNET,
        ERROR
    }

    private companion object {
        private const val TAG = "ProfileViewModel"
    }
}