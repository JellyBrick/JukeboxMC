package org.jukeboxmc.form

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.jukeboxmc.form.element.Button
import org.jukeboxmc.form.element.ImageButton

/**
 * @author GoMint
 * @version 1.0
 */
class ButtonList(title: String) : Form<String?>(title) {
    private val buttons: MutableList<Button> = ArrayList()
    private var content = ""
    fun setContent(content: String): ButtonList {
        this.content = content
        dirty = true
        return this
    }

    fun addButton(id: String, text: String): ButtonList {
        val button = Button(id, text)
        buttons.add(button)
        dirty = true
        return this
    }

    fun addImageButton(id: String, text: String, imagePath: String): ButtonList {
        val imageButton = ImageButton(id, text, imagePath)
        buttons.add(imageButton)
        dirty = true
        return this
    }

    override val formType: String
        get() = "form"

    override fun toJSON(): JSONObject {
        // Fast out when cached
        if (cache != null && !dirty) {
            return cache!!
        }

        // Create new JSON view of this form
        val jsonObject = super.toJSON()
        val content = JSONArray()
        for (button in buttons) {
            content.add(button.toJSON())
        }
        jsonObject["content"] = this.content
        jsonObject["buttons"] = content

        // Cache and return
        cache = jsonObject
        dirty = false
        return cache!!
    }

    override fun parseResponse(json: String): String? {
        // Input is LF terminated
        return try {
            val buttonId = json.trim { it <= ' ' }.toInt()
            if (buttonId + 1 > buttons.size) {
                null
            } else {
                buttons[buttonId].id
            }
        } catch (e: NumberFormatException) {
            null
        }
    }
}
