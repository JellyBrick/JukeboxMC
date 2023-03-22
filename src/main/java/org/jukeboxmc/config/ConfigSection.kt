package org.jukeboxmc.config

import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

/**
 * @author LucGamesYT, NukkitX
 * @version 1.0
 */
class ConfigSection() : LinkedHashMap<String, Any>() {
    constructor(map: Map<*, *>) : this() {
        if (map.isEmpty()) return
        map.forEach { (key, value) ->
            when (value) {
                is LinkedHashMap<*, *> -> super.put(key.toString(), ConfigSection(value))
                is List<*> -> super.put(key.toString(), parseList(value))
                else -> value?.let {
                    super.put(key.toString(), it)
                }
            }
        }
    }

    private fun parseList(list: List<*>): List<Any?> {
        val newList = mutableListOf<Any?>()
        list.forEach {
            if (it is LinkedHashMap<*, *>) {
                newList.add(ConfigSection(it))
            } else {
                newList.add(it)
            }
        }
        return newList
    }

    val allMap: MutableMap<String?, Any?>
        get() = LinkedHashMap(this)
    val all: ConfigSection
        get() = ConfigSection(this)

    fun getAny(key: String, defaultValue: Any): Any {
        if (key.isEmpty()) {
            return defaultValue
        }
        if (super.containsKey(key)) {
            return super.get(key)!!
        }
        val keys = key.split('.', limit = 2)
        if (!super.containsKey(keys[0])) {
            return defaultValue
        }
        val value = super.get(keys[0])
        return if (value is ConfigSection) {
            value[keys[1], defaultValue]
        } else {
            defaultValue
        }
    }

    inline operator fun <reified T> get(key: String, defaultValue: T): T =
        this.getAny(key, defaultValue as Any) as T

    override fun get(key: String): Any? {
        return this[key, null]
    }

    inline fun <reified T> getTyped(key: String): T = get(key) as T

    operator fun set(key: String, value: Any) {
        val subKeys = key.split('.', limit = 2)
        if (subKeys.size > 1) {
            var childSection = ConfigSection()
            if (this.containsKey(subKeys[0]) && super.get(subKeys[0]) is ConfigSection) {
                childSection = super.get(subKeys[0]) as ConfigSection
            }
            childSection[subKeys[1]] = value
            super.put(subKeys[0], childSection)
        } else {
            super.put(subKeys[0], value)
        }
    }

    fun isSection(key: String): Boolean {
        val value = this[key]
        return value is ConfigSection
    }

    fun getSection(key: String): ConfigSection {
        return this.get<ConfigSection>(key, ConfigSection())
    }

    val sections: ConfigSection
        get() = getSections(null)

    fun getSections(key: String?): ConfigSection {
        val sections = ConfigSection()
        val parent = if (key.isNullOrEmpty()) {
            all
        } else {
            getSection(key)
        }

        parent.forEach { key1, value ->
            if (value is ConfigSection) {
                sections[key1] = value
            }
        }
        return sections
    }

    fun getInt(key: String): Int {
        return this.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return get<Int>(key, defaultValue)
    }

    fun isInt(key: String): Boolean {
        return get(key) is Int
    }

    fun getLong(key: String): Long {
        return this.getLong(key, 0)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return get<Long>(key, defaultValue)
    }

    fun isLong(key: String): Boolean {
        return get(key) is Long
    }

    fun getDouble(key: String): Double {
        return this.getDouble(key, 0.0)
    }

    fun getDouble(key: String, defaultValue: Double): Double {
        return get<Double>(key, defaultValue)
    }

    fun isDouble(key: String): Boolean {
        return get(key) is Double
    }

    fun getString(key: String): String {
        return this.getString(key, "")
    }

    fun getString(key: String, defaultValue: String): String {
        return this[key, defaultValue].toString()
    }

    fun isString(key: String): Boolean {
        return get(key) is String
    }

    fun getBoolean(key: String): Boolean {
        return this.getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return get<Boolean>(key, defaultValue)
    }

    fun isBoolean(key: String): Boolean {
        return get(key) is Boolean
    }

    fun getFloat(key: String): Float {
        return this.getFloat(key, 0f)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return get<Float>(key, defaultValue)
    }

    fun isFloat(key: String): Boolean {
        return get(key) is Float
    }

    fun getByte(key: String): Byte {
        return this.getByte(key, 0.toByte())
    }

    fun getByte(key: String, defaultValue: Byte): Byte {
        return get<Byte>(key, defaultValue)
    }

    fun isByte(key: String): Boolean {
        return get(key) is Byte
    }

    fun getShort(key: String): Short {
        return this.getShort(key, 0)
    }

    fun getShort(key: String, defaultValue: Short): Short {
        return get<Short>(key, defaultValue)
    }

    fun isShort(key: String): Boolean {
        val value = get(key)
        return value is Short
    }

    fun getList(key: String): List<*> {
        return this.getList(key, emptyList<Any>())
    }

    fun getList(key: String, defaultList: List<*>): List<*> {
        return get<List<*>>(key, defaultList)
    }

    fun isList(key: String): Boolean {
        return get(key) is List<*>
    }

    fun getStringList(key: String): MutableList<String> {
        val value = this.getList(key)
        val result = mutableListOf<String>()
        value.forEach {
            if (it is String || it is Number || it is Boolean || it is Char) {
                result.add(it.toString())
            }
        }
        return result
    }

    fun getIntegerList(key: String): List<Int> {
        val list = getList(key)
        val result = mutableListOf<Int>()
        list.forEach {
            when (it) {
                is Int -> result.add(it)
                is String -> try {
                    result.add(it.toInt())
                } catch (ex: NumberFormatException) {
                    // ignore
                }
                is Char -> result.add(it.code)
                is Number -> result.add(it.toInt())
            }
        }
        return result
    }

    fun getBooleanList(key: String): List<Boolean> {
        val list = getList(key)
        val result = mutableListOf<Boolean>()
        list.forEach {
            when (it) {
                is Boolean -> result.add(it)
                is String -> if (TRUE.toString() == it) {
                    result.add(true)
                } else if (FALSE.toString() == it) {
                    result.add(false)
                }
            }
        }
        return result
    }

    fun getDoubleList(key: String): List<Double> {
        val list = getList(key)
        val result = mutableListOf<Double>()
        list.forEach {
            when (it) {
                is Double -> result.add(it)
                is String -> try {
                    result.add(it.toDouble())
                } catch (ex: NumberFormatException) {
                    // ignore
                }
                is Char -> result.add(it.code.toDouble())
                is Number -> result.add(it.toDouble())
            }
        }
        return result
    }

    fun getFloatList(key: String): List<Float> {
        val list = getList(key)
        val result = mutableListOf<Float>()
        list.forEach {
            when (it) {
                is Float -> result.add(it)
                is String -> try {
                    result.add(it.toFloat())
                } catch (ex: NumberFormatException) {
                    // ignore
                }
                is Char -> result.add(it.code.toFloat())
                is Number -> result.add(it.toFloat())
            }
        }
        return result
    }

    fun getLongList(key: String): List<Long> {
        val list = getList(key)
        val result = mutableListOf<Long>()
        list.forEach {
            when (it) {
                is Long -> result.add(it)
                is String -> try {
                    result.add(it.toLong())
                } catch (ex: NumberFormatException) {
                    // ignore
                }
                is Char -> result.add(it.code.toLong())
                is Number -> result.add(it.toLong())
            }
        }
        return result
    }

    fun getByteList(key: String): List<Byte> {
        val list = getList(key)
        val result = mutableListOf<Byte>()
        list.forEach {
            when (it) {
                is Byte -> result.add(it)
                is String -> try {
                    result.add(it.toByte())
                } catch (ex: Exception) {
                    // ignore
                }
                is Char -> result.add(it.code.toByte())
                is Number -> result.add(it.toByte())
            }
        }
        return result
    }

    fun getCharacterList(key: String): List<Char> {
        val list = getList(key)
        val result = mutableListOf<Char>()
        list.forEach {
            when (it) {
                is Char -> result.add(it)
                is String -> if (it.length == 1) {
                    result.add(it[0])
                }
                is Number -> result.add(it.toInt().toChar())
            }
        }
        return result
    }

    fun getShortList(key: String): List<Short> {
        val list = getList(key)
        val result = mutableListOf<Short>()
        list.forEach {
            when (it) {
                is Short -> result.add(it)
                is String -> try {
                    result.add(it.toShort())
                } catch (ex: NumberFormatException) {
                    // ignore
                }
                is Char -> result.add(it.code.toShort())
                is Number -> result.add(it.toShort())
            }
        }
        return result
    }

    fun getMapList(key: String): List<Map<*, *>> {
        val list = getList(key)
        val result = mutableListOf<Map<*, *>>()
        if (list.isEmpty()) {
            return result
        }
        list.forEach {
            if (it is Map<*, *>) {
                result.add(it)
            }
        }
        return result
    }

    @JvmOverloads
    fun exists(key: String, ignoreCase: Boolean = false): Boolean {
        getKeys(true).forEach {
            if (key.equals(it, ignoreCase)) {
                return true
            }
        }
        return false
    }

    override fun remove(key: String) {
        if (key.isEmpty()) return
        if (super.containsKey(key)) {
            super.remove(key)
        } else if (this.containsKey(".")) {
            val keys = key.split('.', limit = 2)
            if (super.get(keys[0]) is ConfigSection) {
                val section = super.get(keys[0]) as ConfigSection
                section.remove(keys[1])
            }
        }
    }

    fun getKeys(child: Boolean): MutableSet<String> {
        val keys = linkedSetOf<String>()
        this.forEach { (key, value) ->
            keys.add(key)
            if (value is ConfigSection && child) {
                value.getKeys(true).forEach { childKey ->
                    keys.add(
                        "$key.$childKey",
                    )
                }
            }
        }
        return keys
    }

    override val keys: MutableSet<String>
        get() = getKeys(true)
}
