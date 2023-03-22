package org.jukeboxmc.player.info

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class UIProfile {
    CLASSIC, POCKET;

    companion object {
        fun getById(id: Int): UIProfile? {
            return when (id) {
                0 -> CLASSIC
                1 -> POCKET
                else -> null
            }
        }
    }
}
