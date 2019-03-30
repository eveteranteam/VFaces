package ua.gov.mva.vfaces.presentation.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<DATA: Any>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun setup(data: DATA)
}