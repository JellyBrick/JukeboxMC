package org.jukeboxmc.form

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jukeboxmc.form.element.Dropdown
import org.jukeboxmc.form.element.Element
import org.jukeboxmc.form.element.Input
import org.jukeboxmc.form.element.Label
import org.jukeboxmc.form.element.Slider
import org.jukeboxmc.form.element.StepSlider
import org.jukeboxmc.form.element.Toggle

/**
 * @author GoMint
 * @version 1.0
 */
class CustomForm(title: String) : Form<FormResponse?>(title) {
    private val elements: MutableList<Element> = ArrayList()
    fun createDropdown(id: String, text: String): Dropdown {
        val dropdown = Dropdown(this, id, text)
        elements.add(dropdown)
        dirty = true
        return dropdown
    }

    fun addInputField(id: String, text: String, placeHolder: String, defaultValue: String): CustomForm {
        val input = Input(id, text, placeHolder, defaultValue)
        elements.add(input)
        dirty = true
        return this
    }

    fun addLabel(text: String): CustomForm {
        val label = Label("", text)
        elements.add(label)
        dirty = true
        return this
    }

    fun addSlider(id: String, text: String, min: Float, max: Float, step: Float, defaultValue: Float): CustomForm {
        val slider = Slider(id, text, min, max, step, defaultValue)
        elements.add(slider)
        dirty = true
        return this
    }

    fun createStepSlider(id: String, text: String): StepSlider {
        val stepSlider = StepSlider(this, id, text)
        elements.add(stepSlider)
        dirty = true
        return stepSlider
    }

    fun addToggle(id: String, text: String, value: Boolean): CustomForm {
        val toggle = Toggle(id, text, value)
        elements.add(toggle)
        dirty = true
        return this
    }

    override val formType: String
        get() = "custom_form"

    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        val content = obj["content"] as JSONArray
        for (element in elements) {
            content.add(element.toJSON())
        }
        return obj
    }

    override fun parseResponse(json: String): FormResponse {
        // Response is an array with values
        val response = FormResponse()
        val answers = JSONParser().parse(json.trim { it <= ' ' }) as JSONArray
        for (i in answers.indices) {
            val element = elements[i]
            response.addAnswer(element.id, element.getAnswer(answers[i]!!))
        }
        return response
    }
}
