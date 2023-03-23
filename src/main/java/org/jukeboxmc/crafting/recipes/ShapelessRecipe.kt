package org.jukeboxmc.crafting.recipes

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.CraftingRecipeData
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapelessRecipeData
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount
import org.jukeboxmc.crafting.CraftingManager
import org.jukeboxmc.item.Item
import java.util.UUID

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ShapelessRecipe : Recipe() {
    private val ingredients: MutableList<ItemDescriptorWithCount> = ObjectArrayList()
    override val outputs: MutableList<ItemData?> = ObjectArrayList()

    fun addIngredient(vararg items: Item): ShapelessRecipe {
        val itemDataList: MutableList<ItemDescriptorWithCount> = ArrayList()
        for (item in items) {
            itemDataList.add(ItemDescriptorWithCount.fromItem(item.toItemData()))
        }
        ingredients.addAll(itemDataList)
        return this
    }

    fun addOutput(vararg items: Item): ShapelessRecipe {
        val itemDataList: MutableList<ItemData?> = ArrayList()
        for (item in items) {
            itemDataList.add(item.toItemData())
        }
        outputs.addAll(itemDataList)
        return this
    }

    override fun doRegister(craftingManager: CraftingManager, recipeId: String?): CraftingRecipeData {
        return ShapelessRecipeData.of(
            CraftingDataType.SHAPELESS,
            recipeId,
            ingredients,
            outputs,
            UUID.randomUUID(),
            "crafting_table",
            1,
            craftingManager.highestNetworkId + 1,
        )
    }
}
