package org.jukeboxmc.inventory

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class CraftingGridInventory(holder: InventoryHolder?, holderId: Int, size: Int) :
    ContainerInventory(holder, holderId, size) {
    abstract val offset: Int
}