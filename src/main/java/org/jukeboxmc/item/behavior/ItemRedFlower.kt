package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockRedFlower
import org.jukeboxmc.block.data.FlowerType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemRedFlower : Item {
    private val block: BlockRedFlower

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.RED_FLOWER)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.RED_FLOWER)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemRedFlower {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setFlowerType(flowerType: FlowerType): ItemRedFlower {
        blockRuntimeId = block.setFlowerType(flowerType).runtimeId
        return this
    }

    val flowerType: FlowerType
        get() = block.flowerType
}
