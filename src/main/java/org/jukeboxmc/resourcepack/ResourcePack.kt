package org.jukeboxmc.resourcepack

import java.io.File
import java.io.FileInputStream
import java.util.UUID

/**
 * @author Kaooot
 * @version 1.0
 */
class ResourcePack(
    private val file: File,
    val name: String,
    private val uuid: String,
    val version: String,
    val size: Long,
    val sha256: ByteArray,
    private var chunk: ByteArray,
) {
    fun getUuid(): UUID {
        return UUID.fromString(uuid)
    }

    fun getChunk(offset: Int, length: Int): ByteArray {
        chunk = if (size - offset > length) {
            ByteArray(length)
        } else {
            ByteArray((size - offset).toInt())
        }
        FileInputStream(file).use { fileInputStream ->
            fileInputStream.skip(offset.toLong())
            fileInputStream.read(chunk)
        }
        return chunk
    }
}
