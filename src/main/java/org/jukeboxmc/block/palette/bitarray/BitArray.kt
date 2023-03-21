package org.jukeboxmc.block.palette.bitarray

import com.nukkitx.network.VarInts
import io.netty.buffer.ByteBuf

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