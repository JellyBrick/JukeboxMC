package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.data.NpcRequestType
import com.nukkitx.protocol.bedrock.packet.NpcRequestPacket
import org.jukeboxmc.Server
import org.jukeboxmc.form.NpcDialogueForm
import org.jukeboxmc.form.element.NpcDialogueButton.ButtonMode
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class NpcRequestPacketHandler : PacketHandler<NpcRequestPacket> {
    override fun handle(packet: NpcRequestPacket, server: Server, player: Player) {
        val form = player.openNpcDialogueForms.stream()
            .filter { npcDialogueForm: NpcDialogueForm? ->
                npcDialogueForm!!.sceneName.equals(packet.sceneName, ignoreCase = true)
            }
            .findAny().orElse(null) ?: return
        val requestType: NpcRequestType = packet.requestType
        if (form.dialogueButtons.size == 0) {
            if (requestType == NpcRequestType.EXECUTE_CLOSING_COMMANDS) {
                player.removeNpcDialogueForm(form)
            }
            return
        }
        val button = form.dialogueButtons[packet.actionType] ?: return
        val mode = button.mode
        if (mode == ButtonMode.ON_ENTER && requestType == NpcRequestType.EXECUTE_OPENING_COMMANDS || mode == ButtonMode.BUTTON_MODE && requestType == NpcRequestType.EXECUTE_COMMAND_ACTION || mode == ButtonMode.ON_EXIT && requestType == NpcRequestType.EXECUTE_CLOSING_COMMANDS) {
            button.click?.run()
            form.close(player)
        }
    }
}
