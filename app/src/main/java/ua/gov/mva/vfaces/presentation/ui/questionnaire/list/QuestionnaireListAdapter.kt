package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R

class QuestionnaireListAdapter(private val clickListener: OnItemClickListener) :
        RecyclerView.Adapter<QuestionnaireListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.questionnaire_list_item, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(view: View, private val clickListener: OnItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {

        // TODO
        private val root= view.findViewById<View>(R.id.card_view_questionnaire).setOnClickListener(this)
        private val optionsView = view.findViewById<View>(R.id.image_view_options).setOnClickListener(this)

        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.card_view_questionnaire -> clickListener.onClick()
                R.id.image_view_options -> clickListener.onOptionsClick(v)
            }
        }

    }

    interface OnItemClickListener {
        fun onClick()
        fun onOptionsClick(anchor : View)
    }
}