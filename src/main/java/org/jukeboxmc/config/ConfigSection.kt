package org.jukeboxmc.config

import java.util.Locale
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * @author LucGamesYT, NukkitX
 * @version 1.0
 */
class ConfigSection() : LinkedHashMap<String?, Any?>() {
    constructor(map: LinkedHashMap<String?, Any>?) : this() {
        if (map == null || map.isEmpty()) return
        for ((key, value) in map) {
            if (value is LinkedHashMap<*, *>) {
                super.put(key, ConfigSection(value as LinkedHashMap<String?, Any>))
            } else if (value is List<*>) {
                super.put(key, parseList(value as List<Any>))
            } else {
                super.put(key, value)
            }
        }
    }

    private fun parseList(list: List<Any>): List<Any> {
        val newList: MutableList<Any> = ArrayList()
        for (o in list) {
            if (o is LinkedHashMap<*, *>) {
                newList.add(ConfigSection(o as LinkedHashMap<String?, Any>))
            } else {
                newList.add(o)
            }
        }
        return newList
    }

    val allMap: Map<String, Any>
        get() = LinkedHashMap(this)
    val all: ConfigSection
        get() = ConfigSection(this)

    operator fun <T> get(key: String?, defaultValue: T?): T? {
        if (key == null || key.isEmpty()) {
            return defaultValue
        }
        if (super.containsKey(key)) {
            return super.get(key) as T?
        }
        val keys = key.split("\\.".toRegex(), limit = 2).toTypedArray()
        if (!super.containsKey(keys[0])) {
            return defaultValue
        }
        val value = super.get(keys[0])
        return if (value is ConfigSection) {
            value.get<T?>(keys[1], defaultValue)
        } else defaultValue
    }

    override fun get(key: String?): Any? {
        return this.get<Any?>(key, null)
    }

    operator fun set(key: String, value: Any?) {
        val subKeys = key.split("\\.".toRegex(), limit = 2).toTypedArray()
        if (subKeys.size > 1) {
            var childSection: ConfigSection? = ConfigSection()
            if (this.containsKey(subKeys[0]) && super.get(subKeys[0]) is ConfigSection) {
                childSection = super.get(subKeys[0]) as ConfigSection?
            }
            childSection!![subKeys[1]] = value
            super.put(subKeys[0], childSection)
        } else super.put(subKeys[0], value)
    }

    fun isSection(key: String?): Boolean {
        val value = this[key]
        return value is ConfigSection
    }

    fun getSection(key: String?): ConfigSection {
        return this.get(key, ConfigSection())!!
    }

    val sections: ConfigSection
        get() = getSections(null)

    fun getSections(key: String?): ConfigSection {
        val sections = ConfigSection()
        val parent = (if (key == null || key.isEmpty()) all else getSection(key)) ?: return sections
        parent.forEach(BiConsumer { key1: String, value: Any? ->
            if (value is ConfigSection) sections.put(
                key1,
                value
            )
        })
        return sections
    }

    fun getInt(key: String?): Int {
        return this.getInt(key, 0)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return this.get(key, defaultValue as Number)!!.toInt()
    }

    fun isInt(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Int
    }

    fun getLong(key: String?): Long {
        return this.getLong(key, 0)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return this.get(key, defaultValue as Number)!!.toLong()
    }

    fun isLong(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Long
    }

    fun getDouble(key: String?): Double {
        return this.getDouble(key, 0.0)
    }

    fun getDouble(key: String?, defaultValue: Double): Double {
        return this.get(key, defaultValue as Number)!!.toDouble()
    }

    fun isDouble(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Double
    }

    fun getString(key: String?): String {
        return this.getString(key, "")
    }

    fun getString(key: String?, defaultValue: String): String {
        val result: Any = this.get(key, defaultValue)!!
        return result.toString()
    }

    fun isString(key: String?): Boolean {
        val `val` = get(key)
        return `val` is String
    }

    fun getBoolean(key: String?): Boolean {
        return this.getBoolean(key, false)
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return this.get(key, defaultValue)!!
    }

    fun isBoolean(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Boolean
    }

    fun getFloat(key: String?): Float {
        return this.getFloat(key, 0f)
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return this.get(key, defaultValue as Number)!!.toFloat()
    }

    fun isFloat(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Float
    }

    fun getByte(key: String?): Byte {
        return this.getByte(key, 0.toByte())
    }

    fun getByte(key: String?, defaultValue: Byte): Byte {
        return this.get(key, defaultValue as Number)!!.toByte()
    }

    fun isByte(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Byte
    }

    fun getShort(key: String?): Short {
        return this.getShort(key, 0.toShort())
    }

    fun getShort(key: String?, defaultValue: Short): Short {
        return this.get(key, defaultValue as Number)!!.toShort()
    }

    fun isShort(key: String?): Boolean {
        val `val` = get(key)
        return `val` is Short
    }

    fun getList(key: String?): List<*> {
        return this.getList(key, null)
    }

    fun getList(key: String?, defaultList: List<*>?): List<*> {
        return this[key, defaultList]!!
    }

    fun isList(key: String?): Boolean {
        val `val` = get(key)
        return `val` is List<*>
    }

    fun getStringList(key: String?): MutableList<String> {
        val value = this.getList(key) ?: return ArrayList(0)
        val result: MutableList<String> = ArrayList()
        for (o in value) {
            if (o is String || o is Number || o is Boolean || o is Char) {
                result.add(o.toString())
            }
        }
        return result
    }

    fun getIntegerList(key: String?): List<Int> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Int> = ArrayList()
        for (`object` in list) {
            if (`object` is Int) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(Integer.valueOf(`object` as String?))
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code)
            } else if (`object` is Number) {
                result.add(`object`.toInt())
            }
        }
        return result
    }

    fun getBooleanList(key: String?): List<Boolean> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Boolean> = ArrayList()
        for (`object` in list) {
            if (`object` is Boolean) {
                result.add(`object`)
            } else if (`object` is String) {
                if (java.lang.Boolean.TRUE.toString() == `object`) {
                    result.add(true)
                } else if (java.lang.Boolean.FALSE.toString() == `object`) {
                    result.add(false)
                }
            }
        }
        return result
    }

    fun getDoubleList(key: String?): List<Double> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Double> = ArrayList()
        for (`object` in list) {
            if (`object` is Double) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(java.lang.Double.valueOf(`object` as String?))
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toDouble())
            } else if (`object` is Number) {
                result.add(`object`.toDouble())
            }
        }
        return result
    }

    fun getFloatList(key: String?): List<Float> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Float> = ArrayList()
        for (`object` in list) {
            if (`object` is Float) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(java.lang.Float.valueOf(`object` as String?))
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toFloat())
            } else if (`object` is Number) {
                result.add(`object`.toFloat())
            }
        }
        return result
    }

    fun getLongList(key: String?): List<Long> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Long> = ArrayList()
        for (`object` in list) {
            if (`object` is Long) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(java.lang.Long.valueOf(`object` as String?))
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toLong())
            } else if (`object` is Number) {
                result.add(`object`.toLong())
            }
        }
        return result
    }

    fun getByteList(key: String?): List<Byte> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Byte> = ArrayList()
        for (`object` in list) {
            if (`object` is Byte) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(java.lang.Byte.valueOf(`object` as String?))
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toByte())
            } else if (`object` is Number) {
                result.add(`object`.toByte())
            }
        }
        return result
    }

    fun getCharacterList(key: String?): List<Char> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Char> = ArrayList()
        for (`object` in list) {
            if (`object` is Char) {
                result.add(`object`)
            } else if (`object` is String) {
                if (`object`.length == 1) {
                    result.add(`object`[0])
                }
            } else if (`object` is Number) {
                result.add(`object`.toInt().toChar())
            }
        }
        return result
    }

    fun getShortList(key: String?): List<Short> {
        val list = getList(key) ?: return ArrayList(0)
        val result: MutableList<Short> = ArrayList()
        for (`object` in list) {
            if (`object` is Short) {
                result.add(`object`)
            } else if (`object` is String) {
                try {
                    result.add(`object`.toShort())
                } catch (ex: Exception) {
                    //ignore
                }
            } else if (`object` is Char) {
                result.add(`object`.code.toShort())
            } else if (`object` is Number) {
                result.add(`object`.toShort())
            }
        }
        return result
    }

    fun getMapList(key: String?): List<Map<String, Any>> {
        val list = getList(key)
        val result: MutableList<Map<String, Any>> = ArrayList()
        if (list == null) {
            return result
        }
        for (`object` in list) {
            if (`object` is Map<*, *>) {
                result.add(`object` as Map<String, Any>)
            }
        }
        return result
    }

    @JvmOverloads
    fun exists(key: String, ignoreCase: Boolean = false): Boolean {
        var key = key
        if (ignoreCase) key = key.lowercase(Locale.getDefault())
        for (existKey in getKeys(true)) {
            if (ignoreCase) existKey = existKey.lowercase(Locale.getDefault())
            if (existKey == key) return true
        }
        return false
    }

    override fun remove(key: String?) {
        if (key == null || key.isEmpty()) return
        if (super.containsKey(key)) super.remove(key) else if (this.containsKey(".")) {
            val keys = key.split("\\.".toRegex(), limit = 2).toTypedArray()
            if (super.get(keys[0]) is ConfigSection) {
                section.remove(keys[1])
            }
        }
    }

    fun getKeys(child: Boolean): Set<String> {
        val keys: MutableSet<String> = LinkedHashSet()
        this.forEach(BiConsumer { key: String, value: Any? ->
            keys.add(key)
            if (value is ConfigSection) {
                if (child) value.getKeys(true).forEach(Consumer { childKey: String ->
                    keys.add(
                        "$key.$childKey"
                    )
                })
            }
        })
        return keys
    }

    override val keys: Set<String>
        get() = getKeys(true)
}