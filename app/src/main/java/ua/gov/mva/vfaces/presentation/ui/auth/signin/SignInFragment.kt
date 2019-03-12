package ua.gov.mva.vfaces.presentation.ui.auth.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseFragment
import ua.gov.mva.vfaces.utils.InputValidationUtils
import ua.gov.mva.vfaces.utils.KeyboardUtils

class SignInFragment : BaseFragment<SignInViewModel>() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var textInputPassword: TextInputEditText

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        viewModel.resultLiveData().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                SignInViewModel.ResultType.SUCCESS -> onSignedIn()
                SignInViewModel.ResultType.INVALID_CREDENTIALS -> showErrorMessage(R.string.sign_in_wrong_credentials)
                SignInViewModel.ResultType.ERROR -> showErrorMessage(R.string.sign_in_error)
            }
        })
    }

    override fun initViewModel(): SignInViewModel {
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        return viewModel
    }

    private fun onSignedIn() {
        val email = textInputEmail.text.toString().trim()
        val msg = String.format(getString(R.string.sign_in_success), email)
        showMessage(msg)
        // TODO check if user has filled his profile data
        Navigation.findNavController(view!!).navigate(R.id.profileFragment)
    }

    private fun onSignInClick() {
        val email = textInputEmail.text.toString().trim()
        val password = textInputPassword.text.toString().trim()
        // Validate email
        if (InputValidationUtils.isEmailValid(email)) {
            tilEmail.isErrorEnabled = false
        } else {
            tilEmail.error = getString(R.string.wrong_email)
            return
        }
        // Validate password
        if (InputValidationUtils.isPasswordValid(password)) {
            tilPassword.isErrorEnabled = false
        } else {
            tilPassword.error = getString(R.string.wrong_password)
            return
        }
        // If credentials are valid
        KeyboardUtils.hideKeyboard(context!!, view!!)
        viewModel.signIn(email, password)
    }

    private fun initUi(view: View) {
        tilEmail = view.findViewById(R.id.text_input_layout_email)
        tilPassword = view.findViewById(R.id.text_input_layout_password)
        textInputEmail = view.findViewById(R.id.text_input_edit_text_email)
        textInputPassword = view.findViewById(R.id.text_input_edit_text_password)
        view.findViewById<View>(R.id.button_sign_in).setOnClickListener {
            onSignInClick()
        }
        view.findViewById<View>(R.id.text_view_forget_password).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.forgotPasswordFragment)
        }
        view.findViewById<View>(R.id.text_view_register).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.registerFragment)
        }
    }
}
