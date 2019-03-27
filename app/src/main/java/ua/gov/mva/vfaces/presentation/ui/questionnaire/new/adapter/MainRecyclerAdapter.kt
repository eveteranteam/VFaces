package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.data.entity.BlockType
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item

class MainRecyclerAdapter(private val block : Block) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = block.items

    companion object {
        const val VIEW_TYPE_FIELD = 0
        const val VIEW_TYPE_CHECKBOX = 1
        const val VIEW_TYPE_RADIO_GROUP = 2
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
            VIEW_TYPE_RADIO_GROUP -> {
                val view = inflater.inflate(R.layout.radio_group_view_type_layout, parent, false)
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
                return
            }
            VIEW_TYPE_CHECKBOX -> {
                val holder = viewHolder as CheckboxViewHolder
                holder.setup(data)
                return

            }
            VIEW_TYPE_RADIO_GROUP -> {
                val holder = viewHolder as RadioButtonViewHolder
                holder.setup(data)
                return
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        when (item.type) {
            BlockType.FIELD -> return VIEW_TYPE_FIELD
            BlockType.CHECKBOX -> return VIEW_TYPE_CHECKBOX
            BlockType.MULTIPLE_CHOICES -> return VIEW_TYPE_RADIO_GROUP
            else -> throw UnsupportedOperationException("Unknown view type. viewType = ${item.type}")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class FieldViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        override fun setup(data: Item) {
            val tilName = view.findViewById<TextInputLayout>(R.id.text_input_layout_name)
            tilName.hint = data.name
            setImeOptions(tilName.editText!!)
        }

        /**
         * Method sets imeOption for EditText.
         * IME_ACTION_NEXT or IME_ACTION_DONE will be set.
         */
        private fun setImeOptions(editText: EditText) {
            // Check if block has more items with type FIELD
            // If true - add IME_ACTION_NEXT action to keyboard
            // Otherwise - add IME_ACTION_DONE
            val options: Int = if (block.hasMoreItemsOfType(BlockType.FIELD, adapterPosition + 1)) {
                EditorInfo.IME_ACTION_NEXT
            } else {
                EditorInfo.IME_ACTION_DONE
            }
            editText.imeOptions = options
        }
    }

    internal inner class CheckboxViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        override fun setup(data: Item) {
            view.findViewById<TextView>(R.id.text_view_question).text = data.name
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_checkboxes)
            recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            recyclerView.adapter = CheckboxRecyclerAdapter(data.choices)
        }
    }

    internal inner class RadioButtonViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        override fun setup(data: Item) {
            view.findViewById<TextView>(R.id.text_view_question).text = data.name
            val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
            val context = view.context

            for (choice in data.choices) {
                radioGroup.addView(buildRadioButton(choice, context))
            }
        }

        private fun buildRadioButton(text: String, context: Context) : AppCompatRadioButton {
            val button = AppCompatRadioButton(context)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.topMargin = context.resources.getDimensionPixelOffset(R.dimen.layout_margin_medium)
            params.bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.layout_margin_medium)
            button.layoutParams = params
            button.text = text
            return button
        }
    }
}
