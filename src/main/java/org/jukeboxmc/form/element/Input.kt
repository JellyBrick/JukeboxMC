package org.jukeboxmc.form.element

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
class Input(id: String?, text: String?, private val placeHolder: String, private var defaultValue: String) :
    Element(id, text) {
    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        obj!!["type"] = "input"
        obj["placeholder"] = placeHolder
        obj["default"] = defaultValue
        return obj
    }

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as String
        defaultValue = answer
        return answer
    }
}