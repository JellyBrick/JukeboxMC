package org.jukeboxmc.util

import com.google.gson.Gson
import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtUtils
import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import org.jukeboxmc.Bootstrap
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Base64
import java.util.LinkedList
import java.util.Objects

/**
 * @author LucGamesYT
 * @version 1.0
 */
object CreativeItems {
    private val CREATIVE_ITEMS: MutableList<ItemData> = LinkedList()
    private val IDENTIFIER_BY_NETWORK_ID: MutableMap<Int, Identifier> = LinkedHashMap()
    fun init() {
        val GSON = Gson()
        try {
            Objects.requireNonNull<InputStream>(
                Bootstrap::class.java.classLoader.getResourceAsStream("creative_items.json"),
            ).use { inputStream ->
                val inputStreamReader = InputStreamReader(inputStream)
                val itemEntries =
                    GSON.fromJson<Map<String, List<Map<String, Any>>>>(inputStreamReader, MutableMap::class.java)
                var netIdCounter = 0
                for (itemEntry in itemEntries["items"]!!) {
                    val item: ItemData.Builder
                    val identifier: Identifier = Identifier.fromString((itemEntry["id"] as String?)!!)
                    item = if (itemEntry.containsKey("blockRuntimeId")) {
                        toItemData(identifier, (itemEntry["blockRuntimeId"] as Double).toInt())
                    } else {
                        toItemData(identifier, 0)
                    }
                    if (itemEntry.containsKey("damage")) {
                        item.damage((itemEntry["damage"] as Double).toInt().toShort().toInt())
                    }
                    val nbtTag = itemEntry["nbt_b64"] as String?
                    if (nbtTag != null) {
                        NbtUtils.createReaderLE(ByteArrayInputStream(Base64.getDecoder().decode(nbtTag.toByteArray())))
                            .use { nbtReader -> item.tag(nbtReader.readTag() as NbtMap) }
                    }
                    netIdCounter++
                    IDENTIFIER_BY_NETWORK_ID[netIdCounter] = identifier
                    CREATIVE_ITEMS.add(item.netId(netIdCounter).build())
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    val creativeItems: List<ItemData>
        get() = CREATIVE_ITEMS

    fun getIdentifier(networkId: Int): Identifier? {
        return IDENTIFIER_BY_NETWORK_ID[networkId]
    }

    private fun toItemData(identifier: Identifier, blockRuntimeId: Int): ItemData.Builder {
        return ItemData.builder()
            .id(ItemPalette.getRuntimeId(identifier))
            .blockRuntimeId(blockRuntimeId)
            .count(1)
            .canPlace(arrayOf())
            .canBreak(arrayOf())
    }
}
