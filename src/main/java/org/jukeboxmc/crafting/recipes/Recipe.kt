package org.jukeboxmc.crafting.recipes

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.CraftingRecipeData
import org.jukeboxmc.crafting.CraftingManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Recipe {
    abstract val outputs: List<ItemData?>
    abstract fun doRegister(craftingManager: CraftingManager, recipeId: String?): CraftingRecipeData?
}
