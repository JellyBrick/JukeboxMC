package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.PlantType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemDoublePlant
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoublePlant : Block {
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
        val blockAbove = world.getBlock(placePosition.add(0f, 1f, 0f))
        val blockDown = world.getBlock(placePosition.subtract(0f, 1f, 0f))
        if (blockAbove.type == BlockType.AIR && (blockDown.type == BlockType.GRASS || blockDown.type == BlockType.DIRT)) {
            if (!isUpperBlock) {
                val blockDoublePlant: BlockDoublePlant =
                    create(BlockType.DOUBLE_PLANT)
                blockDoublePlant.location = Location(world, placePosition.add(0f, 1f, 0f))
                blockDoublePlant.setPlantType(plantType)
                blockDoublePlant.setUpperBlock(true)
                world.setBlock(placePosition.add(0f, 1f, 0f), blockDoublePlant)
                world.setBlock(placePosition, this)
            } else {
                val blockDoublePlant: BlockDoublePlant =
                    create(BlockType.DOUBLE_PLANT)
                blockDoublePlant.location = Location(world, placePosition)
                blockDoublePlant.setPlantType(plantType)
                blockDoublePlant.setUpperBlock(false)
                world.setBlock(placePosition, blockDoublePlant)
                world.setBlock(placePosition.add(0f, 1f, 0f), this)
            }
            return true
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        if (isUpperBlock) {
            location.world?.setBlock(location.subtract(0f, 1f, 0f), create(BlockType.AIR))
        } else {
            location.world?.setBlock(location.add(0f, 1f, 0f), create(BlockType.AIR))
        }
        location.world?.setBlock(location, create(BlockType.AIR))
    }

    override fun toItem(): Item {
        return Item.create<ItemDoublePlant>(ItemType.DOUBLE_PLANT).setPlantType(
            plantType,
        )
    }

    fun setPlantType(plantType: PlantType): BlockDoublePlant {
        return setState("double_plant_type", plantType.name.lowercase(Locale.getDefault()))
    }

    val plantType: PlantType
        get() = if (stateExists("double_plant_type")) PlantType.valueOf(getStringState("double_plant_type")) else PlantType.SUNFLOWER

    fun setUpperBlock(value: Boolean): BlockDoublePlant {
        return setState("upper_block_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isUpperBlock: Boolean
        get() = stateExists("upper_block_bit") && getByteState("upper_block_bit").toInt() == 1
}
