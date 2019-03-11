package ua.gov.mva.vfaces.presentation.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val registerLiveData = MutableLiveData<Boolean>()

    fun registerLiveData() : LiveData<Boolean> = registerLiveData

    fun register(email: String, password: String) {
        showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            hideProgress()
            registerLiveData.value = task.isSuccessful
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}