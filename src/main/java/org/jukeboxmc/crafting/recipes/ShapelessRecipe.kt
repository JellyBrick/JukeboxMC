package org.jukeboxmc.crafting.recipes

import com.nukkitx.protocol.bedrock.data.inventory.CraftingData
import com.nukkitx.protocol.bedrock.data.inventory.CraftingDataType
import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import com.nukkitx.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.jukeboxmc.crafting.CraftingManager
import org.jukeboxmc.item.Item
import java.util.UUID

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ShapelessRecipe : Recipe() {
    private val ingredients: MutableList<ItemDescriptorWithCount> = ObjectArrayList<ItemDescriptorWithCount>()
    override val outputs: MutableList<ItemData?> = ObjectArrayList()

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
            craftingManager.highestNetworkId + 1,
        )
    }
}
