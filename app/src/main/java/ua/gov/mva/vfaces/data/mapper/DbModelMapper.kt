package ua.gov.mva.vfaces.data.mapper

import android.util.Log
import ua.gov.mva.vfaces.data.db.child.BlockDao
import ua.gov.mva.vfaces.data.db.child.ItemDao
import ua.gov.mva.vfaces.data.db.child.QuestionnaireDao
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.domain.model.Item
import ua.gov.mva.vfaces.domain.model.Questionnaire

class DbModelMapper : IDbModelMapper {

    override fun questionnaireToDao(data: Questionnaire): QuestionnaireDao {
        return QuestionnaireDao(data.id, data.name, data.number, data.settlement,
                data.progress!!, data.lastEditTime, blocksListToDao(data.blocks!!))
    }

    override fun blocksListToDao(blocks: List<Block>): List<BlockDao> {
        val result = arrayListOf<BlockDao>()
        if (blocks.isNullOrEmpty()) {
            Log.e(TAG, "No blocks. Nothing to map")
            return result
        }
        blocks.forEach {
            result.add(blockToDao(it))
        }
        return result
    }

    override fun blockToDao(block: Block): BlockDao {
        return BlockDao(block.id, block.title, itemsListToDao(block.items!!))
    }

    override fun itemsListToDao(items: List<Item>): List<ItemDao> {
        val result = arrayListOf<ItemDao>()
        if (items.isNullOrEmpty()) {
            Log.e(TAG, "No items. Nothing to map")
            return result
        }
        items.forEach {
            result.add(itemToDao(it))
        }
        return result
    }

    override fun itemToDao(item: Item): ItemDao {
        return ItemDao(item.name, item.answers)
    }

    private companion object {
        private const val TAG = "DbModelMapper"
    }

}

interface IDbModelMapper {
    fun questionnaireToDao(data: Questionnaire): QuestionnaireDao
    fun blocksListToDao(blocks: List<Block>): List<BlockDao>
    fun blockToDao(block: Block): BlockDao
    fun itemsListToDao(items: List<Item>): List<ItemDao>
    fun itemToDao(item: Item): ItemDao
}