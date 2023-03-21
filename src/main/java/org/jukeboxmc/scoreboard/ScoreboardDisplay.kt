package org.jukeboxmc.scoreboard

import org.jukeboxmc.entity.Entity

/**
 * @author GoMint
 * @version 1.0
 */
class ScoreboardDisplay(
    val scoreboard: Scoreboard,
    val objectiveName: String,
    val displayName: String,
    val sortOrder: SortOrder,
    val lineEntry: LinkedHashMap<Int, String>,
) {
    fun addEntity(entity: Entity, score: Int): DisplayEntry {
        val scoreId = scoreboard.addOrUpdateEntity(entity, objectiveName, score)
        return DisplayEntry(scoreboard, scoreId)
    }

    fun addLine(line: String, score: Int): DisplayEntry {
        val scoreId = scoreboard.addOrUpdateLine(line, objectiveName, score)
        lineEntry[score] = line
        return DisplayEntry(scoreboard, scoreId)
    }

    fun removeEntry(entry: DisplayEntry) {
        scoreboard.removeScoreEntry(entry.scoreId)
    }

    fun getLine(score: Int): String? {
        return lineEntry.getOrDefault(score, null)
    }
}
