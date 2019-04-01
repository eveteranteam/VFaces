package ua.gov.mva.vfaces.domain.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import ua.gov.mva.vfaces.data.entity.BlockType

class Block : Parcelable {

    var id: String? = null
    var title: String? = null
    var items: List<Item>? = null

    constructor()

    constructor(id: String, title: String, items: List<Item>) {
        this.id = id
        this.title = title
        this.items = items
    }

    fun isCompleted(): Boolean {
        if (items == null) {
            Log.e(TAG, "items == null")
            return false
        }
        items!!.forEach {
            if (it.isNotAnswered()) {
                return false
            }
        }
        return true
    }

    fun isNotCompleted(): Boolean {
        if (items == null) {
            Log.e(TAG, "items == null")
            return false
        }
        items!!.forEach {
            if (it.isNotAnswered()) {
                return true
            }
        }
        return false
    }

    /**
     * Check if Block contains items with specified type.
     * @param type - type of block.
     * @param position - start position in the loop to iterate from.
     *
     * @return - true if Block contains more items, false - if Block doesn't.
     */
    fun hasMoreItemsOfType(type: BlockType, position: Int): Boolean {
        if (items == null) {
            Log.e(TAG, "items == null")
            return false
        }
        if (position < 0 || position > items!!.size - 1) {
            Log.d(TAG, "Wrong position. position = $position")
            return false
        }
        for (i in position until items!!.size) {
            val item = items!![i]
            if (item.type == type) {
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
        private const val TAG = "Block"

        @JvmField
        val CREATOR: Parcelable.Creator<Block> = object : Parcelable.Creator<Block> {
            override fun createFromParcel(source: Parcel): Block = Block(source)
            override fun newArray(size: Int): Array<Block?> = arrayOfNulls(size)
        }
    }
}

class Item : Parcelable {

    var type: BlockType? = null
    var name: String? = null
    var choices: ArrayList<String>? = null
    var answers: ArrayList<String>? = null

    constructor()

    @JvmOverloads constructor(type: BlockType, name: String, choices: ArrayList<String>, answers: ArrayList<String> = arrayListOf()) {
        this.type = type
        this.name = name
        this.choices = choices
        this.answers = answers
    }

    fun isAnswered(): Boolean {
        if (answers == null) {
            Log.e(TAG, "items == null")
            return false
        }
        return answers!!.isNotEmpty()
    }

    fun isNotAnswered(): Boolean {
        return answers.isNullOrEmpty()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    constructor(source: Parcel) : this(
            BlockType.values()[source.readInt()],
            source.readString(),
            source.createStringArrayList(),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(type!!.ordinal)
        writeString(name)
        writeStringList(choices)
        writeStringList(answers)
    }

    companion object {
        private const val TAG = "Item"

        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item = Item(source)
            override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
        }
    }
}