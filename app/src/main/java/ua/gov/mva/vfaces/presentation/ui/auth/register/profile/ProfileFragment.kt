package ua.gov.mva.vfaces.presentation.ui.auth.register.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListActivity

class ProfileFragment : BaseFragment<ProfileViewModel>() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_finish_registration).setOnClickListener {
            QuestionnaireListActivity.start(context!!)
            activity!!.finish()
        }
    }

    override fun initViewModel(): ProfileViewModel {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        return viewModel
    }
}
