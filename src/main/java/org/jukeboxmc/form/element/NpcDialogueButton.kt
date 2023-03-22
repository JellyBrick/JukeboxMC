package org.jukeboxmc.form.element

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * @author Kaooot
 * @version 1.0
 */
class NpcDialogueButton {
    var text: String? = null
    var commands: List<String>? = null
    var mode: ButtonMode? = null
    var click: Runnable? = null

    enum class ButtonMode {
        BUTTON_MODE, ON_ENTER, ON_EXIT
    }

    fun toJsonObject(): JsonObject {
        val button = JsonObject()
        button.addProperty("button_name", text)
        val data = JsonArray()
        for (command in commands!!) {
            val cmdLine = JsonObject()
            cmdLine.addProperty("cmd_line", command)
            cmdLine.addProperty("cmd_ver", 19)
            data.add(cmdLine)
        }
        button.add("data", data)
        button.addProperty("mode", mode!!.ordinal)
        button.addProperty("text", "")
        button.addProperty("type", 1)
        return button
    }
}
