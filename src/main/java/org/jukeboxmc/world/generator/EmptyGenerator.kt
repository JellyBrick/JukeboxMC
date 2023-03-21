package org.jukeboxmc.world.generator

import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EmptyGenerator(world: World?) : Generator() {
    override fun generate(chunk: Chunk, chunkX: Int, chunkZ: Int) {}
    override fun populate(manager: PopulationChunkManager, chunkX: Int, chunkZr: Int) {}
    override fun finish(manager: PopulationChunkManager?, chunkX: Int, chunkZ: Int) {}
    override val spawnLocation: Vector
        get() = Vector(0, 64, 0)
}