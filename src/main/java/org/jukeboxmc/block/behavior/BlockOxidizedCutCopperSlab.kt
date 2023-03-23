package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.BlockType
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
class BlockOxidizedCutCopperSlab : BlockSlab {
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
            if (targetBlock is BlockOxidizedCutCopperSlab) {
                if (targetBlock.isTopSlot) {
                    world.setBlock(
                        blockPosition,
                        create(BlockType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB),
                    )
                    return true
                }
            } else if (block is BlockOxidizedCutCopperSlab) {
                world.setBlock(placePosition, create(BlockType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB))
                return true
            }
        } else if (blockFace == BlockFace.UP) {
            if (targetBlock is BlockOxidizedCutCopperSlab) {
                if (!targetBlock.isTopSlot) {
                    world.setBlock(
                        blockPosition,
                        create(BlockType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB),
                    )
                    return true
                }
            } else if (block is BlockOxidizedCutCopperSlab) {
                world.setBlock(placePosition, create(BlockType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB))
                return true
            }
        } else {
            if (block is BlockOxidizedCutCopperSlab) {
                world.setBlock(placePosition, create(BlockType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB))
                return true
            }
        }
        super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        world.setBlock(placePosition, this)
        return true
    }
}
