package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockWoodenSlab
import org.jukeboxmc.block.data.WoodType
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier
import java.time.Duration

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemWoodenSlab : Item, Burnable {
    private val block: BlockWoodenSlab

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.WOODEN_SLAB)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.WOODEN_SLAB)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemWoodenSlab {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setWoodType(woodType: WoodType): ItemWoodenSlab {
        blockRuntimeId = block.setWoodType(woodType).runtimeId
        return this
    }

    val woodType: WoodType
        get() = block.woodType
    override val burnTime: Duration?
        get() = Duration.ofMillis(150)
}
