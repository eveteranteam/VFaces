package ua.gov.mva.vfaces.presentation.ui.questionnaire.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
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
    var sortType = SortType.TIMESTAMP
    var results = arrayListOf<Questionnaire>()

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    fun loadQuestionnaires() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId.isNullOrEmpty()) {
            resultLiveData.value = ResultType.ERROR
            return
        }

        showProgress() // TODO fix progress
        val query = db.child(getChildFor(type))
            .orderByChild(FirebaseDbChild.USER_ID)
            .equalTo(userId) // get results belonging to current user only
        // TODO pagination
        // .limitToLast(LOAD_LIMIT)

        query.addValueEventListener(object : ValueEventListener {
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
                    Log.d(TAG, "results == $results")
                    resultLiveData.value = ResultType.NO_RESULTS
                } else {
                    sortResultsBy(sortType)
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
                        resultLiveData.value = ResultType.DELETE_SUCCESS
                    } else {
                        resultLiveData.value = ResultType.DELETE_ERROR
                        Log.e(TAG, "Could not delete. ${task.exception}")
                    }
                }
    }

    fun sortResultsBy(newSort: SortType) {
        sortType = newSort
        when(sortType) {
            SortType.TIMESTAMP -> {
                results.sortByDescending {
                    it.lastEditTime
                }
                return
            }
            SortType.NAME -> {
                results.sortBy {
                    it.name
                }
                return
            }
            SortType.SETTLEMENT -> {
                results.sortBy {
                    it.settlement
                }
            }
            SortType.COMPLETED -> {
                results.sortByDescending {
                    it.progress
                }
            }
            SortType.NOT_COMPLETED -> {
                results.sortByDescending {
                    it.progress < COMPLETED_PROGRESS
                }
                return
            }
        }
    }

    fun update() {
        if (results.isNotEmpty()) {
            resultLiveData.value = ResultType.SUCCESS
        } else {
            resultLiveData.value = ResultType.NO_RESULTS
        }
    }

    private fun getChildFor(type: QuestionnaireType): String {
        return when (type) {
            QuestionnaireType.MAIN -> FirebaseDbChild.QUESTIONNAIRE_MAIN
            QuestionnaireType.ADDITIONAL -> FirebaseDbChild.QUESTIONNAIRE_ADDITIONAL
            else -> throw IllegalArgumentException("Unknown questionnaire type. type == $type")
        }
    }

    enum class SortType {
        TIMESTAMP, // Last edit time
        NAME,
        SETTLEMENT,
        COMPLETED,
        NOT_COMPLETED
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
        private const val COMPLETED_PROGRESS: Int = 100
    }
}