package ua.gov.mva.vfaces.presentation.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<DATA : Any>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun setup(data: DATA)

    /**
     * Method should be overridden in subclasses to validate data
     * in own implementation of [RecyclerView.ViewHolder] class.
     */
    open fun isDataValid(): Boolean {
        return false
    }
}