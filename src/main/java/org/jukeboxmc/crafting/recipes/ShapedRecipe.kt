package org.jukeboxmc.crafting.recipes

import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.UUID
import org.jukeboxmc.item.Item

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ShapedRecipe : Recipe() {
    private val ingredients: Char2ObjectMap<ItemDescriptorWithCount> = Char2ObjectOpenHashMap<ItemDescriptorWithCount>()
    private override val outputs: MutableList<ItemData?> = ObjectArrayList()
    private var pattern: Array<String>
    override fun getOutputs(): List<ItemData?> {
        return outputs
    }

    fun shape(vararg pattern: String): ShapedRecipe {
        if (pattern.size > 3 || pattern.size < 1) {
            throw RuntimeException("Shape height must be between 3 and 1!")
        }
        val first = pattern[0]
        if (first.length > 3 || first.length < 1) {
            throw RuntimeException("Shape width must be between 3 and 1!")
        }
        val last = first.length
        for (line in pattern) {
            if (line.length != last) {
                throw RuntimeException("Shape colums must be have the same length!")
            }
        }
        this.pattern = pattern
        return this
    }

    fun setIngredient(symbol: Char, item: Item): ShapedRecipe {
        ingredients.put(symbol, ItemDescriptorWithCount.fromItem(item.toItemData()))
        return this
    }

    fun addOutput(vararg items: Item): ShapedRecipe {
        val itemDataList: MutableList<ItemData?> = ArrayList()
        for (item in items) {
            itemDataList.add(item.toItemData())
        }
        outputs.addAll(itemDataList)
        return this
    }

    override fun doRegister(craftingManager: CraftingManager, recipeId: String?): CraftingData? {
        val ingredients: MutableList<ItemDescriptorWithCount> = ArrayList<ItemDescriptorWithCount>()
        for (s in pattern) {
            val chars = s.toCharArray()
            for (c in chars) {
                val ingredient: ItemDescriptorWithCount = this.ingredients.get(c)
                if (c == ' ') {
                    ingredients.add(ItemDescriptorWithCount.EMPTY)
                    continue
                }
                if (ingredient == null) {
                    return null
                }
                ingredients.add(ingredient)
            }
        }
        return CraftingData(
            CraftingDataType.SHAPED,
            recipeId,
            pattern[0].length,
            pattern.size,
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