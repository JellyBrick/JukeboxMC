package org.jukeboxmc.world.chunk.manager

import java.util.concurrent.locks.Lock
import java.util.function.BiFunction
import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.ChunkState

/**
 * @author LucGamesYT
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PopulationTask : BiFunction<Chunk, MutableList<Chunk>, Chunk> {
    override fun apply(chunk: Chunk, chunks: MutableList<Chunk>): Chunk {
        if (chunk.isPopulated) {
            return chunk
        }
        chunks.add(chunk)
        val locks: MutableSet<Lock> = HashSet()
        for (value in chunks) {
            val lock = value.writeLock
            lock.lock()
            locks.add(lock)
        }
        try {
            chunk.world.getGenerator(chunk.dimension).populate(PopulationChunkManager(chunk, chunks), chunk.x, chunk.z)
            chunk.chunkState = ChunkState.FINISHED
            chunk.isDirty = true
        } finally {
            for (lock in locks) {
                lock.unlock()
            }
        }
        return chunk
    }

    companion object {
        val INSTANCE = PopulationTask()
    }
}