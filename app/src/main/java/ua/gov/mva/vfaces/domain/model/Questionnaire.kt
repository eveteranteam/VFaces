package ua.gov.mva.vfaces.domain.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class Questionnaire : Parcelable {

    var id: String? = null
    var blocks: ArrayList<Block>? = null
    set(value) {
        field = value
        calculateProgress()
    }
    var name: String? = null
    var number: String? = null
    var settlement: String? = null
    var lastEditTime: Long = 0
    var progress : Int = 0

    constructor()

    @JvmOverloads constructor(id: String,
                blocks: ArrayList<Block>,
                name: String? = null,
                number: String? = null,
                settlement: String? = null,
                lastEditTime: Long = 0, progress: Int = 0) : this() {
        this.id = id
        this.blocks = blocks
        this.name = name
        this.number = number
        this.settlement = settlement
        this.lastEditTime = lastEditTime
        this.progress = progress
    }

    constructor(source: Parcel) : this()

    fun isFinished(): Boolean {
        if (blocks == null) {
            Log.e(TAG, "blocks == null")
            return false
        }
        blocks!!.forEach {
            if (it.isNotCompleted()) {
                return false
            }
        }
        return true
    }

    fun isNotFinished(): Boolean {
        if (blocks == null) {
            Log.e(TAG, "blocks == null")
            return false
        }
        blocks!!.forEach {
            if (it.isNotCompleted()) {
                return true
            }
        }
        return false
    }

    private fun calculateProgress(): Int {
        val result: Int
        if (blocks == null) {
            Log.e(TAG, "blocks == null")
            return 0
        }
        var completedCount = 0
        blocks!!.forEach {
            if (it.isCompleted()) {
                completedCount++
            }
        }
        result = completedCount * 100 / blocks!!.size
        Log.d(TAG, "progress == $result")
        return result
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        private const val TAG = "Questionnaire"

        @JvmField
        val CREATOR: Parcelable.Creator<Questionnaire> = object : Parcelable.Creator<Questionnaire> {
            override fun createFromParcel(source: Parcel): Questionnaire = Questionnaire(source)
            override fun newArray(size: Int): Array<Questionnaire?> = arrayOfNulls(size)
        }
    }
}