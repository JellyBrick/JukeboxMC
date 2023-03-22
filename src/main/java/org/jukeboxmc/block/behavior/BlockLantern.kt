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
class BlockLantern : Block {
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
        val hanging =
            blockFace != BlockFace.UP && isBlockAboveValid && (isBlockUnderValid || blockFace == BlockFace.DOWN)
        if (isBlockUnderValid && !hanging) {
            return false
        }
        isHanging = hanging
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    private val isBlockAboveValid: Boolean
        private get() {
            return when (val up = this.getSide(BlockFace.UP)) {
                is BlockLeaves -> false
                is BlockFence, is BlockWall -> true
                is BlockSlab -> !up.isTopSlot
                is BlockStairs -> !up.isUpsideDown
                else -> up.isSolid
            }
        }
    private val isBlockUnderValid: Boolean
        private get() {
            return when (val down = this.getSide(BlockFace.DOWN)) {
                is BlockLeaves -> true
                is BlockFence, is BlockWall -> false
                is BlockSlab -> !down.isTopSlot
                is BlockStairs -> !down.isUpsideDown
                else -> !down.isSolid
            }
        }
    var isHanging: Boolean
        get() = stateExists("hanging") && getByteState("hanging").toInt() == 1
        set(value) {
            setState<Block>("hanging", if (value) 1.toByte() else 0.toByte())
        }
}
