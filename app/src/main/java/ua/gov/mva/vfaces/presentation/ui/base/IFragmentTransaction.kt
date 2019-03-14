package ua.gov.mva.vfaces.presentation.ui.base

import androidx.fragment.app.Fragment

interface IFragmentTransaction {

    fun replaceFragment(fragment: Fragment)

    fun popBackStack()
}