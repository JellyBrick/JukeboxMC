package org.jukeboxmc.form.element

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
class Toggle(id: String?, text: String?, private var value: Boolean) : Element(id, text) {
    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        obj!!["type"] = "toggle"
        obj["default"] = value
        return obj
    }

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as Boolean
        value = answer
        return answer
    }
}