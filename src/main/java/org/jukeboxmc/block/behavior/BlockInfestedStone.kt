package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.MonsterEggStoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemInfestedStone
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockInfestedStone : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemInfestedStone>(ItemType.INFESTED_STONE).setMonsterEggStoneType(
            monsterEggStoneType,
        )
    }

    fun setMonsterEggStoneType(monsterEggStoneType: MonsterEggStoneType): BlockInfestedStone {
        return setState<BlockInfestedStone>("monster_egg_stone_type", monsterEggStoneType.name.lowercase(Locale.getDefault()))
    }

    val monsterEggStoneType: MonsterEggStoneType
        get() = if (stateExists("monster_egg_stone_type")) MonsterEggStoneType.valueOf(getStringState("monster_egg_stone_type")) else MonsterEggStoneType.STONE
}
