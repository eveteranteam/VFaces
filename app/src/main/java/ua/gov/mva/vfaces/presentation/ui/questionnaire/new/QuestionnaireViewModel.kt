package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import ua.gov.mva.vfaces.data.db.FirebaseDbChild
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.domain.model.QuestionnaireType
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class QuestionnaireViewModel : BaseViewModel() {

    private val db = FirebaseDatabase.getInstance().reference.child(FirebaseDbChild.QUESTIONNAIRE)
    private val resultLiveData = MutableLiveData<ResultType>()
    var type = QuestionnaireType.MAIN

    lateinit var block: Block
    lateinit var questionnaire: Questionnaire

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    /**
     * TODO remove position
     */
    fun save(answeredItems: List<Item>, position: Int) {
        showProgress()

        val time = if (questionnaire.lastEditTime == 0L) System.currentTimeMillis() else questionnaire.lastEditTime
        questionnaire.lastEditTime = time

        //!!! TODO refactor!!!
        if (position == 0) {
            questionnaire.name = answeredItems[0].answers!![0]
            questionnaire.number = answeredItems[1].answers!![0]
            questionnaire.settlement = answeredItems[2].answers!![0]
        }

        block.items = answeredItems
        questionnaire.setBlockAt(block, position)

        db.child(getChildFor(type))
                .child(time.toString()) // Key
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