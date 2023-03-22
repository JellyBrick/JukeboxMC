package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.Axis
import org.jukeboxmc.block.data.LogType2
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemLog2
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLog2 : Block {
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
        return Item.create<ItemLog2>(ItemType.LOG2).setLogType(logType)
    }

    var axis: Axis
        get() = if (stateExists("pillar_axis")) Axis.valueOf(getStringState("pillar_axis")) else Axis.Y
        set(axis) {
            setState<Block>("pillar_axis", axis.name.lowercase(Locale.getDefault()))
        }

    fun setLogType(logType: LogType2): BlockLog2 {
        return setState<BlockLog2>("new_log_type", logType.name.lowercase(Locale.getDefault()))
    }

    val logType: LogType2
        get() = if (stateExists("new_log_type")) LogType2.valueOf(getStringState("new_log_type")) else LogType2.ACACIA
}
