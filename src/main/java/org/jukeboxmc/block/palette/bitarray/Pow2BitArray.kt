package org.jukeboxmc.block.palette.bitarray

import java.util.Arrays
import lombok.Getter
import org.jukeboxmc.util.Utils

@Getter
class Pow2BitArray internal constructor(
    private override val version: BitArrayVersion,
    private override val size: Int,
    private override val words: IntArray
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
        return Pow2BitArray(version, size, Arrays.copyOf(words, words.size))
    }
}