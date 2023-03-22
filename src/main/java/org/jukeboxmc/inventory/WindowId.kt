package org.jukeboxmc.inventory

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class WindowId(val id: Int) {
    PLAYER(0), ARMOR(6), CONTAINER(7), COMBINED_INVENTORY(12), CRAFTING_INPUT(13), CRAFTING_OUTPUT(14), ENCHANTMENT_TABLE_INPUT(
        21,
    ),
    ENCHANTMENT_TABLE_MATERIAL(22), HOTBAR(27), INVENTORY(28), OFFHAND(33), CURSOR(58), CREATED_OUTPUT(59), OPEN_CONTAINER(
        60,
    ),
    OFFHAND_DEPRECATED(119), ARMOR_DEPRECATED(120), CURSOR_DEPRECATED(124);

    companion object {
        fun getWindowIdById(windowId: Int): WindowId? {
            for (value in values()) {
                if (value.id == windowId) {
                    return value
                }
            }
            return null
        }
    }
}
