package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemCandle
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockCandle : Block {
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
        val clickedBlock = world.getBlock(blockPosition)
        if (clickedBlock is BlockCandle) {
            return if (clickedBlock.identifier == itemInHand.identifier) {
                val candles: Int = clickedBlock.candles
                if (candles < 3) {
                    clickedBlock.setCandles(candles + 1)
                    world.setBlock(blockPosition, clickedBlock, 0)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
        world.setBlock(placePosition, this, 0)
        return true
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item,
    ): Boolean {
        if (itemInHand is ItemCandle) {
            return false
        }
        if (isLit && itemInHand.type != ItemType.FLINT_AND_STEEL) {
            setLit(false)
            location.world.playSound(blockPosition, SoundEvent.FIZZ)
            return true
        } else if (!isLit && itemInHand.type == ItemType.FLINT_AND_STEEL) {
            setLit(true)
            location.world.playSound(blockPosition, SoundEvent.IGNITE)
            return true
        }
        return false
    }

    fun setCandles(value: Int): BlockCandle {
        return setState("candles", value)
    }

    val candles: Int
        get() = if (stateExists("candles")) getIntState("candles") else 0

    fun setLit(value: Boolean): BlockCandle {
        return setState("lit", if (value) 1.toByte() else 0.toByte())
    }

    val isLit: Boolean
        get() = stateExists("lit") && getByteState("lit").toInt() == 1
}
