package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal abstract class BaseViewHolder<DATA: Any>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun setup(data: DATA)
}