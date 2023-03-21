package org.jukeboxmc.item.behavior

import java.time.Duration
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockWood
import org.jukeboxmc.block.data.WoodType
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemWood : Item, Burnable {
    private val block: BlockWood

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockWood>(BlockType.WOOD)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockWood>(BlockType.WOOD)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemWood {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setWoodType(woodType: WoodType): ItemWood {
        blockRuntimeId = block.setWoodType(woodType).runtimeId
        return this
    }

    val woodType: WoodType
        get() = block.woodType

    fun setStripped(value: Boolean): ItemWood {
        blockRuntimeId = block.setStripped(value).runtimeId
        return this
    }

    val isStripped: Boolean
        get() = block.isStripped
    override val burnTime: Duration?
        get() = Duration.ofMillis(300)
}