package org.jukeboxmc.item.behavior

import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemGoldenAxe : Item, Durability {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun addToHand(player: Player) {
        val attribute = player.getAttribute(AttributeType.ATTACK_DAMAGE)
        attribute.setCurrentValue(3f)
    }

    override fun removeFromHand(player: Player) {
        val attribute = player.getAttribute(AttributeType.ATTACK_DAMAGE)
        attribute.setCurrentValue(attribute.minValue)
    }

    override val maxDurability: Int
        get() = 32
    override val toolType: ToolType
        get() = ToolType.AXE
    override val tierType: TierType
        get() = TierType.GOLD
}
