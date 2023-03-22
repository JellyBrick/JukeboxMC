package org.jukeboxmc.event.world

import org.jukeboxmc.event.Event
import org.jukeboxmc.world.World

/**
 * @author Kaooot
 * @version 1.0
 */
abstract class WorldEvent
/**
 * Creates a new [WorldEvent]
 *
 * @param world which represents the world which comes with this event
 */(
    /**
     * Retrives the [World] which comes with this [WorldEvent]
     *
     * @return a fresh [World]
     */
    open val world: World,
) : Event()
