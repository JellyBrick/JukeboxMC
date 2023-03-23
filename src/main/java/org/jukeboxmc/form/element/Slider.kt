package org.jukeboxmc.form.element

import com.fasterxml.jackson.annotation.JsonProperty

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
    @JsonProperty("default")
    private var defaultValue: Float,
) : Element(id, text) {
    val type = "slider"

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as Double
        defaultValue = answer.toFloat()
        return answer
    }
}
