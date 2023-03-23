package org.jukeboxmc.form

/**
 * @author GoMint
 * @version 1.0
 */
class FormResponse {
    private val answers = mutableMapOf<String, Any?>()
    fun addAnswer(id: String, data: Any?) {
        answers[id] = data
    }

    fun getToggle(id: String): Boolean? {
        val value = answers[id]
        if (value != null && value is Boolean) {
            return value
        }
        return null
    }

    fun getStepSlider(id: String): String? {
        val value = answers[id]
        if (value != null && value is String) {
            return value
        }
        return null
    }

    fun getSlider(id: String): Float? {
        val value = answers[id]
        if (value != null && value is Double) {
            return value.toFloat()
        }
        return null
    }

    fun getInput(id: String): String? {
        val value = answers[id]
        if (value != null && value is String) {
            return value
        }
        return null
    }

    fun getDropbox(id: String): String? {
        val value = answers[id]
        if (value != null && value is String) {
            return value
        }
        return null
    }
}
