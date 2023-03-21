package org.jukeboxmc.player.skin

import lombok.ToString

/**
 * @author LucGamesYT
 * @version 1.0
 */
@ToString(exclude = ["data"])
class Image(val width: Int, val height: Int, val data: ByteArray) {

    companion object {
        fun getImage(data: ByteArray): Image? {
            return when (data.size) {
                Skin.Companion.SINGLE_SKIN_SIZE -> Image(64, 32, data)
                Skin.Companion.DOUBLE_SKIN_SIZE -> Image(64, 64, data)
                Skin.Companion.SKIN_128_64_SIZE -> Image(128, 64, data)
                Skin.Companion.SKIN_128_128_SIZE -> Image(128, 128, data)
                else -> null
            }
        }
    }
}