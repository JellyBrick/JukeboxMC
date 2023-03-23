package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.Axis
import org.jukeboxmc.block.data.WoodType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemWood
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockWood : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        axis = if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) {
            Axis.Y
        } else if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
            Axis.Z
        } else {
            Axis.X
        }
        world.setBlock(placePosition, this)
        return true
    }

    override fun toItem(): Item {
        return Item.create<ItemWood>(ItemType.WOOD).setWoodType(woodType).setStripped(
            isStripped,
        )
    }

    fun setWoodType(woodType: WoodType): BlockWood {
        return setState("wood_type", woodType.name.lowercase(Locale.getDefault()))
    }

    val woodType: WoodType
        get() = if (stateExists("wood_type")) WoodType.valueOf(getStringState("wood_type")) else WoodType.OAK

    fun setStripped(value: Boolean): BlockWood {
        return setState("stripped_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isStripped: Boolean
        get() = stateExists("stripped_bit") && getByteState("stripped_bit").toInt() == 1
    var axis: Axis
        get() = if (stateExists("pillar_axis")) Axis.valueOf(getStringState("pillar_axis")) else Axis.Y
        set(axis) {
            setState<Block>("pillar_axis", axis.name.lowercase(Locale.getDefault()))
        }
}
