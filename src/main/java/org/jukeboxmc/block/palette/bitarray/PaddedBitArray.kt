package org.jukeboxmc.block.palette.bitarray

import org.jukeboxmc.util.Utils

class PaddedBitArray internal constructor(
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
        val arrayIndex = index / version.entriesPerWord
        val offset = index % version.entriesPerWord * version.bits
        words[arrayIndex] =
            words[arrayIndex] and (version.maxEntryValue shl offset).inv() or (value and version.maxEntryValue shl offset)
    }

    override fun get(index: Int): Int {
        val arrayIndex = index / version.entriesPerWord
        val offset = index % version.entriesPerWord * version.bits
        return words[arrayIndex] ushr offset and version.maxEntryValue
    }

    override fun copy(): BitArray {
        return PaddedBitArray(version, size, words.copyOf(words.size))
    }
}
