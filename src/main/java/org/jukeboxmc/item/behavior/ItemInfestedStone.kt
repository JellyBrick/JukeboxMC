package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockInfestedStone
import org.jukeboxmc.block.data.MonsterEggStoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemInfestedStone : Item {
    private val block: BlockInfestedStone

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockInfestedStone>(BlockType.INFESTED_STONE)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockInfestedStone>(BlockType.INFESTED_STONE)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemInfestedStone {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setMonsterEggStoneType(monsterEggStoneType: MonsterEggStoneType): ItemInfestedStone {
        blockRuntimeId = block.setMonsterEggStoneType(monsterEggStoneType).runtimeId
        return this
    }

    val monsterEggStoneType: MonsterEggStoneType
        get() = block.monsterEggStoneType
}