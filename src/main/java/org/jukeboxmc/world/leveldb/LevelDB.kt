package org.jukeboxmc.world.leveldb

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtUtils
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import net.daporkchop.ldbjni.LevelDB
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.palette.Palette
import org.jukeboxmc.block.palette.PersistentDataDeserializer
import org.jukeboxmc.block.palette.RuntimeDataDeserializer
import org.jukeboxmc.block.palette.RuntimeDataSerializer
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityRegistry
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.util.Utils
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.ChunkState
import org.jukeboxmc.world.chunk.SubChunk
import java.io.File
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LevelDB(private val world: World) : AutoCloseable {
    private var db: DB

    init {
        val options = Options()
            .createIfMissing(true)
            .compressionType(CompressionType.ZLIB_RAW)
            .blockSize(64 * 1024)
        db = LevelDB.PROVIDER.open(File(world.worldFolder, "db"), options)
    }

    fun readChunk(chunk: Chunk): CompletableFuture<Chunk> {
        return CompletableFuture.supplyAsync({
            var version = db[Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x2C.toByte())]
            if (version == null) {
                version = db[Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x76.toByte())]
            }
            if (version == null) {
                return@supplyAsync null
            }
            val finalized = db[Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x36.toByte())]
            if (finalized == null) {
                chunk.chunkState = ChunkState.FINISHED
            } else {
                chunk.chunkState =
                    ChunkState.values()[Unpooled.wrappedBuffer(finalized).readIntLE() + 1]
            }
            for (subChunkIndex in chunk.minY shr 4 until (chunk.maxY shr 4)) {
                val chunkData = db[
                    Utils.getSubChunkKey(
                        chunk.x,
                        chunk.z,
                        chunk.dimension,
                        0x2f.toByte(),
                        subChunkIndex.toByte(),
                    ),
                ]
                val arrayIndex = subChunkIndex + (Math.abs(chunk.minY) shr 4)
                if (chunkData != null) {
                    loadSection(chunk.getOrCreateSubChunk(arrayIndex, true)!!, chunkData)
                }
            }
            val heightAndBiomes = db[Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x2b.toByte())]
            if (heightAndBiomes != null) {
                loadHeightAndBiomes(chunk, heightAndBiomes)
            }
            val blockEntities = db[Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x31.toByte())]
            if (blockEntities != null) {
                loadBlockEntities(chunk, blockEntities)
            }
            chunk
        }, Server.instance.scheduler.chunkExecutor)
    }

    private fun loadSection(chunk: SubChunk, chunkData: ByteArray) {
        val buffer = Unpooled.wrappedBuffer(chunkData)
        try {
            val subChunkVersion = buffer.readByte()
            chunk.subChunkVersion = subChunkVersion.toInt()
            var storages = 1
            when (subChunkVersion) {
                9.toByte(), 8.toByte() -> {
                    storages = buffer.readByte().toInt()
                    chunk.layer = storages
                    if (subChunkVersion.toInt() == 9) {
                        val subY = buffer.readByte()
                    }
                    var layer = 0
                    while (layer < storages) {
                        try {
                            buffer.markReaderIndex()
                            chunk.blocks[layer].readFromStoragePersistent(
                                buffer,
                                object : PersistentDataDeserializer<Block> {
                                    override fun deserialize(nbtMap: NbtMap): Block {
                                        val identifier = nbtMap.getString("name")
                                        val states = nbtMap.getCompound("states")
                                        return BlockPalette.getBlock(Identifier.fromString(identifier), states)
                                    }
                                },
                            )
                        } catch (e: IllegalArgumentException) {
                            buffer.resetReaderIndex()
                            chunk.blocks[layer].readFromStorageRuntime(
                                buffer,
                                object : RuntimeDataDeserializer<Block> {
                                    override fun deserialize(id: Int): Block {
                                        return BlockPalette.getBlockByNBT(
                                            BlockPalette.getBlockNbt(id),
                                        )
                                    }
                                },
                                null,
                            )
                        }
                        layer++
                    }
                }

                1.toByte() -> {
                    var layer = 0
                    while (layer < storages) {
                        try {
                            buffer.markReaderIndex()
                            chunk.blocks[layer].readFromStoragePersistent(
                                buffer,
                                object : PersistentDataDeserializer<Block> {
                                    override fun deserialize(nbtMap: NbtMap): Block {
                                        val identifier = nbtMap.getString("name")
                                        val states = nbtMap.getCompound("states")
                                        return BlockPalette.getBlock(Identifier.fromString(identifier), states)
                                    }
                                },
                            )
                        } catch (e: IllegalArgumentException) {
                            buffer.resetReaderIndex()
                            chunk.blocks[layer].readFromStorageRuntime(
                                buffer,
                                object : RuntimeDataDeserializer<Block> {
                                    override fun deserialize(id: Int): Block {
                                        return BlockPalette.getBlockByNBT(
                                            BlockPalette.getBlockNbt(id),
                                        )
                                    }
                                },
                                null,
                            )
                        }
                        layer++
                    }
                }
            }
        } finally {
            buffer.release()
        }
    }

    private fun loadHeightAndBiomes(chunk: Chunk, heightAndBiomes: ByteArray) {
        val buffer = Unpooled.wrappedBuffer(heightAndBiomes)
        try {
            val height = chunk.height
            for (i in height.indices) {
                height[i] = buffer.readShortLE()
            }
            if (buffer.readableBytes() <= 0) return
            var last: Palette<Biome>? = null
            var biomePalette: Palette<Biome>
            for (y in chunk.minY shr 4 until (chunk.maxY + 1 shr 4)) {
                try {
                    biomePalette = chunk.getOrCreateSubChunk(chunk.getSubY(y shl 4)).biomes
                    biomePalette.readFromStorageRuntime(
                        buffer,
                        object : RuntimeDataDeserializer<Biome> {
                            override fun deserialize(id: Int): Biome {
                                return Biome.findById(id)
                            }
                        },
                        last,
                    )
                    last = biomePalette
                } catch (ignored: Exception) {
                }
            }
        } finally {
            buffer.release()
        }
    }

    private fun loadBlockEntities(chunk: Chunk, blockEntityData: ByteArray) {
        val byteBuf = Unpooled.wrappedBuffer(blockEntityData)
        try {
            val reader = NbtUtils.createReaderLE(ByteBufInputStream(byteBuf))
            while (byteBuf.readableBytes() > 0) {
                try {
                    val nbtMap = reader.readTag() as NbtMap
                    val x = nbtMap.getInt("x", 0)
                    val y = nbtMap.getInt("y", 0)
                    val z = nbtMap.getInt("z", 0)
                    val block = chunk.getBlock(x, y, z, 0)
                    val blockEntityType = BlockEntityRegistry.getBlockEntityTypeById(nbtMap.getString("id"))
                    if (blockEntityType != null) {
                        val blockEntity: BlockEntity =
                            BlockEntity.create(blockEntityType, block)
                        blockEntity.fromCompound(nbtMap)
                        chunk.setBlockEntity(x, y, z, blockEntity)
                        blockEntity.isSpawned = true
                    }
                } catch (e: IOException) {
                    break
                }
            }
        } finally {
            byteBuf.release()
        }
    }

    fun saveChunk(chunk: Chunk): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync({
            if (!chunk.isGenerated) {
                return@supplyAsync null
            }
            db.createWriteBatch().use { writeBatch ->
                // Write subChunks
                val minY = chunk.minY shr 4
                for (subY in 0 until chunk.availableSubChunks) {
                    if (chunk.subChunks[subY] == null) {
                        continue
                    }
                    val subChunkIndex = subY + minY
                    chunk.saveChunkSlice(chunk.subChunks[subY]!!.blocks, subChunkIndex, writeBatch)
                }

                // Write chunkVersion
                val chunkVersion = Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x2c.toByte())
                writeBatch.put(chunkVersion, byteArrayOf(Chunk.CHUNK_VERSION.toByte()))

                // Write blockEntities
                val blockEntitiesKey = Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x31.toByte())
                val buffer = Unpooled.buffer()
                val blockEntities = chunk.blockEntities
                if (!blockEntities.isEmpty()) {
                    NbtUtils.createWriterLE(ByteBufOutputStream(buffer)).use { networkWriter ->
                        blockEntities.forEach { (_, blockEntity) ->
                            val build = blockEntity.toCompound().build()
                            networkWriter.writeTag(build)
                        }
                    }
                    if (buffer.readableBytes() > 0) {
                        writeBatch.put(blockEntitiesKey, Utils.array(buffer))
                    }
                    buffer.release()
                }

                // Write biomeAndHeight
                val biomeAndHeight = Utils.getKey(chunk.x, chunk.z, chunk.dimension, 0x2b.toByte())
                val heightAndBiomesBuffer = Unpooled.buffer()
                for (height in chunk.height) {
                    heightAndBiomesBuffer.writeShortLE(height.toInt())
                }
                var last: Palette<Biome>? = null
                var biomePalette: Palette<Biome>
                for (y in chunk.minY shr 4 until (chunk.maxY + 1 shr 4)) {
                    biomePalette = chunk.getOrCreateSubChunk(chunk.getSubY(y shl 4)).biomes
                    biomePalette.writeToStorageRuntime(
                        heightAndBiomesBuffer,
                        object : RuntimeDataSerializer<Biome> {
                            override fun serialize(value: Biome): Int {
                                return value.id
                            }
                        },
                        last,
                    )
                    last = biomePalette
                }
                writeBatch.put(biomeAndHeight, Utils.array(heightAndBiomesBuffer))
                db.write(writeBatch)
                return@supplyAsync null
            }
        }, Server.instance.scheduler.chunkExecutor)
    }

    override fun close() {
        db.close()
    }
}
