package ua.gov.mva.vfaces.presentation.ui.base.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import ua.gov.mva.vfaces.presentation.ui.base.activity.ActionBarListener
import ua.gov.mva.vfaces.presentation.ui.base.activity.IFragmentTransaction

/**
 *
 */
abstract class SimpleBaseFragment : Fragment() {

    protected abstract val TAG: String

    protected lateinit var transaction: IFragmentTransaction
    protected var actionBarListener: ActionBarListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        transaction = context as IFragmentTransaction
        actionBarListener = if (context is ActionBarListener) context else null
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * Set title to Action Bar.
     */
    protected fun setTitle(title: String) {
        actionBarListener?.setTitle(title)
    }
}