package ua.gov.mva.vfaces.presentation.ui.auth.profile

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import ua.gov.mva.vfaces.data.db.Collections
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class ProfileViewModel : BaseViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val user = HashMap<String, Any>()

    private val resultLiveData = MutableLiveData<ResultType>()

    fun save(name: String, work: String, phone: String) {
        // TODO
        user["name"] = name
        user["work"] = work
        user["phone"] = phone

        db.collection(Collections.USERS)
                .add(user)
                .addOnCompleteListener { task ->
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