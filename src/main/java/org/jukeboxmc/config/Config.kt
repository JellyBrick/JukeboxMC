package org.jukeboxmc.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Config {
    private val configType: ConfigType
    var configSection: ConfigSection
        private set
    private val jackson: ObjectMapper
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
        jackson = when (configType) {
            ConfigType.JSON -> JsonMapper()
            ConfigType.YAML -> YAMLMapper()
            ConfigType.PROPERTIES -> JavaPropsMapper()
            ConfigType.TOML -> TomlMapper()
        }.registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
        load()
    }

    constructor(inputStream: InputStream, configType: ConfigType) {
        file = null
        this.inputStream = inputStream
        this.configType = configType
        configSection = ConfigSection()
        jackson = when (configType) {
            ConfigType.JSON -> JsonMapper()
            ConfigType.YAML -> YAMLMapper()
            ConfigType.PROPERTIES -> JavaPropsMapper()
            ConfigType.TOML -> TomlMapper()
        }.registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
        load()
    }

    fun load() {
        (
            if (file == null) {
                inputStream
            } else {
                FileInputStream(file)
            }
            )?.let {
            BufferedInputStream(it).use { reader ->
                configSection = ConfigSection(
                    try {
                        this.jackson.readValue<LinkedHashMap<String, Any>>(reader)
                    } catch (ignored: MismatchedInputException) {
                        emptyMap()
                    },
                )
            }
        }
    }

    fun exists(key: String): Boolean {
        return configSection.exists(key)
    }

    operator fun set(key: String, value: Any) {
        configSection[key] = value
    }

    fun remove(key: String) {
        configSection.remove(key)
        save()
    }

    fun addDefault(key: String, value: Any) {
        if (!exists(key)) {
            this[key] = value
            save()
        }
    }

    operator fun get(key: String): Any? {
        return configSection[key]
    }

    fun getStringList(key: String): MutableList<String> {
        return configSection.getStringList(key)
    }

    fun getString(key: String): String {
        return configSection.getString(key)
    }

    fun getIntegerList(key: String): List<Int> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toInt() }
            else -> configSection.getIntegerList(key)
        }
    }

    fun getInt(key: String): Int {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toInt()
            else -> configSection.getInt(key)
        }
    }

    fun getLongList(key: String): List<Long> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toLong() }
            else -> configSection.getLongList(key)
        }
    }

    fun getLong(key: String): Long {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toLong()
            else -> configSection.getLong(key)
        }
    }

    fun getDoubleList(key: String): List<Double> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toDouble() }
            else -> configSection.getDoubleList(key)
        }
    }

    fun getDouble(key: String): Double {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toDouble()
            else -> configSection.getDouble(key)
        }
    }

    fun getFloatList(key: String): List<Float> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toFloat() }
            else -> configSection.getFloatList(key)
        }
    }

    fun getFloat(key: String): Float {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toFloat()
            else -> configSection.getFloat(key)
        }
    }

    fun getByteList(key: String): List<Byte> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toByte() }
            else -> configSection.getByteList(key)
        }
    }

    fun getByte(key: String): Byte {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toByte()
            else -> configSection.getByte(key)
        }
    }

    fun getShortList(key: String): List<Short> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toShort() }
            else -> configSection.getShortList(key)
        }
    }

    fun getShort(key: String): Short {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toShort()
            else -> configSection.getShort(key)
        }
    }

    fun getBooleanList(key: String): List<Boolean> {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getStringList(key).map { it.toBoolean() }
            else -> configSection.getBooleanList(key)
        }
    }

    fun getBoolean(key: String): Boolean {
        return when (configType) {
            ConfigType.PROPERTIES -> configSection.getString(key).toBoolean()
            else -> configSection.getBoolean(key)
        }
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
        BufferedOutputStream(FileOutputStream(file)).use { writer ->
            jackson.writeValue(writer, configSection)
        }
    }
}
