package org.jukeboxmc.block.palette.bitarray

import org.jukeboxmc.util.Utils

class Pow2BitArray internal constructor(
    override val version: BitArrayVersion,
    override val size: Int,
    override val words: IntArray,
) : BitArray {
    init {
        val expectedWordsLength = Utils.ceil(size.toFloat() / version.entriesPerWord)
        require(words.size == expectedWordsLength) {
            "Invalid length given for storage, got: " + words.size +
                " but expected: " + expectedWordsLength
        }
    }

    override fun set(index: Int, value: Int) {
        val bitIndex = index * version.bits
        val arrayIndex = bitIndex shr 5
        val offset = bitIndex and 31
        words[arrayIndex] =
            words[arrayIndex] and (version.maxEntryValue shl offset).inv() or (value and version.maxEntryValue shl offset)
    }

    override fun get(index: Int): Int {
        val bitIndex = index * version.bits
        val arrayIndex = bitIndex shr 5
        val wordOffset = bitIndex and 31
        return words[arrayIndex] ushr wordOffset and version.maxEntryValue
    }

    override fun copy(): BitArray {
        return Pow2BitArray(version, size, words.copyOf(words.size))
    }
}
