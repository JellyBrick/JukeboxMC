package org.jukeboxmc.crafting.recipes

import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.UUID
import org.jukeboxmc.item.Item

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ShapelessRecipe : Recipe() {
    private val ingredients: MutableList<ItemDescriptorWithCount> = ObjectArrayList<ItemDescriptorWithCount>()
    private override val outputs: MutableList<ItemData?> = ObjectArrayList()
    override fun getOutputs(): List<ItemData?> {
        return outputs
    }

    fun addIngredient(vararg items: Item): ShapelessRecipe {
        val itemDataList: MutableList<ItemDescriptorWithCount> = ArrayList<ItemDescriptorWithCount>()
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

    override fun doRegister(craftingManager: CraftingManager, recipeId: String?): CraftingData {
        return CraftingData(
            CraftingDataType.SHAPELESS,
            recipeId,
            -1,
            -1,
            -1,
            -1,
            ingredients,
            outputs,
            UUID.randomUUID(),
            "crafting_table",
            1,
            craftingManager.getHighestNetworkId() + 1
        )
    }
}