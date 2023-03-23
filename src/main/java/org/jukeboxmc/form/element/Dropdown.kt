package org.jukeboxmc.form.element

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author GoMint
 * @version 1.0
 */
class Dropdown(
    id: String,
    text: String,
) : Element(id, text) {
    val options = mutableListOf<String>()

    @JsonProperty("default")
    var defaultOption = 0
    val type = "dropdown"

    override fun getAnswer(answerOption: Any): Any {
        val optionIndex = answerOption as Long
        defaultOption = optionIndex.toInt()
        return options[optionIndex.toInt()]
    }
}
