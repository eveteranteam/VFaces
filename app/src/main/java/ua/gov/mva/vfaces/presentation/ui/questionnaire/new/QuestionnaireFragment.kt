package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment

class QuestionnaireFragment : BaseFragment<QuestionnaireViewModel>(), OnBackPressedCallback {

    override val TAG = "QuestionnaireFragment"

    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
    }

    override fun initViewModel(): QuestionnaireViewModel {
        viewModel = ViewModelProviders.of(this).get(QuestionnaireViewModel::class.java)
        return viewModel
    }

    // TODO
    override fun onBackPressed(): Boolean {
        return true
    }

    private fun initUi(view: View) {

    }

    companion object {
        fun newInstance() : QuestionnaireFragment {
            return QuestionnaireFragment()
        }
    }
}