package org.jukeboxmc.form.element

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.jukeboxmc.form.CustomForm

/**
 * @author GoMint
 * @version 1.0
 */
class Dropdown(private val form: CustomForm, id: String, text: String) : Element(id, text) {
    private val options: MutableList<String> = ArrayList()
    private var defaultOption = 0
    fun addOption(option: String): Dropdown {
        options.add(option)
        form.setDirty()
        return this
    }

    fun addOption(option: String, defaultOption: Boolean): Dropdown {
        if (defaultOption) {
            this.defaultOption = options.size
        }
        options.add(option)
        form.setDirty()
        return this
    }

    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        obj["type"] = "dropdown"
        val jsonOptions = JSONArray()
        jsonOptions.addAll(options)
        obj["options"] = jsonOptions
        obj["default"] = defaultOption
        return obj
    }

    override fun getAnswer(answerOption: Any): Any {
        val optionIndex = answerOption as Long
        defaultOption = optionIndex.toInt()
        return options[optionIndex.toInt()]
    }
}
