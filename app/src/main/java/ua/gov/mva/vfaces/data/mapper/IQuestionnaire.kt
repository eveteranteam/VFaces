package ua.gov.mva.vfaces.data.mapper

import ua.gov.mva.vfaces.data.entity.QuestionnaireBlock
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.domain.model.Questionnaire

interface IQuestionnaire {

    fun entityToModel(entity: ua.gov.mva.vfaces.data.entity.Questionnaire) : Questionnaire

    fun mapBlocks(blocks: ArrayList<ua.gov.mva.vfaces.data.entity.QuestionnaireBlock>?): ArrayList<Block>

    fun mapSingleBlock(block: QuestionnaireBlock): Block

    fun mapItems(items: ArrayList<ua.gov.mva.vfaces.data.entity.Item>?): ArrayList<Item>

    fun mapSingleItem(item: ua.gov.mva.vfaces.data.entity.Item): Item?

    fun mapChoices(choices: ArrayList<String>?): ArrayList<String>
}