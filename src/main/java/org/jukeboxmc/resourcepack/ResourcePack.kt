package org.jukeboxmc.resourcepack

import java.io.File
import java.io.IOException
import java.util.UUID
import lombok.ToString

/**
 * @author Kaooot
 * @version 1.0
 */
@ToString(exclude = ["file"])
class ResourcePack(
    private val file: File,
    val name: String,
    private val uuid: String,
    val version: String,
    val size: Long,
    val sha256: ByteArray,
    private var chunk: ByteArray
) {
    fun getUuid(): UUID {
        return UUID.fromString(uuid)
    }

    fun getChunk(offset: Int, length: Int): ByteArray {
        if (size - offset > length) {
            chunk = ByteArray(length)
        } else {
            chunk = ByteArray((size - offset).toInt())
        }
        try {
            FileInputStream(file).use { fileInputStream ->
                fileInputStream.skip(offset.toLong())
                fileInputStream.read(chunk)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return chunk
    }
}