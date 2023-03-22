package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockPlanks
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
class ItemPlanks : Item, Burnable {
    private val block: BlockPlanks

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockPlanks>(BlockType.PLANKS)
        blockRuntimeId = block.runtimeId
    }

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create<BlockPlanks>(BlockType.PLANKS)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemPlanks {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setWoodType(woodType: WoodType): ItemPlanks {
        blockRuntimeId = block.setWoodType(woodType).runtimeId
        return this
    }

    val woodType: WoodType
        get() = block.woodType
    override val burnTime: Duration?
        get() = Duration.ofMillis(300)
}
