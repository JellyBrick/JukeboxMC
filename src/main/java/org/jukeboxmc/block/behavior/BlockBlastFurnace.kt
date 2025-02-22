package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityBlastFurnace
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
class BlockBlastFurnace : Block {
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
        this.blockFace = player.direction.toBlockFace().opposite()
        world.setBlock(placePosition, this, 0)
        BlockEntity.create<BlockEntity>(BlockEntityType.BLAST_FURNACE, this).spawn()
        return true
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item,
    ): Boolean {
        val blockEntity: BlockEntityBlastFurnace? = blockEntity as BlockEntityBlastFurnace?
        if (blockEntity != null) {
            blockEntity.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            return true
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        val blockEntity: BlockEntityBlastFurnace? = blockEntity as BlockEntityBlastFurnace?
        if (blockEntity != null) {
            val furnaceInventory = blockEntity.blastFurnaceInventory
            for (content in furnaceInventory.contents) {
                if (content.type != ItemType.AIR) {
                    location.world?.dropItem(content, breakPosition, null)?.spawn()
                }
            }
            furnaceInventory.clear()
            furnaceInventory.viewer.clear()
        }
        super.onBlockBreak(breakPosition)
    }

    override val blockEntity: BlockEntity?
        get() = location.world?.getBlockEntity(location, location.dimension) as BlockEntityBlastFurnace?
    var blockFace: BlockFace
        get() = if (stateExists("facing_direction")) BlockFace.values()[getIntState("facing_direction")] else BlockFace.NORTH
        set(blockFace) {
            setState<Block>("facing_direction", blockFace.ordinal)
        }
}
