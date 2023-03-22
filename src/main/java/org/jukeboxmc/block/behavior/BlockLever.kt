package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.LeverDirection
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Sound
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLever : Block {
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
        val block = world.getBlock(blockPosition)
        if (block.isTransparent) {
            return false
        }
        leverDirection = LeverDirection.forDirection(blockFace, player.direction.opposite())
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
        val open = isOpen
        isOpen = !open
        player.playSound(location, Sound.RANDOM_CLICK, 0.8f, if (open) 0.5f else 0.58f, true)
        return true
    }

    var isOpen: Boolean
        get() = stateExists("open_bit") && getByteState("open_bit").toInt() == 1
        set(value) {
            setState<Block>("open_bit", if (value) 1.toByte() else 0.toByte())
        }
    var leverDirection: LeverDirection
        get() = if (stateExists("lever_direction")) LeverDirection.valueOf(getStringState("lever_direction")) else LeverDirection.DOWN_EAST_WEST
        set(leverDirection) {
            setState<Block>("lever_direction", leverDirection.name.lowercase(Locale.getDefault()))
        }
}
