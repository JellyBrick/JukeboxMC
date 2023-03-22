package org.jukeboxmc.form

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
class Modal(title: String, private val question: String) : Form<Boolean>(title) {
    private var trueButtonText: String? = null
    private var falseButtonText: String? = null
    fun setTrueButtonText(text: String?) {
        trueButtonText = text
        dirty = true
    }

    fun setFalseButtonText(text: String?) {
        falseButtonText = text
        dirty = true
    }

    override val formType: String
        get() = "modal"

    override fun toJSON(): JSONObject {
        // Fast out when cached
        if (cache != null && !dirty) {
            return cache!!
        }

        // Create new JSON view of this form
        val jsonObject = super.toJSON()
        jsonObject["content"] = question
        jsonObject["button1"] = trueButtonText
        jsonObject["button2"] = falseButtonText

        // Cache and return
        cache = jsonObject
        dirty = false
        return cache!!
    }

    override fun parseResponse(json: String): Boolean {
        return json.trim { it <= ' ' } == "true"
    }
}
