package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.base.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListActivity

class ProfileFragment : BaseFragment<ProfileViewModel>(), OnBackPressedCallback {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_save_profile).setOnClickListener {
            QuestionnaireListActivity.start(context!!)
            activity!!.finish()
        }
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

    companion object {
        fun newInstance() : ProfileFragment {
            return ProfileFragment()
        }
    }
}
