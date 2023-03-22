package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockWoodenFence
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
class ItemFence : Item, Burnable {
    private val block: BlockWoodenFence

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockWoodenFence>(BlockType.FENCE)
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockWoodenFence>(BlockType.FENCE)
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemFence {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setWoodType(woodType: WoodType): ItemFence {
        blockRuntimeId = block.setWoodType(woodType).runtimeId
        return this
    }

    val woodType: WoodType
        get() = block.woodType
    override val burnTime: Duration?
        get() = Duration.ofMillis(300)
}
