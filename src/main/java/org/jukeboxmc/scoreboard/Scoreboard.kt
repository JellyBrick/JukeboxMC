package org.jukeboxmc.scoreboard

import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.ScoreInfo
import com.nukkitx.protocol.bedrock.packet.RemoveObjectivePacket
import com.nukkitx.protocol.bedrock.packet.SetDisplayObjectivePacket
import com.nukkitx.protocol.bedrock.packet.SetScorePacket
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongList
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.player.Player
import java.util.Locale

/**
 * @author GoMint
 * @version 1.0
 */
class Scoreboard {
    private var scoreIdCounter: Long = 0
    private val scoreboardLines: Long2ObjectMap<ScoreboardLine> = Long2ObjectArrayMap()
    private val viewers: MutableSet<Player> = HashSet()
    private val displays: MutableMap<DisplaySlot, ScoreboardDisplay> = HashMap()

    fun addDisplay(slot: DisplaySlot, objectiveName: String, displayName: String): ScoreboardDisplay {
        return this.addDisplay(slot, objectiveName, displayName, SortOrder.ASCENDING)
    }

    fun addDisplay(
        slot: DisplaySlot,
        objectiveName: String,
        displayName: String,
        sortOrder: SortOrder,
    ): ScoreboardDisplay {
        var scoreboardDisplay = displays[slot]
        if (scoreboardDisplay == null) {
            scoreboardDisplay = ScoreboardDisplay(this, objectiveName, displayName, sortOrder, LinkedHashMap())
            displays[slot] = scoreboardDisplay
            broadcast(constructDisplayPacket(slot, scoreboardDisplay))
        }
        return scoreboardDisplay
    }

    fun removeDisplay(slot: DisplaySlot) {
        val display = displays.remove(slot)
        if (display != null) {
            val validScoreIDs: LongList = LongArrayList()
            val fastSet = scoreboardLines.long2ObjectEntrySet() as Long2ObjectMap.FastEntrySet<ScoreboardLine>
            val fastIterator = fastSet.fastIterator()
            while (fastIterator.hasNext()) {
                val entry = fastIterator.next()
                if (entry.value.objective == display.objectiveName) {
                    validScoreIDs.add(entry.longKey)
                }
            }
            for (scoreID in validScoreIDs) {
                scoreboardLines.remove(scoreID)
            }
            broadcast(this.constructRemoveScores(validScoreIDs))
            broadcast(constructRemoveDisplayPacket(display))
        }
    }

    private fun constructDisplayPacket(slot: DisplaySlot, display: ScoreboardDisplay): SetDisplayObjectivePacket {
        val packetSetObjective = SetDisplayObjectivePacket()
        packetSetObjective.criteria = "dummy"
        packetSetObjective.displayName = display.displayName
        packetSetObjective.objectiveId = display.objectiveName
        packetSetObjective.displaySlot = slot.name.lowercase(Locale.getDefault())
        packetSetObjective.sortOrder = display.sortOrder.ordinal
        return packetSetObjective
    }

    private fun broadcast(packet: BedrockPacket) {
        for (viewer in viewers) {
            viewer.playerConnection.sendPacket(packet)
        }
    }

    fun addOrUpdateLine(line: String, objective: String, score: Int): Long {
        val fastEntrySet = scoreboardLines.long2ObjectEntrySet() as Long2ObjectMap.FastEntrySet<ScoreboardLine>
        val fastIterator = fastEntrySet.fastIterator()
        while (fastIterator.hasNext()) {
            val entry = fastIterator.next()
            if (entry.value.type.toInt() == 3 && entry.value.line == line && entry.value.objective == objective) {
                return entry.longKey
            }
        }
        val newId = scoreIdCounter++
        val scoreboardLine = ScoreboardLine(3.toByte(), 0, line, objective, score)
        scoreboardLines.put(newId, scoreboardLine)
        broadcast(this.constructSetScore(newId, scoreboardLine))
        return newId
    }

    fun addOrUpdateEntity(entity: Entity, objective: String, score: Int): Long {
        // Check if we already have this registered
        val fastEntrySet = scoreboardLines.long2ObjectEntrySet() as Long2ObjectMap.FastEntrySet<ScoreboardLine>
        val fastIterator = fastEntrySet.fastIterator()
        while (fastIterator.hasNext()) {
            val entry = fastIterator.next()
            if (entry.value.entityId == entity.entityId && entry.value.objective == objective) {
                return entry.longKey
            }
        }
        val newId = scoreIdCounter++
        val scoreboardLine =
            ScoreboardLine((if (entity is Player) 1 else 2).toByte(), entity.entityId, "", objective, score)
        scoreboardLines.put(newId, scoreboardLine)
        broadcast(this.constructSetScore(newId, scoreboardLine))
        return newId
    }

    private fun constructSetScore(newId: Long, line: ScoreboardLine): SetScorePacket {
        val setScorePacket = SetScorePacket()
        setScorePacket.action = SetScorePacket.Action.SET
        setScorePacket.infos.add(ScoreInfo(newId, line.objective, line.score, line.line))
        return setScorePacket
    }

    private fun constructSetScore(): SetScorePacket {
        val setScorePacket = SetScorePacket()
        setScorePacket.action = SetScorePacket.Action.SET
        val entries: MutableList<ScoreInfo> = ArrayList<ScoreInfo>()
        val fastEntrySet = scoreboardLines.long2ObjectEntrySet() as Long2ObjectMap.FastEntrySet<ScoreboardLine>
        val fastIterator = fastEntrySet.fastIterator()
        while (fastIterator.hasNext()) {
            val entry = fastIterator.next()
            entries.add(ScoreInfo(entry.longKey, entry.value.objective, entry.value.score, entry.value.line))
        }
        setScorePacket.infos.addAll(entries)
        return setScorePacket
    }

    fun showFor(player: Player) {
        if (viewers.add(player)) {
            for ((key, value) in displays) {
                player.playerConnection.sendPacket(constructDisplayPacket(key, value))
            }
            player.playerConnection.sendPacket(this.constructSetScore())
        }
    }

    fun hideFor(player: Player) {
        if (viewers.remove(player)) {
            val validScoreIDs: LongList = LongArrayList()
            val fastSet = scoreboardLines.long2ObjectEntrySet() as Long2ObjectMap.FastEntrySet<ScoreboardLine>
            val fastIterator = fastSet.fastIterator()
            while (fastIterator.hasNext()) {
                validScoreIDs.add(fastIterator.next().longKey)
            }
            player.playerConnection.sendPacket(this.constructRemoveScores(validScoreIDs))
            for ((_, value) in displays) {
                player.playerConnection.sendPacket(constructRemoveDisplayPacket(value))
            }
        }
    }

    private fun constructRemoveScores(scoreIDs: LongList): SetScorePacket {
        val setScorePacket = SetScorePacket()
        setScorePacket.action = SetScorePacket.Action.REMOVE
        val entries: MutableList<ScoreInfo> = ArrayList<ScoreInfo>()
        for (scoreID in scoreIDs) {
            entries.add(ScoreInfo(scoreID, "", 0))
        }
        setScorePacket.infos.addAll(entries)
        return setScorePacket
    }

    private fun constructRemoveDisplayPacket(display: ScoreboardDisplay): RemoveObjectivePacket {
        val removeObjectivePacket = RemoveObjectivePacket()
        removeObjectivePacket.objectiveId = display.objectiveName
        return removeObjectivePacket
    }

    fun updateScore(scoreId: Long, score: Int) {
        val line = scoreboardLines[scoreId]
        if (line != null) {
            line.score = score
            broadcast(this.constructSetScore(scoreId, line))
        }
    }

    fun removeScoreEntry(scoreId: Long) {
        val line = scoreboardLines.remove(scoreId)
        if (line != null) {
            broadcast(this.constructRemoveScores(scoreId))
        }
    }

    private fun constructRemoveScores(scoreId: Long): SetScorePacket {
        val setScorePacket = SetScorePacket()
        setScorePacket.action = SetScorePacket.Action.REMOVE
        setScorePacket.infos.add(ScoreInfo(scoreId, "", 0))
        return setScorePacket
    }

    fun getScore(scoreId: Long): Int {
        val line = scoreboardLines.remove(scoreId)
        return if (line != null) {
            line.score
        } else {
            0
        }
    }

    private data class ScoreboardLine(
        val type: Byte = 0,
        val entityId: Long = 0,
        val line: String,
        val objective: String,
        var score: Int,
    )
}
