package org.jukeboxmc.player.skin

import lombok.ToString

/**
 * @author LucGamesYT
 * @version 1.0
 */
@ToString
class PersonaPiece(
    val pieceId: String,
    val pieceType: String,
    val packId: String,
    val productId: String,
    val isDefault: Boolean
)