package org.jukeboxmc.form

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import org.jukeboxmc.form.element.Dropdown
import org.jukeboxmc.form.element.Element
import org.jukeboxmc.form.element.Input
import org.jukeboxmc.form.element.Label
import org.jukeboxmc.form.element.Slider
import org.jukeboxmc.form.element.StepSlider
import org.jukeboxmc.form.element.Toggle
import org.jukeboxmc.util.Utils

/**
 * @author GoMint
 * @version 1.0
 */
class CustomForm(title: String) : Form<FormResponse?>(title) {
    @JsonProperty("content")
    val elements = mutableListOf<Element>()
    fun createDropdown(id: String, text: String): Dropdown {
        val dropdown = Dropdown(id, text)
        elements.add(dropdown)
        return dropdown
    }

    fun addInputField(id: String, text: String, placeHolder: String, defaultValue: String): CustomForm {
        val input = Input(id, text, placeHolder, defaultValue)
        elements.add(input)
        return this
    }

    fun addLabel(text: String): CustomForm {
        val label = Label("", text)
        elements.add(label)
        return this
    }

    fun addSlider(id: String, text: String, min: Float, max: Float, step: Float, defaultValue: Float): CustomForm {
        val slider = Slider(id, text, min, max, step, defaultValue)
        elements.add(slider)
        return this
    }

    fun createStepSlider(id: String, text: String): StepSlider {
        val stepSlider = StepSlider(id, text)
        elements.add(stepSlider)
        return stepSlider
    }

    fun addToggle(id: String, text: String, value: Boolean): CustomForm {
        val toggle = Toggle(id, text, value)
        elements.add(toggle)
        return this
    }

    override val formType: String
        get() = "custom_form"

    override fun parseResponse(json: String): FormResponse {
        // Response is an array with values
        val response = FormResponse()
        val answers = Utils.jackson.readValue<List<Any>>(json.trim { it <= ' ' })
        for (i in answers.indices) {
            val element = elements[i]
            response.addAnswer(element.id, element.getAnswer(answers[i]))
        }
        return response
    }
}
