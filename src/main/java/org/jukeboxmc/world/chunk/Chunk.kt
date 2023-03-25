package org.jukeboxmc.world.chunk

import com.google.common.collect.ImmutableSet
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket
import org.iq80.leveldb.WriteBatch
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.palette.Palette
import org.jukeboxmc.block.palette.PersistentDataSerializer
import org.jukeboxmc.block.palette.RuntimeDataSerializer
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.NonStream
import org.jukeboxmc.util.Utils
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import java.util.Collections
import java.util.IdentityHashMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.math.abs

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Chunk(val world: World, val dimension: Dimension, val x: Int, val z: Int) {
    val minY: Int = when (dimension) {
        Dimension.OVERWORLD -> -64
        Dimension.NETHER, Dimension.THE_END -> 0
    }
    val maxY: Int = when (dimension) {
        Dimension.OVERWORLD -> 319
        Dimension.NETHER -> 127
        Dimension.THE_END -> 255
    }
    val fullHeight: Int = abs(minY) + maxY + 1
    private val entities: MutableSet<Entity>
    val players: MutableSet<Player>
    val blockEntities: Int2ObjectMap<BlockEntity>
    val subChunks: Array<SubChunk?>
    val height: ShortArray
    val writeLock: Lock
    private val readLock: Lock
    private val loaders = Collections.newSetFromMap(IdentityHashMap<ChunkLoader, Boolean>())
    var isDirty = false
    var chunkState: ChunkState

    init {
        entities = CopyOnWriteArraySet()
        players = CopyOnWriteArraySet()
        blockEntities = Int2ObjectOpenHashMap()
        subChunks = arrayOfNulls(fullHeight shr 4)
        height = ShortArray(16 * 16)
        chunkState = ChunkState.NEW
        val lock: ReadWriteLock = ReentrantReadWriteLock()
        writeLock = lock.writeLock()
        readLock = lock.readLock()
    }

    val isGenerated: Boolean
        get() = chunkState.ordinal >= 1
    val isPopulated: Boolean
        get() = chunkState.ordinal >= 2
    val isFinished: Boolean
        get() = chunkState.ordinal >= 3

    fun addLoader(chunkLoader: ChunkLoader) {
        loaders.add(chunkLoader)
    }

    fun removeLoader(chunkLoader: ChunkLoader) {
        loaders.remove(chunkLoader)
    }

    fun getLoaders(): Set<ChunkLoader> {
        return ImmutableSet.copyOf(loaders)
    }

    fun getEntities(): Set<Entity> {
        return entities
    }

    fun getPlayers(): Collection<Player> {
        return players
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
        if (entity is Player) {
            players.add(entity)
        }
    }

    fun removeEntity(entity: Entity) {
        entities.removeIf { target: Entity -> target.entityId == entity.entityId }
        if (entity is Player) {
            players.removeIf { target: Player -> target.entityId == entity.entityId }
        }
    }

    fun setBlockEntity(x: Int, y: Int, z: Int, blockEntity: BlockEntity) {
        blockEntities.put(Utils.indexOf(x, y, z), blockEntity)
        isDirty = true
    }

    fun removeBlockEntity(x: Int, y: Int, z: Int) {
        blockEntities.remove(Utils.indexOf(x, y, z))
    }

    fun getBlockEntity(x: Int, y: Int, z: Int): BlockEntity {
        return blockEntities[Utils.indexOf(x, y, z)]
    }

    fun getBlockEntities(): Collection<BlockEntity> {
        return blockEntities.values
    }

    fun setBlock(x: Int, y: Int, z: Int, layer: Int, block: Block) {
        writeLock.lock()
        try {
            if (isHeightOutOfBounds(y)) return
            this.getOrCreateSubChunk(getSubY(y)).setBlock(x, y, z, layer, block)
            isDirty = true
        } finally {
            writeLock.unlock()
        }
    }

    fun setBlock(position: Vector, layer: Int, block: Block) {
        this.setBlock(position.blockX, position.blockY, position.blockZ, layer, block)
    }

    fun getBlock(x: Int, y: Int, z: Int, layer: Int): Block {
        readLock.lock()
        return try {
            if (isHeightOutOfBounds(y)) {
                return BLOCK_AIR
            }
            val subY = getSubY(y)
            if (subChunks[subY] == null) {
                subChunks[subY] = SubChunk(subY)
            }
            val block = subChunks[subY]!!.getBlock(x, y, z, layer)
            block.location = Location(world, x, y, z, dimension)
            block.layer = layer
            block
        } finally {
            readLock.unlock()
        }
    }

    fun getHighestBlockY(x: Int, z: Int): Int {
        var y: Int = maxY
        while (y > minY) {
            val blockType = getBlock(x, y, z, 0).type
            if (blockType != BlockType.AIR) {
                break
            }
            --y
        }
        return ++y
    }

    fun getHighestBlock(x: Int, z: Int): Block? {
        for (y in maxY downTo minY + 1) {
            val block = getBlock(x, y, z, 0)
            val blockType = block.type
            if (blockType != BlockType.AIR) {
                return block
            }
        }
        return null
    }

    fun setBiome(x: Int, y: Int, z: Int, biome: Biome) {
        writeLock.lock()
        try {
            if (isHeightOutOfBounds(y)) return
            this.getOrCreateSubChunk(getSubY(y)).setBiome(x, y, z, biome)
            isDirty = true
        } finally {
            writeLock.unlock()
        }
    }

    fun getBiome(x: Int, y: Int, z: Int): Biome? {
        readLock.lock()
        return try {
            if (isHeightOutOfBounds(y)) null else this.getOrCreateSubChunk(getSubY(y)).getBiome(x, y, z)
        } finally {
            readLock.unlock()
        }
    }

    private fun isHeightOutOfBounds(y: Int): Boolean {
        return y < minY || y > maxY
    }

    fun getOrCreateSubChunk(subY: Int): SubChunk {
        return this.getOrCreateSubChunk(subY, false)
    }

    fun getOrCreateSubChunk(subY: Int, lock: Boolean): SubChunk {
        if (lock) writeLock.lock()
        return try {
            for (y in 0..subY) {
                if (subChunks[y] == null) {
                    subChunks[y] = SubChunk(y + (abs(minY) shr 4))
                }
            }
            subChunks[subY]!!
        } finally {
            if (lock) writeLock.unlock()
        }
    }

    fun getSubY(y: Int): Int {
        return (y shr 4) + (abs(minY) shr 4)
    }

    val availableSubChunks: Int
        get() = NonStream.sum(subChunks) { o: SubChunk? -> if (o == null) 0 else 1 }

    private fun writeTo(byteBuf: ByteBuf) {
        var lastBiomes = Palette(Biome.PLAINS)
        for (subChunk in subChunks) {
            if (subChunk == null) break
            subChunk.writeToNetwork(byteBuf)
        }
        for (subChunk in subChunks) {
            if (subChunk == null) {
                lastBiomes.writeToNetwork(
                    byteBuf,
                    object : RuntimeDataSerializer<Biome> {
                        override fun serialize(value: Biome): Int {
                            return value.id
                        }
                    },
                    lastBiomes,
                )
                continue
            }
            subChunk.biomes.writeToNetwork(
                byteBuf,
                object : RuntimeDataSerializer<Biome> {
                    override fun serialize(value: Biome): Int {
                        return value.id
                    }
                },
            )
            lastBiomes = subChunk.biomes
        }
        byteBuf.writeByte(0) // edu - border blocks
        val blockEntities = getBlockEntities()
        if (blockEntities.isNotEmpty()) {
            NbtUtils.createNetworkWriter(ByteBufOutputStream(byteBuf)).use { writer ->
                for (blockEntity in blockEntities) {
                    val tag = blockEntity.toCompound().build()
                    writer.writeTag(tag)
                }
            }
        }
    }

    fun createLevelChunkPacket(): LevelChunkPacket {
        val byteBuf = Unpooled.buffer()
        val levelChunkPacket = LevelChunkPacket()
        levelChunkPacket.chunkX = x
        levelChunkPacket.chunkZ = z
        levelChunkPacket.isCachingEnabled = false
        levelChunkPacket.isRequestSubChunks = false
        levelChunkPacket.subChunksLength = availableSubChunks
        writeTo(byteBuf)
        levelChunkPacket.data = byteBuf.slice()
        return levelChunkPacket
    }

    fun saveChunkSlice(blockPalettes: Array<Palette<Block>>, subY: Int, writeBatch: WriteBatch) {
        val buffer = Unpooled.buffer()
        buffer.writeByte(SUB_CHUNK_VERSION.toByte().toInt())
        buffer.writeByte(blockPalettes.size.toByte().toInt())
        buffer.writeByte(subY.toByte().toInt())
        blockPalettes.forEach { blockPalette ->
            blockPalette.writeToStoragePersistent(
                buffer,
                object : PersistentDataSerializer<Block> {
                    override fun serialize(value: Block): NbtMap {
                        return BlockPalette.getBlockNbt(value.runtimeId)
                    }
                },
            )
        }
        val subChunkKey = Utils.getSubChunkKey(x, z, dimension, 0x2f.toByte(), subY.toByte())
        writeBatch.put(subChunkKey, Utils.array(buffer))
    }

    override fun toString(): String {
        return "X: $x; Z: $z"
    }

    companion object {
        const val CHUNK_LAYERS = 2
        const val CHUNK_VERSION = 40
        const val SUB_CHUNK_VERSION = 9
        private val BLOCK_AIR: Block = Block.create(BlockType.AIR)
    }
}
