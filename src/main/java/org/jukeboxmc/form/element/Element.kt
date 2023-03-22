package org.jukeboxmc.form.element

import org.json.simple.JSONObject

/**
 * @author GoMint
 * @version 1.0
 */
abstract class Element(
    val id: String,
    val text: String,
) {

    /**
     * Get the JSON representation of a form
     *
     * @return json representation of the form
     */
    open fun toJSON(): JSONObject {
        val element = JSONObject()
        element["text"] = text
        return element
    }

    /**
     * Get the correct answer object for this form element
     *
     * @param answerOption object given from the client
     * @return correct answer object for the listener
     */
    open fun getAnswer(answerOption: Any): Any {
        return answerOption
    }
}
