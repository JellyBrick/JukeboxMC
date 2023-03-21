package org.jukeboxmc.scoreboard

import lombok.Getter
import lombok.RequiredArgsConstructor

/**
 * @author GoMint
 * @version 1.0
 */
@RequiredArgsConstructor
class DisplayEntry {
    /**
     * The api is from the server software GoMint
     */
    private val scoreboard: Scoreboard = null

    @Getter
    private val scoreId: Long = 0
    var score: Int
        get() = scoreboard.getScore(scoreId)
        set(score) {
            scoreboard.updateScore(scoreId, score)
        }
}