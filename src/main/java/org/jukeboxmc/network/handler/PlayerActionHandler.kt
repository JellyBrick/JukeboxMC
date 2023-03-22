package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.data.LevelEventType
import com.nukkitx.protocol.bedrock.data.PlayerActionType
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.event.player.PlayerInteractEvent
import org.jukeboxmc.event.player.PlayerJumpEvent
import org.jukeboxmc.event.player.PlayerToggleGlideEvent
import org.jukeboxmc.event.player.PlayerToggleSneakEvent
import org.jukeboxmc.event.player.PlayerToggleSprintEvent
import org.jukeboxmc.event.player.PlayerToggleSwimEvent
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.world.Particle

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerActionHandler : PacketHandler<PlayerActionPacket> {
    override fun handle(packet: PlayerActionPacket, server: Server, player: Player) {
        val lasBreakPosition: Vector = Vector(packet.blockPosition)
        when (packet.action) {
            PlayerActionType.DIMENSION_CHANGE_SUCCESS -> {
                val playerActionPacket = PlayerActionPacket()
                playerActionPacket.action = PlayerActionType.DIMENSION_CHANGE_SUCCESS
                player.playerConnection.sendPacket(playerActionPacket)
            }

            PlayerActionType.START_SNEAK -> {
                val playerToggleSneakEvent = PlayerToggleSneakEvent(player, true)
                server.pluginManager.callEvent(playerToggleSneakEvent)
                if (playerToggleSneakEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isSneaking = true
                }
            }

            PlayerActionType.STOP_SNEAK -> {
                val playerToggleSneakEvent = PlayerToggleSneakEvent(player, false)
                Server.instance.pluginManager.callEvent(playerToggleSneakEvent)
                if (playerToggleSneakEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isSneaking = false
                }
            }

            PlayerActionType.START_SPRINT -> {
                val playerToggleSprintEvent = PlayerToggleSprintEvent(player, true)
                Server.instance.pluginManager.callEvent(playerToggleSprintEvent)
                if (playerToggleSprintEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isSprinting = true
                }
            }

            PlayerActionType.STOP_SPRINT -> {
                val playerToggleSprintEvent = PlayerToggleSprintEvent(player, false)
                Server.instance.pluginManager.callEvent(playerToggleSprintEvent)
                if (playerToggleSprintEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isSprinting = false
                }
            }

            PlayerActionType.START_SWIMMING -> {
                val playerToggleSwimEvent = PlayerToggleSwimEvent(player, true)
                Server.instance.pluginManager.callEvent(playerToggleSwimEvent)
                if (playerToggleSwimEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isSwimming = true
                }
            }

            PlayerActionType.STOP_SWIMMING -> {
                val playerToggleSwimEvent = PlayerToggleSwimEvent(player, false)
                Server.instance.pluginManager.callEvent(playerToggleSwimEvent)
                if (playerToggleSwimEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isSwimming = false
                }
            }

            PlayerActionType.START_GLIDE -> {
                val playerToggleGlideEvent = PlayerToggleGlideEvent(player, true)
                Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
                if (playerToggleGlideEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isGliding = true
                }
            }

            PlayerActionType.STOP_GLIDE -> {
                val playerToggleGlideEvent = PlayerToggleGlideEvent(player, false)
                Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
                if (playerToggleGlideEvent.isCancelled) {
                    player.updateMetadata()
                } else {
                    player.isGliding = false
                }
            }

            PlayerActionType.START_BREAK -> {
                val currentBreakTime = System.currentTimeMillis()
                val startBreakBlock = player.world!!.getBlock(lasBreakPosition)
                val playerInteractEvent = PlayerInteractEvent(
                    player,
                    if (startBreakBlock.type == BlockType.AIR) PlayerInteractEvent.Action.LEFT_CLICK_AIR else PlayerInteractEvent.Action.LEFT_CLICK_BLOCK,
                    player.inventory.itemInHand,
                    startBreakBlock,
                )
                Server.instance.pluginManager.callEvent(playerInteractEvent)
                if (player.gameMode == GameMode.SURVIVAL) {
                    if (player.lasBreakPosition == lasBreakPosition && currentBreakTime - player.lastBreakTime < 10 || lasBreakPosition.distanceSquared(
                            player.location,
                        ) > 100
                    ) {
                        return
                    }
                    val breakTime = Math.ceil(startBreakBlock.getBreakTime(player.inventory.itemInHand, player) * 20)
                    if (breakTime > 0) {
                        val levelEventPacket = LevelEventPacket()
                        levelEventPacket.type = LevelEventType.BLOCK_START_BREAK
                        levelEventPacket.position = packet.blockPosition.toFloat()
                        levelEventPacket.data = (65535 / breakTime).toInt()
                        player.world?.sendChunkPacket(
                            lasBreakPosition.chunkX,
                            lasBreakPosition.chunkZ,
                            levelEventPacket,
                        )
                    }
                }
                player.isBreakingBlock = true
                player.lasBreakPosition = lasBreakPosition
                player.lastBreakTime = currentBreakTime
            }

            PlayerActionType.STOP_BREAK, PlayerActionType.ABORT_BREAK -> {
                val levelEventPacket = LevelEventPacket()
                levelEventPacket.type = LevelEventType.BLOCK_STOP_BREAK
                levelEventPacket.position = packet.blockPosition.toFloat()
                levelEventPacket.data = 0
                player.world?.sendChunkPacket(lasBreakPosition.chunkX, lasBreakPosition.chunkZ, levelEventPacket)
                player.isBreakingBlock = false
            }

            PlayerActionType.CONTINUE_BREAK -> {
                if (player.isBreakingBlock) {
                    val continueBlockBreak = player.world!!.getBlock(lasBreakPosition)
                    player.world!!.spawnParticle(
                        null,
                        Particle.CRACK_BLOCK,
                        lasBreakPosition,
                        continueBlockBreak.runtimeId or (packet.face shl 24),
                    )
                }
            }

            PlayerActionType.RESPAWN -> {
                player.respawn()
            }

            PlayerActionType.JUMP -> {
                val playerJumpEvent = PlayerJumpEvent(player)
                Server.instance.pluginManager.callEvent(playerJumpEvent)
                if (player.isSprinting) {
                    if (player.gameMode == GameMode.SURVIVAL) {
                        player.exhaust(0.8f)
                    }
                } else {
                    if (player.gameMode == GameMode.SURVIVAL) {
                        player.exhaust(0.2f)
                    }
                }
            }

            else -> {}
        }
    }
}
