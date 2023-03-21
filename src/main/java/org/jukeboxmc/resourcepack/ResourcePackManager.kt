package org.jukeboxmc.resourcepack

import com.google.common.io.Files
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Locale
import java.util.Objects
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.logger.Logger

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
        for (file in Objects.requireNonNull(resourcePacksPath.listFiles())) {
            if (!file.isDirectory) {
                val fileEnding = Files.getFileExtension(file.name)
                if (fileEnding.equals("zip", ignoreCase = true) || fileEnding.equals("mcpack", ignoreCase = true)) {
                    try {
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
                            val manifest = JsonParser()
                                .parse(
                                    InputStreamReader(
                                        zipFile.getInputStream(manifestEntry),
                                        StandardCharsets.UTF_8
                                    )
                                ).asJsonObject
                            require(isManifestValid(manifest)) { "The $manifestFileName file is invalid" }
                            val manifestHeader = manifest.getAsJsonObject("header")
                            val resourcePackName = manifestHeader["name"].asString
                            val resourcePackUuid = manifestHeader["uuid"].asString
                            val resourcePackVersionArray = manifestHeader["version"].asJsonArray
                            val resourcePackVersion = resourcePackVersionArray.toString()
                                .replace("[", "").replace("]", "")
                                .replace(",".toRegex(), ".")
                            val resourcePackSize = FastMath.toIntExact(file.length())
                            val resourcePackSha256: ByteArray = MessageDigest.getInstance("SHA-256")
                                .digest(java.nio.file.Files.readAllBytes(file.toPath()))
                            logger.info("Read resource pack " + resourcePackName + " §rsuccessful from " + file.name)
                            val resourcePack = ResourcePack(
                                file, resourcePackName, resourcePackUuid,
                                resourcePackVersion, resourcePackSize.toLong(), resourcePackSha256, ByteArray(0)
                            )
                            resourcePacks.put(resourcePackUuid, resourcePack)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: NoSuchAlgorithmException) {
                        e.printStackTrace()
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

    private fun isManifestValid(manifest: JsonObject): Boolean {
        if (manifest.has("format_version") && manifest.has("header") &&
            manifest.has("modules")
        ) {
            val manifestHeader = manifest["header"].asJsonObject
            if (manifestHeader.has("description") && manifestHeader.has("name") &&
                manifestHeader.has("uuid") && manifestHeader.has("version")
            ) {
                val headerVersion = manifestHeader.getAsJsonArray("version")
                return headerVersion.size() == 3
            }
        }
        return false
    }
}