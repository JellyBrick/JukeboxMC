package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.UpdateReason
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
class BlockButton : Block {
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
        val block = world.getBlock(blockPosition)
        if (block.isTransparent) {
            return false
        }
        this.blockFace = blockFace
        isButtonPressed = false
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
        if (isButtonPressed) {
            return false
        }
        isButtonPressed = true
        location.world?.playSound(location, SoundEvent.POWER_ON)
        location.world?.scheduleBlockUpdate(location, 20)
        return true
    }

    override fun onUpdate(updateReason: UpdateReason): Long {
        if (updateReason == UpdateReason.SCHEDULED) {
            isButtonPressed = false
            location.world?.playSound(location, SoundEvent.POWER_OFF)
        }
        return -1
    }

    var isButtonPressed: Boolean
        get() = stateExists("button_pressed_bit") && getByteState("button_pressed_bit").toInt() == 1
        set(value) {
            setState<Block>("button_pressed_bit", if (value) 1.toByte() else 0.toByte())
        }
    var blockFace: BlockFace
        get() = if (stateExists("facing_direction")) BlockFace.values()[getIntState("facing_direction")] else BlockFace.NORTH
        set(blockFace) {
            setState<Block>("facing_direction", blockFace.ordinal)
        }
}
