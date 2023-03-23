package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket
import org.cloudburstmc.protocol.bedrock.packet.AnvilDamagePacket
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket
import org.cloudburstmc.protocol.bedrock.packet.BlockPickRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket
import org.cloudburstmc.protocol.bedrock.packet.CraftingEventPacket
import org.cloudburstmc.protocol.bedrock.packet.EmotePacket
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.PacketViolationWarningPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket
import org.cloudburstmc.protocol.bedrock.packet.RequestAbilityPacket
import org.cloudburstmc.protocol.bedrock.packet.RequestChunkRadiusPacket
import org.cloudburstmc.protocol.bedrock.packet.RequestNetworkSettingsPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackClientResponsePacket
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket
import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket
import org.cloudburstmc.protocol.bedrock.packet.TextPacket

/**
 * @author LucGamesYT
 * @version 1.0
 */
object HandlerRegistry {
    private val packetHandlerMap: MutableMap<Class<out BedrockPacket>, PacketHandler<out BedrockPacket>> = HashMap()

    @JvmStatic
    fun init() {
        packetHandlerMap[PacketViolationWarningPacket::class.java] = PacketViolationWarningHandler()
        packetHandlerMap[RequestNetworkSettingsPacket::class.java] = RequestNetworkSettingsHandler()
        packetHandlerMap[LoginPacket::class.java] = LoginHandler()
        packetHandlerMap[ResourcePackClientResponsePacket::class.java] = ResourcePackClientResponseHandler()
        packetHandlerMap[RequestChunkRadiusPacket::class.java] = RequestChunkRadiusHandler()
        packetHandlerMap[MovePlayerPacket::class.java] = PlayerMoveHandler()
        packetHandlerMap[TextPacket::class.java] = TextHandler()
        packetHandlerMap[InventoryTransactionPacket::class.java] = InventoryTransactionHandler()
        packetHandlerMap[ContainerClosePacket::class.java] = ContainerCloseHandler()
        packetHandlerMap[InteractPacket::class.java] = InteractHandler()
        packetHandlerMap[ItemStackRequestPacket::class.java] = ItemStackRequestHandler()
        packetHandlerMap[MobEquipmentPacket::class.java] = MobEquipmentHandler()
        packetHandlerMap[AnimatePacket::class.java] = AnimateHandler()
        packetHandlerMap[PlayerActionPacket::class.java] = PlayerActionHandler()
        packetHandlerMap[RequestAbilityPacket::class.java] = RequestAbilityHandler()
        packetHandlerMap[LevelSoundEventPacket::class.java] = LevelSoundEventHandler()
        packetHandlerMap[BlockPickRequestPacket::class.java] = BlockPickRequestHandler()
        packetHandlerMap[BlockEntityDataPacket::class.java] = BlockEntityDataHandler()
        packetHandlerMap[CraftingEventPacket::class.java] = CraftingEventHandler()
        packetHandlerMap[CommandRequestPacket::class.java] = CommandRequestHandler()
        packetHandlerMap[AnvilDamagePacket::class.java] = AnvilDamageHandler()
        packetHandlerMap[RespawnPacket::class.java] = RespawnHandler()
        packetHandlerMap[EntityEventPacket::class.java] = EntityEventHandler()
        packetHandlerMap[PlayerSkinPacket::class.java] = PlayerSkinHandler()
        packetHandlerMap[ModalFormResponsePacket::class.java] = ModalFormResponseHandler()
        packetHandlerMap[NpcRequestPacket::class.java] = NpcRequestPacketHandler()
        packetHandlerMap[SetLocalPlayerAsInitializedPacket::class.java] =
            SetLocalPlayerAsInitializedHandler()
        packetHandlerMap[EmotePacket::class.java] = EmoteHandler()
        packetHandlerMap[ResourcePackChunkRequestPacket::class.java] = ResourcePackChunkRequestHandler()
    }

    fun getPacketHandlerAsPacketHandler(clazz: Class<out BedrockPacket>): PacketHandler<out BedrockPacket>? {
        return packetHandlerMap[clazz]
    }

    inline fun <reified T : PacketHandler<out BedrockPacket>> getPacketHandler(clazz: Class<out BedrockPacket>): T? =
        getPacketHandlerAsPacketHandler(clazz) as? T
}
