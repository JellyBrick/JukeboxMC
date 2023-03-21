package org.jukeboxmc.util

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtUtils
import java.io.InputStream
import org.jukeboxmc.Bootstrap

/**
 * @author LucGamesYT
 * @version 1.0
 */
object EntityIdentifiers {
    var identifiers: NbtMap? = null
        private set

    fun init() {
        val inputStream: InputStream =
            Bootstrap::class.java.classLoader.getResourceAsStream("entity_identifiers.dat")
                ?: throw AssertionError("Could not find entity_identifiers.dat")
        try {
            NbtUtils.createNetworkReader(inputStream)
                .use { nbtInputStream -> identifiers = nbtInputStream.readTag() as NbtMap }
        } catch (e: Exception) {
            throw AssertionError("Error whilst loading entity_identifiers.dat", e)
        }
    }
}
