package org.jukeboxmc.item.behavior

import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemRabbitStew : ItemFood {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override val saturation: Float
        get() = 12
    override val hunger: Int
        get() = 10
}