package ua.gov.mva.vfaces.domain.model

import android.os.Parcel
import android.os.Parcelable
import ua.gov.mva.vfaces.data.entity.BlockType

class Block(
    val id: String,
    val title: String,
    val items: ArrayList<Item>
) : Parcelable {

    fun isCompleted(): Boolean {
        for (i in items) {
            if (i.isNotAnswered()) {
                return false
            }
        }
        return true
    }

    fun isNotCompleted(): Boolean {
        for (i in items) {
            if (i.isNotAnswered()) {
                return true
            }
        }
        return false
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        ArrayList<Item>().apply { source.readList(this, Item::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeList(items)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Block> = object : Parcelable.Creator<Block> {
            override fun createFromParcel(source: Parcel): Block = Block(source)
            override fun newArray(size: Int): Array<Block?> = arrayOfNulls(size)
        }
    }
}

data class Item @JvmOverloads constructor(
    val type: BlockType,
    val name: String,
    val choices: ArrayList<String>,
    val answers: ArrayList<String> = arrayListOf()
) : Parcelable {

    fun isAnswered(): Boolean = answers.isNotEmpty()

    fun isNotAnswered(): Boolean = answers.isEmpty()

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    constructor(source: Parcel) : this(
        BlockType.values()[source.readInt()],
        source.readString(),
        source.createStringArrayList(),
        source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(type.ordinal)
        writeString(name)
        writeStringList(choices)
        writeStringList(answers)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item = Item(source)
            override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
        }
    }
}