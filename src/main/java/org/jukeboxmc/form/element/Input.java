package org.jukeboxmc.form.element;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

/**
 * @author GoMint
 * @version 1.0
 */
public class Input extends Element {

    private String defaultValue;
    private final String placeHolder;

    public Input( String id, String text, String placeHolder, String defaultValue ) {
        super( id, text );
        this.defaultValue = defaultValue;
        this.placeHolder = placeHolder;
    }

    @Override
    public @NotNull JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put( "type", "input" );
        obj.put( "placeholder", this.placeHolder );
        obj.put( "default", this.defaultValue );
        return obj;
    }

    @Override
    public Object getAnswer( Object answerOption ) {
        String answer = (String) answerOption;
        this.defaultValue = answer;
        return answer;
    }
}