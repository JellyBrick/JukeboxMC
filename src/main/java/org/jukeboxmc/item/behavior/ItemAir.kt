package org.jukeboxmc.item.behavior

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemAir(itemType: ItemType) : Item(itemType) {
    init {
        amount = 0
        stackNetworkId = 0
    }

    override fun toItemData(): ItemData {
        return ItemData.AIR
    }
}
