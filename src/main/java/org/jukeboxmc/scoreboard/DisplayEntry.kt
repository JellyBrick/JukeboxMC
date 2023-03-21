package org.jukeboxmc.scoreboard

/**
 * @author GoMint
 * @version 1.0
 */
data class DisplayEntry @JvmOverloads constructor(
    val scoreboard: Scoreboard,
    val scoreId: Long = 0,
) {

    /**
     * The api is from the server software GoMint
     */
    var score: Int
        get() = scoreboard.getScore(scoreId)
        set(score) {
            scoreboard.updateScore(scoreId, score)
        }
}
