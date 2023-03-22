package org.jukeboxmc.player

import com.nukkitx.protocol.bedrock.data.GameType

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class GameMode(val identifier: String, gameType: GameType, id: Int) {
    SURVIVAL("Survival", GameType.SURVIVAL, 0),
    CREATIVE("Creative", GameType.CREATIVE, 1),
    ADVENTURE(
        "Adventure",
        GameType.ADVENTURE,
        2,
    ),
    SPECTATOR("Spectator", GameType.SPECTATOR, 6),
    ;

    private val gameType: GameType
    val id: Int

    init {
        this.gameType = gameType
        this.id = id
    }

    fun toGameType(): GameType {
        return gameType
    }
}
