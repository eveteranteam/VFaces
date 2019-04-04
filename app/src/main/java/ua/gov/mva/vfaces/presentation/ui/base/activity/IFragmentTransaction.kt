package ua.gov.mva.vfaces.presentation.ui.base.activity

import androidx.fragment.app.Fragment

interface IFragmentTransaction {

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true)

    fun popBackStack()
}