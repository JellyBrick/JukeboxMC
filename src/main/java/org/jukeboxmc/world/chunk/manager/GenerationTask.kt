package org.jukeboxmc.world.chunk.manager

import java.util.function.Function
import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.ChunkState

/**
 * @author LucGamesYT
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GenerationTask : Function<Chunk, Chunk> {
    override fun apply(chunk: Chunk): Chunk {
        if (chunk.isGenerated) {
            return chunk
        }
        val writeLock = chunk.writeLock
        writeLock.lock()
        try {
            chunk.world.getGenerator(chunk.dimension).generate(chunk, chunk.x, chunk.z)
            chunk.chunkState = ChunkState.GENERATED
            chunk.isDirty = true
        } finally {
            writeLock.unlock()
        }
        return chunk
    }

    companion object {
        val INSTANCE = GenerationTask()
    }
}