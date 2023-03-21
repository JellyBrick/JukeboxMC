package org.jukeboxmc.player.manager

import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets
import java.util.function.LongConsumer
import org.jukeboxmc.Server
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Utils
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.ChunkComparator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerChunkManager(private val player: Player) {
    private val comparator: ChunkComparator
    private val loadedChunks: LongSet = LongOpenHashSet()
    private val sendQueue: Long2ObjectMap<LevelChunkPacket?> = Long2ObjectOpenHashMap()
    private val chunksSentCounter: AtomicLong = AtomicLong()
    private val removeChunkLoader: LongConsumer

    @Volatile
    private var radius = 0

    init {
        comparator = ChunkComparator(player)
        removeChunkLoader = LongConsumer { chunkKey: Long ->
            val chunk = player.world.getLoadedChunk(chunkKey, player.dimension)
            chunk?.removeLoader(player)
        }
    }

    @Synchronized
    fun sendQueued() {
        var chunksPerTick = 4 //this.player.getServer().getConfig("chunk-sending.per-tick", 4);
        val sendQueueIterator = sendQueue.long2ObjectEntrySet().iterator()
        // Remove chunks which are out of range
        while (sendQueueIterator.hasNext()) {
            val entry = sendQueueIterator.next()
            val key = entry.longKey
            if (!loadedChunks.contains(key)) {
                sendQueueIterator.remove()
                val chunk = player.world.getLoadedChunk(key, player.dimension)
                chunk?.removeLoader(player)
            }
        }
        val list: LongList = LongArrayList(sendQueue.keys)
        list.unstableSort(comparator)
        for (key in list.toLongArray()) {
            if (chunksPerTick < 0) {
                break
            }
            val packet = sendQueue[key]
                ?: // Next packet is not available.
                break
            sendQueue.remove(key)
            player.playerConnection.sendPacket(packet)
            val chunk = player.world.getLoadedChunk(key, player.dimension)
            if (chunk != null) {
                for (entity in chunk.entities) {
                    if (entity !is Player && !entity!!.isClosed) {
                        entity!!.spawn(player)
                    }
                }
            }
            chunksPerTick--
            chunksSentCounter.incrementAndGet()
        }
    }

    @JvmOverloads
    fun queueNewChunks(pos: Vector = player.location) {
        this.queueNewChunks(pos.blockX shr 4, pos.blockZ shr 4)
    }

    @Synchronized
    fun queueNewChunks(chunkX: Int, chunkZ: Int) {
        val radius = chunkRadius
        val radiusSqr = radius * radius
        val chunksForRadius: LongSet = LongOpenHashSet()
        val sentCopy: LongSet = LongOpenHashSet(loadedChunks)
        val chunksToLoad: LongList = LongArrayList()
        for (x in -radius..radius) {
            for (z in -radius..radius) {
                // Chunk radius is circular so we need to remove the corners.
                if (x * x + z * z > radiusSqr) {
                    continue
                }
                val cx = chunkX + x
                val cz = chunkZ + z
                val key = Utils.toLong(cx, cz)
                chunksForRadius.add(key)
                if (loadedChunks.add(key)) {
                    chunksToLoad.add(key)
                }
            }
        }
        val loadedChunksChanged = loadedChunks.retainAll(chunksForRadius)
        if (loadedChunksChanged || !chunksToLoad.isEmpty()) {
            val packet = NetworkChunkPublisherUpdatePacket()
            packet.setPosition(player.location.toVector3i())
            packet.setRadius(this.radius)
            player.playerConnection.sendPacket(packet)
        }

        // Order chunks for smoother loading
        chunksToLoad.sort(comparator)
        for (key in chunksToLoad.toLongArray()) {
            val cx = Utils.fromHashX(key)
            val cz = Utils.fromHashZ(key)
            if (sendQueue.putIfAbsent(key, null) == null) {
                player.world.getChunkFuture(cx, cz, player.dimension).thenApply { chunk: Chunk ->
                    chunk.addLoader(player)
                    chunk
                }.thenApplyAsync(
                    { obj: Chunk -> obj.createLevelChunkPacket() },
                    Server.Companion.getInstance().getScheduler().getChunkExecutor()
                )
                    .whenComplete { packet: LevelChunkPacket?, throwable: Throwable? ->
                        synchronized(this@PlayerChunkManager) {
                            if (throwable != null) {
                                if (sendQueue.remove(key, null)) {
                                    loadedChunks.remove(key)
                                }
                            } else if (!sendQueue.replace(key, null, packet)) {
                                // The chunk was already loaded!?
                                if (sendQueue.containsKey(key)) {
                                    Server.Companion.getInstance().getLogger().debug(
                                        "Chunk (" + cx + "," + cz + ") already loaded for "
                                                + player.name + ", values " + sendQueue[key]
                                    )
                                }
                                //                                    packet.release();
                            }
                        }
                    }
            }
        }
        sentCopy.removeAll(chunksForRadius)
        // Remove player from chunk loaders
        sentCopy.forEach(removeChunkLoader)
    }

    fun getRadius(): Int {
        return radius
    }

    fun setRadius(radius: Int) {
        if (this.radius != radius) {
            this.radius = radius
            val chunkRadiusUpdatePacket = ChunkRadiusUpdatedPacket()
            chunkRadiusUpdatePacket.setRadius(radius shr 4)
            player.playerConnection.sendPacket(chunkRadiusUpdatePacket)
            this.queueNewChunks()
        }
    }

    var chunkRadius: Int
        get() = radius shr 4
        set(chunkRadius) {
            var chunkRadius = chunkRadius
            chunkRadius = Utils.clamp(chunkRadius, 8, player.server.viewDistance)
            setRadius(chunkRadius shl 4)
        }

    fun isChunkInView(x: Int, z: Int): Boolean {
        return this.isChunkInView(Utils.toLong(x, z))
    }

    @Synchronized
    fun isChunkInView(key: Long): Boolean {
        return loadedChunks.contains(key)
    }

    val chunksSent: Long
        get() = chunksSentCounter.get()

    fun getLoadedChunks(): LongSet {
        return LongSets.unmodifiable(loadedChunks)
    }

    @Synchronized
    fun resendChunk(chunkX: Int, chunkZ: Int) {
        val chunkKey = Utils.toLong(chunkX, chunkZ)
        loadedChunks.remove(chunkKey)
        removeChunkLoader.accept(chunkKey)
    }

    fun prepareRegion(pos: Vector) {
        this.prepareRegion(pos.chunkX, pos.chunkZ)
    }

    fun prepareRegion(chunkX: Int, chunkZ: Int) {
        this.clear()
        this.queueNewChunks(chunkX, chunkZ)
    }

    @Synchronized
    fun clear() {
        sendQueue.clear()
        loadedChunks.forEach(removeChunkLoader)
        loadedChunks.clear()
    }
}