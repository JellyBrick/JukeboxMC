package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.Axis
import org.jukeboxmc.block.data.PurpurType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemPurpurBlock
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockPurpurBlock : Block {
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
        if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) {
            axis = Axis.Y
        } else if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
            axis = Axis.Z
        } else {
            axis = Axis.X
        }
        world.setBlock(placePosition, this, 0)
        return true
    }

    override fun toItem(): Item {
        return Item.create<ItemPurpurBlock>(ItemType.PURPUR_BLOCK).setPurpurType(
            purpurType,
        )
    }

    var axis: Axis
        get() = if (stateExists("pillar_axis")) Axis.valueOf(getStringState("pillar_axis")) else Axis.Y
        set(axis) {
            setState<Block>("pillar_axis", axis.name.lowercase(Locale.getDefault()))
        }

    fun setPurpurType(purpurType: PurpurType): BlockPurpurBlock {
        return setState<BlockPurpurBlock>("chisel_type", purpurType.name.lowercase(Locale.getDefault()))
    }

    val purpurType: PurpurType
        get() = if (stateExists("chisel_type")) PurpurType.valueOf(getStringState("chisel_type")) else PurpurType.DEFAULT
}
