package ua.gov.mva.vfaces.presentation.ui.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class SignInViewModel : BaseViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val resultLiveData = MutableLiveData<ResultType>()

    fun resultLiveData() : LiveData<ResultType> = resultLiveData

    fun signIn(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            hideProgress()
            if (task.isSuccessful) {
                resultLiveData.value = ResultType.SUCCESS
            } else if (task.exception is FirebaseAuthInvalidUserException) {
                resultLiveData.value = ResultType.INVALID_CREDENTIALS
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
        INVALID_CREDENTIALS, // Email already used
        ERROR // All other errors
    }
}