package org.jukeboxmc.util

import com.google.gson.Gson
import com.nukkitx.protocol.bedrock.packet.StartGamePacket
import org.jukeboxmc.Bootstrap
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Collections
import java.util.Objects
import java.util.function.Function
import java.util.stream.Collectors

/**
 * @author LucGamesYT
 * @version 1.0
 */
object ItemPalette {
    private val GSON = Gson()
    val IDENTIFIER_TO_RUNTIME: MutableMap<Identifier, Short> = LinkedHashMap()
    val RUNTIME_TO_IDENTIFIER: MutableMap<Short, Identifier> = LinkedHashMap()
    val MAPPING_IDENTIEFER: MutableMap<Identifier, Identifier> = LinkedHashMap()
    fun init() {
        try {
            Objects.requireNonNull(
                Bootstrap::class.java.classLoader.getResourceAsStream("runtime_item_states.json"),
            ).use { inputStream ->
                val parseItem =
                    GSON.fromJson<List<Map<String, Any>>>(InputStreamReader(inputStream), MutableList::class.java)
                for (entry in parseItem) {
                    val name: Identifier = Identifier.fromString((entry["name"] as String?)!!)
                    val runtimeId = (entry["id"] as Double).toInt().toShort()
                    IDENTIFIER_TO_RUNTIME[name] = runtimeId
                    RUNTIME_TO_IDENTIFIER[runtimeId] = name
                }
                mappingIdentifier(
                    Identifier.fromString("minecraft:acacia_door"),
                    Identifier.fromString("minecraft:item.acacia_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:bed"),
                    Identifier.fromString("minecraft:item.bed"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:beetroot"),
                    Identifier.fromString("minecraft:item.beetroot"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:birch_door"),
                    Identifier.fromString("minecraft:item.birch_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:brewing_stand"),
                    Identifier.fromString("minecraft:item.brewing_stand"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:cake"),
                    Identifier.fromString("minecraft:item.cake"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:camera"),
                    Identifier.fromString("minecraft:item.camera"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:campfire"),
                    Identifier.fromString("minecraft:item.campfire"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:cauldron"),
                    Identifier.fromString("minecraft:item.cauldron"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:chain"),
                    Identifier.fromString("minecraft:item.chain"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:crimson_door"),
                    Identifier.fromString("minecraft:item.crimson_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:dark_oak_door"),
                    Identifier.fromString("minecraft:item.dark_oak_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:flower_pot"),
                    Identifier.fromString("minecraft:item.flower_pot"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:frame"),
                    Identifier.fromString("minecraft:item.frame"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:glow_frame"),
                    Identifier.fromString("minecraft:item.glow_frame"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:hopper"),
                    Identifier.fromString("minecraft:item.hopper"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:iron_door"),
                    Identifier.fromString("minecraft:item.iron_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:jungle_door"),
                    Identifier.fromString("minecraft:item.jungle_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:kelp"),
                    Identifier.fromString("minecraft:item.kelp"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:mangrove_door"),
                    Identifier.fromString("minecraft:item.mangrove_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:nether_sprouts"),
                    Identifier.fromString("minecraft:item.nether_sprouts"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:nether_wart"),
                    Identifier.fromString("minecraft:item.nether_wart"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:reeds"),
                    Identifier.fromString("minecraft:item.reeds"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:skull"),
                    Identifier.fromString("minecraft:item.skull"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:soul_campfire"),
                    Identifier.fromString("minecraft:item.soul_campfire"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:spruce_door"),
                    Identifier.fromString("minecraft:item.spruce_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:warped_door"),
                    Identifier.fromString("minecraft:item.warped_door"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:wheat"),
                    Identifier.fromString("minecraft:item.wheat"),
                )
                mappingIdentifier(
                    Identifier.fromString("minecraft:wooden_door"),
                    Identifier.fromString("minecraft:item.wooden_door"),
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun mappingIdentifier(blockIdentifier: Identifier, itemIdentifier: Identifier) {
        MAPPING_IDENTIEFER[blockIdentifier] = itemIdentifier
    }

    fun getItemIdentifier(blockIdentifier: Identifier): Identifier {
        return MAPPING_IDENTIEFER.getOrDefault(blockIdentifier, blockIdentifier)
    }

    @JvmStatic
    fun getRuntimeId(identifier: Identifier?): Int {
        return IDENTIFIER_TO_RUNTIME[identifier]!!.toInt()
    }

    @JvmStatic
    fun getIdentifier(runtimeId: Short): Identifier {
        return RUNTIME_TO_IDENTIFIER.getOrDefault(runtimeId, Identifier.fromString("minecraft:air"))
    }

    @JvmStatic
    val entries: MutableList<StartGamePacket.ItemEntry> = IDENTIFIER_TO_RUNTIME.entries.stream()
        .map { entry: Map.Entry<Identifier, Short> -> toEntry(entry) }
        .collect(Collectors.toList())

    private fun toEntry(entry: Map.Entry<Identifier, Short>): StartGamePacket.ItemEntry {
        return StartGamePacket.ItemEntry(entry.key.fullName, entry.value, false)
    }
}
