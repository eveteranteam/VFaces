package ua.gov.mva.vfaces.data.entity

import android.util.Log

enum class BlockType(private val value : String) {
    FIELD("field"),
    MULTIPLE_CHOICES("multiple_choices"),
    CHECKBOX("checkbox");

    companion object {
        private const val TAG = "BlockType"

        @JvmStatic
        fun fromString(value: String?) : BlockType? {
            if (value != null) {
                for (item in BlockType.values()) {
                    if (value.equals(item.value, ignoreCase = true)) {
                        return item
                    }
                }
            }
            Log.e(TAG, "Unknown type. type = $value")
            return null
        }
    }
}