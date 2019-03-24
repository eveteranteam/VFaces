package ua.gov.mva.vfaces.domain.model

import ua.gov.mva.vfaces.data.entity.BlockType

class Block(val id: String,
            val title: String,
            val items: ArrayList<Item>) {

}

data class Item(val type: BlockType,
                val name: String,
                val choices: ArrayList<String>)