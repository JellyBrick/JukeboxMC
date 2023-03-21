package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.SignDirection
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSpruceStandingSign : BlockSign {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace
    ): Boolean {
        val block = world.getBlock(placePosition)
        if (blockFace == BlockFace.UP) {
            setSignDirection(
                SignDirection.values()[FastMath.floor((player.location.yaw + 180) * 16 / 360 + 0.5).toInt() and 0x0f]
            )
            world.setBlock(placePosition, this, 0)
        } else {
            val blockWallSign: BlockSpruceWallSign =
                Block.Companion.create<BlockSpruceWallSign>(BlockType.SPRUCE_WALL_SIGN)
            blockWallSign.setBlockFace(blockFace)
            world.setBlock(placePosition, blockWallSign, 0)
        }
        BlockEntity.Companion.create<BlockEntity>(BlockEntityType.SIGN, this).spawn()
        return true
    }

    override fun toItem(): Item {
        return Item.Companion.create<Item>(ItemType.SPRUCE_SIGN)
    }
}