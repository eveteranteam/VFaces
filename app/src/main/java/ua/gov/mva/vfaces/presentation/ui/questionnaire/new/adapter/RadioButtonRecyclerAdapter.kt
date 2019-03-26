package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R

class RadioButtonRecyclerAdapter(private val data: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_group_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        holder.setup(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal inner class ViewHolder(private val view: View) : BaseViewHolder<String>(view) {

        override fun setup(data: String) {
            //view.findViewById<RadioButton>(R.id.radio_button).text = data
        }
    }
}