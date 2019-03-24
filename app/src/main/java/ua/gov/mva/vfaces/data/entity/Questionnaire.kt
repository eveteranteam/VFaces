package ua.gov.mva.vfaces.data.entity

import com.google.gson.annotations.SerializedName

class Questionnaire {

    @SerializedName("id")
    var id : String? = null

    @SerializedName("block")
    var blocks : ArrayList<QuestionnaireBlock>? = null
}