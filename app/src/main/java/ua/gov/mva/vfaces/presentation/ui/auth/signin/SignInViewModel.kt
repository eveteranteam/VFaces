package ua.gov.mva.vfaces.presentation.ui.auth.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import ua.gov.mva.vfaces.data.db.Collections
import ua.gov.mva.vfaces.data.db.collection.User
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class SignInViewModel : BaseViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val resultLiveData = MutableLiveData<ResultType>()

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    fun signIn(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> handleSuccessfulSignIn(task.result)
                task.exception is FirebaseAuthInvalidUserException ||
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                    hideProgress()
                    resultLiveData.value = ResultType.INVALID_CREDENTIALS
                }
                else -> {
                    hideProgress()
                    resultLiveData.value = ResultType.ERROR
                }
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
        val user = result.user
        // Check whether user has verified his email
        if (user.isEmailVerified) {
            // Check if user has filled his profile data
            getUser(user.uid)
        } else {
            // Prompt user to verify his email.
            resultLiveData.value = ResultType.EMAIL_NOT_VERIFIED
        }
    }

    /**
     * Get user from Cloud Firestore.
     *
     * If user != null - profile exists.
     * Otherwise profile isn't filled in yet.
     */
    private fun getUser(id: String) {
        db.collection(Collections.USERS)
                .document(id)
                .get()
                .addOnCompleteListener { task ->
                    hideProgress()
                    // It must be java class. Otherwise exception will be thrown.
                    val user = task.result?.toObject(User::class.java)
                    if (user == null) {
                        Log.d(TAG, "User == null. Profile is not filled in")
                        resultLiveData.value = ResultType.SUCCESS_PROFILE_NOT_FILLED
                    } else {
                        resultLiveData.value = ResultType.SUCCESS_PROFILE_FILLED
                    }
                }
                .addOnFailureListener {
                    hideProgress()
                    Log.e(TAG, "Can't get user. $it")
                    resultLiveData.value = ResultType.ERROR
                }
    }

    enum class ResultType {
        SUCCESS_PROFILE_FILLED,
        SUCCESS_PROFILE_NOT_FILLED,
        INVALID_CREDENTIALS, // Email already used
        EMAIL_NOT_VERIFIED,
        ERROR // All other errors
    }

    private companion object {
        const val TAG = "SignInViewModel"
    }
}