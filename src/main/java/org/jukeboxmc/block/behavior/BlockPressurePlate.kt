package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.entity.passiv.EntityHuman
import org.jukeboxmc.event.player.PlayerInteractEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockPressurePlate : Block {
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
        val target = world.getBlock(blockPosition)
        if (target.isTransparent) {
            return false
        }
        redstoneSignal = 0
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    override fun onUpdate(updateReason: UpdateReason): Long {
        if (updateReason == UpdateReason.SCHEDULED) {
            val power = redstoneSignal
            if (power > 0) {
                updateState(power)
            }
        }
        return -1
    }

    override fun enterBlock(player: Player) {
        val playerInteractEvent = PlayerInteractEvent(
            player,
            PlayerInteractEvent.Action.PHYSICAL,
            player.inventory.itemInHand,
            this,
        )
        Server.instance.pluginManager.callEvent(playerInteractEvent)
        if (playerInteractEvent.isCancelled) {
            return
        }
        updateState(redstoneSignal)
    }

    override fun leaveBlock(player: Player?) {
        updateState(redstoneSignal)
    }

    override val boundingBox: AxisAlignedBB
        get() = AxisAlignedBB(
            location.getX() + 0.0625f,
            location.getY(),
            location.getZ() + 0.0625f,
            location.getX() + 0.9375f,
            location.getY() + 1f,
            location.getZ() + 0.9375f,
        )
    var redstoneSignal: Int
        get() = if (stateExists("redstone_signal")) getIntState("redstone_signal") else 0
        set(value) {
            setState<Block>("redstone_signal", value)
        }
    private val redstoneStrength: Int
        private get() {
            val boundingBox = boundingBox
            for (entity in location.world!!.getNearbyEntities(boundingBox, location.dimension, null)) {
                if (entity is EntityHuman) {
                    return 15
                }
            }
            return 0
        }

    private fun updateState(oldSignal: Int) {
        val redstoneStrength = redstoneStrength
        val wasPowered = oldSignal > 0
        val isPowered = redstoneStrength > 0
        if (oldSignal != redstoneStrength) {
            redstoneSignal = redstoneStrength
            if (!isPowered && wasPowered) {
                location.world?.playSound(location, SoundEvent.POWER_OFF)
            } else if (isPowered && !wasPowered) {
                location.world?.playSound(location, SoundEvent.POWER_ON)
            }
        }
        if (isPowered) {
            location.world?.scheduleBlockUpdate(location, 20)
        }
    }
}
