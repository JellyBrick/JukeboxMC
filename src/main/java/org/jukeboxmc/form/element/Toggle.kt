package org.jukeboxmc.form.element

/**
 * @author GoMint
 * @version 1.0
 */
class Toggle(id: String, text: String, var default: Boolean) : Element(id, text) {
    val type = "toggle"

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as Boolean
        default = answer
        return answer
    }
}
