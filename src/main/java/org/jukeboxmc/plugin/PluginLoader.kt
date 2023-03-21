package org.jukeboxmc.plugin

import java.io.DataInputStream
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.util.jar.JarEntry
import org.jukeboxmc.logger.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.yaml.snakeyaml.Yaml

/**
 * @author WaterdogPE
 * @version 1.0
 */
class PluginLoader(private val logger: Logger?, private val pluginManager: PluginManager) {
    fun loadPluginJAR(pluginConfig: PluginYAML, pluginJar: File): Plugin? {
        val loader: PluginClassLoader
        try {
            loader = PluginClassLoader(pluginManager, this.javaClass.classLoader, pluginJar)
            pluginManager.pluginClassLoaders[pluginConfig.getName()] = loader
        } catch (e: MalformedURLException) {
            logger!!.error("Error while creating class loader(plugin=" + pluginConfig.getName() + ")" + e.message)
            return null
        }
        try {
            val mainClass = loader.loadClass(pluginConfig.getMain())
            if (!Plugin::class.java.isAssignableFrom(mainClass)) {
                return null
            }
            val castedMain = mainClass.asSubclass(
                Plugin::class.java
            )
            val plugin = castedMain.getDeclaredConstructor().newInstance()
            plugin.init(pluginConfig, pluginManager.server, pluginJar)
            return plugin
        } catch (e: Exception) {
            logger!!.error("Error while loading plugin main class(main=" + pluginConfig.getMain() + ",plugin=" + pluginConfig.getName() + ")" + e.message)
        }
        return null
    }

    fun loadPluginData(file: File, yaml: Yaml): PluginYAML? {
        try {
            JarFile(file).use { pluginJar ->
                val configEntry: JarEntry = pluginJar.getJarEntry("plugin.yml")
                if (configEntry == null) {
                    val entries: Enumeration<JarEntry> = pluginJar.entries()
                    val pluginYAML = PluginYAML()
                    while (entries.hasMoreElements()) {
                        val entry: JarEntry = entries.nextElement()
                        if (entry.name.endsWith(".class")) {
                            val classReader = ClassReader(DataInputStream(pluginJar.getInputStream(entry)))
                            if (classReader.superName == "org/jukeboxmc/plugin/Plugin") {
                                val pluginName: AtomicReference<String> = AtomicReference<String>()
                                val version: AtomicReference<String> = AtomicReference<String>()
                                val author: AtomicReference<String> = AtomicReference<String>()
                                val depends: AtomicReference<List<String>> = AtomicReference<List<String>>()
                                val pluginLoadOrder: AtomicReference<String> =
                                    AtomicReference<String>(PluginLoadOrder.POSTWORLD.name)
                                classReader.accept(object : ClassVisitor(Opcodes.ASM7) {
                                    override fun visitAnnotation(
                                        descriptor: String,
                                        visible: Boolean
                                    ): AnnotationVisitor {
                                        when (descriptor) {
                                            "Lorg/jukeboxmc/plugin/annotation/PluginName;" -> {
                                                return object : AnnotationVisitor(Opcodes.ASM7) {
                                                    override fun visit(key: String, value: Any) {
                                                        pluginName.set(value as String)
                                                    }
                                                }
                                            }

                                            "Lorg/jukeboxmc/plugin/annotation/Version;" -> {
                                                return object : AnnotationVisitor(Opcodes.ASM7) {
                                                    override fun visit(key: String, value: Any) {
                                                        version.set(value as String)
                                                    }
                                                }
                                            }

                                            "Lorg/jukeboxmc/plugin/annotation/Author;" -> {
                                                return object : AnnotationVisitor(Opcodes.ASM7) {
                                                    override fun visit(key: String, value: Any) {
                                                        author.set(value as String)
                                                    }
                                                }
                                            }

                                            "Lorg/jukeboxmc/plugin/annotation/Depends;" -> {
                                                return object : AnnotationVisitor(Opcodes.ASM7) {
                                                    override fun visitArray(name: String): AnnotationVisitor {
                                                        depends.set(ArrayList<String>())
                                                        return object : AnnotationVisitor(Opcodes.ASM7) {
                                                            override fun visit(name: String, value: Any) {
                                                                depends.get().add(value as String)
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            "Lorg/jukeboxmc/plugin/annotation/Startup;" -> {
                                                return object : AnnotationVisitor(Opcodes.ASM7) {
                                                    override fun visitEnum(
                                                        name: String,
                                                        descriptor: String,
                                                        value: String
                                                    ) {
                                                        pluginLoadOrder.set(value)
                                                    }
                                                }
                                            }
                                        }
                                        return super.visitAnnotation(descriptor, visible)
                                    }
                                }, 0)
                                pluginYAML.setMain(classReader.className.replace("/", "."))
                                pluginYAML.setName(pluginName.get())
                                pluginYAML.setVersion(version.get())
                                pluginYAML.setAuthor(author.get())
                                pluginYAML.setDepends(depends.get())
                                pluginYAML.setLoad(PluginLoadOrder.valueOf(pluginLoadOrder.get()))
                            }
                        }
                    }
                    if (pluginYAML.getName() != null && pluginYAML.getVersion() != null && pluginYAML.getAuthor() != null && pluginYAML.getMain() != null) {
                        return pluginYAML
                    }
                    logger!!.warn("Jar file " + file.name + " doesnt contain a plugin.yml!")
                    return null
                }
                pluginJar.getInputStream(configEntry).use { fileStream ->
                    val pluginConfig: PluginYAML = yaml.loadAs<PluginYAML>(fileStream, PluginYAML::class.java)
                    if (pluginConfig.getMain() != null && pluginConfig.getName() != null) {
                        // Valid plugin.yml, main and name set
                        return pluginConfig
                    }
                }
                logger!!.warn("Invalid plugin.yml for " + file.name + ": main and/or name property missing")
            }
        } catch (e: IOException) {
            logger!!.error("Error while reading plugin directory$e")
        }
        return null
    }

    companion object {
        fun isJarFile(file: Path): Boolean {
            return file.fileName.toString().endsWith(".jar")
        }
    }
}