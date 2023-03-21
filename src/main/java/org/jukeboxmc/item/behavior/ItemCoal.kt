package org.jukeboxmc.item.behavior

import java.time.Duration
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCoal : Item, Burnable {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override val burnTime: Duration?
        get() = Duration.ofMillis(1600)
}