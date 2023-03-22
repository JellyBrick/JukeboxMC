package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.DripstoneThickness
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
class BlockPointedDripstone : Block {
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
        val upBlock = world.getBlock(placePosition).getSide(BlockFace.UP)
        val downBlock = world.getBlock(placePosition).getSide(BlockFace.DOWN)
        if (upBlock.isSolid) {
            isHanging = true
            world.setBlock(placePosition, this)
            return true
        } else if (downBlock.isSolid) {
            isHanging = false
            world.setBlock(placePosition, this)
        }
        return false
    }

    var dripstoneThickness: DripstoneThickness
        get() = if (stateExists("dripstone_thickness")) DripstoneThickness.valueOf(getStringState("dripstone_thickness")) else DripstoneThickness.TIP
        set(dripstoneThickness) {
            setState<Block>("dripstone_thickness", dripstoneThickness.name.lowercase(Locale.getDefault()))
        }
    var isHanging: Boolean
        get() = stateExists("hanging") && getByteState("hanging").toInt() == 1
        set(value) {
            setState<Block>("hanging", if (value) 1.toByte() else 0.toByte())
        }
}
