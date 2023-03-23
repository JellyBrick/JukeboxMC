package org.jukeboxmc.resourcepack

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.io.Files
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.logger.Logger
import org.jukeboxmc.util.Utils
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * @author Kaooot
 * @version 1.0
 */
class ResourcePackManager(private val logger: Logger) {
    private val resourcePacks: MutableMap<String, ResourcePack> = HashMap()
    fun loadResourcePacks() {
        val resourcePacksPath = File(System.getProperty("user.dir") + "/resource_packs")
        if (!resourcePacksPath.exists()) {
            resourcePacksPath.mkdirs()
        }
        if (!resourcePacksPath.isDirectory) {
            return
        }
        resourcePacksPath.listFiles()!!.forEach { file ->
            if (!file.isDirectory) {
                val fileEnding = Files.getFileExtension(file.name)
                if (fileEnding.equals("zip", ignoreCase = true) || fileEnding.equals("mcpack", ignoreCase = true)) {
                    ZipFile(file).use { zipFile ->
                        val manifestFileName = "manifest.json"
                        var manifestEntry = zipFile.getEntry(manifestFileName)

                        // due to the mcpack file extension
                        if (manifestEntry == null) {
                            manifestEntry = zipFile.stream().filter { zipEntry: ZipEntry ->
                                !zipEntry.isDirectory && zipEntry.name.lowercase(Locale.getDefault())
                                    .endsWith(manifestFileName)
                            }
                                .filter { zipEntry: ZipEntry ->
                                    val zipEntryFile = File(zipEntry.name)
                                    if (!zipEntryFile.name.equals(manifestFileName, ignoreCase = true)) {
                                        return@filter false
                                    }
                                    zipEntryFile.parent == null || zipEntryFile.parentFile.parent == null
                                }
                                .findFirst()
                                .orElseThrow { IllegalArgumentException("The $manifestFileName file could not be found") }
                        }
                        val manifest: Map<String, *> = InputStreamReader(
                            zipFile.getInputStream(manifestEntry),
                            StandardCharsets.UTF_8,
                        ).use(Utils.jackson::readValue)
                        require(isManifestValid(manifest)) { "The $manifestFileName file is invalid" }
                        val manifestHeader = manifest["header"] as Map<*, *>
                        val resourcePackName = manifestHeader["name"] as String
                        val resourcePackUuid = manifestHeader["uuid"] as String
                        val resourcePackVersionArray = manifestHeader["version"] as List<*>
                        val resourcePackVersion = resourcePackVersionArray.toString()
                            .replace("[", "").replace("]", "")
                            .replace(',', '.')
                        val resourcePackSize = FastMath.toIntExact(file.length())
                        val resourcePackSha256: ByteArray = MessageDigest.getInstance("SHA-256")
                            .digest(java.nio.file.Files.readAllBytes(file.toPath()))
                        logger.info("Read resource pack " + resourcePackName + " §rsuccessful from " + file.name)
                        val resourcePack = ResourcePack(
                            file,
                            resourcePackName,
                            resourcePackUuid,
                            resourcePackVersion,
                            resourcePackSize.toLong(),
                            resourcePackSha256,
                            ByteArray(0),
                        )
                        resourcePacks.put(resourcePackUuid, resourcePack)
                    }
                }
            }
        }
    }

    fun retrieveResourcePackById(uuid: String): ResourcePack? {
        return resourcePacks[uuid]
    }

    fun retrieveResourcePacks(): Collection<ResourcePack> {
        return resourcePacks.values
    }

    private fun isManifestValid(manifest: Map<String, *>): Boolean {
        if (manifest.containsKey("format_version") && manifest.containsKey("header") &&
            manifest.containsKey("modules")
        ) {
            val manifestHeader = manifest["header"] as Map<*, *>
            if (manifestHeader.containsKey("description") && manifestHeader.containsKey("name") &&
                manifestHeader.containsKey("uuid") && manifestHeader.containsKey("version")
            ) {
                val headerVersion = manifestHeader["version"] as List<*>
                return headerVersion.size == 3
            }
        }
        return false
    }
}
