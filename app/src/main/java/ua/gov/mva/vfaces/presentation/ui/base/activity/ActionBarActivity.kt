package ua.gov.mva.vfaces.presentation.ui.base.activity

import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.utils.Strings

abstract class ActionBarActivity : BaseActivity(), ActionBarListener {

    override fun enableHomeAsUp(enable: Boolean) {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(enable)
    }

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

    override fun clear() {
        enableHomeAsUp(false)
        setTitle(Strings.EMPTY)
    }
}