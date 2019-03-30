package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewHolder

class CheckboxRecyclerAdapter(val data: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkbox_list_item, parent, false)
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

        private lateinit var checkBox: AppCompatCheckBox

        override fun setup(data: String) {
            checkBox = view.findViewById(R.id.check_box)
            checkBox.text = data
        }

        /**
         * @return true if [androidx.appcompat.widget.AppCompatCheckBox] is selected, false - otherwise.
         */
        override fun isDataValid(): Boolean {
            return checkBox.isChecked
        }
    }
}