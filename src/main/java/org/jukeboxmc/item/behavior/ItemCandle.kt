package org.jukeboxmc.item.behavior

import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCandle : Item {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)
}