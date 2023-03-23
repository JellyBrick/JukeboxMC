package org.jukeboxmc.form.element

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author GoMint
 * @version 1.0
 */
class StepSlider(id: String, text: String) : Element(id, text) {
    val steps = mutableListOf<String>()

    @JsonProperty("default")
    private var defaultStep = 0
    val type = "step_slider"
    fun addStep(step: String): StepSlider {
        steps.add(step)
        return this
    }

    fun addStep(step: String, defaultStep: Boolean): StepSlider {
        if (defaultStep) {
            this.defaultStep = steps.size
        }
        steps.add(step)
        return this
    }

    override fun getAnswer(answerOption: Any): Any {
        val answer = answerOption as String
        defaultStep = steps.indexOf(answer)
        return answer
    }
}
