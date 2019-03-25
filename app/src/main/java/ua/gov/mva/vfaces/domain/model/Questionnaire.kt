package ua.gov.mva.vfaces.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Questionnaire(
    val id: String,
    val blocks: ArrayList<Block>
) : Parcelable {

    fun isFinished(): Boolean {
        for (b in blocks) {
            if (b.isNotCompleted()) {
                return false
            }
        }
        return true
    }

    fun isNotFilished(): Boolean {
        for (b in blocks) {
            if (b.isNotCompleted()) {
                return true
            }
        }
        return false
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    constructor(source: Parcel) : this(
        source.readString(),
        ArrayList<Block>().apply { source.readList(this, Block::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeList(blocks)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Questionnaire> = object : Parcelable.Creator<Questionnaire> {
            override fun createFromParcel(source: Parcel): Questionnaire = Questionnaire(source)
            override fun newArray(size: Int): Array<Questionnaire?> = arrayOfNulls(size)
        }
    }
}