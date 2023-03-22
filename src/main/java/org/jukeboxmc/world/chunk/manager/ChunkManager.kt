package org.jukeboxmc.world.chunk.manager

import com.google.common.collect.ImmutableSet
import com.spotify.futures.CompletableFutures
import it.unimi.dsi.fastutil.longs.Long2LongMap
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import org.jukeboxmc.Server
import org.jukeboxmc.event.world.ChunkLoadEvent
import org.jukeboxmc.event.world.ChunkUnloadEvent
import org.jukeboxmc.util.Utils
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import java.util.Objects
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.abs

@ParametersAreNonnullByDefault
class ChunkManager(private val world: World, val dimension: Dimension) {
    private val chunks: Long2ObjectMap<LoadingChunk> = Long2ObjectOpenHashMap()
    private val chunkLoadedTimes: Long2LongMap = Long2LongOpenHashMap()
    private val chunkLastAccessTimes: Long2LongMap = Long2LongOpenHashMap()
    private val executor: Executor

    val log = Server.instance.logger

    init {
        executor = world.server.scheduler.chunkExecutor
    }

    @get:Synchronized
    val loadedChunks: Set<Chunk>
        get() {
            val chunks = ImmutableSet.builder<Chunk>()
            for (loadingChunk in this.chunks.values) {
                val chunk = loadingChunk.getChunk()
                if (chunk != null) {
                    chunks.add(chunk)
                }
            }
            return chunks.build()
        }

    @get:Synchronized
    val loadedCount: Int
        get() = chunks.size

    @Synchronized
    fun getLoadedChunk(key: Long): Chunk? {
        val chunk = chunks[key]
        return chunk.getChunk()
    }

    @Synchronized
    fun getLoadedChunk(x: Int, z: Int): Chunk? {
        return getLoadedChunk(Utils.toLong(x, z))
    }

    fun getChunk(x: Int, z: Int): Chunk? {
        var chunk = this.getLoadedChunk(x, z)
        if (chunk == null) {
            chunk = this.getChunkFuture(x, z).join()
        }
        return chunk
    }

    fun getChunkFuture(x: Int, z: Int): CompletableFuture<Chunk> {
        return this.getChunkFuture(x, z, generate = true, populate = true, finish = true)
    }

    @Synchronized
    private fun getChunkFuture(
        chunkX: Int,
        chunkZ: Int,
        generate: Boolean,
        populate: Boolean,
        finish: Boolean,
    ): CompletableFuture<Chunk> {
        val chunkKey = Utils.toLong(chunkX, chunkZ)
        chunkLastAccessTimes.put(chunkKey, System.currentTimeMillis())
        val chunk = chunks.computeIfAbsent(
            chunkKey,
            Long2ObjectFunction { key: Long ->
                LoadingChunk(
                    dimension,
                    key,
                    true,
                )
            },
        )
        if (finish) {
            chunk.finish()
        } else if (populate) {
            chunk.populate()
        } else if (generate) {
            chunk.generate()
        }
        Server.instance.pluginManager.callEvent(ChunkLoadEvent(world, chunk.getChunk()))
        return chunk.future
    }

    @Synchronized
    fun isChunkLoaded(hash: Long): Boolean {
        val chunk = chunks[hash]
        return chunk?.getChunk() != null
    }

    @Synchronized
    fun isChunkLoaded(x: Int, z: Int): Boolean {
        return this.isChunkLoaded(Utils.toLong(x, z))
    }

    @Synchronized
    fun unloadChunk(hash: Long): Boolean {
        return this.unloadChunk(hash, true, true)
    }

    @JvmOverloads
    fun unloadChunk(chunk: Chunk, save: Boolean = true, safe: Boolean = true): Boolean {
        return this.unloadChunk(Utils.toLong(chunk.x, chunk.z), save, safe)
    }

    fun unloadChunk(chunkKey: Long, save: Boolean, safe: Boolean): Boolean {
        val loadedChunk = this.getLoadedChunk(chunkKey) ?: return false
        val chunkUnloadEvent = ChunkUnloadEvent(world, loadedChunk, save)
        Server.instance.pluginManager.callEvent(chunkUnloadEvent)
        if (chunkUnloadEvent.isCancelled) return false
        val result = unloadChunk0(chunkKey, chunkUnloadEvent.isSaveChunk, safe)
        if (result) {
            chunks.remove(chunkKey)
        }
        return result
    }

    @Synchronized
    private fun unloadChunk0(chunkKey: Long, save: Boolean, safe: Boolean): Boolean {
        val loadingChunk = chunks[chunkKey] ?: return false
        val chunk = loadingChunk.getChunk() ?: return false
        if (chunk.getLoaders().isNotEmpty()) {
            return false
        }
        if (save) {
            saveChunk(chunk)
        }
        if (safe && world.getChunkPlayers(chunk.x, chunk.z, chunk.dimension).isNotEmpty()) {
            return false
        }
        chunkLastAccessTimes.remove(chunkKey)
        chunkLoadedTimes.remove(chunkKey)
        return true
    }

    @Synchronized
    fun saveChunks(): CompletableFuture<Void> {
        val futures: MutableList<CompletableFuture<*>> = ArrayList()
        for (loadingChunk in chunks.values) {
            val chunk = loadingChunk.getChunk()
            if (chunk != null) {
                futures.add(saveChunk(chunk))
            }
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    fun saveChunk(chunk: Chunk): CompletableFuture<Void?> {
        return if (chunk.isDirty) {
            world.saveChunk(chunk).exceptionally { throwable: Throwable? ->
                log.error("Unable to save chunk", throwable)
                null
            }
        } else {
            COMPLETED_VOID_FUTURE
        }
    }

    @Synchronized
    fun tick() {
        if (chunks.isEmpty()) {
            return
        }
        val time = System.currentTimeMillis()

        // Spawn chunk
        val spawnX = world.spawnLocation.chunkX
        val spawnZ = world.spawnLocation.chunkZ
        val spawnRadius: Int = Server.instance.viewDistance

        // Do chunk garbage collection
        val iterator = chunks.long2ObjectEntrySet().iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val chunkKey = entry.longKey
            val loadingChunk = entry.value
            val chunk = loadingChunk.getChunk()
                ?: continue // Chunk hasn't loaded
            if (abs(chunk.x - spawnX) <= spawnRadius && abs(chunk.z - spawnZ) <= spawnRadius ||
                chunk.getLoaders().isNotEmpty()
            ) {
                continue // Spawn protection or is loaded
            }
            val loadedTime = chunkLoadedTimes[chunkKey]
            if (time - loadedTime <= TimeUnit.SECONDS.toMillis(30)) {
                continue
            }
            val lastAccessTime = chunkLastAccessTimes[chunkKey]
            if (time - lastAccessTime <= TimeUnit.SECONDS.toMillis(120)) {
                continue
            }
            val chunkUnloadEvent = ChunkUnloadEvent(world, chunk, true)
            Server.instance.pluginManager.callEvent(chunkUnloadEvent)
            if (chunkUnloadEvent.isCancelled) {
                return
            }
            if (unloadChunk0(chunkKey, chunkUnloadEvent.isSaveChunk, true)) {
                iterator.remove()
            }
        }
    }

    private inner class LoadingChunk(dimension: Dimension, key: Long, load: Boolean) {
        private val x: Int
        private val z: Int
        var future: CompletableFuture<Chunk>
            private set

        @JvmField
        @Volatile
        var generationRunning = 0

        @JvmField
        @Volatile
        var populationRunning = 0

        @JvmField
        @Volatile
        var finishRunning = 0
        private var chunk: Chunk? = null

        init {
            x = Utils.fromHashX(key)
            z = Utils.fromHashZ(key)
            if (load) {
                future = world.readChunk(Chunk(world, dimension, x, z))
                    .thenApply { chunk: Chunk? ->
                        Objects.requireNonNullElseGet(chunk) {
                            Chunk(
                                world,
                                dimension,
                                x,
                                z,
                            )
                        }
                    }
                future.whenComplete { chunk: Chunk?, throwable: Throwable? ->
                    if (throwable != null) {
                        log.error("Unable to load chunk $x:$z", throwable)
                        synchronized(this@ChunkManager) { chunks.remove(key) }
                    } else {
                        val currentTime = System.currentTimeMillis()
                        synchronized(this@ChunkManager) { chunkLoadedTimes.put(key, currentTime) }
                    }
                }
            } else {
                future = CompletableFuture.completedFuture(Chunk(world, dimension, x, z))
            }
            future.whenComplete { chunk: Chunk?, throwable: Throwable? -> this.chunk = chunk }
        }

        fun getChunk(): Chunk? {
            return if (chunk != null && chunk!!.isGenerated && chunk!!.isPopulated && chunk!!.isFinished) {
                chunk
            } else {
                null
            }
        }

        fun generate() {
            if ((chunk == null || !chunk!!.isGenerated) && GENERATION_RUNNING_UPDATER.compareAndSet(this, 0, 1)) {
                future = future.thenApplyAsync(GenerationTask.INSTANCE, executor)
                future.thenRun { GENERATION_RUNNING_UPDATER.compareAndSet(this, 1, 0) }
            }
        }

        fun populate() {
            generate()
            if ((chunk == null || !chunk!!.isPopulated) && POPULATION_RUNNING_UPDATER.compareAndSet(this, 0, 1)) {
                // Load and generate chunks around the chunk to be populated.
                val chunksToLoad: MutableList<CompletableFuture<Chunk>?> = ArrayList(8)
                run {
                    var z = this.z - 1
                    val maxZ = this.z + 1
                    while (z <= maxZ) {
                        run {
                            var x = this.x - 1
                            val maxX = this.x + 1
                            while (x <= maxX) {
                                if (x == this.x && z == this.z) {
                                    x++
                                    continue
                                }
                                chunksToLoad.add(this@ChunkManager.getChunkFuture(x, z, true, false, false))
                                x++
                            }
                        }
                        z++
                    }
                }
                val aroundFuture = CompletableFutures.allAsList(chunksToLoad)
                future = future.thenCombineAsync(aroundFuture, PopulationTask.INSTANCE, executor)
                future.thenRun { POPULATION_RUNNING_UPDATER.compareAndSet(this, 1, 0) }
            }
        }

        fun finish() {
            populate()
            if ((chunk == null || !chunk!!.isFinished) && FINISH_RUNNING_UPDATER.compareAndSet(this, 0, 1)) {
                val chunksToLoad: MutableList<CompletableFuture<Chunk>?> = ArrayList(8)
                run {
                    var z = this.z - 1
                    val maxZ = this.z + 1
                    while (z <= maxZ) {
                        run {
                            var x = this.x - 1
                            val maxX = this.x + 1
                            while (x <= maxX) {
                                if (x == this.x && z == this.z) {
                                    x++
                                    continue
                                }
                                chunksToLoad.add(this@ChunkManager.getChunkFuture(x, z, true, true, false))
                                x++
                            }
                        }
                        z++
                    }
                }
                val aroundFuture = CompletableFutures.allAsList(chunksToLoad)
                future = future.thenCombineAsync(aroundFuture, FinishingTask.INSTANCE, executor)
                future.thenRun { FINISH_RUNNING_UPDATER.compareAndSet(this, 1, 0) }
            }
        }

        private fun clear() {
            future = future.thenApply { chunk: Chunk? ->
                GENERATION_RUNNING_UPDATER[this] = 0
                POPULATION_RUNNING_UPDATER[this] = 0
                FINISH_RUNNING_UPDATER[this] = 0
                chunk
            }
        }
    }

    companion object {
        private val COMPLETED_VOID_FUTURE = CompletableFuture.completedFuture<Void?>(null)
        private val GENERATION_RUNNING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            LoadingChunk::class.java,
            "generationRunning",
        )
        private val POPULATION_RUNNING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            LoadingChunk::class.java,
            "populationRunning",
        )
        private val FINISH_RUNNING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            LoadingChunk::class.java,
            "finishRunning",
        )
    }
}
