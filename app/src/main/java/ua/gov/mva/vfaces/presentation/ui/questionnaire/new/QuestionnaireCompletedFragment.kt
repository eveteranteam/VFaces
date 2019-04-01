package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.SimpleBaseFragment

class QuestionnaireCompletedFragment : SimpleBaseFragment(), OnBackPressedCallback {

    override val TAG = "QuestionnaireCompletedFragment"

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarListener?.clear()
        setTitle(getString(R.string.completed_questionnaire_title))
        name = arguments?.getString(NAME_EXTRAS)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_questionnaire_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
    }

    override fun onBackPressed(): Boolean {
        activity!!.finish()
        return true
    }

    private fun initUi(view: View) {
        val text = String.format(getString(R.string.completed_questionnaire_completed_prompt), name)
        view.findViewById<TextView>(R.id.text_view_questionnaire_completed_prompt).text = text
        view.findViewById<View>(R.id.button_back_to_questionnaire_list).setOnClickListener {
            activity!!.finish()
        }
    }

    companion object {
        private const val NAME_EXTRAS = "name_extras_key"

        fun newInstance(name: String): QuestionnaireCompletedFragment {
            val args = Bundle()
            args.putString(NAME_EXTRAS, name)
            val fragment = QuestionnaireCompletedFragment()
            fragment.arguments = args
            return fragment
        }
    }
}