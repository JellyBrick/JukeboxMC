package org.jukeboxmc.form

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author GoMint
 * @version 1.0
 */
class Modal(
    title: String,
    @get:JsonProperty("content")
    val question: String,
) : Form<Boolean>(title) {
    @JsonProperty("button1")
    var trueButtonText: String? = null

    @JsonProperty("button2")
    var falseButtonText: String? = null

    override val formType: String
        get() = "modal"

    override fun parseResponse(json: String): Boolean {
        return json.trim { it <= ' ' } == "true"
    }
}
