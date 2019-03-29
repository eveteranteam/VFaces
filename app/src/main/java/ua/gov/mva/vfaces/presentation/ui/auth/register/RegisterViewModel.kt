package ua.gov.mva.vfaces.presentation.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val resultLiveData = MutableLiveData<ResultType>()

    fun resultLiveData() : LiveData<ResultType> = resultLiveData

    fun register(email: String, password: String) {
        showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    onUserCreated(task.result)
                    return@addOnCompleteListener
                }
                task.exception is FirebaseNetworkException -> {
                    hideProgress()
                    resultLiveData.value = ResultType.NO_INTERNET
                    return@addOnCompleteListener
                }
                task.exception is FirebaseAuthUserCollisionException -> {
                    hideProgress()
                    resultLiveData.value = ResultType.USER_COLLISION
                    return@addOnCompleteListener
                }
                else -> {
                    hideProgress()
                    resultLiveData.value = ResultType.ERROR
                }
            }
        }
    }

    private fun onUserCreated(result: AuthResult?) {
        if (result == null || result.user == null) {
            hideProgress()
            // Should never happen
            Log.e(TAG, "Registered successfully but result or user == null. WTF?")
            resultLiveData.value = ResultType.ERROR
            return
        }
        sendVerificationEmail(result.user)
    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            hideProgress()
            if (task.isSuccessful) {
                resultLiveData.value = ResultType.VERIFICATION_EMAIL_SENT
            } else {
                resultLiveData.value = ResultType.VERIFICATION_EMAIL_ERROR
            }
        }
    }

    enum class ResultType {
        VERIFICATION_EMAIL_SENT,
        VERIFICATION_EMAIL_ERROR,
        USER_COLLISION, // Email already used
        NO_INTERNET,
        ERROR // All other errors
    }

    private companion object {
        const val TAG = "RegisterViewModel"
    }
}