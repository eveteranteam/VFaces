package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.fragment.BaseFragment
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.NewQuestionnaireActivity

class QuestionnaireListFragment : BaseFragment<QuestionnaireListViewModel>(), QuestionnaireListAdapter.OnItemClickListener {

    override val TAG = "QuestionnaireListFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionnaireListAdapter
    private lateinit var fab: FloatingActionButton

    private lateinit var viewModel: QuestionnaireListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questionnaire_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.questionnaire_list_title))
        initUi(view)
        showResults()// TODO
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun initViewModel(): QuestionnaireListViewModel {
        viewModel = ViewModelProviders.of(this).get(QuestionnaireListViewModel::class.java)
        return viewModel
    }

    override fun onClick() {
        // TODO
    }

    override fun onOptionsClick(anchor : View) {
        showPopupMenu(anchor)
    }

    private fun onEdit() {
        // TODO
    }

    private fun onDelete() {
        // TODO
    }

    private fun showPopupMenu(anchor : View) {
        val menu = PopupMenu(activity, anchor)
        menu.inflate(R.menu.options_item_menu)
        menu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.edit -> {
                        onEdit()
                        true
                    }
                    R.id.delete -> {
                        onDelete()
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

    private fun showResults() {
        val view = view
        view!!.findViewById<View>(R.id.text_view_no_items_prompt).visibility = View.GONE
        view.findViewById<View>(R.id.image_view_arrow).visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
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
        @JvmStatic
        fun newInstance(): QuestionnaireListFragment {
            return QuestionnaireListFragment()
        }
    }
}
