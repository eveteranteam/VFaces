package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ua.gov.mva.vfaces.data.db.FirebaseDbChild
import ua.gov.mva.vfaces.data.db.child.UserDao
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel
import ua.gov.mva.vfaces.utils.ConnectionUtils

class ProfileViewModel : BaseViewModel() {

    private val db = FirebaseDatabase.getInstance().reference
    private val resultLiveData = MutableLiveData<ResultType>()

    var user : UserDao? = null

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    // TODO remove context!
    fun save(name: String, phone: String, work: String, selectedPosition : Int, context: Context) {
        showProgress()
        val authUser = FirebaseAuth.getInstance().currentUser
        val id = authUser!!.uid
        // Create own user and store him in Firebase Database
        val user = UserDao(id, name, authUser.email!!, phone, work, selectedPosition)

        // TODO should use better solution
        if (ConnectionUtils.isNetworkConnected(context)) {
            db.child(FirebaseDbChild.USERS)
                    .child(id)
                    .setValue(user)
                    .addOnCompleteListener { task ->
                        hideProgress()
                        if (task.isSuccessful) {
                            resultLiveData.value = ResultType.SAVE_SUCCESS
                        } else {
                            Log.e(TAG, "Error while saving profile. ${task.exception}")
                            resultLiveData.value = ResultType.SAVE_ERROR
                        }
                    }
        } else {
            db.child(FirebaseDbChild.USERS).child(id)
                    .setValue(user)

            // User will be saved
            hideProgress()
            resultLiveData.value = ResultType.SAVE_SUCCESS
        }
    }

    fun loadUser() {
        showProgress()
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        db.child(FirebaseDbChild.USERS).child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hideProgress()
                // It must be java class. Otherwise exception will be thrown.
                user = snapshot.getValue(UserDao::class.java)
                if (user == null) {
                    Log.d(TAG, "User == null. WTF?")
                    resultLiveData.value = ResultType.GET_ERROR
                } else {
                    resultLiveData.value = ResultType.GET_SUCCESS
                }
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgress()
                Log.e(TAG, "Can't get user. $error")
                resultLiveData.value = ResultType.GET_ERROR
            }
        })
    }

    enum class ResultType {
        SAVE_SUCCESS,
        SAVE_ERROR,
        GET_SUCCESS,
        GET_ERROR
    }

    private companion object {
        private const val TAG = "ProfileViewModel"
    }
}