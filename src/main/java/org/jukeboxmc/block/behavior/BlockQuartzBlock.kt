package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.Axis
import org.jukeboxmc.block.data.QuartzType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemQuartzBlock
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockQuartzBlock : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

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
        world.setBlock(placePosition, this)
        return true
    }

    override fun toItem(): Item {
        return Item.create<ItemQuartzBlock>(ItemType.QUARTZ_BLOCK).setPrismarineType(
            quartzType,
        )
    }

    var axis: Axis
        get() = if (stateExists("pillar_axis")) Axis.valueOf(getStringState("pillar_axis")) else Axis.Y
        set(axis) {
            setState<Block>("pillar_axis", axis.name.lowercase(Locale.getDefault()))
        }

    fun setQuartzType(quartzType: QuartzType): BlockQuartzBlock {
        return setState("chisel_type", quartzType.name.lowercase(Locale.getDefault()))
    }

    val quartzType: QuartzType
        get() = if (stateExists("chisel_type")) QuartzType.valueOf(getStringState("chisel_type")) else QuartzType.DEFAULT
}
