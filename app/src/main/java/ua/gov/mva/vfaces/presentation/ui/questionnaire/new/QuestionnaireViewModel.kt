package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ua.gov.mva.vfaces.data.db.FirebaseDbChild
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.domain.model.QuestionnaireType
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel
import ua.gov.mva.vfaces.utils.ConnectionUtils

class QuestionnaireViewModel : BaseViewModel() {

    private val db = FirebaseDatabase.getInstance().reference.child(FirebaseDbChild.QUESTIONNAIRE)
    private val resultLiveData = MutableLiveData<ResultType>()
    var type = QuestionnaireType.MAIN

    lateinit var block: Block
    lateinit var questionnaire: Questionnaire

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    /**
     * TODO remove position and context!
     */
    fun save(answeredItems: List<Item>, position: Int, context: Context) {
        showProgress()
        // If key does not exist - create. Otherwise - reuse
        val key = if (questionnaire.key.isNullOrEmpty()) System.currentTimeMillis().toString() else questionnaire.key
        questionnaire.key = key
        questionnaire.userId = FirebaseAuth.getInstance().currentUser!!.uid
        questionnaire.lastEditTime = System.currentTimeMillis()

        //!!! TODO refactor!!!
        if (position == 0) {
            questionnaire.name = answeredItems[0].answers!![0]
            questionnaire.settlement = answeredItems[1].answers!![0]
        }

        block.items = answeredItems
        questionnaire.setBlockAt(block, position)

        // TODO should use better solution
        if (ConnectionUtils.isNetworkConnected(context)) {
            db.child(getChildFor(type))
                    .child(key)
                    .setValue(questionnaire)
                    .addOnCompleteListener { task ->
                        hideProgress()
                        if (task.isSuccessful) {
                            resultLiveData.value = ResultType.SAVE_SUCCESS
                        } else {
                            Log.e(TAG, "${task.exception}")
                            resultLiveData.value = ResultType.SAVE_ERROR
                        }
                    }
        } else {
            db.child(getChildFor(type))
                    .child(key)
                    .setValue(questionnaire)

            // Will be saved locally
            hideProgress()
            resultLiveData.value = ResultType.SAVE_SUCCESS
        }
    }

    private fun getChildFor(type: QuestionnaireType): String {
        return when (type) {
            QuestionnaireType.MAIN -> FirebaseDbChild.QUESTIONNAIRE_MAIN
            QuestionnaireType.ADDITIONAL -> FirebaseDbChild.QUESTIONNAIRE_ADDITIONAL
            else -> throw IllegalArgumentException("Unknown questionnaire type. type == $type")
        }
    }

    private companion object {
        private const val TAG = "QuestionnaireViewModel"
    }

    enum class ResultType {
        SAVE_SUCCESS,
        SAVE_ERROR
    }
}