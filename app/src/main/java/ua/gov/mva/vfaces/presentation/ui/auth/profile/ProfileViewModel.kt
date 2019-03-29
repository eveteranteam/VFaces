package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ua.gov.mva.vfaces.data.db.Collections
import ua.gov.mva.vfaces.data.db.collection.User
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class ProfileViewModel : BaseViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val resultLiveData = MutableLiveData<ResultType>()

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    fun save(name: String, phone: String, work: String) {
        showProgress()
        val authUser = FirebaseAuth.getInstance().currentUser
        val id = authUser!!.uid
        // Create own user and store him in Cloud Firestore
        val user = User(id, name, authUser.email!!, phone, work)

        db.collection(Collections.USERS)
                .document(id) // Document id is the same as user id
                .set(user)
                // TODO known issue with Cloud Firestore.
                // This listener might not be called when offline and try to add data to subcollection.
                // https://github.com/flutter/flutter/issues/21205
                .addOnCompleteListener { task ->
                    hideProgress()
                    if (task.isSuccessful) {
                        resultLiveData.value = ResultType.SUCCESS
                    } else {
                        resultLiveData.value = ResultType.ERROR
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error while saving profile. $it")
                    resultLiveData.value = ResultType.ERROR
                }
    }

    enum class ResultType {
        SUCCESS,
        ERROR
    }

    private companion object {
        private const val TAG = "ProfileViewModel"
    }
}