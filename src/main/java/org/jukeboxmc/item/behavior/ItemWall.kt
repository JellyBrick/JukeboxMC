package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockCobblestoneWall
import org.jukeboxmc.block.data.WallType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemWall : Item {
    private val block: BlockCobblestoneWall

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockCobblestoneWall>(BlockType.COBBLESTONE_WALL)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockCobblestoneWall>(BlockType.COBBLESTONE_WALL)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemWall {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setWallType(wallType: WallType): ItemWall {
        blockRuntimeId = block.setWallBlockType(wallType).runtimeId
        return this
    }

    val wallType: WallType
        get() = block.wallBlockType
}