package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.data.WallType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemWall
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockCobblestoneWall : BlockWall {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemWall>(ItemType.COBBLESTONE_WALL).setWallType(
            wallBlockType,
        )
    }

    fun setWallBlockType(wallType: WallType): BlockCobblestoneWall {
        return setState("wall_block_type", wallType.name.lowercase(Locale.getDefault()))
    }

    val wallBlockType: WallType
        get() = if (stateExists("wall_block_type")) WallType.valueOf(getStringState("wall_block_type")) else WallType.COBBLESTONE
}
