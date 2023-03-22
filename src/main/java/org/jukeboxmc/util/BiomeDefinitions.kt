package org.jukeboxmc.util

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtUtils
import org.jukeboxmc.Bootstrap
import java.io.FileNotFoundException

/**
 * @author LucGamesYT
 * @version 1.0
 */
object BiomeDefinitions {
    var biomeDefinitions: NbtMap? = null
        private set

    fun init() {
        Bootstrap::class.java.classLoader.getResourceAsStream("biome_definitions.dat").use { inputStream ->
            if (inputStream == null) {
                throw FileNotFoundException("Could not find biome_definitions.dat")
            }
            NbtUtils.createNetworkReader(inputStream)
                .use { stream -> biomeDefinitions = stream.readTag() as NbtMap }
        }
    }
}
