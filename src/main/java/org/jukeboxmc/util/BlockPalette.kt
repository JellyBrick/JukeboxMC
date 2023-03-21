package org.jukeboxmc.util

import com.nukkitx.nbt.NBTInputStream
import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtType
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.jukeboxmc.Bootstrap
import org.jukeboxmc.block.Block
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Collections
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate
import java.util.zip.GZIPInputStream

/**
 * @author LucGamesYT
 * @version 1.0
 */
object BlockPalette {
    val STATE_FROM_RUNTIME: Int2ObjectMap<NbtMap> = Int2ObjectLinkedOpenHashMap()
    val BLOCK_STATE_FROM_RUNTIME: Int2ObjectMap<NbtMap> = Int2ObjectLinkedOpenHashMap()
    val BLOCK_CACHE: Object2ObjectMap<BlockData, Block> = Object2ObjectOpenHashMap()
    val IDENTIFIER_TO_RUNTIME: Object2ObjectMap<Identifier, Int> = Object2ObjectOpenHashMap()
    val BLOCK_NBT: MutableList<NbtMap> = LinkedList()

    @JvmStatic
    fun init() {
        val RUNTIME_COUNTER = AtomicInteger(0)
        val resourceAsStream: InputStream? =
            Bootstrap::class.java.classLoader.getResourceAsStream("block_palette.nbt")
        if (resourceAsStream != null) {
            try {
                NBTInputStream(DataInputStream(GZIPInputStream(resourceAsStream))).use { nbtReader ->
                    val nbtMap = nbtReader.readTag() as NbtMap
                    BLOCK_NBT.addAll(nbtMap.getList("blocks", NbtType.COMPOUND))
                    for (blockMap in nbtMap.getList("blocks", NbtType.COMPOUND)) {
                        val runtimeId = RUNTIME_COUNTER.getAndIncrement()
                        BLOCK_STATE_FROM_RUNTIME.put(runtimeId, blockMap.getCompound("states"))
                        STATE_FROM_RUNTIME.put(runtimeId, blockMap)
                        IDENTIFIER_TO_RUNTIME.putIfAbsent(
                            Identifier.fromString(blockMap.getString("name")),
                            runtimeId,
                        )
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    @JvmStatic
    fun getRuntimeByIdentifier(identifier: Identifier): Int {
        return IDENTIFIER_TO_RUNTIME[identifier]!!
    }

    @JvmStatic
    fun getBlockNbt(runtimeId: Int): NbtMap {
        return BLOCK_NBT[runtimeId]
    }

    @JvmStatic
    fun getBlockByNBT(nbtMap: NbtMap): Block {
        val identifier: Identifier = Identifier.fromString(nbtMap.getString("name"))
        val blockStates = nbtMap.getCompound("states")
        val blockData = BlockData(identifier, blockStates)
        if (BLOCK_CACHE.containsKey(blockData)) {
            return BLOCK_CACHE[blockData]!!.clone()
        }
        val block: Block = Block.create<Block>(identifier, blockStates)
        BLOCK_CACHE[blockData] = block
        return block
    }

    @JvmStatic
    fun getBlock(identifier: Identifier, blockStates: NbtMap): Block {
        val blockData = BlockData(identifier, blockStates)
        return if (BLOCK_CACHE.containsKey(blockData)) {
            BLOCK_CACHE[blockData]!!.clone()
        } else {
            try {
                val block: Block = Block.create(identifier, blockStates)
                BLOCK_CACHE[blockData] = block
                block
            } catch (e: Exception) {
                val block: Block = Block.create(identifier)
                BLOCK_CACHE[blockData] = block
                block
            }
        }
    }

    @JvmStatic
    fun getRuntimeId(blockMap: NbtMap?): Int {
        for (runtimeId in STATE_FROM_RUNTIME.keys) {
            if (STATE_FROM_RUNTIME[runtimeId] == blockMap) {
                return runtimeId
            }
        }
        throw NullPointerException("Block was not found")
    }

    @JvmStatic
    fun getBlockRuntimeId(blockMap: NbtMap): Int {
        for (runtimeId in BLOCK_STATE_FROM_RUNTIME.keys) {
            if (BLOCK_STATE_FROM_RUNTIME[runtimeId] == blockMap) {
                return runtimeId
            }
        }
        throw NullPointerException("Block was not found: $blockMap")
    }

    @JvmStatic
    fun searchBlocks(predicate: Predicate<NbtMap>): List<NbtMap> {
        val blocks: MutableList<NbtMap> = ArrayList()
        for (nbtMap in STATE_FROM_RUNTIME.values) {
            if (predicate.test(nbtMap)) {
                blocks.add(nbtMap)
            }
        }
        return Collections.unmodifiableList(blocks)
    }

    class BlockData(
        val identifier: Identifier,
        val states: NbtMap,
    )
}
