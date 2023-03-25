package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntitySkull
import org.jukeboxmc.blockentity.BlockEntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import kotlin.math.floor

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSkull : Block {
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
        this.blockFace = blockFace
        val value =
            super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        if (value) {
            BlockEntity.create<BlockEntitySkull>(BlockEntityType.SKULL, this)
                .setRotation((floor(player.yaw * 16 / 360 + 0.5).toInt() and 0xF).toByte())
                .setSkullMeta(itemInHand.meta.toByte())
                .spawn()
        }
        return value
    }

    override val blockEntity: BlockEntity?
        get() = location.world.getBlockEntity(location, location.dimension) as BlockEntitySkull?

    fun setNoDrop(value: Boolean): BlockSkull {
        return setState("no_drop_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isNoDrop: Boolean
        get() = stateExists("no_drop_bit") && getByteState("no_drop_bit").toInt() == 1
    var blockFace: BlockFace
        get() = if (stateExists("facing_direction")) BlockFace.values()[getIntState("facing_direction")] else BlockFace.NORTH
        set(blockFace) {
            setState<Block>("facing_direction", blockFace.ordinal)
        }
}
