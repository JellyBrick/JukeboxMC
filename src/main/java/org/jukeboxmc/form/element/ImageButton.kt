package org.jukeboxmc.form.element

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
class ImageButton(id: String?, text: String?, private val image: String) : Button(id, text) {
    override fun toJSON(): JSONObject {
        val button = super.toJSON()
        val jsonIcon = JSONObject()
        jsonIcon["type"] = if (image.startsWith("http") || image.startsWith("https")) "url" else "path"
        jsonIcon["data"] = image
        button!!["image"] = jsonIcon
        return button
    }
}