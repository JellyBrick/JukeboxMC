package org.jukeboxmc.event.world

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class WorldUnloadEvent
/**
 * Creates a new [WorldUnloadEvent]
 *
 * @param world which should be unloaded
 */(
    /**
     * Modifies the [World]
     *
     * @param world which should be modified
     */
    override var world: World,
) : WorldEvent(world), Cancellable
