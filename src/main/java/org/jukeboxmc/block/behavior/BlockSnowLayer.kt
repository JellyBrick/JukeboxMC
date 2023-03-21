package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
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
class BlockSnowLayer : Block {
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
        var block = world.getBlock(blockPosition)
        if (!world.getBlock(placePosition.subtract(0f, 1f, 0f)).isSolid) {
            return false
        }
        if (block is BlockSnowLayer) {
            if (block.height != 7) {
                block.setHeight(block.height + 1)
                world.setBlock(blockPosition, block)
            } else {
                block = world.getBlock(placePosition)
                if (block is BlockSnowLayer) {
                    block = block
                    if (block.height != 7) {
                        block.setHeight(block.height + 1)
                        world.setBlock(placePosition, block)
                    } else {
                        setHeight(0)
                        world.setBlock(placePosition, this)
                    }
                } else {
                    setHeight(0)
                    world.setBlock(placePosition, this)
                }
            }
        } else {
            setHeight(0)
            world.setBlock(placePosition, this)
        }
        return true
    }

    override fun canBeReplaced(block: Block?): Boolean {
        return if (block is BlockSnowLayer) {
            height != 7
        } else {
            false
        }
    }

    var isCovered: Boolean
        get() = stateExists("covered_bit") && getByteState("covered_bit").toInt() == 1
        set(value) {
            setState<Block>("covered_bit", if (value) 1.toByte() else 0.toByte())
        }

    fun setHeight(value: Int): BlockSnowLayer {
        return setState("height", value)
    }

    val height: Int
        get() = if (stateExists("height")) getIntState("height") else 0
}