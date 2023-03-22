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
        val value = answers[id]
        if (value != null) {
            if (value is Boolean) {
                return value
            }
        }
        return null
    }

    fun getStepSlider(id: String): String? {
        val value = answers[id]
        if (value != null) {
            if (value is String) {
                return value
            }
        }
        return null
    }

    fun getSlider(id: String): Float? {
        val value = answers[id]
        if (value != null) {
            if (value is Double) {
                return value.toFloat()
            }
        }
        return null
    }

    fun getInput(id: String): String? {
        val value = answers[id]
        if (value != null) {
            if (value is String) {
                return value
            }
        }
        return null
    }

    fun getDropbox(id: String): String? {
        val value = answers[id]
        if (value != null) {
            if (value is String) {
                return value
            }
        }
        return null
    }
}
