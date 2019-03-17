package ua.gov.mva.vfaces.presentation.ui.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.base.OnBackPressedCallback
import ua.gov.mva.vfaces.utils.InputValidationUtils
import ua.gov.mva.vfaces.utils.KeyboardUtils

class ForgotPasswordFragment : BaseFragment<ForgotPasswordViewModel>(), OnBackPressedCallback {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var textInputEmail: TextInputEditText

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        viewModel.resultLiveData().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                ForgotPasswordViewModel.ResultType.SUCCESS -> onResetEmailSent()
                ForgotPasswordViewModel.ResultType.INVALID_EMAIL -> showErrorMessage(R.string.reset_password_invalid_email)
                ForgotPasswordViewModel.ResultType.ERROR -> showErrorMessage(R.string.reset_password_error)
            }
        })
    }

    override fun initViewModel(): ForgotPasswordViewModel {
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        return viewModel
    }

    /**
     * Disable Back Press if email has been sent successfully.
     */
    override fun onBackPressed(): Boolean {
        return viewModel.resultLiveData().value == ForgotPasswordViewModel.ResultType.SUCCESS
    }

    private fun onResetEmailSent() {
        val view = view!!
        view.findViewById<View>(R.id.text_view_reset_pass_prompt).visibility = View.INVISIBLE
        view.findViewById<View>(R.id.card_view_reset_password).visibility = View.INVISIBLE
        view.findViewById<View>(R.id.text_view_forgot_pass_back).visibility = View.INVISIBLE
        val textView = view.findViewById<TextView>(R.id.text_view_reset_success_prompt)
        val email = textInputEmail.text.toString().trim()
        textView.text = String.format(getString(R.string.reset_password_success), email)
        textView.visibility = View.VISIBLE
        view.findViewById<View>(R.id.button_done).visibility = View.VISIBLE
    }

    private fun onResetPasswordClick() {
        val email = textInputEmail.text.toString().trim()
        // Validate email
        if (InputValidationUtils.isEmailValid(email)) {
            tilEmail.isErrorEnabled = false
        } else {
            tilEmail.error = getString(R.string.wrong_email)
            return
        }
        // If email is valid
        KeyboardUtils.hideKeyboard(activity)
        viewModel.resetPassword(email)
    }

    private fun initUi(view: View) {
        tilEmail = view.findViewById(R.id.text_input_layout_email)
        textInputEmail = view.findViewById(R.id.text_input_edit_text_email)
        view.findViewById<View>(R.id.button_done).setOnClickListener {
            transaction.popBackStack()
        }
        view.findViewById<View>(R.id.text_view_forgot_pass_back).setOnClickListener {
            transaction.popBackStack()
        }
        view.findViewById<View>(R.id.button_reset_password).setOnClickListener {
            onResetPasswordClick()
        }
    }

    companion object {
        fun newInstance() : ForgotPasswordFragment {
            return ForgotPasswordFragment()
        }
    }
}
