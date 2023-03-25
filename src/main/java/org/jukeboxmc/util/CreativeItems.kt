package org.jukeboxmc.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.bedrock.data.defintions.BlockDefinition
import org.cloudburstmc.protocol.bedrock.data.defintions.ItemDefinition
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.jukeboxmc.Bootstrap
import org.jukeboxmc.Server
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemRegistry
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.util.Base64
import java.util.LinkedList

/**
 * @author LucGamesYT
 * @version 1.0
 */
object CreativeItems {
    private val CREATIVE_ITEMS: MutableList<ItemData> = mutableListOf()
    private val IDENTIFIER_BY_NETWORK_ID: MutableMap<Int, Identifier> = LinkedHashMap()
    fun init() {
        Bootstrap::class.java.classLoader.getResourceAsStream("creative_items.json")!!.use { inputStream ->
            val inputStreamReader = InputStreamReader(inputStream)
            val itemEntries =
                jacksonObjectMapper().readValue<Map<String, List<Map<String, Any>>>>(inputStreamReader)
            var netIdCounter = 0
            itemEntries.getValue("items").forEach { itemEntry ->
                // TODO: nbt_b64 contains the info of item enchantments
                val identifier: Identifier = Identifier.fromString(itemEntry["id"] as String)
                if (!ItemRegistry.hasItemType(identifier)) {
                    Server.instance.logger.debug("Item $identifier is not registered, skipping")
                    return@forEach
                }
                if (!itemEntry.containsKey("block_state_b64")) {
                    throw RuntimeException("Creative item " + identifier.fullName + " is missing block state")
                }
                val item: ItemData.Builder = run {
                    val blockStateNBT = if (itemEntry.containsKey("block_state_b64")) {
                        val blockState = itemEntry["block_state_b64"] as String
                        NbtUtils.createReaderLE(
                            ByteArrayInputStream(
                                Base64.getDecoder().decode(blockState.toByteArray()),
                            ),
                        )
                            .use { nbtReader -> nbtReader.readTag() as NbtMap }
                    } else {
                        NbtMap.EMPTY
                    }
                    val block = BlockPalette.getBlock(
                        Identifier.fromString(blockStateNBT.getString("name")),
                        blockStateNBT,
                    )
                    val meta = if (itemEntry.containsKey("damage")) {
                        itemEntry["damage"] as Int
                    } else {
                        0
                    }
                    val i = Item.createItem(identifier)
                    toItemData(i.definition, block.definition, meta)
                }
                if (itemEntry.containsKey("damage")) {
                    item.damage((itemEntry["damage"] as Int).toShort().toInt())
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
    }

    val creativeItems: List<ItemData>
        get() = CREATIVE_ITEMS

    fun getIdentifier(networkId: Int): Identifier? {
        return IDENTIFIER_BY_NETWORK_ID[networkId]
    }

    private fun toItemData(
        itemDefinition: ItemDefinition,
        blockDefinition: BlockDefinition,
        meta: Int,
    ): ItemData.Builder {
        return ItemData.builder()
            .usingNetId(false)
            .definition(itemDefinition) // TODO: component based
            .blockDefinition(blockDefinition)
            .count(1)
            .damage(meta)
            .canPlace(*arrayOf())
            .canBreak(*arrayOf())
    }
}
