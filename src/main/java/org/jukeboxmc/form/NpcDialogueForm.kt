package org.jukeboxmc.form

import com.google.gson.JsonObject
import com.nukkitx.protocol.bedrock.data.entity.EntityData
import com.nukkitx.protocol.bedrock.packet.NpcDialoguePacket
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.jukeboxmc.Server
import org.jukeboxmc.entity.passiv.EntityNPC
import org.jukeboxmc.form.element.NpcDialogueButton
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Utils
import java.util.UUID

/**
 * @author Kaooot
 * @version 1.0
 */
class NpcDialogueForm(
    private var title: String,
    private var dialogue: String,
    private var npc: EntityNPC,
) {
    val sceneName = UUID.randomUUID().toString()
    private var actionJson = ""

    val dialogueButtons = ObjectArrayList<NpcDialogueButton>()

    fun buttons(vararg buttons: NpcDialogueButton): NpcDialogueForm {
        val urlTag = npc.metadata.getString(EntityData.URL_TAG)
        val urlTags: MutableList<JsonObject> =
            if (Utils.gson.fromJson<List<*>>(urlTag, MutableList::class.java) == null) {
                mutableListOf()
            } else {
                Utils.gson
                    .fromJson<MutableList<JsonObject>>(urlTag, MutableList::class.java)
            }
        for (button in buttons) {
            var found = false
            for (tag in urlTags) {
                if (tag["button_name"].asString.equals(button.text, ignoreCase = true)) {
                    found = true
                }
            }
            if (!found) {
                urlTags.add(button.toJsonObject())
            }
        }
        actionJson = Utils.gson.toJson(urlTags)
        dialogueButtons.addAll(listOf(*buttons))
        return this
    }

    fun create(player: Player) {
        val setEntityDataPacket = SetEntityDataPacket()
        setEntityDataPacket.runtimeEntityId = npc.entityId
        setEntityDataPacket.metadata.putAll(
            npc.metadata
                .setString(EntityData.NAMETAG, title)
                .setString(EntityData.INTERACTIVE_TAG, dialogue)
                .getEntityDataMap(),
        )
        Server.instance.broadcastPacket(setEntityDataPacket)
        val npcDialoguePacket = NpcDialoguePacket()
        npcDialoguePacket.uniqueEntityId = npc.entityId
        npcDialoguePacket.action = NpcDialoguePacket.Action.OPEN
        npcDialoguePacket.dialogue = dialogue
        npcDialoguePacket.npcName = title
        npcDialoguePacket.sceneName = sceneName
        npcDialoguePacket.actionJson = actionJson
        player.playerConnection.sendPacket(npcDialoguePacket)
        player.addNpcDialogueForm(this)
    }

    fun close(player: Player) {
        val npcDialoguePacket = NpcDialoguePacket()
        npcDialoguePacket.uniqueEntityId = npc.entityId
        npcDialoguePacket.action = NpcDialoguePacket.Action.CLOSE
        npcDialoguePacket.dialogue = dialogue
        npcDialoguePacket.npcName = title
        npcDialoguePacket.sceneName = sceneName
        npcDialoguePacket.actionJson = actionJson
        player.playerConnection.sendPacket(npcDialoguePacket)
        player.removeNpcDialogueForm(this)
    }
}
