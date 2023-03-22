package org.jukeboxmc.form.element

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
class Slider(
    id: String,
    text: String,
    private val min: Float,
    private val max: Float,
    private val step: Float,
    private var defaultValue: Float,
) : Element(id, text) {
    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        obj["type"] = "slider"
        obj["min"] = min
        obj["max"] = max
        if (step > 0) {
            obj["step"] = step
        }
        if (defaultValue > min) {
            obj["default"] = defaultValue
        }
        return obj
    }

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as Double
        defaultValue = answer.toFloat()
        return answer
    }
}
