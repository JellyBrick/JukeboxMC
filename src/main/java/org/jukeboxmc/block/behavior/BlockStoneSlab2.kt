package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.StoneSlab2Type
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStoneSlab2 : BlockSlab {
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
        val targetBlock = world.getBlock(blockPosition)
        val block = world.getBlock(placePosition)
        if (blockFace == BlockFace.DOWN) {
            if (targetBlock is BlockStoneSlab2 && targetBlock.isTopSlot && targetBlock.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    blockPosition,
                    create<BlockDoubleStoneSlab2>(BlockType.DOUBLE_STONE_BLOCK_SLAB2).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            } else if (block is BlockStoneSlab2 && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleStoneSlab2>(BlockType.DOUBLE_STONE_BLOCK_SLAB2).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            }
        } else if (blockFace == BlockFace.UP) {
            if (targetBlock is BlockStoneSlab2 && !targetBlock.isTopSlot && targetBlock.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    blockPosition,
                    create<BlockDoubleStoneSlab2>(BlockType.DOUBLE_STONE_BLOCK_SLAB2).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            } else if (block is BlockStoneSlab2 && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleStoneSlab2>(BlockType.DOUBLE_STONE_BLOCK_SLAB2).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            }
        } else {
            if (block is BlockStoneSlab2 && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleStoneSlab2>(BlockType.DOUBLE_STONE_BLOCK_SLAB2).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            } else {
                this.setTopSlot(clickedPosition.getY() > 0.5 && !world.getBlock(blockPosition).canBeReplaced(this))
            }
        }
        super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        world.setBlock(placePosition, this)
        return true
    }

    fun setStoneSlabType(stoneSlabType: StoneSlab2Type): BlockStoneSlab2 {
        return setState("stone_slab_type_2", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlab2Type
        get() = if (stateExists("stone_slab_type_2")) StoneSlab2Type.valueOf(getStringState("stone_slab_type_2")) else StoneSlab2Type.RED_SANDSTONE
}
