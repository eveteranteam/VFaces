package ua.gov.mva.vfaces.data.mapper

import android.util.Log
import ua.gov.mva.vfaces.data.entity.BlockType
import ua.gov.mva.vfaces.data.entity.QuestionnaireBlock
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.domain.model.Questionnaire

class QuestionnaireMapper {

    fun entityToModel(entity: ua.gov.mva.vfaces.data.entity.Questionnaire): Questionnaire {
        return Questionnaire(entity.id!!, mapBlocks(entity.blocks))
    }

    private fun mapBlocks(blocks: ArrayList<ua.gov.mva.vfaces.data.entity.QuestionnaireBlock>?): ArrayList<Block> {
        val result = ArrayList<Block>()
        if (blocks.isNullOrEmpty()) {
            Log.e(TAG, "No blocks. Nothing to map")
            return result
        }
        for (b in blocks) {
            result.add(mapSingleBlock(b))
        }
        return result
    }

    private fun mapSingleBlock(block: QuestionnaireBlock): Block {
        return Block(block.id!!, block.title!!, mapItems(block.items))
    }

    private fun mapItems(items: ArrayList<ua.gov.mva.vfaces.data.entity.Item>?): ArrayList<Item> {
        val result = ArrayList<Item>()
        if (items.isNullOrEmpty()) {
            Log.e(TAG, "No items. Nothing to map")
            return result
        }

        for (i in items) {
            val item = mapSingleItem(i)
            if (item != null) {
                result.add(item)
            }
        }
        return result
    }

    private fun mapSingleItem(item: ua.gov.mva.vfaces.data.entity.Item): Item? {
        val type = BlockType.fromString(item.type) ?: return null
        val choices = mapChoices(item.choices)
        return Item(type, item.name!!, choices)
    }

    private fun mapChoices(choices: ArrayList<String>?): ArrayList<String> {
        val empty = ArrayList<String>()
        if (choices.isNullOrEmpty()) {
            Log.w(TAG, "No choices. Nothing to map")
            return empty
        }
        return choices
    }

    companion object {
        private const val TAG = "QuestionnaireMapper"
    }
}