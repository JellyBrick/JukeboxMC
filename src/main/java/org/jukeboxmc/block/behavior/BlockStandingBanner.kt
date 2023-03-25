package org.jukeboxmc.block.behavior

import org.apache.commons.math3.util.FastMath
import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.SignDirection
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityBanner
import org.jukeboxmc.blockentity.BlockEntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStandingBanner : Block {
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
        val block = world.getBlock(placePosition)
        if (blockFace == BlockFace.UP) {
            signDirection = SignDirection.values()[
                FastMath.floor((player.location.yaw + 180) * 16 / 360 + 0.5)
                    .toInt() and 0x0f,
            ]
            world.setBlock(placePosition, this, 0)
        } else {
            val blockWallBanner: BlockWallBanner = create(BlockType.WALL_BANNER)
            blockWallBanner.blockFace = blockFace
            world.setBlock(placePosition, blockWallBanner, 0)
        }
        val type = if (itemInHand.nbt != null) itemInHand.nbt!!.getInt("Type", 0) else 0
        BlockEntity.create<BlockEntityBanner>(BlockEntityType.BANNER, block)
            .setColor(BlockColor.values()[itemInHand.meta]).setType(type)
        return true
    }

    override val blockEntity: BlockEntity?
        get() = location.world.getBlockEntity(location, location.dimension) as BlockEntityBanner?
    var signDirection: SignDirection
        get() = if (stateExists("ground_sign_direction")) SignDirection.values()[getIntState("ground_sign_direction")] else SignDirection.SOUTH
        set(signDirection) {
            setState<Block>("ground_sign_direction", signDirection.ordinal)
        }
}
