package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockAnvil
import org.jukeboxmc.block.data.AnvilDamage
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemAnvil : Item {
    private val block: BlockAnvil

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockAnvil>(BlockType.ANVIL)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockAnvil>(BlockType.ANVIL)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemAnvil {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setDamage(anvilDamage: AnvilDamage): ItemAnvil {
        blockRuntimeId = block.setDamage(anvilDamage).runtimeId
        return this
    }

    val damage: AnvilDamage
        get() = block.damage
}
