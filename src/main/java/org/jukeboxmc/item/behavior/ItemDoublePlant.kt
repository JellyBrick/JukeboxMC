package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockDoublePlant
import org.jukeboxmc.block.data.PlantType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemDoublePlant : Item {
    private val block: BlockDoublePlant

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.DOUBLE_PLANT)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.DOUBLE_PLANT)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemDoublePlant {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setPlantType(plantType: PlantType): ItemDoublePlant {
        blockRuntimeId = block.setPlantType(plantType).runtimeId
        return this
    }

    val plantType: PlantType
        get() = block.plantType
}
