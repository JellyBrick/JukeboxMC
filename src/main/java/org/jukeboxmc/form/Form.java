package org.jukeboxmc.form;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author GoMint
 * @version 1.0
 */
@RequiredArgsConstructor
public abstract class Form<R> {

    private final @NotNull String title;
    private @Nullable String icon = null;

    // Caching
    protected JSONObject cache;
    protected boolean dirty;

    /**
     * Get the type of form we have right here
     *
     * @return type of form
     */
    public abstract String getFormType();

    public void setIcon( String icon ) {
        this.icon = icon;
        this.dirty = true;
    }

    public @Nullable String getIcon() {
        return this.icon;
    }

    public @NotNull String getTitle() {
        return this.title;
    }

    /**
     * Get the JSON representation of this form
     *
     * @return ready to be sent JSON
     */
    public JSONObject toJSON() {
        // Basic data
        JSONObject obj = new JSONObject();
        obj.put( "type", this.getFormType() );
        obj.put( "title", this.getTitle() );
        obj.put( "content", new JSONArray() );

        // Check if we have a icon
        if ( this.icon != null ) {
            JSONObject jsonIcon = new JSONObject();
            jsonIcon.put( "type", this.icon.startsWith( "http" ) || this.icon.startsWith( "https" ) ? "url" : "path" );
            jsonIcon.put( "data", this.icon );
            obj.put( "icon", jsonIcon );
        }

        return obj;
    }

    /**
     * Parse the given response into the correct listener format
     *
     * @param json data from the client
     * @return correct formatted object for the listener
     */
    public abstract R parseResponse( String json );

    public void setDirty() {
        this.dirty = true;
    }

}