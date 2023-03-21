package org.jukeboxmc.crafting.recipes

import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import lombok.RequiredArgsConstructor

/**
 * @author LucGamesYT
 * @version 1.0
 */
@RequiredArgsConstructor
abstract class Recipe {
    abstract val outputs: List<ItemData?>
    abstract fun doRegister(craftingManager: CraftingManager, recipeId: String?): CraftingData?
}