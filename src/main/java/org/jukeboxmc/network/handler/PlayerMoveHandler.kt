package org.jukeboxmc.network.handler

import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket
import org.jukeboxmc.Server
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import java.io.ByteArrayOutputStream

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerMoveHandler : PacketHandler<MovePlayerPacket> {
    override fun handle(packet: MovePlayerPacket, server: Server, player: Player) {
        if (!player.isSpawned) {
            return
        }
        if (player.teleportLocation != null) {
            return
        }
        val toLocation: Location = Location(
            player.world,
            Vector(packet.getPosition().sub(0f, player.eyeHeight, 0f)),
            packet.getRotation().y,
            packet.getRotation().x,
            player.location.dimension
        )
        val playerMoveEvent = PlayerMoveEvent(player, player.location, toLocation)
        Server.Companion.getInstance().getPluginManager().callEvent(playerMoveEvent)
        if (playerMoveEvent.isCancelled()) {
            playerMoveEvent.setTo(playerMoveEvent.getFrom())
        }
        player.lastLocation = player.getLocation()
        player.location = toLocation
        player.isOnGround = packet.isOnGround()
        val to: Location = playerMoveEvent.getTo()
        val fromLocation = player.location
        if (to.x != fromLocation.x || to.y != fromLocation.y || to.z != fromLocation.z || to.world !== fromLocation.world || to.yaw != fromLocation.yaw || to.pitch != fromLocation.pitch) {
            player.teleport(playerMoveEvent.getFrom())
        } else {
            val fromChunk = player.lastLocation.loadedChunk ?: return
            val toChunk = player.loadedChunk ?: return
            if (toChunk.x != fromChunk.x || toChunk.z != fromChunk.z) {
                fromChunk.removeEntity(player)
                toChunk.addEntity(player)
            }
            val moveX = toLocation.x - player.lastLocation.x
            val moveZ = toLocation.z - player.lastLocation.z
            var distance = Math.sqrt((moveX * moveX + moveZ * moveZ).toDouble()).toFloat()
            if (distance >= 0.01) {
                val swimmingValue = if (player.isSwimming || player.isInWater) 0.15f * distance else 0f
                if (swimmingValue != 0f) {
                    distance = 0f
                }
                if (player.isOnGround) {
                    if (player.isSprinting) {
                        if (player.gameMode == GameMode.SURVIVAL) {
                            player.exhaust(0.6f * distance + swimmingValue)
                        }
                    } else {
                        if (player.gameMode == GameMode.SURVIVAL) {
                            player.exhaust(0.1f * distance + swimmingValue)
                        }
                    }
                }
            }
            for (onlinePlayer in player.server.onlinePlayers) {
                if (onlinePlayer !== player) {
                    if (player.isSpawned && onlinePlayer!!.isSpawned) {
                        move(player, onlinePlayer)
                    }
                }
            }
        }
    }

    private fun getBytesFromObject(`object`: Any): ByteArray {
        try {
            val bos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(bos)
            oos.writeObject(`object`)
            val bytes = bos.toByteArray()
            bos.close()
            oos.close()
            return bytes
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }

    private fun move(target: Player, player: Player) {
        val moveAbsolutePacket = MoveEntityAbsolutePacket()
        moveAbsolutePacket.setRuntimeEntityId(target.entityId)
        moveAbsolutePacket.setPosition(target.location.toVector3f().add(0f, target.eyeHeight, 0f))
        moveAbsolutePacket.setRotation(Vector3f.from(target.location.pitch, target.location.yaw, target.location.yaw))
        moveAbsolutePacket.setOnGround(target.isOnGround)
        moveAbsolutePacket.setTeleported(false)
        player.playerConnection.sendPacket(moveAbsolutePacket)
    }
}