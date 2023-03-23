package org.jukeboxmc.form

import org.jukeboxmc.form.element.Button
import org.jukeboxmc.form.element.ImageButton

/**
 * @author GoMint
 * @version 1.0
 */
class ButtonList(title: String) : Form<String?>(title) {
    val buttons = mutableListOf<Button>()
    var content = ""

    fun setContent(content: String): ButtonList {
        this.content = content
        return this
    }

    fun addButton(id: String, text: String): ButtonList {
        val button = Button(id, text)
        buttons.add(button)
        return this
    }

    fun addImageButton(id: String, text: String, imagePath: Icon): ButtonList {
        val imageButton = ImageButton(id, text, imagePath)
        buttons.add(imageButton)
        return this
    }

    override val formType: String
        get() = "form"

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
