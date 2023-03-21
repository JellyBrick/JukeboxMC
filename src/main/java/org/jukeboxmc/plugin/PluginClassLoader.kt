package org.jukeboxmc.plugin

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.io.File

/**
 * @author WaterdogPE
 * @version 1.0
 */
class PluginClassLoader(private val pluginManager: PluginManager, parent: ClassLoader?, file: File) : URLClassLoader(
    arrayOf(file.toURI().toURL()), parent
) {
    private val classes = Object2ObjectOpenHashMap<String, Class<*>?>()
    @Throws(ClassNotFoundException::class)
    protected override fun findClass(name: String): Class<*>? {
        return this.findClass(name, true)
    }

    @Throws(ClassNotFoundException::class)
    fun findClass(name: String, checkGlobal: Boolean): Class<*>? {
        if (name.startsWith("dev.waterdog.")) { // Proxy classes should be known
            throw ClassNotFoundException(name)
        }
        var result = classes[name]
        if (result != null) {
            return result
        }
        if (checkGlobal) {
            result = pluginManager.getClassFromCache(name)
        }
        if (result == null) {
            if (super.findClass(name).also { result = it } != null) {
                pluginManager.cacheClass(name, result)
            }
        }
        classes[name] = result
        return result
    }
}