package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.QuestionnaireMainActivity
import ua.gov.mva.vfaces.utils.InputValidationUtils
import ua.gov.mva.vfaces.utils.KeyboardUtils
import ua.gov.mva.vfaces.utils.Preferences

class ProfileFragment : BaseFragment<ProfileViewModel>(), OnBackPressedCallback {

    override val TAG = "ProfileFragment"

    private lateinit var tilName: TextInputLayout
    private lateinit var textInputName: TextInputEditText
    private lateinit var tilPhone: TextInputLayout
    private lateinit var textPhone: TextInputEditText
    private lateinit var spinnerOrganizations: Spinner

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        viewModel.resultLiveData().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                ProfileViewModel.ResultType.SUCCESS -> onProfileSaved()
                ProfileViewModel.ResultType.ERROR -> showErrorMessage(R.string.profile_save_error)
            }
        })
    }

    override fun initViewModel(): ProfileViewModel {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        return viewModel
    }

    /**
     * Always return true.
     * Never allow user to return back from this Fragment.
     */
    override fun onBackPressed(): Boolean {
        return true
    }

    private fun onProfileSaved() {
        Preferences.putBoolean(PROFILE_SAVED_KEY, true)
        showToastMessage(R.string.profile_save_success)
        QuestionnaireMainActivity.start(context!!)
        activity!!.finish()
    }

    private fun onSaveClick() {
        val name = textInputName.text.toString().trim()
        if (InputValidationUtils.isNameValid(name)) {
            tilName.isErrorEnabled = false
        } else {
            tilName.error = getString(R.string.wrong_data_field)
            return
        }

        val phone = textPhone.text.toString().trim()
        if (InputValidationUtils.isPhoneValid(phone)) {
            tilPhone.isErrorEnabled = false
        } else {
            tilPhone.error = getString(R.string.wrong_number)
            return
        }

        if (spinnerOrganizations.selectedItemPosition == SPINNER_PROMPT_POSITION) {
            showErrorMessage(getString(R.string.profile_select_work_error))
            return
        }
        val work = spinnerOrganizations.selectedItem as String
        KeyboardUtils.hideKeyboard(activity)
        // In case profile data is valid
        viewModel.save(name, phone, work, context!!)
    }

    private fun initUi(view: View) {
        tilName = view.findViewById(R.id.text_input_layout_name)
        textInputName = view.findViewById(R.id.text_input_edit_text_name)
        tilPhone = view.findViewById(R.id.text_input_layout_phone_number)
        textPhone = view.findViewById(R.id.text_input_edit_text_phone_number)
        spinnerOrganizations = view.findViewById(R.id.spinner_work)
        // Setting up Spinner view
        val arrayAdapter = ArrayAdapter.createFromResource(context!!,
                R.array.profile_organisations_list,
                android.R.layout.simple_spinner_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrganizations.adapter = arrayAdapter

        view.findViewById<View>(R.id.button_save_profile).setOnClickListener {
             onSaveClick()
        }
    }

    companion object {
        /**
         * Used in [ua.gov.mva.vfaces.presentation.ui.SplashActivity] to verify whether user has filled his profile.
         */
        const val PROFILE_SAVED_KEY = "profile_saved_key"
        private const val SPINNER_PROMPT_POSITION = 0

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
