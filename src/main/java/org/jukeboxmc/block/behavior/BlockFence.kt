package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BlockFence : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override val boundingBox: AxisAlignedBB
        get() {
            val north = canConnect(this.getSide(Direction.NORTH))
            val south = canConnect(this.getSide(Direction.SOUTH))
            val west = canConnect(this.getSide(Direction.WEST))
            val east = canConnect(this.getSide(Direction.EAST))
            val n: Float = if (north) 0 else 0.375f
            val s: Float = if (south) 1 else 0.625f
            val w: Float = if (west) 0 else 0.375f
            val e: Float = if (east) 1 else 0.625f
            return AxisAlignedBB(
                location!!.x + w,
                location!!.y,
                location!!.z + n,
                location!!.x + e,
                location!!.y + 1.5f,
                location!!.z + s
            )
        }

    fun canConnect(block: Block?): Boolean {
        return block is BlockFence || block is BlockFenceGate || block!!.isSolid && !block.isTransparent
    }
}