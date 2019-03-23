package ua.gov.mva.vfaces.presentation.ui.base.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import ua.gov.mva.vfaces.presentation.ui.base.activity.IFragmentTransaction

/**
 *
 */
open class SimpleBaseFragment : Fragment() {

    protected lateinit var transaction: IFragmentTransaction

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        transaction = context as IFragmentTransaction
    }

    override fun onDetach() {
        super.onDetach()
    }
}