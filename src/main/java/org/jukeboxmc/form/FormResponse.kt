package org.jukeboxmc.form

/**
 * @author GoMint
 * @version 1.0
 */
class FormResponse {
    private val answers: MutableMap<String, Any?> = HashMap()
    fun addAnswer(id: String, data: Any?) {
        answers[id] = data
    }

    fun getToggle(id: String): Boolean? {
        val `val` = answers[id]
        if (`val` != null) {
            if (`val` is Boolean) {
                return `val`
            }
        }
        return null
    }

    fun getStepSlider(id: String): String? {
        val `val` = answers[id]
        if (`val` != null) {
            if (`val` is String) {
                return `val`
            }
        }
        return null
    }

    fun getSlider(id: String): Float? {
        val `val` = answers[id]
        if (`val` != null) {
            if (`val` is Double) {
                return `val`.toFloat()
            }
        }
        return null
    }

    fun getInput(id: String): String? {
        val `val` = answers[id]
        if (`val` != null) {
            if (`val` is String) {
                return `val`
            }
        }
        return null
    }

    fun getDropbox(id: String): String? {
        val `val` = answers[id]
        if (`val` != null) {
            if (`val` is String) {
                return `val`
            }
        }
        return null
    }
}