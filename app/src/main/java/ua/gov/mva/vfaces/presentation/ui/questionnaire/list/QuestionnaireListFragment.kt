package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.domain.model.QuestionnaireType
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListViewModel.ResultType
import ua.gov.mva.vfaces.presentation.ui.questionnaire.list.QuestionnaireListViewModel.SortType
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.NewQuestionnaireActivity

class QuestionnaireListFragment : BaseFragment<QuestionnaireListViewModel>(),
    QuestionnaireListAdapter.OnItemClickListener {

    override val TAG = "QuestionnaireListFragment"
    private var dialog: AlertDialog? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionnaireListAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var viewModel: QuestionnaireListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.type = QuestionnaireType.values()[arguments?.getInt(TYPE_EXTRAS)!!]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questionnaire_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = if (viewModel.type == QuestionnaireType.MAIN)
            getString(R.string.questionnaire_list_title) else getString(R.string.questionnaire_list_family_title)
        setTitle(title)
        initUi(view)
        viewModel.resultLiveData().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                ResultType.SUCCESS -> showResults()
                ResultType.NO_RESULTS -> showNoResultsView()
                ResultType.ERROR -> showErrorMessage(R.string.questionnaire_list_load_error)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadQuestionnaires()
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.sort_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val result: Boolean
        when (item?.itemId) {
            R.id.action_one -> {
                viewModel.sortResultsBy(SortType.TIMESTAMP)
                result = true
            }
            R.id.action_two -> {
                viewModel.sortResultsBy(SortType.NOT_COMPLETED)
                result = true
            }
            R.id.action_three -> {
                viewModel.sortResultsBy(SortType.NAME)
                result = true
            }
            R.id.action_four -> {
                viewModel.sortResultsBy(SortType.SETTLEMENT)
                result = true
            }
            R.id.action_five -> {
                viewModel.sortResultsBy(SortType.COMPLETED)
                result = true
            }
            else -> {
                Log.e(TAG, "Unknown itemId. itemId == ${item?.itemId}")
                result = false
            }
        }
        if (result) {
            viewModel.update()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViewModel(): QuestionnaireListViewModel {
        viewModel = ViewModelProviders.of(this).get(QuestionnaireListViewModel::class.java)
        return viewModel
    }

    override fun onClick() {
        // TODO
    }

    override fun onOptionsClick(anchor: View, position: Int) {
        showPopupMenu(anchor, position)
    }

    private fun onEdit(position: Int) {
        if (position <= viewModel.results.size - 1) {
            NewQuestionnaireActivity.start( context!!, viewModel.results[position])
        } else {
            Log.e(TAG, "Invalid position. position == $position")
        }
    }

    private fun onDelete(position: Int) {
        showRemoveQuestionnaireDialog(position)
    }

    private fun updateUi() {
        if (viewModel.results.isNullOrEmpty()) {
            showNoResultsView()
        } else {
            showResults()
        }
    }

    private fun showPopupMenu(anchor: View, position: Int) {
        val menu = PopupMenu(activity, anchor)
        menu.inflate(R.menu.options_item_menu)
        menu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.edit -> {
                        onEdit(position)
                        true
                    }
                    R.id.delete -> {
                        onDelete(position)
                        true
                    }
                    else -> {
                        Log.e(TAG, "Unknown id = ${item?.itemId}. Can't handle click")
                        false
                    }
                }
            }
        })
        menu.show()
    }

    private fun showRemoveQuestionnaireDialog(position: Int) {
        if (!isAdded) {
            Log.w(TAG, "isFinishing(). Skipping...")
            return
        }
        dialog = AlertDialog.Builder(context!!)
            .setTitle(R.string.alert_dialog_remove_questionnaire_title)
            .setMessage(R.string.alert_dialog_remove_questionnaire_msg)
            .setPositiveButton(R.string.action_yes) { dialog, _ ->
                dialog.dismiss()
                viewModel.delete(position)
                adapter.removeAt(position)
                updateUi()
            }
            .setNegativeButton(R.string.action_no) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        dialog!!.show()
    }

    private fun showResults() {
        val view = view
        view!!.findViewById<View>(R.id.text_view_no_items_prompt).visibility = View.GONE
        view.findViewById<View>(R.id.image_view_arrow).visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.update(viewModel.results)
    }

    private fun showNoResultsView() {
        recyclerView.visibility = View.GONE
        val view = view
        view!!.findViewById<View>(R.id.text_view_no_items_prompt).visibility = View.VISIBLE
        view.findViewById<View>(R.id.image_view_arrow).visibility = View.VISIBLE
    }

    private fun initUi(view: View) {
        fab = view.findViewById(R.id.fab_new_questionnaire)
        fab.setOnClickListener { NewQuestionnaireActivity.start(context!!) }
        recyclerView = view.findViewById(R.id.recycler_view_questionnaires)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = QuestionnaireListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        })
    }

    companion object {
        private const val TYPE_EXTRAS = "type_extras"

        @JvmStatic
        fun newInstance(type: QuestionnaireType): QuestionnaireListFragment {
            val args = Bundle()
            args.putInt(TYPE_EXTRAS, type.ordinal)
            val fragment = QuestionnaireListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
