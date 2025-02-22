package org.jukeboxmc.item.behavior

import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import java.time.Duration

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemWoodenHoe : Item, Durability, Burnable {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun addToHand(player: Player) {
        val attribute = player.getAttribute(AttributeType.ATTACK_DAMAGE)
        attribute.setCurrentValue(2f)
    }

    override fun removeFromHand(player: Player) {
        val attribute = player.getAttribute(AttributeType.ATTACK_DAMAGE)
        attribute.setCurrentValue(attribute.minValue)
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(200)
    override val maxDurability: Int
        get() = 59
    override val toolType: ToolType
        get() = ToolType.HOE
    override val tierType: TierType
        get() = TierType.WOODEN
}
