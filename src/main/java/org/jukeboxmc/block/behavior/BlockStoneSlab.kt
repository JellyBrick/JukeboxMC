package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.StoneSlabType
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
class BlockStoneSlab : BlockSlab {
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
        val targetBlock = world.getBlock(blockPosition)
        val block = world.getBlock(placePosition)
        if (blockFace == BlockFace.DOWN) {
            if (targetBlock is BlockStoneSlab && targetBlock.isTopSlot && targetBlock.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    blockPosition,
                    Block.Companion.create<BlockDoubleStoneSlab>(BlockType.DOUBLE_STONE_BLOCK_SLAB).setStoneSlabType(
                        stoneSlabType
                    )
                )
                return true
            } else if (block is BlockStoneSlab && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    Block.Companion.create<BlockDoubleStoneSlab>(BlockType.DOUBLE_STONE_BLOCK_SLAB).setStoneSlabType(
                        stoneSlabType
                    )
                )
                return true
            }
        } else if (blockFace == BlockFace.UP) {
            if (targetBlock is BlockStoneSlab && !targetBlock.isTopSlot && targetBlock.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    blockPosition,
                    Block.Companion.create<BlockDoubleStoneSlab>(BlockType.DOUBLE_STONE_BLOCK_SLAB).setStoneSlabType(
                        stoneSlabType
                    )
                )
                return true
            } else if (block is BlockStoneSlab && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    Block.Companion.create<BlockDoubleStoneSlab>(BlockType.DOUBLE_STONE_BLOCK_SLAB).setStoneSlabType(
                        stoneSlabType
                    )
                )
                return true
            }
        } else {
            if (block is BlockStoneSlab && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    Block.Companion.create<BlockDoubleStoneSlab>(BlockType.DOUBLE_STONE_BLOCK_SLAB).setStoneSlabType(
                        stoneSlabType
                    )
                )
                return true
            } else {
                this.isTopSlot = clickedPosition.y > 0.5 && !world.getBlock(blockPosition).canBeReplaced(this)
            }
        }
        super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        world.setBlock(placePosition, this)
        return true
    }

    fun setStoneSlabType(stoneSlabType: StoneSlabType): BlockStoneSlab {
        return setState("stone_slab_type", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlabType
        get() = if (stateExists("stone_slab_type")) StoneSlabType.valueOf(getStringState("stone_slab_type")) else StoneSlabType.SMOOTH_STONE
}