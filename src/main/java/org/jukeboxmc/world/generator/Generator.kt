package org.jukeboxmc.world.generator

import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Generator {
    abstract fun generate(chunk: Chunk, chunkX: Int, chunkZ: Int)
    abstract fun populate(manager: PopulationChunkManager, chunkX: Int, chunkZ: Int)
    abstract fun finish(manager: PopulationChunkManager, chunkX: Int, chunkZ: Int)
    abstract val spawnLocation: Vector
}
