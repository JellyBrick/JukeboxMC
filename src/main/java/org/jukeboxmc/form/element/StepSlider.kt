package org.jukeboxmc.form.element

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.jukeboxmc.form.CustomForm

/**
 * @author GoMint
 * @version 1.0
 */
class StepSlider(private val form: CustomForm, id: String, text: String) : Element(id, text) {
    private val steps: MutableList<String> = ArrayList()
    private var defaultStep = 0
    fun addStep(step: String): StepSlider {
        steps.add(step)
        form.setDirty()
        return this
    }

    fun addStep(step: String, defaultStep: Boolean): StepSlider? {
        if (defaultStep) {
            this.defaultStep = steps.size
        }
        steps.add(step)
        form.setDirty()
        return null
    }

    override fun toJSON(): JSONObject {
        val obj = super.toJSON()
        obj!!["type"] = "step_slider"
        val jsonSteps = JSONArray()
        jsonSteps.addAll(steps)
        obj["steps"] = jsonSteps
        obj["default"] = defaultStep
        return obj
    }

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as String
        defaultStep = steps.indexOf(answer)
        return answer
    }
}