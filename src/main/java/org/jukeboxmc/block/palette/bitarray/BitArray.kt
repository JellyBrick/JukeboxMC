package org.jukeboxmc.block.palette.bitarray

import io.netty.buffer.ByteBuf
import org.cloudburstmc.protocol.common.util.VarInts

interface BitArray {
    operator fun set(index: Int, value: Int)
    operator fun get(index: Int): Int
    fun writeSizeToNetwork(buffer: ByteBuf, size: Int) {
        VarInts.writeInt(buffer, size)
    }

    fun readSizeFromNetwork(buffer: ByteBuf): Int {
        return VarInts.readInt(buffer)
    }

    val size: Int
    val words: IntArray
    val version: BitArrayVersion
    fun copy(): BitArray
}
