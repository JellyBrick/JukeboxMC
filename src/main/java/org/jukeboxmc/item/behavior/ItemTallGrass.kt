package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockTallGrass
import org.jukeboxmc.block.data.GrassType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemTallGrass : Item {
    private val block: BlockTallGrass

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.TALLGRASS)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.TALLGRASS)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemTallGrass {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setGrassType(grassType: GrassType): ItemTallGrass {
        blockRuntimeId = block.setGrassType(grassType).runtimeId
        return this
    }

    val grassType: GrassType
        get() = block.grassType
}
