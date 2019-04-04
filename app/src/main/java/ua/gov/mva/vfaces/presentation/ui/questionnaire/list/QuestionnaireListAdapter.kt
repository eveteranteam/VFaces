package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewHolder
import ua.gov.mva.vfaces.utils.DateUtils

class QuestionnaireListAdapter(private val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<QuestionnaireListAdapter.ViewHolder>() {

    private val data = mutableListOf<Questionnaire>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.questionnaire_list_item, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setup(data[position])
    }

    fun update(newData: List<Questionnaire>) {
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

    class ViewHolder(view: View, private val clickListener: OnItemClickListener)
        : BaseViewHolder<Questionnaire>(view), View.OnClickListener {

        @SuppressLint("SetTextI18n")
        override fun setup(data: Questionnaire) {
            itemView.findViewById<TextView>(R.id.text_view_name).text = data.name
            itemView.findViewById<TextView>(R.id.text_view_settlement).text = data.settlement
            val context = itemView.context
            val status = if (data.isRefusedToAnswer) {
                context.getString(R.string.questionnaire_list_item_status_refused)
            } else if (data.isVeteranAbsent) {
                context.getString(R.string.questionnaire_list_item_status_absent)
            } else {
                "${data.progress}%"
            }
            itemView.findViewById<TextView>(R.id.text_view_status).text = status
            val locale = DateUtils.getLocale(itemView.context!!)
            itemView.findViewById<TextView>(R.id.text_view_date).text = DateUtils.format(data.lastEditTime, locale)

            itemView.findViewById<View>(R.id.card_view_questionnaire).setOnClickListener(this)
            itemView.findViewById<View>(R.id.image_view_options).setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.card_view_questionnaire -> clickListener.onClick()
                R.id.image_view_options -> clickListener.onOptionsClick(v, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick()
        fun onOptionsClick(anchor: View, position: Int)
    }
}