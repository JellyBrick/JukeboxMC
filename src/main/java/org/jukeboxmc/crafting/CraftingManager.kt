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
import org.jukeboxmc.config.ConfigSection
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
            config.map.getTyped<List<ConfigSection>>("recipes").forEach { recipe ->
                val craftingDataType: CraftingDataType = CraftingDataType.valueOf(recipe.getString("type"))
                val recipeId = recipe.getString("recipeId")
                val width = (recipe["width"] as Double).toInt()
                val height = (recipe["height"] as Double).toInt()
                val inputId = (recipe["inputId"] as Double).toInt()
                val inputDamage = (recipe["inputDamage"] as Double).toInt()
                val inputItems: MutableList<ItemDescriptorWithCount> = ArrayList<ItemDescriptorWithCount>()
                if (recipe.containsKey("inputs")) {
                    recipe.getTyped<List<ConfigSection>>("inputs").forEach { input ->
                        if (input.containsKey("descriptor")) {
                            input.getTyped<List<ConfigSection>>("descriptor").forEach { map ->
                                val type = map["descriptorType"] as String?
                                if (type.equals("DEFAULT", ignoreCase = true)) {
                                    val id = (map["id"] as Double).toInt()
                                    val damage = (map["damage"] as Double).toInt()
                                    val count = (map["count"] as Double).toInt()
                                    val canPlace = map.getTyped<List<Any>>("canPlace")
                                    val canBreak = map.getTyped<List<Any>>("canBreak")
                                    val blockingTicks = (map["blockingTicks"] as Double).toInt()
                                    val blockRuntimeId = (map["blockRuntimeId"] as Double).toInt()
                                    val usingNetId = map["usingNetId"] as Boolean
                                    val netId = (map["netId"] as Double).toInt()
                                    inputItems.add(
                                        ItemDescriptorWithCount.fromItem(
                                            ItemData.builder()
                                                .id(id)
                                                .damage(damage)
                                                .count(count)
                                                .blockingTicks(blockingTicks.toLong())
                                                .blockRuntimeId(blockRuntimeId)
                                                .usingNetId(usingNetId)
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
                val outputs = recipe.getTyped<List<ConfigSection>>("outputs")
                if (recipe.containsKey("outputs")) {
                    outputs.forEach { output ->
                        val id = (output["id"] as Double).toInt()
                        val damage = (output["damage"] as Double).toInt()
                        val count = (output["count"] as Double).toInt()
                        val canPlace = output.getTyped<List<Any>>("canPlace")
                        val canBreak = output.getTyped<List<Any>>("canBreak")
                        val blockingTicks = output.getInt("blockingTicks")
                        val blockRuntimeId = output.getInt("blockRuntimeId")
                        val usingNetId = output.getBoolean("usingNetId")
                        val netId = output.getInt("netId")
                        outputItems.add(
                            ItemData.builder()
                                .id(id)
                                .damage(damage)
                                .count(count)
                                .blockingTicks(blockingTicks.toLong())
                                .blockRuntimeId(blockRuntimeId)
                                .usingNetId(usingNetId)
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
                val priority = (recipe["priority"] as Double).toInt()
                val networkId = (recipe["networkId"] as Double).toInt()
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
            val containerMixes = config.map["containerMixes"] as List<Map<String, Any>>?
            for (recipe in containerMixes!!) {
                val inputId = (recipe["inputId"] as Double).toInt()
                val reagentId = (recipe["reagentId"] as Double).toInt()
                val outputId = (recipe["outputId"] as Double).toInt()
                containerMixData.add(ContainerMixData(inputId, reagentId, outputId))
            }
            val potionMixes = config.map["potionMixes"] as List<Map<String, Any>>?
            for (recipe in potionMixes!!) {
                val inputId = (recipe["inputId"] as Double).toInt()
                val inputMeta = (recipe["inputMeta"] as Double).toInt()
                val reagentId = (recipe["reagentId"] as Double).toInt()
                val reagentMeta = (recipe["reagentMeta"] as Double).toInt()
                val outputId = (recipe["outputId"] as Double).toInt()
                val outputMeta = (recipe["outputMeta"] as Double).toInt()
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
        return smeltingRecipes.stream().filter(
            { smeltingRecipe: SmeltingRecipe? ->
                smeltingRecipe?.input?.type == input.type
            },
        ).findFirst().orElse(null)
    }
}
