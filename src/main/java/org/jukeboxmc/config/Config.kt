package org.jukeboxmc.config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.Objects
import java.util.Properties
import lombok.SneakyThrows
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Config {
    private val configType: ConfigType
    var configSection: ConfigSection
        private set
    private var gson: Gson? = null
    private var yaml: Yaml? = null
    private var properties: Properties? = null
    private val file: File?
    private val inputStream: InputStream?

    @SneakyThrows
    constructor(file: File, configType: ConfigType) {
        this.file = file
        inputStream = null
        this.configType = configType
        configSection = ConfigSection()
        if (!this.file.exists()) {
            this.file.createNewFile()
        }
        load()
    }

    @SneakyThrows
    constructor(inputStream: InputStream, configType: ConfigType) {
        file = null
        this.inputStream = inputStream
        this.configType = configType
        configSection = ConfigSection()
        load()
    }

    @SneakyThrows
    fun load() {
        InputStreamReader(
            if (file == null) Objects.requireNonNull(inputStream) else FileInputStream(
                file
            )
        ).use { reader ->
            when (configType) {
                ConfigType.JSON -> {
                    val gson = GsonBuilder()
                    gson.setPrettyPrinting()
                    gson.registerTypeAdapter(
                        Double::class.java,
                        JsonSerializer { src: Double, typeOfSrc: Type?, context: JsonSerializationContext? ->
                            if (src == src.toInt()) {
                                return@JsonSerializer JsonPrimitive(src.toInt())
                            } else if (src == src.toLong()) {
                                return@JsonSerializer JsonPrimitive(src.toLong())
                            }
                            JsonPrimitive(src)
                        } as JsonSerializer<Double>)
                    this.gson = gson.create()
                    configSection = ConfigSection(
                        this.gson.fromJson(
                            reader,
                            object : TypeToken<LinkedHashMap<String?, Any?>?>() {}.type
                        )
                    )
                }

                ConfigType.YAML -> {
                    val dumperOptions = DumperOptions()
                    dumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                    yaml = Yaml(dumperOptions)
                    configSection = ConfigSection(yaml!!.loadAs(reader, LinkedHashMap::class.java))
                }

                ConfigType.PROPERTIES -> {
                    properties = Properties()
                    properties!!.load(reader)
                }
            }
        }
    }

    fun exists(key: String): Boolean {
        return when (configType) {
            ConfigType.JSON, ConfigType.YAML -> configSection.exists(key)
            ConfigType.PROPERTIES -> properties!!.getProperty(key) != null
        }
    }

    operator fun set(key: String, `object`: Any?) {
        when (configType) {
            ConfigType.JSON, ConfigType.YAML -> configSection[key] = `object`
            ConfigType.PROPERTIES -> properties!!.setProperty(key, `object`.toString())
        }
    }

    fun remove(key: String?) {
        when (configType) {
            ConfigType.JSON, ConfigType.YAML -> {
                configSection.remove(key)
                save()
            }

            ConfigType.PROPERTIES -> {
                properties!!.remove(key)
                save()
            }
        }
    }

    fun addDefault(key: String, value: Any?) {
        if (!exists(key)) {
            this[key] = value
            save()
        }
    }

    operator fun get(key: String?): Any {
        return when (configType) {
            ConfigType.JSON, ConfigType.YAML -> configSection[key]
            ConfigType.PROPERTIES -> properties!!.getProperty(key)
        }!!
    }

    fun getStringList(key: String?): MutableList<String?> {
        return configSection.getStringList(key)
    }

    fun getString(key: String?): String? {
        return configSection.getString(key)
    }

    fun getIntegerList(key: String?): List<Int?> {
        return configSection.getIntegerList(key)
    }

    fun getInt(key: String?): Int {
        return configSection.getInt(key)
    }

    fun getLongList(key: String?): List<Long?> {
        return configSection.getLongList(key)
    }

    fun getLong(key: String?): Long {
        return configSection.getLong(key)
    }

    fun getDoubleList(key: String?): List<Double?> {
        return configSection.getDoubleList(key)
    }

    fun getDouble(key: String?): Double {
        return configSection.getDouble(key)
    }

    fun getFloatList(key: String?): List<Float?> {
        return configSection.getFloatList(key)
    }

    fun getFloat(key: String?): Float {
        return configSection.getFloat(key)
    }

    fun getByteList(key: String?): List<Byte?> {
        return configSection.getByteList(key)
    }

    fun getByte(key: String?): Byte {
        return configSection.getByte(key)
    }

    fun getShortList(key: String?): List<Short?> {
        return configSection.getShortList(key)
    }

    fun getShort(key: String?): Short {
        return configSection.getShort(key)
    }

    fun getBooleanList(key: String?): List<Boolean?> {
        return configSection.getBooleanList(key)
    }

    fun getBoolean(key: String?): Boolean {
        return configSection.getBoolean(key)
    }

    val map: Map<String?, Any?>
        get() = configSection
    val keys: Set<String?>
        get() = configSection.keys
    val values: Collection<Any?>
        get() = configSection.values

    fun toJSON(): String {
        val objectMapper = ObjectMapper()
        return try {
            objectMapper.enableDefaultTyping().writeValueAsString(this)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    @SneakyThrows
    fun save() {
        if (file == null) {
            throw IOException("This config can not be saved!")
        }
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        OutputStreamWriter(FileOutputStream(file)).use { writer ->
            when (configType) {
                ConfigType.JSON -> gson.toJson(
                    configSection, writer
                )

                ConfigType.YAML -> yaml!!.dump(configSection, writer)
                ConfigType.PROPERTIES -> properties.store(writer, "")
            }
        }
    }
}