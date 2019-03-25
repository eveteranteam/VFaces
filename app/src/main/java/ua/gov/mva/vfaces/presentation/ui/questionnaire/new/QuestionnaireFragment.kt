package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter.MainRecyclerAdapter

class QuestionnaireFragment : BaseFragment<QuestionnaireViewModel>(), OnBackPressedCallback {

    override val TAG = "QuestionnaireFragment"

    private lateinit var recyclerViewContent: RecyclerView
    private lateinit var viewModel: QuestionnaireViewModel
    private lateinit var data: Block

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getParcelable(BLOCK_EXTRAS)!!
    }

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
        view.findViewById<TextView>(R.id.text_view_title).text = data.title
        recyclerViewContent = view.findViewById(R.id.recycler_view_content)
        recyclerViewContent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewContent.adapter = MainRecyclerAdapter(data.items)
    }

    companion object {
        private const val BLOCK_EXTRAS = "block_extras_key"

        fun newInstance(block: Block): QuestionnaireFragment {
            val args = Bundle()
            args.putParcelable(BLOCK_EXTRAS, block)
            val fragment = QuestionnaireFragment()
            fragment.arguments = args
            return fragment
        }
    }
}