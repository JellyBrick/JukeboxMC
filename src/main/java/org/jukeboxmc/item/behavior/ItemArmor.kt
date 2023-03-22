package org.jukeboxmc.item.behavior

import org.jukeboxmc.item.ArmorTierType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class ItemArmor : Item {
    constructor(itemType: ItemType) : super(itemType)
    constructor(identifier: Identifier) : super(identifier)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        playEquipSound(player)
        return true
    }

    abstract val armorTierType: ArmorTierType
    abstract val armorPoints: Int
    abstract fun playEquipSound(player: Player)
}
