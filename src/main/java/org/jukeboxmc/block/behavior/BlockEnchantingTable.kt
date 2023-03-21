package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityEnchantmentTable
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
class BlockEnchantingTable : Block {
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
        val value =
            super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        if (value) {
            BlockEntity.Companion.create<BlockEntity>(BlockEntityType.ENCHANTMENT_TABLE, this).spawn()
        }
        return value
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item
    ): Boolean {
        val blockEntity: BlockEntityEnchantmentTable? = blockEntity
        if (blockEntity != null) {
            blockEntity.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            return true
        }
        return false
    }

    override val blockEntity: BlockEntity?
        get() = location.world.getBlockEntity(location, location.dimension) as BlockEntityEnchantmentTable?
}