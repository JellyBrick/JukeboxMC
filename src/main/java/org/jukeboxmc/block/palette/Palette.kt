package org.jukeboxmc.block.palette

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtUtils
import com.nukkitx.network.VarInts
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import it.unimi.dsi.fastutil.objects.ReferenceArrayList
import org.jukeboxmc.block.palette.bitarray.BitArray
import org.jukeboxmc.block.palette.bitarray.BitArrayVersion
import java.util.Objects

class Palette<V> @JvmOverloads constructor(first: V, version: BitArrayVersion = BitArrayVersion.V2) {
    private val palette: MutableList<V>
    private var bitArray: BitArray

    init {
        bitArray = version.createArray(SIZE)
        palette = ReferenceArrayList(16)
        palette.add(first)
    }

    operator fun get(index: Int): V {
        return palette[bitArray[index]]
    }

    operator fun set(index: Int, value: V) {
        val paletteIndex = paletteIndexFor(value)
        bitArray[index] = paletteIndex
    }

    fun writeToNetwork(byteBuf: ByteBuf, serializer: RuntimeDataSerializer<V>) {
        byteBuf.writeByte(getPaletteHeader(bitArray.version, true))
        for (word in bitArray.words) byteBuf.writeIntLE(word)
        bitArray.writeSizeToNetwork(byteBuf, palette.size)
        for (value in palette) VarInts.writeInt(byteBuf, serializer.serialize(value))
    }

    fun writeToNetwork(byteBuf: ByteBuf, serializer: RuntimeDataSerializer<V>, last: Palette<V>?) {
        if (last != null && last.palette == palette) {
            byteBuf.writeByte(copyLastFlagHeader)
            return
        }
        if (isEmpty) {
            byteBuf.writeByte(getPaletteHeader(BitArrayVersion.V0, true))
            byteBuf.writeIntLE(serializer.serialize(palette[0]))
            return
        }
        byteBuf.writeByte(getPaletteHeader(bitArray.version, true))
        for (word in bitArray.words) byteBuf.writeIntLE(word)
        bitArray.writeSizeToNetwork(byteBuf, palette.size)
        for (value in palette) VarInts.writeInt(byteBuf, serializer.serialize(value))
    }

    fun readFromNetwork(byteBuf: ByteBuf, deserializer: RuntimeDataDeserializer<V>) {
        val header = byteBuf.readUnsignedByte()
        val version = Objects.requireNonNull(getVersionFromHeader(header))
        val wordCount = version!!.getWordsForSize(SIZE)
        val words = IntArray(wordCount)
        for (i in 0 until wordCount) words[i] = byteBuf.readIntLE()
        bitArray = version.createArray(SIZE, words)
        palette.clear()
        val size = bitArray.readSizeFromNetwork(byteBuf)
        for (i in 0 until size) palette.add(deserializer.deserialize(VarInts.readInt(byteBuf)))
    }

    fun writeToStoragePersistent(byteBuf: ByteBuf, serializer: PersistentDataSerializer<V>) {
        byteBuf.writeByte(getPaletteHeader(bitArray.version, false))
        for (word in bitArray.words) byteBuf.writeIntLE(word)
        byteBuf.writeIntLE(palette.size)
        ByteBufOutputStream(byteBuf).use { bufOutputStream ->
            NbtUtils.createWriterLE(bufOutputStream)
                .use { outputStream -> for (value in palette) outputStream.writeTag(serializer.serialize(value)) }
        }
    }

    fun writeToStorageRuntime(byteBuf: ByteBuf, serializer: RuntimeDataSerializer<V>, last: Palette<V>?) {
        if (last != null && last.palette == palette) {
            byteBuf.writeByte(copyLastFlagHeader)
            return
        }
        if (isEmpty) {
            byteBuf.writeByte(getPaletteHeader(BitArrayVersion.V0, true))
            byteBuf.writeIntLE(serializer.serialize(palette[0]))
            return
        }
        byteBuf.writeByte(getPaletteHeader(bitArray.version, true))
        for (word in bitArray.words) byteBuf.writeIntLE(word)
        byteBuf.writeIntLE(palette.size)
        for (value in palette) byteBuf.writeIntLE(serializer.serialize(value))
    }

    fun readFromStoragePersistent(byteBuf: ByteBuf, deserializer: PersistentDataDeserializer<V>) {
        val header = byteBuf.readUnsignedByte()
        val version = Objects.requireNonNull(getVersionFromHeader(header))
        val wordCount = version!!.getWordsForSize(SIZE)
        val words = IntArray(wordCount)
        for (i in 0 until wordCount) words[i] = byteBuf.readIntLE()
        bitArray = version.createArray(SIZE, words)
        palette.clear()
        val paletteSize = byteBuf.readIntLE()
        ByteBufInputStream(byteBuf).use { bufInputStream ->
            NbtUtils.createReaderLE(bufInputStream).use { inputStream ->
                for (i in 0 until paletteSize) palette.add(
                    deserializer.deserialize(inputStream.readTag() as NbtMap),
                )
            }
        }
    }

    fun readFromStorageRuntime(byteBuf: ByteBuf, deserializer: RuntimeDataDeserializer<V>, last: Palette<V>?) {
        val header = byteBuf.readUnsignedByte()
        if (hasCopyLastFlag(header)) {
            last!!.copyTo(this)
            return
        }
        val version = Objects.requireNonNull(getVersionFromHeader(header))
        if (version == BitArrayVersion.V0) {
            bitArray = version.createArray(SIZE, null)
            palette.clear()
            palette.add(deserializer.deserialize(byteBuf.readIntLE()))
            onResize(BitArrayVersion.V2)
            return
        }
        val wordCount = version!!.getWordsForSize(SIZE)
        val words = IntArray(wordCount)
        for (i in 0 until wordCount) words[i] = byteBuf.readIntLE()
        bitArray = version.createArray(SIZE, words)
        palette.clear()
        val paletteSize = byteBuf.readIntLE()
        for (i in 0 until paletteSize) palette.add(deserializer.deserialize(byteBuf.readIntLE()))
    }

    private fun onResize(version: BitArrayVersion) {
        val newBitArray = version.createArray(SIZE)
        for (i in 0 until SIZE) newBitArray[i] = bitArray[i]
        bitArray = newBitArray
    }

    fun paletteIndexFor(value: V): Int {
        var index = palette.indexOf(value)
        if (index != -1) return index
        index = palette.size
        palette.add(value)
        val version = bitArray.version
        if (index > version.maxEntryValue) {
            val next = version.next
            if (next != null) onResize(next)
        }
        return index
    }

    val isEmpty: Boolean
        get() {
            if (palette.size == 1) return true
            for (word in bitArray.words) if (Integer.toUnsignedLong(word) != 0L) return false
            return true
        }

    fun copyTo(palette: Palette<V>) {
        palette.bitArray = bitArray.copy()
        palette.palette.clear()
        palette.palette.addAll(this.palette)
    }

    companion object {
        const val SIZE = 4096
        private fun getPaletteHeader(version: BitArrayVersion, runtime: Boolean): Int {
            return version.bits.toInt() shl 1 or if (runtime) 1 else 0
        }

        private fun getVersionFromHeader(header: Short): BitArrayVersion? {
            return BitArrayVersion[header.toInt() shr 1, true]
        }

        private fun hasCopyLastFlag(header: Short): Boolean {
            return header.toInt() shr 1 == 0x7F
        }

        private val copyLastFlagHeader: Int
            private get() = 0x7F shl 1 or 1

        private fun isPersistent(header: Short): Boolean {
            return header.toInt() and 1 == 0
        }
    }
}
