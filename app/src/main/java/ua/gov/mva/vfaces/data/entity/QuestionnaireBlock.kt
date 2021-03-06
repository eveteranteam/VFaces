package ua.gov.mva.vfaces.data.entity

import com.google.gson.annotations.SerializedName

class QuestionnaireBlock {

    @SerializedName("id")
    var id : String? = null

    @SerializedName("title")
    var title : String? = null

    @SerializedName("item")
    var items : ArrayList<Item>? = null
}

class Item {
    @SerializedName("type")
    var type : String? = null

    @SerializedName("name")
    var name : String? = null

    @SerializedName("optional")
    var optional : Boolean? = null

    @SerializedName("choices")
    var choices : ArrayList<String>? = null

    @SerializedName("other_choice")
    var otherChoice : Boolean? = null
}