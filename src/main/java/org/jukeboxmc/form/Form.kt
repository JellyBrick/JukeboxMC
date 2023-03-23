package org.jukeboxmc.form

import com.fasterxml.jackson.annotation.JsonProperty
/**
 * @author GoMint
 * @version 1.0
 */
abstract class Form<R>(
    val title: String,
) {

    /**
     * Get the type of form we have right here
     *
     * @return type of form
     */
    @get:JsonProperty("type")
    abstract val formType: String

    /**
     * Parse the given response into the correct listener format
     *
     * @param json data from the client
     * @return correct formatted object for the listener
     */
    abstract fun parseResponse(json: String): R
}
