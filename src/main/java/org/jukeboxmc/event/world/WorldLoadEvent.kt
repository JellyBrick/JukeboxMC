package org.jukeboxmc.event.world

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class WorldLoadEvent
/**
 * Creates a new [WorldLoadEvent]
 *
 * @param world which should be loaded
 */(
    override var world: World,
    val loadType: LoadType,
) : WorldEvent(world), Cancellable {

    enum class LoadType {
        LOAD, CREATE
    }
}
