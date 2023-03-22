package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.TorchFacing
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockTorch : Block {
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
        if (!block.isTransparent && blockFace != BlockFace.DOWN) {
            torchFacing = blockFace.opposite().torchFacing()
            world.setBlock(placePosition, this)
            return true
        }
        if (!world.getBlock(placePosition.subtract(0f, 1f, 0f)).isTransparent) {
            torchFacing = TorchFacing.TOP
            world.setBlock(placePosition, this)
            return true
        }
        return false
    }

    override val boundingBox: AxisAlignedBB
        get() {
            var size = 0.15f
            when (torchFacing) {
                TorchFacing.EAST -> {
                    return AxisAlignedBB(
                        location.getX(),
                        location.getY() + 0.2f,
                        location.getZ() + 0.5f - size,
                        location.getX() + size * 2f,
                        location.getY() + 0.8f,
                        location.getZ() + 0.5f + size,
                    )
                }

                TorchFacing.WEST -> {
                    return AxisAlignedBB(
                        location.getX() + 1.0f - size * 2f,
                        location.getY() + 0.2f,
                        location.getZ() + 0.5f - size,
                        location.getX() + 1f,
                        location.getY() + 0.8f,
                        location.getZ() + 0.5f + size,
                    )
                }

                TorchFacing.SOUTH -> {
                    return AxisAlignedBB(
                        location.getX() + 0.5f - size,
                        location.getY() + 0.2f,
                        location.getZ(),
                        location.getX() + 0.5f + size,
                        location.getY() + 0.8f,
                        location.getZ() + size * 2f,
                    )
                }

                TorchFacing.NORTH -> {
                    return AxisAlignedBB(
                        location.getX() + 0.5f - size,
                        location.getY() + 0.2f,
                        location.getZ() + 1f - size * 2f,
                        location.getX() + 0.5f + size,
                        location.getY() + 0.8f,
                        location.getZ() + 1f,
                    )
                }

                else -> {}
            }
            size = 0.1f
            return AxisAlignedBB(
                location.getX() + 0.5f - size,
                location.getY() + 0.0f,
                location.getZ() + 0.5f - size,
                location.getX() + 0.5f + size,
                location.getY() + 0.6f,
                location.getZ() + 0.5f + size,
            )
        }
    var torchFacing: TorchFacing
        get() = if (stateExists("torch_facing_direction")) TorchFacing.valueOf(getStringState("torch_facing_direction")) else TorchFacing.TOP
        set(torchFacing) {
            setState<Block>("torch_facing_direction", torchFacing.name.lowercase(Locale.getDefault()))
        }
}
