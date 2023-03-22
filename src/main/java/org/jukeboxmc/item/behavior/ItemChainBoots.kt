package org.jukeboxmc.item.behavior

import org.jukeboxmc.item.ArmorTierType
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Sound

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemChainBoots : ItemArmor, Durability {
    constructor(itemType: ItemType) : super(itemType)
    constructor(identifier: Identifier?) : super(identifier)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        val oldItem = player.getArmorInventory().boots
        player.getArmorInventory().boots = this
        player.inventory.itemInHand = oldItem
        return super.useInAir(player, clickVector)
    }

    override val armorTierType: ArmorTierType
        get() = ArmorTierType.CHAIN
    override val armorPoints: Int
        get() = 1

    override fun playEquipSound(player: Player) {
        player.playSound(Sound.ARMOR_EQUIP_CHAIN)
    }

    override val maxDurability: Int
        get() = 195
}
