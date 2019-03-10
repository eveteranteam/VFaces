package ua.gov.mva.vfaces.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This is a base class for ViewModels.
 *
 * !Note!
 * Only [ViewModel] itself should be able to update LiveData.
 * Views should be able to only observe [LiveData].
 *
 * We encapsulate [MutableLiveData] by providing specific method
 * called [getProgressLiveData] and return [LiveData].
 */
open class BaseViewModel : ViewModel() {

    private var progressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    protected fun showProgress() {
        progressLiveData.value = true
    }

    protected fun hideProgress() {
        progressLiveData.value = false
    }

    /**
     * Encapsulate access to [MutableLiveData].
     */
    fun getProgressLiveData() : LiveData<Boolean> = progressLiveData
}