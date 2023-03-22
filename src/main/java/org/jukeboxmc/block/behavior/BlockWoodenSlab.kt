package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.WoodType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemWoodenSlab
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockWoodenSlab : BlockSlab {
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
            if (targetBlock is BlockWoodenSlab && targetBlock.isTopSlot && targetBlock.woodType == woodType) {
                world.setBlock(
                    blockPosition,
                    create<BlockDoubleWoodenSlab>(BlockType.DOUBLE_WOODEN_SLAB).setWoodType(
                        woodType,
                    ),
                )
                return true
            } else if (block is BlockWoodenSlab && block.woodType == woodType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleWoodenSlab>(BlockType.DOUBLE_WOODEN_SLAB).setWoodType(
                        woodType,
                    ),
                )
                return true
            }
        } else if (blockFace == BlockFace.UP) {
            if (targetBlock is BlockWoodenSlab && !targetBlock.isTopSlot && targetBlock.woodType == woodType) {
                world.setBlock(
                    blockPosition,
                    create<BlockDoubleWoodenSlab>(BlockType.DOUBLE_WOODEN_SLAB).setWoodType(
                        woodType,
                    ),
                )
                return true
            } else if (block is BlockWoodenSlab && block.woodType == woodType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleWoodenSlab>(BlockType.DOUBLE_WOODEN_SLAB).setWoodType(
                        woodType,
                    ),
                )
                return true
            }
        } else {
            if (block is BlockWoodenSlab && block.woodType == woodType) {
                world.setBlock(
                    placePosition,
                    create<BlockDoubleWoodenSlab>(BlockType.DOUBLE_WOODEN_SLAB).setWoodType(
                        woodType,
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

    override fun toItem(): Item {
        return Item.create<ItemWoodenSlab>(ItemType.WOODEN_SLAB).setWoodType(
            woodType,
        )
    }

    fun setWoodType(woodType: WoodType): BlockWoodenSlab {
        return setState("wood_type", woodType.name.lowercase(Locale.getDefault()))
    }

    val woodType: WoodType
        get() = if (stateExists("wood_type")) WoodType.valueOf(getStringState("wood_type")) else WoodType.OAK
}
