package org.jukeboxmc.form

import com.google.gson.JsonObject
import com.nukkitx.protocol.bedrock.data.entity.EntityData
import com.nukkitx.protocol.bedrock.packet.NpcDialoguePacket
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.Arrays
import java.util.UUID
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors
import org.jukeboxmc.Server
import org.jukeboxmc.entity.passiv.EntityNPC
import org.jukeboxmc.form.element.NpcDialogueButton
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Utils

/**
 * @author Kaooot
 * @version 1.0
 */
@Accessors(fluent = true, chain = true)
class NpcDialogueForm {
    @Getter
    val sceneName = UUID.randomUUID().toString()

    @Getter
    @Setter
    private val title: String? = null

    @Getter
    @Setter
    private val dialogue: String? = null

    @Getter
    @Setter
    private val npc: EntityNPC? = null
    private var actionJson = ""

    @Getter
    val dialogueButtons = ObjectArrayList<NpcDialogueButton>()
    fun buttons(vararg buttons: NpcDialogueButton): NpcDialogueForm {
        val urlTag = npc.getMetadata().getString(EntityData.URL_TAG)
        val urlTags: MutableList<JsonObject> =
            if (Utils.gson().fromJson<List<*>>(urlTag, MutableList::class.java) == null) ArrayList() else Utils.gson()
                .fromJson<List<*>>(urlTag, MutableList::class.java)
        for (button in buttons) {
            var found = false
            for (tag in urlTags) {
                if (tag["button_name"].asString.equals(button.text(), ignoreCase = true)) {
                    found = true
                }
            }
            if (!found) {
                urlTags.add(button.toJsonObject())
            }
        }
        actionJson = Utils.gson().toJson(urlTags)
        dialogueButtons.addAll(Arrays.asList(*buttons))
        return this
    }

    fun create(player: Player) {
        val setEntityDataPacket = SetEntityDataPacket()
        setEntityDataPacket.runtimeEntityId = npc.getEntityId()
        setEntityDataPacket.metadata.putAll(
            npc.getMetadata()
                .setString(EntityData.NAMETAG, title)
                .setString(EntityData.INTERACTIVE_TAG, dialogue)
                .entityDataMap
        )
        Server.Companion.getInstance().broadcastPacket(setEntityDataPacket)
        val npcDialoguePacket = NpcDialoguePacket()
        npcDialoguePacket.uniqueEntityId = npc.getEntityId()
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
        npcDialoguePacket.uniqueEntityId = npc.getEntityId()
        npcDialoguePacket.action = NpcDialoguePacket.Action.CLOSE
        npcDialoguePacket.dialogue = dialogue
        npcDialoguePacket.npcName = title
        npcDialoguePacket.sceneName = sceneName
        npcDialoguePacket.actionJson = actionJson
        player.playerConnection.sendPacket(npcDialoguePacket)
        player.removeNpcDialogueForm(this)
    }
}