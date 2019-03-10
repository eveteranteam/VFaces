package ua.gov.mva.vfaces.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}