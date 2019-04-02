package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewHolder

class CheckboxRecyclerAdapter(val data: Item) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkbox_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        holder.setup(data)
    }

    override fun getItemCount(): Int {
        return data.choices!!.size
    }

    internal inner class ViewHolder(private val view: View) : BaseViewHolder<Item>(view), DataValidator<Item> {

        private lateinit var checkBox: CheckBox

        override fun setup(newData: Item) {
            super.setup(newData)
            checkBox = view.findViewById(R.id.check_box)
            checkBox.text = data!!.choices!![adapterPosition]

            // TODO fix issue with checking
            checkBox.isChecked = false

            data!!.choices!!.forEach { choice ->
                val check = newData.answers.contains(choice)
                checkBox.isChecked = check
            }
        }

        /**
         * @return true if [androidx.appcompat.widget.AppCompatCheckBox] is selected, false - otherwise.
         */
        override fun isDataValid(): Boolean {
            return checkBox!!.isChecked
        }

        override fun getAnswer(): Item {
            return data!!
        }
    }
}