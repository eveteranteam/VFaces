package ua.gov.mva.vfaces.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.gov.mva.vfaces.presentation.ui.base.model.Message
import ua.gov.mva.vfaces.presentation.ui.base.model.MessageType

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

    private var progressLiveData = MutableLiveData<Boolean>()
    private var messageLiveData = MutableLiveData<Message>()

    /**
     * Encapsulate access to [MutableLiveData] by providing [LiveData] instead.
     */
    fun getProgressLiveData() : LiveData<Boolean> = progressLiveData

    fun getMessageLiveData() : LiveData<Message> = messageLiveData

    /**
     * Progress
     */
    protected fun showProgress() {
        progressLiveData.value = true
    }

    protected fun hideProgress() {
        progressLiveData.value = false
    }

    /**
     * Messages
     */
    protected fun showMessage(text: String) {
        messageLiveData.value = Message(text, MessageType.TEXT)
    }

    protected fun showWarning(text: String) {
        messageLiveData.value = Message(text, MessageType.WARNING)
    }

    protected fun showError(text: String) {
        messageLiveData.value = Message(text, MessageType.ERROR)
    }
}