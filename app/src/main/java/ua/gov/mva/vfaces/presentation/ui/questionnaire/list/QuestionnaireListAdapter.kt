package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewHolder
import ua.gov.mva.vfaces.utils.DateUtils

class QuestionnaireListAdapter(private val clickListener: OnItemClickListener) :
        RecyclerView.Adapter<QuestionnaireListAdapter.ViewHolder>() {

    private val data= mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.questionnaire_list_item, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setup(Any()) // TODO
    }

    fun update(newData: MutableList<Int>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        if (position >= 0 && position <= data.size - 1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class ViewHolder(view: View, private val clickListener: OnItemClickListener) : BaseViewHolder<Any>(view), View.OnClickListener {

        override fun setup(data: Any) {
            itemView.findViewById<TextView>(R.id.text_view_percents).text = "75%" // TODO
            itemView.findViewById<TextView>(R.id.text_view_date).text = DateUtils.format(System.currentTimeMillis())
            itemView.findViewById<View>(R.id.card_view_questionnaire).setOnClickListener(this)
            itemView.findViewById<View>(R.id.image_view_options).setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.card_view_questionnaire -> clickListener.onClick()
                R.id.image_view_options -> clickListener.onOptionsClick(v, adapterPosition)
            }
        }

    }

    interface OnItemClickListener {
        fun onClick()
        fun onOptionsClick(anchor : View, position: Int)
    }
}