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
class ItemIronChestplate : ItemArmor, Durability {
    constructor(itemType: ItemType) : super(itemType)
    constructor(identifier: Identifier?) : super(identifier)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        val oldItem = player.armorInventory.chestplate
        player.armorInventory.chestplate = this
        player.inventory.itemInHand = oldItem
        return super.useInAir(player, clickVector)
    }

    override val armorTierType: ArmorTierType
        get() = ArmorTierType.IRON
    override val armorPoints: Int
        get() = 6

    override fun playEquipSound(player: Player) {
        player.playSound(Sound.ARMOR_EQUIP_IRON)
    }

    override val maxDurability: Int
        get() = 240
}