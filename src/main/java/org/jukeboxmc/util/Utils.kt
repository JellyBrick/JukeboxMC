package org.jukeboxmc.util

import com.google.gson.GsonBuilder
import io.netty.buffer.ByteBuf
import io.netty.buffer.PooledByteBufAllocator
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Random
import lombok.Getter
import lombok.experimental.Accessors
import org.jukeboxmc.world.Dimension

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Accessors(fluent = true)
object Utils {
    @Getter
    val gson = GsonBuilder().setPrettyPrinting().create()
    fun blockToChunk(value: Int): Int {
        return value shr 4
    }

    fun blockHash(x: Int, y: Int, z: Int): Long {
        require(!(y < -64 || y >= 321)) { "Y coordinate y is out of range!" }
        return x.toLong() and 0xFFFFFFFL shl 36 or (y.toLong() and 0xFFL shl 28) or (z.toLong() and 0xFFFFFFFL)
    }

    fun toLong(x: Int, z: Int): Long {
        return x.toLong() shl 32 or (z.toLong() and 0xffffffffL)
    }

    fun indexOf(x: Int, y: Int, z: Int): Int {
        return (x and 15 shl 8) + (z and 15 shl 4) + (y and 15)
    }

    fun fromHashX(hash: Long): Int {
        return (hash shr 32).toInt()
    }

    fun fromHashZ(hash: Long): Int {
        return hash.toInt()
    }

    fun log2(value: Int): Int {
        require(value > 0)
        return 31 - Integer.numberOfLeadingZeros(value)
    }

    fun square(value: Float): Float {
        return value * value
    }

    fun sqrt(value: Float): Float {
        val xhalf = value * 0.5f
        var y = java.lang.Float.intBitsToFloat(0x5f375a86 - (java.lang.Float.floatToIntBits(value) shr 1))
        y = y * (1.5f - xhalf * y * y)
        y = y * (1.5f - xhalf * y * y)
        return value * y
    }

    fun clamp(v: Float, min: Float, max: Float): Float {
        return if (v < min) min else Math.min(v, max)
    }

    fun clamp(value: Int, min: Int, max: Int): Int {
        return if (value < min) min else Math.min(value, max)
    }

    fun divisible(n: Int, d: Int): Int {
        var n = n
        var i = 0
        while (n % d == 0) {
            n /= d
            i++
        }
        return i
    }

    fun allocate(data: ByteArray): ByteBuf {
        val buf = PooledByteBufAllocator.DEFAULT.directBuffer(data.size)
        buf.writeBytes(data)
        return buf
    }

    fun getIndex(x: Int, y: Int, z: Int): Int {
        return (x and 15 shl 8) + (z and 15 shl 4) + (y and 15)
    }

    fun ceil(floatNumber: Float): Int {
        val truncated = floatNumber.toInt()
        return if (floatNumber > truncated) truncated + 1 else truncated
    }

    fun randomRange(random: Random, start: Int, end: Int): Int {
        return start + random.nextInt() % (end + 1 - start)
    }

    fun round(value: Double, precision: Int): Double {
        val pow = Math.pow(10.0, precision.toDouble())
        return Math.round(value * pow).toDouble() / pow
    }

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
                key
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
                key
            )
        }
    }

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
                subChunk
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
                subChunk
            )
        }
    }

    @Throws(IOException::class)
    fun writeFile(file: File, content: InputStream) {
        requireNotNull(content) { "Content must not be null!" }
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

    fun array(buffer: ByteBuf): ByteArray {
        val array = ByteArray(buffer.readableBytes())
        buffer.readBytes(array)
        return array
    }
}