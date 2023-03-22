package org.jukeboxmc.event.world

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ChunkLoadEvent
/**
 * Creates a new [WorldEvent]
 *
 * @param world which represents the world which comes with this event
 */(world: World, val chunk: Chunk?) : WorldEvent(world), Cancellable
