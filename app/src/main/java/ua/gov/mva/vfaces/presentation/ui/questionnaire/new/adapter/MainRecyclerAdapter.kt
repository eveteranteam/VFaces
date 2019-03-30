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
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewHolder

class MainRecyclerAdapter(private val block : Block) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items= block.items

    companion object {
        const val VIEW_TYPE_FIELD = 0
        const val VIEW_TYPE_RADIO_GROUP = 1
        const val VIEW_TYPE_MULTIPLE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FIELD -> {
                val view = inflater.inflate(R.layout.field_view_type_layout, parent, false)
                FieldViewHolder(view)
            }
            VIEW_TYPE_RADIO_GROUP -> {
                val view = inflater.inflate(R.layout.radio_group_view_type_layout, parent, false)
                RadioButtonViewHolder(view)
            }
            VIEW_TYPE_MULTIPLE -> {
                val view = inflater.inflate(R.layout.checkbox_view_type_layout, parent, false)
                CheckboxViewHolder(view)
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
            VIEW_TYPE_RADIO_GROUP -> {
                val holder = viewHolder as RadioButtonViewHolder
                holder.setup(data)
                return
            }
            VIEW_TYPE_MULTIPLE -> {
                val holder = viewHolder as CheckboxViewHolder
                holder.setup(data)
                return

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        when (item.type) {
            BlockType.FIELD -> return VIEW_TYPE_FIELD
            BlockType.CHECKBOX -> return VIEW_TYPE_RADIO_GROUP
            BlockType.MULTIPLE_CHOICES -> return VIEW_TYPE_MULTIPLE
            else -> throw UnsupportedOperationException("Unknown view type. viewType = ${item.type}")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class FieldViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        private lateinit var textInputLayout: TextInputLayout

        override fun setup(data: Item) {
            textInputLayout = view.findViewById(R.id.text_input_layout_name)
            textInputLayout.hint = data.name
            setImeOptions(textInputLayout.editText!!)
        }

        /**
         * Check whether [EditText] in [TextInputLayout] contains any text.
         *
         * !Note!
         * Currently content of text itself is not validating.
         *
         * @return true - if text is not empty, false - otherwise.
         */
        override fun isDataValid(): Boolean {
           return textInputLayout.editText!!.text.toString().trim().isNotEmpty()
        }

        /**
         * Method sets imeOption for EditText.
         * [EditorInfo.IME_ACTION_NEXT] or [EditorInfo.IME_ACTION_DONE] will be set.
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

        private lateinit var recyclerView: RecyclerView
        private lateinit var adapter: CheckboxRecyclerAdapter

        override fun setup(data: Item) {
            view.findViewById<TextView>(R.id.text_view_question).text = data.name
            recyclerView = view.findViewById(R.id.recycler_view_checkboxes)
            recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = CheckboxRecyclerAdapter(data.choices)
            recyclerView.adapter = adapter
        }

        /**
         * Check whether at least one [androidx.appcompat.widget.AppCompatCheckBox] is selected.
         * @return true - if at least one CheckBox is selected, false - otherwise.
         */
        override fun isDataValid(): Boolean {
            var result = false
            adapter.data.forEachIndexed { index, _ ->
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as CheckboxRecyclerAdapter.ViewHolder
                // If at least one checkbox is selected - true
                if (viewHolder.isDataValid()) {
                    result = true
                    return@forEachIndexed
                }
            }
            return result
        }
    }

    internal inner class RadioButtonViewHolder(private val view: View) : BaseViewHolder<Item>(view) {

        private lateinit var radioGroup: RadioGroup

        override fun setup(data: Item) {
            view.findViewById<TextView>(R.id.text_view_question).text = data.name
            radioGroup = view.findViewById(R.id.radio_group)
            val context = view.context

            // Add new RadioButton into RadioGroup
            for (choice in data.choices) {
                radioGroup.addView(buildRadioButton(choice, context))
            }
        }

        /**
         * Check whether [RadioGroup] has checked button.
         * @return true - if RadioGroup has checked RadioButton, false - otherwise.
         */
        override fun isDataValid(): Boolean {
            return radioGroup.checkedRadioButtonId != -1 // Means group is selected
        }

        /**
         * Method creates new [AppCompatRadioButton] with specified text and margins.
         * @param text - text to be added
         */
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
