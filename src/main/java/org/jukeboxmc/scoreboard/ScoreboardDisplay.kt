package org.jukeboxmc.scoreboard

import lombok.AllArgsConstructor
import lombok.Data
import org.jukeboxmc.entity.Entity

/**
 * @author GoMint
 * @version 1.0
 */
@Data
@AllArgsConstructor
class ScoreboardDisplay {
    private val scoreboard: Scoreboard = null
    private val objectiveName: String = null
    private val displayName: String? = null
    private val sortOrder: SortOrder? = null
    private val lineEntry: LinkedHashMap<Int, String?>? = null
    fun addEntity(entity: Entity, score: Int): DisplayEntry {
        val scoreId = scoreboard.addOrUpdateEntity(entity, objectiveName, score)
        return DisplayEntry(scoreboard, scoreId)
    }

    fun addLine(line: String, score: Int): DisplayEntry {
        val scoreId = scoreboard.addOrUpdateLine(line, objectiveName, score)
        lineEntry!![score] = line
        return DisplayEntry(scoreboard, scoreId)
    }

    fun removeEntry(entry: DisplayEntry) {
        scoreboard.removeScoreEntry(entry.scoreId)
    }

    fun getLine(score: Int): String? {
        return lineEntry!!.getOrDefault(score, null)
    }
}