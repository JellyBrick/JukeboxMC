package org.jukeboxmc.crafting.recipes

import com.nukkitx.protocol.bedrock.data.inventory.CraftingData
import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import org.jukeboxmc.crafting.CraftingManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Recipe {
    abstract val outputs: List<ItemData?>
    abstract fun doRegister(craftingManager: CraftingManager, recipeId: String?): CraftingData?
}
