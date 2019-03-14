package ua.gov.mva.vfaces.presentation.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.register.profile.ProfileFragment
import ua.gov.mva.vfaces.presentation.ui.base.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.base.OnBackPressedCallback
import ua.gov.mva.vfaces.utils.InputValidationUtils
import ua.gov.mva.vfaces.utils.KeyboardUtils

class RegisterFragment : BaseFragment<RegisterViewModel>(), OnBackPressedCallback {

    private lateinit var scrollView: ScrollView
    private lateinit var fillProfileView: View
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
                RegisterViewModel.ResultType.SUCCESS -> onRegistered()
                RegisterViewModel.ResultType.USER_COLLISION -> onEmailCollision()
                RegisterViewModel.ResultType.ERROR -> showErrorMessage(R.string.sign_up_error)
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
        val msg = String.format(getString(R.string.sign_up_success), email)
        showMessage(msg)
        scrollView.visibility = View.GONE
        fillProfileView.visibility = View.VISIBLE
    }

    private fun onEmailCollision() {
        val email = textInputEmail.text.toString().trim()
        val msg = String.format(getString(R.string.sign_up_email_collision), email)
        showMessage(msg)
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
        KeyboardUtils.hideKeyboard(context!!, view!!)
        viewModel.register(email, password)
    }

    private fun initUi(view: View) {
        scrollView = view.findViewById(R.id.scroll_view)
        fillProfileView = view.findViewById(R.id.fill_profile_layout)
        tilEmail = view.findViewById(R.id.text_input_layout_email)
        tilPassword = view.findViewById(R.id.text_input_layout_password)
        textInputEmail = view.findViewById(R.id.text_input_edit_text_email)
        textInputPassword = view.findViewById(R.id.text_input_edit_text_password)
        view.findViewById<View>(R.id.button_register).setOnClickListener {
            onRegisterClick()
        }
        view.findViewById<View>(R.id.button_fill_profile).setOnClickListener {
            transaction.replaceFragment(ProfileFragment.newInstance())
        }
        view.findViewById<View>(R.id.text_view_register_back).setOnClickListener {
            transaction.popBackStack()
        }
    }

    companion object {
        fun newInstance() : RegisterFragment {
            return RegisterFragment()
        }
    }
}