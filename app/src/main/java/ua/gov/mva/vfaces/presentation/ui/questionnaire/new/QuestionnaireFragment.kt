package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewHolder
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter.DataValidator
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter.MainRecyclerAdapter

class QuestionnaireFragment : BaseFragment<QuestionnaireViewModel>(), OnBackPressedCallback,
        CompletionCallback, SaveCallback {

    override val TAG = "QuestionnaireFragment"

    private var dialog: AlertDialog? = null

    private lateinit var navigationListener: QuestionnaireNavigationListener
    private lateinit var recyclerViewContent: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter
    private lateinit var viewModel: QuestionnaireViewModel

    private lateinit var questionnaire: Questionnaire
    private lateinit var data: Block
    private var position: Int = 0
    private var isLast : Boolean = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigationListener = context as QuestionnaireNavigationListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionnaire = arguments?.getParcelable(QUESTIONNAIRE_EXTRAS)!!
        position = arguments?.getInt(POSITION_EXTRAS)!!
        isLast = arguments?.getBoolean(IS_LAST_EXTRAS)!!
        data = questionnaire.blocks!![position]
        viewModel.block = data
        viewModel.questionnaire = questionnaire
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        viewModel.resultLiveData().observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                QuestionnaireViewModel.ResultType.SAVE_SUCCESS -> {
                    navigateNext()
                    return@Observer
                }
                QuestionnaireViewModel.ResultType.SAVE_ERROR -> {
                    showErrorMessage(R.string.new_questionnaire_save_error)
                    return@Observer
                }
            }
        })
    }

    override fun onPause() {
        dialog?.dismiss()
        dialog = null
        super.onPause()
    }

    override fun initViewModel(): QuestionnaireViewModel {
        viewModel = ViewModelProviders.of(this).get(QuestionnaireViewModel::class.java)
        return viewModel
    }

    /**
     * Method checks whether questionnaire is completed.
     * If completed - pop back stack
     * Otherwise - show Alert Dialog.
     */
    override fun onBackPressed(): Boolean {
        showBackToListDialog()
        return true
    }

    override fun isQuestionnaireCompleted(): Boolean {
        if (!isAdded) {
            Log.w(TAG, "isAdded == false. Skipping...")
            return false
        }
        return isInputDataValid()
    }

    override fun save() {
        val answeredItems = arrayListOf<Item>()
        data.items!!.forEachIndexed { index, _ ->
            val viewHolder = recyclerViewContent.findViewHolderForAdapterPosition(index)
            if (viewHolder is DataValidator<*>) {
                answeredItems.add(viewHolder.getAnswer() as Item)
            }
        }
        data.items = answeredItems
        Log.d(TAG, "answers: ${answeredItems.size}")
        viewModel.save(answeredItems, position, context!!)
    }

    private fun navigateNext() {
        if (isLast) {
            transaction.replaceFragment(QuestionnaireCompletedFragment.newInstance(questionnaire.name))
        } else {
            navigationListener.navigateNext()
        }
    }

    /**
     * Check whether user has Entered/Selected all data in Questionnaire.
     *
     * !Note!
     * Method will iterate through all RecyclerViews and his ViewHolders, including nested if any.
     * For data to be valid every ViewHolder will be checked.
     *
     * @see BaseViewHolder.isDataValid
     *
     * @return true - if every implementation of [BaseViewHolder.isDataValid] returns true, false - otherwise.
     */
    private fun isInputDataValid(): Boolean {
        var result = true
        adapter.items!!.forEachIndexed { index, _ ->
            val viewHolder = recyclerViewContent.findViewHolderForAdapterPosition(index)
            if (viewHolder is DataValidator<*>) {
                // If data is not valid in at least one ViewHolder - all data is not valid.
                if (!viewHolder.isDataValid()) {
                    result = false
                    return@forEachIndexed
                }
            }
        }
        Log.d(TAG, "isInputDataValid == $result")
        return result
    }

    private fun showBackToListDialog() {
        if (!isAdded) {
            Log.w(TAG, "isAdded == false. Skipping...")
            return
        }
        dialog = AlertDialog.Builder(context!!)
                .setTitle(R.string.alert_dialog_back_to_list_questionnaire_title)
                .setMessage(R.string.alert_dialog_back_to_list_questionnaire_msg)
                .setPositiveButton(R.string.action_yes) { dialog, _ ->
                    dialog.dismiss()
                    activity!!.finish()
                }
                .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .create()
        dialog!!.show()
    }

    private fun initUi(view: View) {
        view.findViewById<TextView>(R.id.text_view_title).text = data.title
        recyclerViewContent = view.findViewById(R.id.recycler_view_content)
        recyclerViewContent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = MainRecyclerAdapter(data)
        recyclerViewContent.adapter = adapter
    }

    companion object {
        private const val QUESTIONNAIRE_EXTRAS = "block_extras_key"
        private const val POSITION_EXTRAS = "position_extras_key"
        private const val IS_LAST_EXTRAS = "is_last_extras_key"

        fun newInstance(questionnaire: Questionnaire, position: Int, isLast : Boolean): QuestionnaireFragment {
            val args = Bundle()
            args.putParcelable(QUESTIONNAIRE_EXTRAS, questionnaire)
            args.putInt(POSITION_EXTRAS, position)
            args.putBoolean(IS_LAST_EXTRAS, isLast)
            val fragment = QuestionnaireFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

interface CompletionCallback {
    fun isQuestionnaireCompleted(): Boolean
}

interface SaveCallback {
    fun save()
}