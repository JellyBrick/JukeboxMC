package org.jukeboxmc.form.element

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
class Label(id: String, text: String) : Element(id, text) {
    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        obj["type"] = "label"
        return obj
    }
}
