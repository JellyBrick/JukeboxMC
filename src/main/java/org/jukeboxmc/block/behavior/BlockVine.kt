package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockVine : Block {
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
        val block = world.getBlock(blockPosition)
        if (block.isSolid && blockFace.horizontalIndex != -1) {
            this.setVineDirection(1 shl blockFace.opposite().horizontalIndex)
            world.setBlock(placePosition, this)
            return true
        }
        return false
    }

    fun setVineDirection(blockFace: BlockFace): BlockVine {
        this.setVineDirection(1 shl blockFace.opposite().horizontalIndex)
        return this
    }

    fun setVineDirection(value: Int): BlockVine { // 0-15 TODO -> Add Direction Class
        setState<Block>("vine_direction_bits", value)
        return this
    }

    val vineDirection: Int
        get() = if (stateExists("vine_direction_bits")) getIntState("vine_direction_bits") else 0
}
