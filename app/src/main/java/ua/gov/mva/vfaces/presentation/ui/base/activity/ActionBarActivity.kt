package ua.gov.mva.vfaces.presentation.ui.base.activity

import ua.gov.mva.vfaces.R

abstract class ActionBarActivity : BaseActivity(), ActionBarListener {

    override fun setTitle(title: String) {
        val actionBar = supportActionBar
        actionBar?.title = title
    }

    override fun setMenuIcon() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    override fun setBackIcon() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }
}