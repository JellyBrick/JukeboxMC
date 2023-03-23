package org.jukeboxmc.form.element

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author Kaooot
 * @version 1.0
 */
class NpcDialogueButton @JvmOverloads constructor(
    @JsonProperty("button_name")
    val name: String,
    val text: String,
    val data: List<DialogueData>?,
    val mode: ButtonMode,
    @JsonIgnore
    val onClick: (() -> Unit)? = null,
    val type: DialogueButtonType = DialogueButtonType.COMMAND,
) {

    enum class ButtonMode(@JsonValue val code: Int) {
        BUTTON(0),
        ON_ENTER(1),
        ON_EXIT(2),
        ;

        companion object {
            @JvmStatic
            @JsonCreator
            fun set(code: Int) = values().find { it.code == code }
        }
    }

    enum class DialogueButtonType(@JsonValue val code: Int) {
        URL(0),
        COMMAND(1),
        UNKNOWN(2),
        ;

        companion object {
            @JvmStatic
            @JsonCreator
            fun set(code: Int) = ButtonMode.values().find { it.code == code }
        }
    }

    class DialogueData @JvmOverloads constructor(
        @JsonProperty("cmd_line")
        val command: String,
        @JsonProperty("cmd_ver")
        val version: Int = COMMAND_VERSION,
    )

    companion object {
        const val COMMAND_VERSION = 19
    }
}
