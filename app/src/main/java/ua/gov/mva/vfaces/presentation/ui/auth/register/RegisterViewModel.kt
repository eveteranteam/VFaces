package ua.gov.mva.vfaces.presentation.ui.auth.register

import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.presentation.ui.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {

            } else {

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}