package ua.gov.mva.vfaces.presentation.ui.base.activity

interface ActionBarListener {

    fun setTitle(title: String)
    fun enableHomeAsUp(enable: Boolean)
    fun setMenuIcon()
    fun setBackIcon()
}