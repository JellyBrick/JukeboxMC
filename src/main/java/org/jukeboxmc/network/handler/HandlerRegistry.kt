package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.packet.AnimatePacket
import com.nukkitx.protocol.bedrock.packet.AnvilDamagePacket
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket
import com.nukkitx.protocol.bedrock.packet.BlockPickRequestPacket
import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket
import com.nukkitx.protocol.bedrock.packet.CraftingEventPacket
import com.nukkitx.protocol.bedrock.packet.EmotePacket
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket
import com.nukkitx.protocol.bedrock.packet.InteractPacket
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket
import com.nukkitx.protocol.bedrock.packet.ItemStackRequestPacket
import com.nukkitx.protocol.bedrock.packet.LevelSoundEventPacket
import com.nukkitx.protocol.bedrock.packet.LoginPacket
import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket
import com.nukkitx.protocol.bedrock.packet.ModalFormResponsePacket
import com.nukkitx.protocol.bedrock.packet.MovePlayerPacket
import com.nukkitx.protocol.bedrock.packet.NpcRequestPacket
import com.nukkitx.protocol.bedrock.packet.PacketViolationWarningPacket
import com.nukkitx.protocol.bedrock.packet.PlayerActionPacket
import com.nukkitx.protocol.bedrock.packet.PlayerSkinPacket
import com.nukkitx.protocol.bedrock.packet.RequestAbilityPacket
import com.nukkitx.protocol.bedrock.packet.RequestChunkRadiusPacket
import com.nukkitx.protocol.bedrock.packet.RequestNetworkSettingsPacket
import com.nukkitx.protocol.bedrock.packet.ResourcePackChunkRequestPacket
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket
import com.nukkitx.protocol.bedrock.packet.RespawnPacket
import com.nukkitx.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket
import com.nukkitx.protocol.bedrock.packet.TextPacket

/**
 * @author LucGamesYT
 * @version 1.0
 */
object HandlerRegistry {
    private val packetHandlerMap: MutableMap<Class<out BedrockPacket>, PacketHandler<out BedrockPacket>> = HashMap()
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

    fun getPacketHandler(clazz: Class<out BedrockPacket>): PacketHandler<out BedrockPacket>? {
        return packetHandlerMap[clazz]
    }

    inline fun <reified T : PacketHandler<out BedrockPacket>> getPacketHandler(clazz: Class<out BedrockPacket>): T? =
        getPacketHandler(clazz) as? T
}
