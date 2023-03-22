package org.jukeboxmc.form

import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
abstract class Form<R> @JvmOverloads constructor(
    val title: String,
    var icon: String = "",
) {

    // Caching
    protected var cache: JSONObject? = null
    protected var dirty = false

    /**
     * Get the type of form we have right here
     *
     * @return type of form
     */
    abstract val formType: String
    fun setIcon(icon: String) {
        this.icon = icon
        dirty = true
    }

    fun getIcon(): String {
        return icon
    }

    /**
     * Get the JSON representation of this form
     *
     * @return ready to be sent JSON
     */
    open fun toJSON(): JSONObject {
        // Basic data
        val obj = JSONObject()
        obj["type"] = formType
        obj["title"] = title
        obj["content"] = JSONArray()

        // Check if we have a icon
        if (icon != null) {
            val jsonIcon = JSONObject()
            jsonIcon["type"] = if (icon!!.startsWith("http") || icon!!.startsWith("https")) "url" else "path"
            jsonIcon["data"] = icon
            obj["icon"] = jsonIcon
        }
        return obj
    }

    /**
     * Parse the given response into the correct listener format
     *
     * @param json data from the client
     * @return correct formatted object for the listener
     */
    abstract fun parseResponse(json: String): R
    fun setDirty() {
        dirty = true
    }
}
