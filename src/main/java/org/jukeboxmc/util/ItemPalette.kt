package org.jukeboxmc.util

import com.google.gson.Gson
import com.nukkitx.protocol.bedrock.packet.StartGamePacket
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Collections
import java.util.Objects
import java.util.function.Function

/**
 * @author LucGamesYT
 * @version 1.0
 */
object ItemPalette {
    private val GSON = Gson()
    val IDENTIFIER_TO_RUNTIME: MutableMap<Identifier?, Short> = LinkedHashMap()
    val RUNTIME_TO_IDENTIFIER: MutableMap<Short, Identifier> = LinkedHashMap()
    val MAPPING_IDENTIEFER: MutableMap<Identifier, Identifier> = LinkedHashMap()
    fun init() {
        try {
            Objects.requireNonNull<InputStream>(
                Bootstrap::class.java.getClassLoader().getResourceAsStream("runtime_item_states.json")
            ).use { inputStream ->
                val parseItem =
                    GSON.fromJson<List<Map<String, Any>>>(InputStreamReader(inputStream), MutableList::class.java)
                for (entry in parseItem) {
                    val name: Identifier = Identifier.Companion.fromString((entry["name"] as String?)!!)
                    val runtimeId = (entry["id"] as Double).toShort()
                    IDENTIFIER_TO_RUNTIME[name] = runtimeId
                    RUNTIME_TO_IDENTIFIER[runtimeId] = name
                }
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:acacia_door"),
                    Identifier.Companion.fromString("minecraft:item.acacia_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:bed"),
                    Identifier.Companion.fromString("minecraft:item.bed")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:beetroot"),
                    Identifier.Companion.fromString("minecraft:item.beetroot")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:birch_door"),
                    Identifier.Companion.fromString("minecraft:item.birch_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:brewing_stand"),
                    Identifier.Companion.fromString("minecraft:item.brewing_stand")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:cake"),
                    Identifier.Companion.fromString("minecraft:item.cake")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:camera"),
                    Identifier.Companion.fromString("minecraft:item.camera")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:campfire"),
                    Identifier.Companion.fromString("minecraft:item.campfire")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:cauldron"),
                    Identifier.Companion.fromString("minecraft:item.cauldron")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:chain"),
                    Identifier.Companion.fromString("minecraft:item.chain")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:crimson_door"),
                    Identifier.Companion.fromString("minecraft:item.crimson_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:dark_oak_door"),
                    Identifier.Companion.fromString("minecraft:item.dark_oak_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:flower_pot"),
                    Identifier.Companion.fromString("minecraft:item.flower_pot")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:frame"),
                    Identifier.Companion.fromString("minecraft:item.frame")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:glow_frame"),
                    Identifier.Companion.fromString("minecraft:item.glow_frame")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:hopper"),
                    Identifier.Companion.fromString("minecraft:item.hopper")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:iron_door"),
                    Identifier.Companion.fromString("minecraft:item.iron_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:jungle_door"),
                    Identifier.Companion.fromString("minecraft:item.jungle_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:kelp"),
                    Identifier.Companion.fromString("minecraft:item.kelp")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:mangrove_door"),
                    Identifier.Companion.fromString("minecraft:item.mangrove_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:nether_sprouts"),
                    Identifier.Companion.fromString("minecraft:item.nether_sprouts")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:nether_wart"),
                    Identifier.Companion.fromString("minecraft:item.nether_wart")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:reeds"),
                    Identifier.Companion.fromString("minecraft:item.reeds")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:skull"),
                    Identifier.Companion.fromString("minecraft:item.skull")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:soul_campfire"),
                    Identifier.Companion.fromString("minecraft:item.soul_campfire")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:spruce_door"),
                    Identifier.Companion.fromString("minecraft:item.spruce_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:warped_door"),
                    Identifier.Companion.fromString("minecraft:item.warped_door")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:wheat"),
                    Identifier.Companion.fromString("minecraft:item.wheat")
                )
                mappingIdentifier(
                    Identifier.Companion.fromString("minecraft:wooden_door"),
                    Identifier.Companion.fromString("minecraft:item.wooden_door")
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

    fun getRuntimeId(identifier: Identifier?): Int {
        return IDENTIFIER_TO_RUNTIME[identifier]
    }

    fun getIdentifier(runtimeId: Short): Identifier {
        return RUNTIME_TO_IDENTIFIER.getOrDefault(runtimeId, Identifier.Companion.fromString("minecraft:air"))
    }

    val entries: List<StartGamePacket.ItemEntry>
        get() = NonStream.Companion.map<Map.Entry<Identifier, Short>, java.util.ArrayList<StartGamePacket.ItemEntry>, List<StartGamePacket.ItemEntry>, StartGamePacket.ItemEntry>(
            IDENTIFIER_TO_RUNTIME.entries,
            Function { obj: Map.Entry<Identifier?, Short?>? -> toEntry() },
            NonStream.CollectionFactory<java.util.ArrayList<StartGamePacket.ItemEntry>, StartGamePacket.ItemEntry> { ArrayList() },
            Function<java.util.ArrayList<StartGamePacket.ItemEntry?>, List<StartGamePacket.ItemEntry>> { list: java.util.ArrayList<StartGamePacket.ItemEntry?>? ->
                Collections.unmodifiableList(list)
            })

    private fun toEntry(entry: Map.Entry<Identifier, Short>): StartGamePacket.ItemEntry {
        return StartGamePacket.ItemEntry(entry.key.fullName, entry.value, false)
    }
}