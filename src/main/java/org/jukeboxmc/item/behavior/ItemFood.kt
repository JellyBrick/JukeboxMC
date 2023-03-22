package org.jukeboxmc.item.behavior

import org.jukeboxmc.Server
import org.jukeboxmc.event.player.PlayerConsumeItemEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import kotlin.math.min

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class ItemFood : Item {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        if (player.isHungry || player.gameMode == GameMode.CREATIVE) {
            return true
        }
        player.hunger = player.hunger
        player.inventory.sendContents(player)
        return false
    }

    override fun onUse(player: Player): Boolean {
        if (player.isHungry) {
            val playerConsumeItemEvent = PlayerConsumeItemEvent(player, this)
            Server.instance.pluginManager.callEvent(playerConsumeItemEvent)
            if (playerConsumeItemEvent.isCancelled) {
                player.inventory.sendContents(player)
                return false
            }
            player.addHunger(hunger)
            val saturation = min(player.saturation + saturation, player.hunger.toFloat())
            player.saturation = saturation
            setAmount(amount - 1)
            player.inventory.itemInHand = this
            return true
        }
        return false
    }

    abstract val saturation: Float
    abstract val hunger: Int
}
