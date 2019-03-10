package ua.gov.mva.vfaces.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    protected fun showProgress() {
        progressLiveData.value = true
    }

    protected fun hideProgress() {
        progressLiveData.value = false
    }

    protected fun getProgressLiveData() : MutableLiveData<Boolean> {
        return progressLiveData
    }
}