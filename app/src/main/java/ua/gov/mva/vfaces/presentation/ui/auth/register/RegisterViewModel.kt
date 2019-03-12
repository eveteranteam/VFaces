package ua.gov.mva.vfaces.presentation.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val resultLiveData = MutableLiveData<ResultType>()

    fun registerLiveData() : LiveData<ResultType> = resultLiveData

    fun register(email: String, password: String) {
        showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            hideProgress()
            if (task.isSuccessful) {
                resultLiveData.value = ResultType.SUCCESS
            } else if (task.exception is FirebaseAuthUserCollisionException) {
                resultLiveData.value = ResultType.USER_COLLISION
            } else {
                resultLiveData.value = ResultType.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    enum class ResultType {
        SUCCESS,
        USER_COLLISION, // Email already used
        ERROR // All other errors
    }
}