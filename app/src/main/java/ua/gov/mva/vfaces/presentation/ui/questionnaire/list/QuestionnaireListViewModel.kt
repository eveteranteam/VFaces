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
    private val loadingLiveData = MutableLiveData<Boolean>()

    var type = QuestionnaireType.MAIN
    var sortType = SortType.TIMESTAMP
    var results = arrayListOf<Questionnaire>()

    private var userId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private var lastVisibleChildId: String? = null
    private var totalQuestionnairesCount = 0

    private var itemsInAdapterCount = 0
    private var visibleItemCount = 0
    private var firstVisibleItemPosition = 0

    fun resultLiveData(): LiveData<ResultType> = resultLiveData

    fun loadingLiveData(): LiveData<Boolean> = loadingLiveData

    fun loadData() {
        if (userId.isNullOrEmpty()) {
            resultLiveData.value = ResultType.ERROR
            return
        }

        loadingLiveData.value = true
        // Getting total count of Questionnaires for user
        db.child(getChildFor(type))
            .orderByChild(FirebaseDbChild.USER_ID)
            .equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    totalQuestionnairesCount = snapshot.childrenCount.toInt()
                    Log.d(TAG, "totalQuestionnaires: $totalQuestionnairesCount")

                    if (totalQuestionnairesCount == 0) {
                        loadingLiveData.value = false
                        resultLiveData.value = ResultType.NO_RESULTS
                        return
                    }

                    if (totalQuestionnairesCount == itemsInAdapterCount) {
                        Log.d(TAG, "$totalQuestionnairesCount items loaded. No more items to loadQuestionnaires")
                        return
                    }
                    loadQuestionnaires()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Can't get total items count. $error")
                    loadingLiveData.value = false
                    resultLiveData.value = ResultType.ERROR
                }
            })
    }

    private fun loadQuestionnaires() {
        if (userId.isNullOrEmpty()) {
            resultLiveData.value = ResultType.ERROR
            return
        }
        loadingLiveData.value = true

        val db = FirebaseDatabase.getInstance().reference.child(FirebaseDbChild.QUESTIONNAIRE)

        val query = if (lastVisibleChildId == null) {
            db.child(getChildFor(type))
                .orderByChild(FirebaseDbChild.USER_ID)
                .equalTo(userId) // get results belonging to current user only
                .limitToFirst(LOAD_LIMIT)
        } else {
            db.child(getChildFor(type))
                .orderByKey()
                // .equalTo(userId) // TODO. Multiple queries are not supported. Throws an exception!!!
                .startAt(lastVisibleChildId)
                .limitToFirst(LOAD_LIMIT)
        }

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "lastVisibleChildId: $lastVisibleChildId")

                for (data in snapshot.children) {
                    val dao = data.getValue(Questionnaire::class.java)
                    // TODO Since FB database does not support multiple queries we need to display only user's Questionnaires!
                    if (dao != null && userId == dao.userId && !results.contains(dao)) {
                        results.add(dao)
                    }
                }

                loadingLiveData.value = false

                if (results.isNullOrEmpty()) {
                    Log.d(TAG, "results == $results")
                    resultLiveData.value = ResultType.NO_RESULTS
                } else {
                    lastVisibleChildId = results.lastOrNull()?.key
                    resultLiveData.value = ResultType.SUCCESS
                }
            }

            override fun onCancelled(error: DatabaseError) {
                loadingLiveData.value = false
                Log.e(TAG, "Can't get user. $error")
                resultLiveData.value = ResultType.ERROR
            }
        })
    }

    fun loadMore(childCount: Int, itemCount: Int, itemPosition: Int) {
        if (loadingLiveData.value == true) {
            Log.d(TAG, "Loading is currently in progress")
            return
        }
        visibleItemCount = childCount
        itemsInAdapterCount = itemCount
        firstVisibleItemPosition = itemPosition

        if (totalQuestionnairesCount == itemsInAdapterCount) {
            Log.d(TAG, "$totalQuestionnairesCount items loaded. No more items to load")
            return
        }

        if ((visibleItemCount + firstVisibleItemPosition) >= itemsInAdapterCount
            && firstVisibleItemPosition >= 0 && itemsInAdapterCount >= LOAD_LIMIT
        ) {
            loadQuestionnaires()
        }
    }

    fun delete(position: Int) {
        if (position < 0 || position > results.size - 1) {
            Log.e(TAG, "wrong position. position == $position")
            return
        }
        val data = results[position]
        showProgress()
        db.child(getChildFor(type))
            .child(data.key)
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
                return
            }
            SortType.COMPLETED -> {
                results.sortBy {
                    it.progress
                }
                return
            }
            SortType.NOT_COMPLETED -> {
                results.sortBy {
                    it.progress
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
        private const val LOAD_LIMIT: Int = 10
    }
}