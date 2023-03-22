package org.jukeboxmc.crafting

import com.nukkitx.protocol.bedrock.data.inventory.ContainerMixData
import com.nukkitx.protocol.bedrock.data.inventory.CraftingData
import com.nukkitx.protocol.bedrock.data.inventory.CraftingDataType
import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import com.nukkitx.protocol.bedrock.data.inventory.PotionMixData
import com.nukkitx.protocol.bedrock.data.inventory.descriptor.InvalidDescriptor
import com.nukkitx.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount
import com.nukkitx.protocol.bedrock.data.inventory.descriptor.ItemTagDescriptor
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.jukeboxmc.Server
import org.jukeboxmc.config.Config
import org.jukeboxmc.config.ConfigType
import org.jukeboxmc.crafting.recipes.Recipe
import org.jukeboxmc.crafting.recipes.SmeltingRecipe
import org.jukeboxmc.item.Item
import org.jukeboxmc.util.ItemPalette
import java.util.LinkedList
import java.util.Optional
import java.util.UUID

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CraftingManager {
    val craftingData: MutableList<CraftingData> = ObjectArrayList()
    val potionMixData: MutableList<PotionMixData> = ObjectArrayList()
    val containerMixData: MutableList<ContainerMixData> = ObjectArrayList()
    val smeltingRecipes: MutableSet<SmeltingRecipe?> = HashSet()
    private val recipes: Set<Recipe> = HashSet()

    init {
        (
            Server::class.java.classLoader.getResourceAsStream("recipes.json")
                ?: throw AssertionError("Unable to find recipes.json")
            ).use { recipesStream ->
            val config = Config(recipesStream, ConfigType.JSON)
            config.map.getTyped<List<Map<String, *>>>("recipes").forEach { recipe ->
                val craftingDataType: CraftingDataType = CraftingDataType.valueOf(recipe["type"] as String)
                val recipeId = recipe["recipeId"] as String?
                val width = recipe["width"] as Int
                val height = recipe["height"] as Int
                val inputId = recipe["inputId"] as Int
                val inputDamage = recipe["inputDamage"] as Int
                val inputItems = mutableListOf<ItemDescriptorWithCount>()
                if (recipe.containsKey("inputs")) {
                    (recipe["inputs"] as List<*>).forEach {
                        val input = it as Map<*, *>
                        if (input.containsKey("descriptor")) {
                            (input["descriptor"] as List<*>).forEach { anyMap ->
                                val map = anyMap as Map<*, *>
                                val type = map["descriptorType"] as String?
                                if (type.equals("DEFAULT", ignoreCase = true)) {
                                    val id = map["id"] as Int
                                    val damage = map["damage"] as Int
                                    val count = map["count"] as Int
                                    val canPlace = map["canPlace"] as List<String>
                                    val canBreak = map["canBreak"] as List<String>
                                    val blockingTicks = map["blockingTicks"] as Int
                                    val blockRuntimeId = map["blockRuntimeId"] as Int
                                    val usingNetId = map["usingNetId"] as Boolean
                                    val netId = map["netId"] as Int
                                    inputItems.add(
                                        ItemDescriptorWithCount.fromItem(
                                            ItemData.builder()
                                                .id(id)
                                                .damage(damage)
                                                .count(count)
                                                .blockingTicks(blockingTicks.toLong())
                                                .blockRuntimeId(blockRuntimeId)
                                                .usingNetId(usingNetId)
                                                .canBreak(canBreak.toTypedArray())
                                                .canPlace(canPlace.toTypedArray())
                                                .netId(netId)
                                                .build(),
                                        ),
                                    )
                                } else if (type.equals("INVALID", ignoreCase = true)) {
                                    inputItems.add(ItemDescriptorWithCount(InvalidDescriptor.INSTANCE, 1))
                                } else if (type.equals("ITEMTAG", ignoreCase = true)) {
                                    val itemTag = map["itemTag"] as String?
                                    inputItems.add(ItemDescriptorWithCount(ItemTagDescriptor(itemTag), 1))
                                }
                            }
                        }
                    }
                }
                val outputItems: MutableList<ItemData> = ArrayList()
                if (recipe.containsKey("outputs")) {
                    val outputs = recipe["outputs"] as List<*>
                    outputs.forEach {
                        val output = it as Map<*, *>
                        val id = output["id"] as Int
                        val damage = output["damage"] as Int
                        val count = output["count"] as Int
                        val canPlace = output["canPlace"] as List<String>
                        val canBreak = output["canBreak"] as List<String>
                        val blockingTicks = output["blockingTicks"] as Int
                        val blockRuntimeId = output["blockRuntimeId"] as Int
                        val usingNetId = output["usingNetId"] as Boolean
                        val netId = output["netId"] as Int
                        outputItems.add(
                            ItemData.builder()
                                .id(id)
                                .damage(damage)
                                .count(count)
                                .blockingTicks(blockingTicks.toLong())
                                .blockRuntimeId(blockRuntimeId)
                                .usingNetId(usingNetId)
                                .canPlace(canPlace.toTypedArray())
                                .canBreak(canBreak.toTypedArray())
                                .netId(netId)
                                .build(),
                        )
                    }
                }
                if (craftingDataType == CraftingDataType.FURNACE || craftingDataType == CraftingDataType.FURNACE_DATA) {
                    val input = Item(ItemPalette.getIdentifier(inputId.toShort()), false)
                    if (inputDamage != 32767) {
                        input.setMeta(inputDamage)
                    }
                    val output = Item(outputItems[0], false)
                    if (output.meta == 32767) {
                        output.setMeta(0)
                    }
                    smeltingRecipes.add(SmeltingRecipe(input, output))
                }
                val uuid = if (recipe["uuid"] != null) UUID.fromString(recipe["uuid"] as String?) else null
                val craftingTag = recipe["craftingTag"] as String?
                val priority = recipe["priority"] as Int
                val networkId = recipe["networkId"] as Int
                craftingData.add(
                    CraftingData(
                        craftingDataType,
                        recipeId,
                        width,
                        height,
                        inputId,
                        inputDamage,
                        inputItems,
                        outputItems,
                        uuid,
                        craftingTag,
                        priority,
                        networkId,
                    ),
                )
            }
            val containerMixes = config.map["containerMixes"] as List<*>
            containerMixes.forEach {
                val recipe = it as Map<*, *>
                val inputId = recipe["inputId"] as Int
                val reagentId = recipe["reagentId"] as Int
                val outputId = recipe["outputId"] as Int
                containerMixData.add(ContainerMixData(inputId, reagentId, outputId))
            }
            val potionMixes = config.map["potionMixes"] as List<*>
            potionMixes.forEach {
                val recipe = it as Map<*, *>
                val inputId = recipe["inputId"] as Int
                val inputMeta = recipe["inputMeta"] as Int
                val reagentId = recipe["reagentId"] as Int
                val reagentMeta = recipe["reagentMeta"] as Int
                val outputId = recipe["outputId"] as Int
                val outputMeta = recipe["outputMeta"] as Int
                potionMixData.add(PotionMixData(inputId, inputMeta, reagentId, reagentMeta, outputId, outputMeta))
            }
        }
    }

    fun registerRecipe(recipeId: String, recipe: Recipe) {
        try {
            val registered = recipe.doRegister(this, recipeId)
            if (registered != null) {
                craftingData.add(registered)
            }
        } catch (e: RuntimeException) {
            Server.instance.logger.error("Could not register recipe $recipeId!", e)
        }
    }

    val highestNetworkId: Int
        get() {
            val optional: Optional<CraftingData> = craftingData.stream().max(
                Comparator.comparing { obj: CraftingData -> obj.networkId },
            )
            return optional.map { obj: CraftingData -> obj.networkId }.orElse(-1)
        }

    fun getResultItem(recipeNetworkId: Int): List<Item> {
        val optional: Optional<CraftingData> = craftingData.stream()
            .filter { craftingData: CraftingData -> craftingData.networkId == recipeNetworkId }
            .findFirst()
        if (optional.isPresent) {
            val craftingData: CraftingData = optional.get()
            val items: MutableList<Item> = LinkedList()
            for (output in craftingData.outputs) {
                items.add(Item(output, false))
            }
            return items
        }
        return emptyList()
    }

    fun getSmeltingRecipe(input: Item): SmeltingRecipe? {
        return smeltingRecipes.stream().filter { smeltingRecipe: SmeltingRecipe? ->
            smeltingRecipe?.input?.type == input.type
        }.findFirst().orElse(null)
    }
}
