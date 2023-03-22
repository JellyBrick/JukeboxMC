package org.jukeboxmc.plugin

import com.google.common.base.Preconditions
import org.jukeboxmc.Server
import org.jukeboxmc.logger.Logger
import org.jukeboxmc.util.Utils
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * @author WaterdogPE
 * @version 1.0
 */
abstract class Plugin {
    var enabled = false
        set(value) {
            if (value == field) {
                return
            }
            field = enabled
            try {
                if (enabled) {
                    onEnable()
                } else {
                    onDisable()
                }
            } catch (e: Exception) {
                throw RuntimeException(
                    "Can not ${if (enabled) "enable" else "disable"} plugin ${description?.name}!",
                    e,
                )
            }
        }
    var description: PluginYAML? = null
        private set
    var server: Server? = null
        private set
    private var pluginFile: File? = null
    var dataFolder: File? = null
        private set
    var logger: Logger? = null
        private set
    private var initialized = false
    fun init(description: PluginYAML, server: Server, pluginFile: File?) {
        Preconditions.checkArgument(!initialized, "Plugin has been already initialized!")
        initialized = true
        this.description = description
        this.server = server
        logger = server.logger
        this.pluginFile = pluginFile
        dataFolder = File(server.pluginFolder.toString() + "/" + description.name + "/")
        if (!dataFolder!!.exists()) {
            dataFolder!!.mkdirs()
        }
    }

    /**
     * Called when the plugin is loaded into the server, but before it was enabled.
     * Can be used to load important information or to establish connections
     */
    fun onStartup() {}

    /**
     * Called when the base server startup is done and the plugins are getting enabled.
     * Also called whenever the plugin state changes to enabled
     */
    abstract fun onEnable()

    /**
     * Called on server shutdown, or when the plugin gets disabled, for example by another plugin or when an error occurred.
     * Also gets called when the plugin state changes to disabled
     */
    fun onDisable() {}

    /**
     * @param filename the file name to read
     * @return Returns a file from inside the plugin jar as an InputStream
     */
    fun getResourceFile(filename: String?): InputStream? {
        try {
            val pluginJar = JarFile(pluginFile)
            val entry: JarEntry = pluginJar.getJarEntry(filename)
            return pluginJar.getInputStream(entry)
        } catch (e: IOException) {
            logger!!.error("Can not get plugin resource!")
        }
        return null
    }

    @JvmOverloads
    fun saveResource(filename: String, replace: Boolean = false): Boolean {
        return this.saveResource(filename, filename, replace)
    }

    /**
     * Saves a resource from the plugin jar's resources to the plugin folder
     *
     * @param filename   the name of the file in the jar's resources
     * @param outputName the name the file should be saved as in the plugin folder
     * @param replace    whether the file should be replaced even if present already
     * @return returns false if an exception occurred, the file already exists and shouldn't be replaced, and when the file could
     * not be found in the jar
     * returns true if the file overwrite / copy was successful
     */
    fun saveResource(filename: String, outputName: String, replace: Boolean): Boolean {
        Preconditions.checkArgument(
            filename != null && !filename.trim { it <= ' ' }.isEmpty(),
            "Filename can not be null!",
        )
        val file = File(dataFolder, outputName)
        if (file.exists() && !replace) {
            return false
        }
        try {
            getResourceFile(filename).use { resource ->
                if (resource == null) {
                    return false
                }
                val outFolder = file.parentFile
                if (!outFolder.exists()) {
                    outFolder.mkdirs()
                }
                Utils.writeFile(file, resource)
            }
        } catch (e: IOException) {
            logger!!.error("Can not save plugin file!")
            return false
        }
        return true
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    val name: String?
        get() = description!!.name
    val version: String?
        get() = description!!.version
    val author: String?
        get() = description!!.author
    val authors: List<String?>?
        get() = description!!.authors
    val loadOrder: PluginLoadOrder
        get() = description!!.loadOrder
}
