package org.jukeboxmc.form.element

/**
 * @author GoMint
 * @version 1.0
 */
abstract class Element(
    val id: String,
    val text: String,
) {

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
