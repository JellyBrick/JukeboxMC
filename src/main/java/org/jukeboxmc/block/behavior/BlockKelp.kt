package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
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
class BlockKelp : Block {
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
        val block = world.getBlock(placePosition)
        if (block.type == BlockType.WATER) {
            val blockDown = block.getSide(BlockFace.DOWN)
            if (blockDown.isSolid || blockDown.type == BlockType.KELP) {
                world.setBlock(placePosition, this, 0)
                world.setBlock(placePosition, block, 1)
                return true
            }
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        val world = this.world
        val block = world!!.getBlock(breakPosition, 1)
        if (block is BlockWater) {
            world.setBlock(breakPosition, block, 0)
            world.setBlock(breakPosition, create<Block>(BlockType.AIR), 1)
            return
        }
        super.onBlockBreak(breakPosition)
    }

    fun setKelpAge(value: Int): BlockKelp {
        setState<Block>("kelp_age", value)
        return this
    }

    val kelpAge: Int
        get() = if (stateExists("kelp_age")) getIntState("kelp_age") else 0
}
