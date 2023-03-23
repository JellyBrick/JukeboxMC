package org.jukeboxmc.form.element

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author GoMint
 * @version 1.0
 */
class Input(
    id: String,
    text: String,
    @JsonProperty("placeholder")
    val placeHolder: String,
    @JsonProperty("default")
    var defaultValue: String,
) :
    Element(id, text) {
    val type = "input"

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as String
        defaultValue = answer
        return answer
    }
}
