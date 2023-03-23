package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.StoneSlab3Type
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.*

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStoneSlab3 : BlockSlab {
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
        val targetBlock = world.getBlock(blockPosition)
        val block = world.getBlock(placePosition)
        if (blockFace == BlockFace.DOWN) {
            if (targetBlock is BlockStoneSlab3 && targetBlock.isTopSlot && targetBlock.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    blockPosition,
                    create<BlockDoubleStoneSlab3>(BlockType.DOUBLE_STONE_BLOCK_SLAB3).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            } else if (block is BlockStoneSlab3 && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleStoneSlab3>(BlockType.DOUBLE_STONE_BLOCK_SLAB3).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            }
        } else if (blockFace == BlockFace.UP) {
            if (targetBlock is BlockStoneSlab3 && !targetBlock.isTopSlot && targetBlock.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    blockPosition,
                    create<BlockDoubleStoneSlab3>(BlockType.DOUBLE_STONE_BLOCK_SLAB3).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            } else if (block is BlockStoneSlab3 && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleStoneSlab3>(BlockType.DOUBLE_STONE_BLOCK_SLAB3).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            }
        } else {
            if (block is BlockStoneSlab3 && block.stoneSlabType == stoneSlabType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleStoneSlab3>(BlockType.DOUBLE_STONE_BLOCK_SLAB3).setStoneSlabType(
                        stoneSlabType,
                    ),
                )
                return true
            } else {
                this.setTopSlot(clickedPosition.y > 0.5 && !world.getBlock(blockPosition).canBeReplaced(this))
            }
        }
        super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        world.setBlock(placePosition, this)
        return true
    }

    fun setStoneSlabType(stoneSlabType: StoneSlab3Type): BlockStoneSlab3 {
        return setState("stone_slab_type_3", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlab3Type
        get() = if (stateExists("stone_slab_type_3")) StoneSlab3Type.valueOf(getStringState("stone_slab_type_3")) else StoneSlab3Type.END_STONE_BRICK
}
