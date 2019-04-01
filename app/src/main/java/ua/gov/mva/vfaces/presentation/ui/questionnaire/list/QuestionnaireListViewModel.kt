package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ua.gov.mva.vfaces.data.db.FirebaseDbChild
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.domain.model.QuestionnaireType
import ua.gov.mva.vfaces.presentation.ui.base.BaseViewModel

class QuestionnaireListViewModel : BaseViewModel() {

    private val db = FirebaseDatabase.getInstance().reference.child(FirebaseDbChild.QUESTIONNAIRE)
    private val resultLiveData = MutableLiveData<ResultType>()

    var type = QuestionnaireType.MAIN
    var results = arrayListOf<Questionnaire>()

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    fun loadQuestionnaires() {
        showProgress() // TODO fix progress
        val query = db.child(getChildFor(type))
               // .orderByKey()
                .limitToLast(LOAD_LIMIT)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snaphot: DataSnapshot) {
                results.clear()
                for (data in snaphot.children) {
                    val dao = data.getValue(Questionnaire::class.java)
                    if (dao != null) {
                        results.add(dao)
                    }
                }

                hideProgress()

                if (results.isNullOrEmpty()) {
                    Log.e(TAG, "results == $results")
                    resultLiveData.value = ResultType.NO_RESULTS
                } else {
                    resultLiveData.value = ResultType.SUCCESS
                }
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgress()
                Log.e(TAG, "Can't get user. $error")
                resultLiveData.value = ResultType.ERROR
            }
        })
    }

    fun delete(position: Int) {
        if (position < 0 || position > results.size - 1) {
            Log.e(TAG, "wrong position. position == $position")
            return
        }
        val data = results[position]
        showProgress()
        db.child(getChildFor(type))
                .child(data.lastEditTime.toString()) // Key
                .removeValue()
                .addOnCompleteListener { task ->
                    hideProgress()
                    if (task.isSuccessful) {
                        results.removeAt(position)
                        resultLiveData.value = ResultType.DELETE_SUCCESS
                    } else {
                        resultLiveData.value = ResultType.DELETE_ERROR
                        Log.e(TAG, "Could not delete. ${task.exception}")
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

    enum class Filter {
        DEFAULT, //
        NAME,
        SETTLEMENT,
        PROGRESS_MIN,
        PROGRESS_MAX
    }

    enum class ResultType {
        SUCCESS,
        NO_RESULTS,
        DELETE_SUCCESS,
        DELETE_ERROR,
        ERROR
    }

    private companion object {
        private const val TAG = "QListViewModel"
        private const val LOAD_LIMIT: Int = 25
    }
}