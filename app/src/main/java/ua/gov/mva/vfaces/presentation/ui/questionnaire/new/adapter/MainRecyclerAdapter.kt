package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.data.entity.BlockType
import ua.gov.mva.vfaces.domain.model.Item

class MainRecyclerAdapter(private val items: ArrayList<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_FIELD = 0
        const val VIEW_TYPE_CHECKBOX = 1
        const val VIEW_TYPE_RADIO_BUTTON = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FIELD -> {
                val view = inflater.inflate(R.layout.field_view_type_layout, parent, false)
                FieldViewHolder(view)
            }
            VIEW_TYPE_CHECKBOX -> {
                val view = inflater.inflate(R.layout.checkbox_view_type_layout, parent, false)
                CheckboxViewHolder(view)
            }
            VIEW_TYPE_RADIO_BUTTON -> {
                val view = inflater.inflate(R.layout.radio_button_view_type_layout, parent, false)
                RadioButtonViewHolder(view)
            }
            else -> throw UnsupportedOperationException("Unknown view type. viewType = $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val data = items[position]
        when (viewType) {
            VIEW_TYPE_FIELD -> {
                val holder = viewHolder as FieldViewHolder
                holder.setup(data)
            }
            VIEW_TYPE_CHECKBOX -> {
                val holder = viewHolder as CheckboxViewHolder
                holder.setup(data)

            }
            VIEW_TYPE_RADIO_BUTTON -> {
                val holder = viewHolder as RadioButtonViewHolder
                holder.setup(data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        when (item.type) {
            BlockType.FIELD -> return VIEW_TYPE_FIELD
            BlockType.CHECKBOX -> return VIEW_TYPE_CHECKBOX
            BlockType.MULTIPLE_CHOICES -> return VIEW_TYPE_RADIO_BUTTON
            else -> throw UnsupportedOperationException("Unknown view type. viewType = ${item.type}")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class FieldViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        override fun setup(data: Item) {
            view.findViewById<TextInputLayout>(R.id.text_input_layout_name).hint = data.name
        }
    }

    internal inner class CheckboxViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        override fun setup(data: Item) {
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_checkboxes)
            recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            recyclerView.adapter = CheckboxRecyclerAdapter(data.choices)
        }
    }

    internal inner class RadioButtonViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        override fun setup(data: Item) {
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_radio_buttons)
            recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            recyclerView.adapter = RadioButtonRecyclerAdapter(data.choices)
        }
    }
}