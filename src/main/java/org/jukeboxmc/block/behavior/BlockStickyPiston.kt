package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.apache.commons.math3.util.FastMath
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
class BlockStickyPiston : Block {
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
        if (FastMath.abs(
                player.x - location
                    .x,
            ) < 2 && FastMath.abs(player.z - location.blockZ) < 2
        ) {
            val y = (player.y + player.eyeHeight).toDouble()
            if (y - location.y > 2) {
                this.blockFace = BlockFace.UP
            } else if (location.y - y > 0) {
                this.blockFace = BlockFace.DOWN
            } else {
                this.blockFace = player.direction.toBlockFace()
            }
        } else {
            this.blockFace = player.direction.toBlockFace()
        }
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    var blockFace: BlockFace
        get() = if (stateExists("facing_direction")) BlockFace.values()[getIntState("facing_direction")] else BlockFace.NORTH
        set(blockFace) {
            setState<Block>("facing_direction", blockFace.ordinal)
        }
}
