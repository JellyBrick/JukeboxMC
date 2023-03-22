package org.jukeboxmc.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Properties

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Config {
    private val configType: ConfigType
    var configSection: ConfigSection
        private set
    private val jackson = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
    private val yaml: Yaml = Yaml(
        DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        },
    )
    private var properties: Properties = Properties()
    private val file: File?
    private val inputStream: InputStream?

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

    constructor(inputStream: InputStream, configType: ConfigType) {
        file = null
        this.inputStream = inputStream
        this.configType = configType
        configSection = ConfigSection()
        load()
    }

    fun load() {
        (
            if (file == null) {
                inputStream
            } else {
                FileInputStream(
                    file,
                )
            }
            )?.let {
            InputStreamReader(
                it,
            ).use { reader ->
                when (configType) {
                    ConfigType.JSON -> {
                        configSection = ConfigSection(
                            try {
                                this.jackson.readValue<LinkedHashMap<String, Any>>(reader)
                            } catch (ignored: MismatchedInputException) {
                                emptyMap()
                            },
                        )
                    }

                    ConfigType.YAML -> {
                        configSection = ConfigSection(yaml.loadAs(reader, LinkedHashMap::class.java))
                    }

                    ConfigType.PROPERTIES -> {
                        properties.load(reader)
                    }
                }
            }
        }
    }

    fun exists(key: String): Boolean {
        return when (configType) {
            ConfigType.JSON, ConfigType.YAML -> configSection.exists(key)
            ConfigType.PROPERTIES -> properties.getProperty(key) != null
        }
    }

    operator fun set(key: String, value: Any) {
        when (configType) {
            ConfigType.JSON, ConfigType.YAML -> configSection[key] = value
            ConfigType.PROPERTIES -> properties.setProperty(key, value.toString())
        }
    }

    fun remove(key: String?) {
        when (configType) {
            ConfigType.JSON, ConfigType.YAML -> {
                configSection.remove(key)
                save()
            }

            ConfigType.PROPERTIES -> {
                properties.remove(key)
                save()
            }
        }
    }

    fun addDefault(key: String, value: Any) {
        if (!exists(key)) {
            this[key] = value
            save()
        }
    }

    operator fun get(key: String?): Any {
        return when (configType) {
            ConfigType.JSON, ConfigType.YAML -> configSection[key]
            ConfigType.PROPERTIES -> properties.getProperty(key)
        }!!
    }

    fun getStringList(key: String): MutableList<String> {
        return configSection.getStringList(key)
    }

    fun getString(key: String): String {
        return configSection.getString(key)
    }

    fun getIntegerList(key: String): List<Int> {
        return configSection.getIntegerList(key)
    }

    fun getInt(key: String): Int {
        return configSection.getInt(key)
    }

    fun getLongList(key: String): List<Long> {
        return configSection.getLongList(key)
    }

    fun getLong(key: String): Long {
        return configSection.getLong(key)
    }

    fun getDoubleList(key: String): List<Double> {
        return configSection.getDoubleList(key)
    }

    fun getDouble(key: String): Double {
        return configSection.getDouble(key)
    }

    fun getFloatList(key: String): List<Float> {
        return configSection.getFloatList(key)
    }

    fun getFloat(key: String): Float {
        return configSection.getFloat(key)
    }

    fun getByteList(key: String): List<Byte> {
        return configSection.getByteList(key)
    }

    fun getByte(key: String): Byte {
        return configSection.getByte(key)
    }

    fun getShortList(key: String): List<Short> {
        return configSection.getShortList(key)
    }

    fun getShort(key: String): Short {
        return configSection.getShort(key)
    }

    fun getBooleanList(key: String): List<Boolean> {
        return configSection.getBooleanList(key)
    }

    fun getBoolean(key: String): Boolean {
        return configSection.getBoolean(key)
    }

    val map: ConfigSection
        get() = configSection
    val keys: Set<String>
        get() = configSection.keys
    val values: Collection<Any>
        get() = configSection.values

    fun toJSON(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.activateDefaultTyping(objectMapper.polymorphicTypeValidator).writeValueAsString(this)
    }

    fun save() {
        if (file == null) {
            throw IOException("This config can not be saved!")
        }
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        OutputStreamWriter(FileOutputStream(file)).use { writer ->
            when (configType) {
                ConfigType.JSON -> jackson.writeValueAsString(configSection)
                ConfigType.YAML -> yaml.dump(configSection, writer)
                ConfigType.PROPERTIES -> properties.store(writer, "")
            }
        }
    }
}
