package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.SimpleBaseFragment

class QuestionnaireCompletedFragment : SimpleBaseFragment(), OnBackPressedCallback {

    override val TAG = "QuestionnaireCompletedFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarListener?.clear()
        setTitle(getString(R.string.completed_questionnaire_title))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_questionnaire_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
    }

    // TODO
    override fun onBackPressed(): Boolean {
        return true
    }

    private fun initUi(view: View) {
        view.findViewById<View>(R.id.button_back_to_questionnaire_list).setOnClickListener {
            activity!!.finish()
        }
    }

    companion object {
        fun newInstance(): QuestionnaireCompletedFragment {
            return QuestionnaireCompletedFragment()
        }
    }
}