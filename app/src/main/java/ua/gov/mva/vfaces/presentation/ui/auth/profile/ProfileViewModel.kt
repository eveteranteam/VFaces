package ua.gov.mva.vfaces.presentation.ui.auth.profile

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
                .addOnCompleteListener { task ->
                    hideProgress()
                    if (task.isSuccessful) {
                        resultLiveData.value = ResultType.SUCCESS
                    } else {
                        resultLiveData.value = ResultType.ERROR
                    }
                }
    }

    enum class ResultType {
        SUCCESS,
        ERROR
    }
}