package org.jukeboxmc.world

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class Difficulty {
    PEACEFUL, EASY, NORMAL, HARD;

    companion object {
        fun getDifficulty(value: Int): Difficulty {
            return when (value) {
                0 -> PEACEFUL
                1 -> EASY
                2 -> NORMAL
                else -> HARD
            }
        }
    }
}