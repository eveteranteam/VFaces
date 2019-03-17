package ua.gov.mva.vfaces.presentation.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.base.OnBackPressedCallback
import ua.gov.mva.vfaces.utils.InputValidationUtils
import ua.gov.mva.vfaces.utils.KeyboardUtils

class RegisterFragment : BaseFragment<RegisterViewModel>(), OnBackPressedCallback {

    private lateinit var scrollView: ScrollView
    private lateinit var verifyEmailView: View
    private lateinit var tilEmail: TextInputLayout
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var textInputPassword: TextInputEditText

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        viewModel.resultLiveData().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                RegisterViewModel.ResultType.VERIFICATION_EMAIL_SENT -> onRegistered()
                RegisterViewModel.ResultType.USER_COLLISION -> onEmailCollision()
                RegisterViewModel.ResultType.VERIFICATION_EMAIL_ERROR,
                RegisterViewModel.ResultType.ERROR ->
                    showErrorMessage(R.string.sign_up_error)
            }
        })
    }

    override fun initViewModel(): RegisterViewModel {
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        return viewModel
    }

    /**
     * Disable Back Press if user has been registered successfully.
     */
    override fun onBackPressed(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    private fun onRegistered() {
        val email = textInputEmail.text.toString().trim()
        val text = String.format(getString(R.string.sign_up_verify_email), email)
        view!!.findViewById<TextView>(R.id.text_view_verify_email_prompt).text = text
        scrollView.visibility = View.GONE
        verifyEmailView.visibility = View.VISIBLE
    }

    private fun onEmailCollision() {
        val email = textInputEmail.text.toString().trim()
        val msg = String.format(getString(R.string.sign_up_email_collision), email)
        showErrorMessage(msg)
    }

    private fun onRegisterClick() {
        val email = textInputEmail.text.toString().trim()
        // Validate email
        if (InputValidationUtils.isEmailValid(email)) {
            tilEmail.isErrorEnabled = false
        } else {
            tilEmail.error = getString(R.string.wrong_email)
            return
        }
        val password = textInputPassword.text.toString().trim()
        // Validate password
        if (InputValidationUtils.isPasswordValid(password)) {
            tilPassword.isErrorEnabled = false
        } else {
            tilPassword.error = getString(R.string.wrong_password)
            return
        }
        // If credentials are valid
        KeyboardUtils.hideKeyboard(activity)
        viewModel.register(email, password)
    }

    private fun initUi(view: View) {
        scrollView = view.findViewById(R.id.scroll_view)
        verifyEmailView = view.findViewById(R.id.verify_email_layout)
        tilEmail = view.findViewById(R.id.text_input_layout_email)
        tilPassword = view.findViewById(R.id.text_input_layout_password)
        textInputEmail = view.findViewById(R.id.text_input_edit_text_email)
        textInputPassword = view.findViewById(R.id.text_input_edit_text_password)
        view.findViewById<View>(R.id.button_register).setOnClickListener {
            onRegisterClick()
        }
        view.findViewById<View>(R.id.text_view_register_back).setOnClickListener {
            transaction.popBackStack()
        }
        view.findViewById<View>(R.id.button_done).setOnClickListener {
            transaction.popBackStack()
        }
    }

    companion object {
        fun newInstance() : RegisterFragment {
            return RegisterFragment()
        }
    }
}