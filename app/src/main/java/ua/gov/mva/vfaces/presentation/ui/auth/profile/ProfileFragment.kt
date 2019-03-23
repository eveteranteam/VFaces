package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.questionnaire.QuestionnaireActivity
import ua.gov.mva.vfaces.utils.InputValidationUtils
import ua.gov.mva.vfaces.utils.KeyboardUtils

class ProfileFragment : BaseFragment<ProfileViewModel>(), OnBackPressedCallback {

    override val TAG = "ProfileFragment"

    private lateinit var tilName: TextInputLayout
    private lateinit var textInputName: TextInputEditText
    private lateinit var tilWork: TextInputLayout
    private lateinit var textWork: TextInputEditText
    private lateinit var tilPhone: TextInputLayout
    private lateinit var textPhone: TextInputEditText

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
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

    private fun onSaveClick() {
        val name = textInputName.text.toString().trim()
        if (InputValidationUtils.isNameValid(name)) {
            tilName.isErrorEnabled = false
        } else {
            tilName.error = getString(R.string.wrong_data_field)
            return
        }

        val work = textWork.text.toString().trim()
        if (InputValidationUtils.isWorkValid(work)) {
            tilWork.isErrorEnabled = false
        } else {
            tilWork.error = getString(R.string.wrong_data_field)
            return
        }

        val phone = textPhone.text.toString().trim()
        if (InputValidationUtils.isPhoneValid(phone)) {
            tilPhone.isErrorEnabled = false
        } else {
            tilPhone.error = getString(R.string.wrong_number)
            return
        }
        KeyboardUtils.hideKeyboard(activity)
        // In case profile data is valid
        viewModel.save(name, work, phone)
    }

    private fun initUi(view: View) {
        tilName = view.findViewById(R.id.text_input_layout_name)
        textInputName = view.findViewById(R.id.text_input_edit_text_name)
        tilWork = view.findViewById(R.id.text_input_layout_work)
        textWork = view.findViewById(R.id.text_input_edit_text_work)
        tilPhone = view.findViewById(R.id.text_input_layout_phone_number)
        textPhone = view.findViewById(R.id.text_input_edit_text_phone_number)

        view.findViewById<View>(R.id.button_save_profile).setOnClickListener {
           // onSaveClick()
            QuestionnaireActivity.start(context!!)
            activity!!.finish()
        }
    }

    companion object {
        fun newInstance() : ProfileFragment {
            return ProfileFragment()
        }
    }
}
