package org.jukeboxmc.event.player

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author Kaooot
 * @version 1.0
 */
class PlayerInteractEvent : PlayerEvent, Cancellable {
    /**
     * Retrieves the [Action]
     *
     * @return a fresh [Action]
     */
    val action: Action
    /**
     * Retrieves the interaction [Item]
     *
     * @return a fresh [Item]
     */
    /**
     * Modifies the interaction [Item]
     *
     * @param item which should be modified
     */
    var item: Item

    /**
     * Retrieves the [Block] the player clicked at
     *
     * @return a fresh [Block]
     */
    val clickedBlock: Block?

    /**
     * Retrieves the [Vector] the player clicked at
     *
     * @return a fresh [Vector]
     */
    val touchVector: Vector?

    /**
     * Creates a new [PlayerInteractEvent]
     *
     * @param player       who has interacted with something
     * @param action       which represents the interaction type
     * @param item         which is the interaction item
     * @param touchVector  which stands for the vector the player clicked at
     */
    constructor(player: Player, action: Action, item: Item, touchVector: Vector?) : super(player) {
        this.action = action
        this.item = item
        this.touchVector = touchVector
        clickedBlock = null
    }

    /**
     * Creates a new [PlayerInteractEvent]
     *
     * @param player       who has interacted with something
     * @param action       which represents the interaction type
     * @param item         which is the interaction item
     * @param clickedBlock which stands for the block the player clicked at
     */
    constructor(player: Player, action: Action, item: Item, clickedBlock: Block) : super(player) {
        this.action = action
        this.item = item
        this.clickedBlock = clickedBlock
        touchVector = clickedBlock.location
    }

    /**
     * Representation of the type of interaction which takes place in this [PlayerInteractEvent]
     */
    enum class Action {
        LEFT_CLICK_AIR, RIGHT_CLICK_AIR, LEFT_CLICK_BLOCK, RIGHT_CLICK_BLOCK, PHYSICAL
    }
}