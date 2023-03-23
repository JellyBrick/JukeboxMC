package org.jukeboxmc.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.netty.buffer.ByteBuf
import io.netty.buffer.PooledByteBufAllocator
import org.jukeboxmc.world.Dimension
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Random
import kotlin.math.min
import kotlin.math.pow

/**
 * @author LucGamesYT
 * @version 1.0
 */
object Utils {
    val jackson = jacksonObjectMapper()
    fun blockToChunk(value: Int): Int {
        return value shr 4
    }

    @JvmStatic
    fun blockHash(x: Int, y: Int, z: Int): Long {
        require(!(y < -64 || y >= 321)) { "Y coordinate y is out of range!" }
        return x.toLong() and 0xFFFFFFFL shl 36 or (y.toLong() and 0xFFL shl 28) or (z.toLong() and 0xFFFFFFFL)
    }

    @JvmStatic
    fun toLong(x: Int, z: Int): Long {
        return x.toLong() shl 32 or (z.toLong() and 0xffffffffL)
    }

    @JvmStatic
    fun indexOf(x: Int, y: Int, z: Int): Int {
        return (x and 15 shl 8) + (z and 15 shl 4) + (y and 15)
    }

    @JvmStatic
    fun fromHashX(hash: Long): Int {
        return (hash shr 32).toInt()
    }

    @JvmStatic
    fun fromHashZ(hash: Long): Int {
        return hash.toInt()
    }

    @JvmStatic
    fun log2(value: Int): Int {
        require(value > 0)
        return 31 - Integer.numberOfLeadingZeros(value)
    }

    @JvmStatic
    fun square(value: Float): Float {
        return value * value
    }

    @JvmStatic
    fun sqrt(value: Float): Float {
        val xhalf = value * 0.5f
        var y = java.lang.Float.intBitsToFloat(0x5f375a86 - (java.lang.Float.floatToIntBits(value) shr 1))
        y *= (1.5f - xhalf * y * y)
        y *= (1.5f - xhalf * y * y)
        return value * y
    }

    @JvmStatic
    fun clamp(v: Float, min: Float, max: Float): Float {
        return if (v < min) min else min(v, max)
    }

    @JvmStatic
    fun clamp(value: Int, min: Int, max: Int): Int {
        return if (value < min) min else min(value, max)
    }

    @JvmStatic
    fun divisible(n: Int, d: Int): Int {
        var n = n
        var i = 0
        while (n % d == 0) {
            n /= d
            i++
        }
        return i
    }

    @JvmStatic
    fun allocate(data: ByteArray): ByteBuf {
        val buf = PooledByteBufAllocator.DEFAULT.directBuffer(data.size)
        buf.writeBytes(data)
        return buf
    }

    @JvmStatic
    fun getIndex(x: Int, y: Int, z: Int): Int {
        return (x and 15 shl 8) + (z and 15 shl 4) + (y and 15)
    }

    @JvmStatic
    fun ceil(floatNumber: Float): Int {
        val truncated = floatNumber.toInt()
        return if (floatNumber > truncated) truncated + 1 else truncated
    }

    @JvmStatic
    fun randomRange(random: Random, start: Int, end: Int): Int {
        return start + random.nextInt() % (end + 1 - start)
    }

    @JvmStatic
    fun round(value: Double, precision: Int): Double {
        val pow = 10.0.pow(precision)
        return (value * pow) / pow
    }

    @JvmStatic
    fun getKey(chunkX: Int, chunkZ: Int, dimension: Dimension, key: Byte): ByteArray {
        return if (dimension == Dimension.OVERWORLD) {
            byteArrayOf(
                (chunkX and 0xff).toByte(),
                (chunkX ushr 8 and 0xff).toByte(),
                (chunkX ushr 16 and 0xff).toByte(),
                (chunkX ushr 24 and 0xff).toByte(),
                (chunkZ and 0xff).toByte(),
                (chunkZ ushr 8 and 0xff).toByte(),
                (chunkZ ushr 16 and 0xff).toByte(),
                (chunkZ ushr 24 and 0xff).toByte(),
                key,
            )
        } else {
            val dimensionId = dimension.ordinal.toByte()
            byteArrayOf(
                (chunkX and 0xff).toByte(),
                (chunkX ushr 8 and 0xff).toByte(),
                (chunkX ushr 16 and 0xff).toByte(),
                (chunkX ushr 24 and 0xff).toByte(),
                (chunkZ and 0xff).toByte(),
                (chunkZ ushr 8 and 0xff).toByte(),
                (chunkZ ushr 16 and 0xff).toByte(),
                (chunkZ ushr 24 and 0xff).toByte(),
                (dimensionId.toInt() and 0xff).toByte(),
                (dimensionId.toInt() ushr 8 and 0xff).toByte(),
                (dimensionId.toInt() ushr 16 and 0xff).toByte(),
                (dimensionId.toInt() ushr 24 and 0xff).toByte(),
                key,
            )
        }
    }

    @JvmStatic
    fun getSubChunkKey(chunkX: Int, chunkZ: Int, dimension: Dimension, key: Byte, subChunk: Byte): ByteArray {
        return if (dimension == Dimension.OVERWORLD) {
            byteArrayOf(
                (chunkX and 0xff).toByte(),
                (chunkX ushr 8 and 0xff).toByte(),
                (chunkX ushr 16 and 0xff).toByte(),
                (chunkX ushr 24 and 0xff).toByte(),
                (chunkZ and 0xff).toByte(),
                (chunkZ ushr 8 and 0xff).toByte(),
                (chunkZ ushr 16 and 0xff).toByte(),
                (chunkZ ushr 24 and 0xff).toByte(),
                key,
                subChunk,
            )
        } else {
            val dimensionId = dimension.ordinal.toByte()
            byteArrayOf(
                (chunkX and 0xff).toByte(),
                (chunkX ushr 8 and 0xff).toByte(),
                (chunkX ushr 16 and 0xff).toByte(),
                (chunkX ushr 24 and 0xff).toByte(),
                (chunkZ and 0xff).toByte(),
                (chunkZ ushr 8 and 0xff).toByte(),
                (chunkZ ushr 16 and 0xff).toByte(),
                (chunkZ ushr 24 and 0xff).toByte(),
                (dimensionId.toInt() and 0xff).toByte(),
                (dimensionId.toInt() ushr 8 and 0xff).toByte(),
                (dimensionId.toInt() ushr 16 and 0xff).toByte(),
                (dimensionId.toInt() ushr 24 and 0xff).toByte(),
                key,
                subChunk,
            )
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeFile(file: File, content: InputStream) {
        if (!file.exists()) {
            file.createNewFile()
        }
        val stream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (content.read(buffer).also { length = it } != -1) {
            stream.write(buffer, 0, length)
        }
        content.close()
        stream.close()
    }

    @JvmStatic
    fun array(buffer: ByteBuf): ByteArray {
        val array = ByteArray(buffer.readableBytes())
        buffer.readBytes(array)
        return array
    }
}
