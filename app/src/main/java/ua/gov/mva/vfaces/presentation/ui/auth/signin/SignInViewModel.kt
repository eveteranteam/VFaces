package ua.gov.mva.vfaces.presentation.ui.auth.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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
            when {
                task.isSuccessful -> handleSuccessfulSignIn(task.result)
                task.exception is FirebaseAuthInvalidUserException ||
                task.exception is FirebaseAuthInvalidCredentialsException -> resultLiveData.value = ResultType.INVALID_CREDENTIALS
                else -> resultLiveData.value = ResultType.ERROR
            }
        }
    }

    private fun handleSuccessfulSignIn(result: AuthResult?) {
        if (result == null || result.user == null) {
            // Shouldn't happen
            Log.e(TAG, "Sign in successful but result or user == null. WTF?")
            resultLiveData.value = ResultType.ERROR
            return
        }
        // Check whether user has verified his email
        if (result.user.isEmailVerified) {
            // Email is verified. Login
            resultLiveData.value = ResultType.SIGN_IN_SUCCESS
        } else {
            // Prompt user to verify his email.
            resultLiveData.value = ResultType.EMAIL_NOT_VERIFIED
        }
    }

    enum class ResultType {
        SIGN_IN_SUCCESS,
        INVALID_CREDENTIALS, // Email already used
        EMAIL_NOT_VERIFIED,
        ERROR // All other errors
    }

    private companion object {
        const val TAG = "SignInViewModel"
    }
}