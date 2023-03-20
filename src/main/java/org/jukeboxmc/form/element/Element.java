package org.jukeboxmc.form.element;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

/**
 * @author GoMint
 * @version 1.0
 */
@RequiredArgsConstructor
public abstract class Element {

    @Getter
    private final @NotNull String id;
    @Getter
    private final @NotNull String text;

    /**
     * Get the JSON representation of a form
     *
     * @return json representation of the form
     */
    public JSONObject toJSON() {
        JSONObject element = new JSONObject();
        element.put( "text", this.text );
        return element;
    }

    /**
     * Get the correct answer object for this form element
     *
     * @param answerOption object given from the client
     * @return correct answer object for the listener
     */
    public Object getAnswer( Object answerOption ) {
        return answerOption;
    }

}