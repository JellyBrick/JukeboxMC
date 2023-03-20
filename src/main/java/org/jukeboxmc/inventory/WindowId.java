package org.jukeboxmc.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public enum WindowId {

    PLAYER( 0 ),
    ARMOR( 6 ),
    CONTAINER( 7 ),
    COMBINED_INVENTORY( 12 ),
    CRAFTING_INPUT( 13 ),
    CRAFTING_OUTPUT( 14 ),
    ENCHANTMENT_TABLE_INPUT( 21 ),
    ENCHANTMENT_TABLE_MATERIAL( 22 ),
    HOTBAR( 27 ),
    INVENTORY( 28 ),
    OFFHAND( 33 ),
    CURSOR( 58 ),
    CREATED_OUTPUT( 59 ),
    OPEN_CONTAINER( 60 ),
    OFFHAND_DEPRECATED( 119 ),
    ARMOR_DEPRECATED( 120 ),
    CURSOR_DEPRECATED( 124 );

    private final int id;

    WindowId( int id ) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static @Nullable WindowId getWindowIdById(int windowId ) {
        for ( WindowId value : values() ) {
            if ( value.getId() == windowId ) {
                return value;
            }
        }
        return null;
    }
}
