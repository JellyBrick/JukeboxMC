package org.jukeboxmc.plugin

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import org.jukeboxmc.Server
import org.jukeboxmc.command.CommandManager
import org.jukeboxmc.event.Event
import org.jukeboxmc.event.EventHandler
import org.jukeboxmc.event.EventPriority
import org.jukeboxmc.event.Listener
import org.jukeboxmc.event.RegisteredListener
import org.jukeboxmc.logger.Logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.LinkedList

/**
 * @author WaterdogPE
 * @version 1.0
 */
class PluginManager(val server: Server) {
    private val logger: Logger = server.logger
    private val pluginLoader: PluginLoader = PluginLoader(logger, this)
    private val commandManager: CommandManager = CommandManager()
    private val yamlLoader = YAMLMapper().registerKotlinModule()
    private val pluginMap: Object2ObjectMap<String, Plugin> = Object2ObjectArrayMap()
    private val cachedClasses: Object2ObjectMap<String, Class<*>?> = Object2ObjectArrayMap<String, Class<*>>()
    val pluginClassLoaders: Object2ObjectMap<String?, PluginClassLoader?> =
        Object2ObjectArrayMap<String, PluginClassLoader>()
    val listeners: MutableMap<Class<out Event>, MutableMap<EventPriority, MutableList<RegisteredListener>>> =
        HashMap()

    init {
        loadPluginsIn(server.pluginFolder.toPath())
    }

    @JvmOverloads
    fun loadPluginsIn(folderPath: Path?, directStartup: Boolean = false) {
        try {
            Files.walk(folderPath).use { pluginPaths ->
                pluginPaths.filter { path: Path? -> Files.isRegularFile(path) }
                    .filter { file: Path -> PluginLoader.isJarFile(file) }
                    .forEach { jarPath: Path -> this.loadPlugin(jarPath, directStartup) }
            }
        } catch (e: IOException) {
            logger.error("Error while filtering plugin files $e")
        }
    }

    fun loadPlugin(path: Path): Plugin? {
        return this.loadPlugin(path, false)
    }

    fun loadPlugin(path: Path, directStartup: Boolean): Plugin? {
        if (!Files.isRegularFile(path) || !PluginLoader.isJarFile(path)) {
            logger.warn("Cannot load plugin: Provided file is no jar file: " + path.fileName)
            return null
        }
        val pluginFile = path.toFile()
        if (!pluginFile.exists()) {
            return null
        }
        val config = pluginLoader.loadPluginData(pluginFile, yamlLoader) ?: return null
        if (getPluginByName(config.name) != null) {
            logger.warn("Plugin is already loaded: " + config.name)
            return null
        }
        val plugin = pluginLoader.loadPluginJAR(config, pluginFile) ?: return null
        pluginMap[config.name] = plugin
        plugin.onStartup()
        if (directStartup) {
            try {
                plugin.enabled = true
            } catch (e: Exception) {
                logger.error("Direct startup failed!" + e.message)
            }
        }
        return plugin
    }

    fun enableAllPlugins(pluginLoadOrder: PluginLoadOrder) {
        val failed = LinkedList<Plugin>()
        for (plugin in pluginMap.values) {
            if (plugin.loadOrder == pluginLoadOrder) {
                if (!enablePlugin(plugin, null)) {
                    failed.add(plugin)
                }
            }
        }
        if (!failed.isEmpty()) {
            val builder = StringBuilder("§cFailed to load plugins: §e")
            while (failed.peek() != null) {
                val plugin = failed.poll()
                builder.append(plugin.name)
                if (failed.peek() != null) {
                    builder.append(", ")
                }
            }
            logger.info(builder.toString())
        }
    }

    fun enablePlugin(plugin: Plugin, parent: String?): Boolean {
        if (plugin.isEnabled()) return true
        val pluginName = plugin.name
        if (plugin.description!!.depends != null) {
            for (depend in plugin.description!!.depends!!) {
                if (depend == parent) {
                    logger.warn("§cCan not enable plugin $pluginName circular dependency $parent!")
                    return false
                }
                val dependPlugin = getPluginByName(depend)
                if (dependPlugin == null) {
                    logger.warn("§cCan not enable plugin $pluginName missing dependency $depend!")
                    return false
                }
                if (!dependPlugin.isEnabled() && !enablePlugin(dependPlugin, pluginName)) {
                    return false
                }
            }
        }
        plugin.enabled = true
        logger.info("Loaded plugin " + plugin.name + " Version: " + plugin.version + " successfully!")
        return true
    }

    fun disableAllPlugins() {
        for (plugin in pluginMap.values) {
            logger.info("Disabling plugin " + plugin.name + "")
            plugin!!.enabled = false
        }
    }

    fun getClassFromCache(className: String): Class<*>? {
        var clazz = cachedClasses[className]
        if (clazz != null) {
            return clazz
        }
        for (loader in pluginClassLoaders.values) {
            try {
                if (loader!!.findClass(className, false).also { clazz = it } != null) {
                    return clazz
                }
            } catch (e: ClassNotFoundException) {
                // ignore
            }
        }
        return null
    }

    fun cacheClass(className: String, clazz: Class<*>?) {
        cachedClasses.putIfAbsent(className, clazz)
    }

    private fun getPluginMap(): Map<String?, Plugin?> {
        return pluginMap
    }

    val plugins: Collection<Plugin?>
        get() = pluginMap.values

    fun getPluginByName(pluginName: String?): Plugin? {
        return pluginMap.getOrDefault(pluginName, null)
    }

    fun registerListener(listener: Listener) {
        val listenerClass: Class<out Listener> = listener.javaClass
        listenerClass.declaredMethods.forEach { method ->
            val eventHandler = method.getAnnotation(
                EventHandler::class.java,
            ) ?: return@forEach
            val eventPriority = eventHandler.priority
            if (method.parameterTypes.size != 1 || !Event::class.java.isAssignableFrom(method.parameterTypes[0])) {
                return@forEach
            }
            val eventClass = method.parameterTypes[0] as Class<out Event>
            listeners.putIfAbsent(eventClass, LinkedHashMap())
            listeners.getValue(eventClass).putIfAbsent(eventPriority, mutableListOf())
            listeners.getValue(eventClass).getValue(eventPriority).add(RegisteredListener(method, listener))
        }
    }

    fun callEvent(event: Event) {
        val eventPriorityListMap = listeners[event.javaClass]
        if (eventPriorityListMap != null) {
            EventPriority.values().forEach { eventPriority ->
                eventPriorityListMap[eventPriority]?.forEach { registeredListener ->
                    registeredListener.method(registeredListener.listener, event)
                }
            }
        }
    }

    fun getCommandManager(): CommandManager {
        return commandManager
    }
}
