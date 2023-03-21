package org.jukeboxmc.block.palette.bitarray

import io.netty.buffer.ByteBuf

class SingletonBitArray : BitArray {
    override fun set(index: Int, value: Int) {}
    override fun get(index: Int): Int {
        return 0
    }

    override fun writeSizeToNetwork(buffer: ByteBuf, size: Int) {}
    override val size: Int
        get() = 1
    override val version: BitArrayVersion
        get() = BitArrayVersion.V0

    override fun copy(): SingletonBitArray {
        return SingletonBitArray()
    }

    companion object {
        val words = IntArray(0)
            get() = Companion.field
    }
}