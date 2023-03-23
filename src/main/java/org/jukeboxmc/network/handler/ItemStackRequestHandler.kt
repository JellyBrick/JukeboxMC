package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ConsumeAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftCreativeAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftRecipeAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftRecipeOptionalAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftResultsDeprecatedAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DestroyAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DropAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.PlaceAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.SwapAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.TakeAction
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket
import org.jukeboxmc.Server
import org.jukeboxmc.event.inventory.InventoryClickEvent
import org.jukeboxmc.event.player.PlayerDropItemEvent
import org.jukeboxmc.inventory.CraftingGridInventory
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.inventory.InventoryType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.CreativeItems
import java.util.LinkedList
import kotlin.math.min

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStackRequestHandler : PacketHandler<ItemStackRequestPacket> {
    override fun handle(packet: ItemStackRequestPacket, server: Server, player: Player) {
        val responses: MutableList<ItemStackResponse> = LinkedList<ItemStackResponse>()
        for (request in packet.requests) {
            val itemEntryMap: MutableMap<Int, MutableList<ItemStackResponseSlot>> = HashMap()
            for (action in request.actions) {
                when (action.type) {
                    ItemStackRequestActionType.CONSUME -> {
                        val itemEntry: ItemStackResponseSlot =
                            handleConsumeAction(player, action as ConsumeAction, request)[0]
                        if (!itemEntryMap.containsKey(request.requestId)) {
                            itemEntryMap[request.requestId] =
                                object : LinkedList<ItemStackResponseSlot>() {
                                    init {
                                        add(itemEntry)
                                    }
                                }
                        } else {
                            itemEntryMap[request.requestId]!!.add(itemEntry)
                        }
                    }

                    ItemStackRequestActionType.CRAFT_CREATIVE -> handleCraftCreativeAction(
                        player,
                        action as CraftCreativeAction,
                    )

                    ItemStackRequestActionType.CRAFT_RECIPE -> responses.addAll(
                        handleCraftRecipeAction(
                            player,
                            action as CraftRecipeAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.TAKE -> responses.addAll(
                        handleTakeAction(
                            player,
                            action as TakeAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.PLACE -> responses.addAll(
                        handlePlaceAction(
                            player,
                            action as PlaceAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.DESTROY -> responses.addAll(
                        handleDestroyAction(
                            player,
                            action as DestroyAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.SWAP -> responses.addAll(
                        handleSwapAction(
                            player,
                            action as SwapAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.DROP -> responses.addAll(
                        handleDropAction(
                            player,
                            action as DropAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL -> responses.addAll(
                        handleCraftRecipeOptionalAction(
                            player,
                            action as CraftRecipeOptionalAction,
                            request,
                        ),
                    )

                    ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED -> responses.addAll(
                        handleCraftResult(player, action as CraftResultsDeprecatedAction, request), // FIXME: 03.08.2021
                    )

                    else -> server.logger.info("Unhandled Action: " + action.javaClass.simpleName + " : " + action.type)
                }
            }
            val itemStackResponsePacket = ItemStackResponsePacket()
            val ItemStackResponseContainerMap = mutableMapOf<Int, MutableList<ItemStackResponseContainer>>()
            if (itemEntryMap.isNotEmpty()) {
                responses.forEach {
                    ItemStackResponseContainerMap[it.requestId] = it.containers
                }
                if (ItemStackResponseContainerMap.containsKey(request.requestId)) {
                    ItemStackResponseContainerMap[request.requestId]?.add(
                        0,
                        ItemStackResponseContainer(
                            ContainerSlotType.CRAFTING_INPUT,
                            itemEntryMap[request.requestId],
                        ),
                    )
                }
                ItemStackResponseContainerMap.forEach { (key, value) ->
                    itemStackResponsePacket.entries
                        .add(ItemStackResponse(ItemStackResponseStatus.OK, key, value))
                }
            } else {
                itemStackResponsePacket.entries.addAll(responses)
            }
            player.playerConnection.sendPacket(itemStackResponsePacket)
        }
    }

    private fun handleCraftRecipeOptionalAction(
        player: Player,
        action: CraftRecipeOptionalAction,
        request: ItemStackRequest,
    ): Collection<ItemStackResponse> {
        return emptyList()
    }

    private fun handleCraftResult(
        player: Player,
        action: CraftResultsDeprecatedAction,
        request: ItemStackRequest,
    ): Collection<ItemStackResponse> {
        val craftingGridInventory: CraftingGridInventory = player.getCraftingGridInventory()
        val itemEntries: MutableList<ItemStackResponseSlot> =
            LinkedList<ItemStackResponseSlot>()
        for (slot in craftingGridInventory.offset until craftingGridInventory.size + craftingGridInventory.offset) {
            val item: Item = craftingGridInventory.getItem(slot)
            itemEntries.add(
                ItemStackResponseSlot(
                    slot,
                    slot,
                    item.amount,
                    item.stackNetworkId,
                    item.displayName,
                    item.durability,
                ),
            )
        }
        val ItemStackResponseContainerList: MutableList<ItemStackResponseContainer> = LinkedList<ItemStackResponseContainer>()
        ItemStackResponseContainerList.add(ItemStackResponseContainer(ContainerSlotType.CRAFTING_INPUT, itemEntries))
        return listOf(
            ItemStackResponse(
                ItemStackResponseStatus.OK,
                request.requestId,
                ItemStackResponseContainerList,
            ),
        )
    }

    private fun handleConsumeAction(
        player: Player,
        action: ConsumeAction,
        request: ItemStackRequest,
    ): List<ItemStackResponseSlot> {
        val amount = action.count
        val source = action.source
        var sourceItem = getItem(player, source.container, source.slot)
        if (sourceItem == null) {
            sourceItem = Item.create(ItemType.AIR)
        } else {
            sourceItem.setAmount(sourceItem.amount - amount)
        }
        if (sourceItem.amount <= 0) {
            sourceItem = Item.create(ItemType.AIR)
        }
        this.setItem(player, source.container, source.slot, sourceItem)
        val ItemStackResponseContainerList: MutableList<ItemStackResponseSlot> =
            LinkedList<ItemStackResponseSlot>()
        ItemStackResponseContainerList.add(
            ItemStackResponseSlot(
                source.slot,
                source.slot,
                sourceItem.amount,
                sourceItem.stackNetworkId,
                sourceItem.displayName,
                sourceItem.durability,
            ),
        )
        return ItemStackResponseContainerList
    }

    private fun handleCraftRecipeAction(
        player: Player,
        action: CraftRecipeAction,
        request: ItemStackRequest,
    ): List<ItemStackResponse> {
        val resultItem: List<Item> =
            Server.instance.getCraftingManager().getResultItem(action.recipeNetworkId)
        player.getCraftingGridInventory().setItem(0, resultItem[0])
        return emptyList()
    }

    private fun handleCraftCreativeAction(player: Player, actionData: CraftCreativeAction) {
        val itemData = CreativeItems.creativeItems[actionData.creativeItemNetworkId - 1]
        val item: Item = Item.create(itemData)
        item.setAmount(item.maxStackSize)
        player.getCreativeItemCacheInventory().setItem(0, item)
    }

    private fun handlePlaceAction(
        player: Player,
        actionData: PlaceAction,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponse> {
        val amount = actionData.count
        val source = actionData.source
        val destination = actionData.destination
        val ItemStackResponseContainerList = LinkedList<ItemStackResponseContainer>()
        var sourceItem = getItem(player, source.container, source.slot) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot}")
            return emptyList()
        }
        var destinationItem = getItem(player, destination.container, destination.slot) ?: run {
            Server.instance.logger.debug("Unknown destination item slot ${destination.slot}")
            return emptyList()
        }
        if (source.container == ContainerSlotType.CREATED_OUTPUT) {
            val sourceInventory = getInventory(player, source.container) ?: run {
                Server.instance.logger.debug("Unknown source inventory ${destination.container.name}")
                return emptyList()
            }
            val destinationInventory = getInventory(player, destination.container) ?: run {
                Server.instance.logger.debug("Unknown destination inventory ${destination.container.name}")
                return emptyList()
            }
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.slot,
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled) {
                sourceInventory.setItem(source.slot, sourceItem)
                return listOf(
                    ItemStackResponse(
                        ItemStackResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ItemStackResponseContainer>(),
                    ),
                )
            }
            sourceItem.setStackNetworkId(Item.stackNetworkCount++)
            if (destinationItem.type != ItemType.AIR) {
                sourceItem.setAmount((destinationItem.amount + sourceItem.amount).coerceAtMost(sourceItem.maxStackSize))
            }
            this.setItem(player, destination.container, destination.slot, sourceItem)
            ItemStackResponseContainerList.add(
                ItemStackResponseContainer(
                    destination.container,
                    listOf(
                        ItemStackResponseSlot(
                            destination.slot,
                            destination.slot,
                            sourceItem.amount,
                            sourceItem.stackNetworkId,
                            sourceItem.displayName,
                            sourceItem.durability,
                        ),
                    ),
                ),
            )
        } else {
            val sourceInventory = getInventory(player, source.container) ?: run {
                Server.instance.logger.debug("Unknown source inventory ${source.container.name}")
                return emptyList()
            }
            val destinationInventory = getInventory(player, destination.container)!!
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.slot,
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled || sourceInventory.type == InventoryType.ARMOR && sourceItem.getEnchantment(
                    EnchantmentType.CURSE_OF_BINDING,
                ) != null
            ) {
                sourceInventory.setItem(source.slot, sourceItem)
                return listOf(
                    ItemStackResponse(
                        ItemStackResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ItemStackResponseContainer>(),
                    ),
                )
            }
            if (destinationItem == sourceItem && sourceItem.amount > 0) {
                sourceItem.setAmount(sourceItem.amount - amount)
                if (sourceItem.amount <= 0) {
                    sourceItem = Item.create(ItemType.AIR)
                }
                destinationItem.setAmount(destinationItem.amount + amount)
            } else if (destinationItem.type == ItemType.AIR) {
                if (sourceItem.amount == amount) {
                    destinationItem = sourceItem.clone()
                    sourceItem = Item.create(ItemType.AIR)
                } else {
                    destinationItem = sourceItem.clone()
                    destinationItem.setAmount(amount)
                    destinationItem.setStackNetworkId(Item.stackNetworkCount++)
                    sourceItem.setAmount(sourceItem.amount - amount)
                }
            }
            this.setItem(player, source.container, source.slot, sourceItem)
            this.setItem(player, destination.container, destination.slot, destinationItem)
            val finalSourceItem = sourceItem
            ItemStackResponseContainerList.add(
                ItemStackResponseContainer(
                    source.container,
                    listOf(
                        ItemStackResponseSlot(
                            source.slot,
                            source.slot,
                            finalSourceItem.amount,
                            finalSourceItem.stackNetworkId,
                            finalSourceItem.displayName,
                            finalSourceItem.durability,
                        ),
                    ),
                ),
            )
            val finalDestinationItem = destinationItem
            ItemStackResponseContainerList.add(
                ItemStackResponseContainer(
                    destination.container,
                    listOf(
                        ItemStackResponseSlot(
                            destination.slot,
                            destination.slot,
                            finalDestinationItem.amount,
                            finalDestinationItem.stackNetworkId,
                            finalDestinationItem.displayName,
                            finalDestinationItem.durability,
                        ),
                    ),
                ),
            )
        }
        return listOf(
            ItemStackResponse(
                ItemStackResponseStatus.OK,
                itemStackRequest.requestId,
                ItemStackResponseContainerList,
            ),
        )
    }

    private fun handleTakeAction(
        player: Player,
        actionData: TakeAction,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponse> {
        val amount = actionData.count
        val source = actionData.source
        val destination = actionData.destination
        val entryList: MutableList<ItemStackResponseContainer> = LinkedList<ItemStackResponseContainer>()
        var sourceItem = getItem(player, source.container, source.slot) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot}")
            return emptyList()
        }
        var destinationItem = getItem(player, destination.container, destination.slot) ?: run {
            Server.instance.logger.debug("Unknown destination item slot ${destination.slot}")
            return emptyList()
        }
        if (source.container == ContainerSlotType.CREATED_OUTPUT) {
            val sourceInventory = getInventory(player, source.container) ?: run {
                Server.instance.logger.debug("Unknown source inventory ${source.container.name}")
                return emptyList()
            }
            val destinationInventory = getInventory(player, destination.container) ?: run {
                Server.instance.logger.debug("Unknown destination inventory ${source.container.name}")
                return emptyList()
            }
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.slot,
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled) {
                sourceInventory.setItem(source.slot, sourceItem)
                return listOf(
                    ItemStackResponse(
                        ItemStackResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ItemStackResponseContainer>(),
                    ),
                )
            }
            sourceItem.setStackNetworkId(Item.stackNetworkCount++)
            if (destinationItem.type != ItemType.AIR) {
                sourceItem.setAmount(min(destinationItem.amount + sourceItem.amount, sourceItem.maxStackSize))
            }
            this.setItem(player, destination.container, destination.slot, sourceItem)
            entryList.add(
                ItemStackResponseContainer(
                    destination.container,
                    listOf(
                        ItemStackResponseSlot(
                            destination.slot,
                            destination.slot,
                            sourceItem.amount,
                            sourceItem.stackNetworkId,
                            sourceItem.displayName,
                            sourceItem.durability,
                        ),
                    ),
                ),
            )
        } else {
            val sourceInventory = getInventory(player, source.container) ?: run {
                Server.instance.logger.debug("Unknown source inventory ${source.container.name}")
                return emptyList()
            }
            val destinationInventory = getInventory(player, destination.container) ?: run {
                Server.instance.logger.debug("Unknown destination inventory ${source.container.name}")
                return emptyList()
            }
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.slot,
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled || sourceInventory.type == InventoryType.ARMOR && sourceItem.getEnchantment(
                    EnchantmentType.CURSE_OF_BINDING,
                ) != null
            ) {
                sourceInventory.setItem(source.slot, sourceItem)
                return listOf(
                    ItemStackResponse(
                        ItemStackResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ItemStackResponseContainer>(),
                    ),
                )
            }
            if (destinationItem == sourceItem && sourceItem.amount > 0) {
                sourceItem.setAmount(sourceItem.amount - amount)
                if (sourceItem.amount <= 0) {
                    sourceItem = Item.create(ItemType.AIR)
                }
                destinationItem.setAmount(destinationItem.amount + amount)
            } else if (destinationItem.type == ItemType.AIR) {
                if (sourceItem.amount == amount) {
                    destinationItem = sourceItem.clone()
                    sourceItem = Item.create(ItemType.AIR)
                } else {
                    destinationItem = sourceItem.clone()
                    destinationItem.setAmount(amount)
                    destinationItem.setStackNetworkId(Item.stackNetworkCount++)
                    sourceItem.setAmount(sourceItem.amount - amount)
                }
            }
            this.setItem(player, source.container, source.slot, sourceItem)
            this.setItem(player, destination.container, destination.slot, destinationItem)
            val finalSourceItem = sourceItem
            entryList.add(
                ItemStackResponseContainer(
                    source.container,
                    listOf(
                        ItemStackResponseSlot(
                            source.slot,
                            source.slot,
                            finalSourceItem.amount,
                            finalSourceItem.stackNetworkId,
                            finalSourceItem.displayName,
                            finalSourceItem.durability,
                        ),
                    ),
                ),
            )
            val finalDestinationItem = destinationItem
            entryList.add(
                ItemStackResponseContainer(
                    destination.container,
                    listOf(
                        ItemStackResponseSlot(
                            destination.slot,
                            destination.slot,
                            finalDestinationItem.amount,
                            finalDestinationItem.stackNetworkId,
                            finalDestinationItem.displayName,
                            finalDestinationItem.durability,
                        ),
                    ),
                ),
            )
        }
        return listOf(
            ItemStackResponse(
                ItemStackResponseStatus.OK,
                itemStackRequest.requestId,
                entryList,
            ),
        )
    }

    private fun handleSwapAction(
        player: Player,
        actionData: SwapAction,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponse> {
        val source = actionData.source
        val destination = actionData.destination
        val sourceItem = getItem(player, source.container, source.slot) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot}")
            return emptyList()
        }
        val destinationItem = getItem(player, destination.container, destination.slot) ?: run {
            Server.instance.logger.debug("Unknown destination item slot ${destination.slot}")
            return emptyList()
        }
        this.setItem(player, source.container, source.slot, destinationItem)
        this.setItem(player, destination.container, destination.slot, sourceItem)
        val ItemStackResponseContainerList: MutableList<ItemStackResponseContainer> = LinkedList<ItemStackResponseContainer>()
        ItemStackResponseContainerList.add(
            ItemStackResponseContainer(
                destination.container,
                listOf(
                    ItemStackResponseSlot(
                        destination.slot,
                        destination.slot,
                        sourceItem.amount,
                        sourceItem.stackNetworkId,
                        sourceItem.displayName,
                        sourceItem.durability,
                    ),
                ),
            ),
        )
        ItemStackResponseContainerList.add(
            ItemStackResponseContainer(
                source.container,
                listOf(
                    ItemStackResponseSlot(
                        source.slot,
                        source.slot,
                        destinationItem.amount,
                        destinationItem.stackNetworkId,
                        destinationItem.displayName,
                        destinationItem.durability,
                    ),
                ),
            ),
        )
        return listOf(
            ItemStackResponse(
                ItemStackResponseStatus.OK,
                itemStackRequest.requestId,
                ItemStackResponseContainerList,
            ),
        )
    }

    private fun handleDestroyAction(
        player: Player,
        actionData: DestroyAction,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponse> {
        val amount = actionData.count
        val source = actionData.source
        var sourceItem = getItem(player, source.container, source.slot) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot}")
            return emptyList()
        }
        sourceItem.setAmount(sourceItem.amount - amount)
        if (sourceItem.amount <= 0) {
            sourceItem = Item.create(ItemType.AIR)
            this.setItem(player, source.container, source.slot, sourceItem)
        }
        val finalSourceItem = sourceItem
        return listOf(
            ItemStackResponse(
                ItemStackResponseStatus.OK,
                itemStackRequest.requestId,
                listOf(
                    ItemStackResponseContainer(
                        source.container,
                        listOf(
                            ItemStackResponseSlot(
                                source.slot,
                                source.slot,
                                finalSourceItem.amount,
                                finalSourceItem.stackNetworkId,
                                finalSourceItem.displayName,
                                finalSourceItem.durability,
                            ),
                        ),
                    ),
                ),
            ),
        )
    }

    private fun handleDropAction(
        player: Player,
        dropStackRequestActionData: DropAction,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponse> {
        val amount = dropStackRequestActionData.count
        val source = dropStackRequestActionData.source
        val sourceInventory = getInventory(player, source.container) ?: run {
            Server.instance.logger.debug("Unknown source inventory ${source.container.name}")
            return emptyList()
        }
        var sourceItem = getItem(player, source.container, source.slot) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot}")
            return emptyList()
        }
        val playerDropItemEvent = PlayerDropItemEvent(player, sourceItem)
        Server.instance.pluginManager.callEvent(playerDropItemEvent)
        if (playerDropItemEvent.isCancelled || sourceInventory.type == InventoryType.ARMOR && sourceItem.getEnchantment(
                EnchantmentType.CURSE_OF_BINDING,
            ) != null
        ) {
            sourceInventory.setItem(source.slot, sourceItem)
            return listOf(
                ItemStackResponse(
                    ItemStackResponseStatus.ERROR,
                    itemStackRequest.requestId,
                    emptyList<ItemStackResponseContainer>(),
                ),
            )
        }
        sourceItem.setAmount(sourceItem.amount - amount)
        val dropItem = sourceItem.clone()
        dropItem.setAmount(amount)
        if (sourceItem.amount <= 0) {
            sourceItem = Item.create(ItemType.AIR)
        }
        this.setItem(player, source.container, source.slot, sourceItem)
        val entityItem = player.world?.dropItem(
            dropItem,
            player.location.add(0f, player.eyeHeight, 0f),
            player.location.direction.multiply(0.4f, 0.4f, 0.4f),
        )!!
        entityItem.playerHasThrown = player
        entityItem.spawn()
        val finalSourceItem = sourceItem
        return listOf(
            ItemStackResponse(
                ItemStackResponseStatus.OK,
                itemStackRequest.requestId,
                listOf(
                    ItemStackResponseContainer(
                        source.container,
                        listOf(
                            ItemStackResponseSlot(
                                source.slot,
                                source.slot,
                                finalSourceItem.amount,
                                finalSourceItem.stackNetworkId,
                                finalSourceItem.displayName,
                                finalSourceItem.durability,
                            ),
                        ),
                    ),
                ),
            ),
        )
    }

    private fun rejectItemStackRequest(
        player: Player,
        requestId: Int,
        sourceInventory: Inventory,
        destinationInventory: Inventory,
        sourceSlot: Int,
        destinationSlot: Int,
        sourceItem: Item,
        destinationItem: Item,
    ): ItemStackResponse {
        Server.instance.scheduler.scheduleDelayed(
            {
                sourceInventory.setItem(sourceSlot, sourceItem)
                sourceInventory.sendContents(sourceSlot, player)
                destinationInventory.setItem(destinationSlot, destinationItem)
                destinationInventory.sendContents(destinationSlot, player)
            },
            20,
        )
        return ItemStackResponse(
            ItemStackResponseStatus.OK,
            requestId,
            listOf(
                ItemStackResponseContainer(
                    ContainerSlotType.HOTBAR,
                    listOf(
                        ItemStackResponseSlot(
                            sourceSlot,
                            sourceSlot,
                            sourceItem.amount,
                            sourceItem.stackNetworkId,
                            sourceItem.displayName,
                            sourceItem.durability,
                        ),
                    ),
                ),
                ItemStackResponseContainer(
                    ContainerSlotType.CURSOR,
                    listOf(
                        ItemStackResponseSlot(
                            destinationSlot,
                            destinationSlot,
                            destinationItem.amount,
                            destinationItem.stackNetworkId,
                            destinationItem.displayName,
                            destinationItem.durability,
                        ),
                    ),
                ),
            ),
        )
    }

    private fun getInventory(player: Player, containerSlotType: ContainerSlotType): Inventory? {
        return when (containerSlotType) {
            ContainerSlotType.CREATED_OUTPUT -> player.getCreativeItemCacheInventory()
            ContainerSlotType.CURSOR -> player.getCursorInventory()
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory
            ContainerSlotType.ARMOR -> player.armorInventory
            ContainerSlotType.LEVEL_ENTITY, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_RESULT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_MATERIAL -> player.currentInventory
            ContainerSlotType.CRAFTING_INPUT -> player.getCraftingGridInventory()
            ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_RESULT -> player.getCartographyTableInventory()
            ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT -> player.getSmithingTableInventory()
            ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT -> player.getAnvilInventory()
            ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT -> player.getStoneCutterInventory()
            ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT -> player.getCraftingGridInventory()
            else -> null
        }
    }

    private fun getItem(player: Player, containerSlotType: ContainerSlotType, slot: Int): Item? {
        return when (containerSlotType) {
            ContainerSlotType.CREATED_OUTPUT -> player.getCreativeItemCacheInventory().getItem(0)
            ContainerSlotType.CURSOR -> player.getCursorInventory().getItem(slot)
            ContainerSlotType.OFFHAND -> player.getOffHandInventory().getItem(slot)
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory.getItem(
                slot,
            )

            ContainerSlotType.ARMOR -> player.armorInventory.getItem(slot)
            ContainerSlotType.LEVEL_ENTITY, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_RESULT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_MATERIAL -> player.currentInventory?.getItem(
                slot,
            )

            ContainerSlotType.CRAFTING_INPUT -> player.getCraftingGridInventory().getItem(slot)
            ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_RESULT -> player.getCartographyTableInventory().getItem(
                slot,
            )

            ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT -> player.getSmithingTableInventory().getItem(
                slot,
            )

            ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT -> player.getAnvilInventory().getItem(
                slot,
            )

            ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT -> player.getStoneCutterInventory().getItem(
                slot,
            )

            ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT -> player.getGrindstoneInventory().getItem(
                slot,
            )

            else -> null
        }
    }

    private fun setItem(player: Player, containerSlotType: ContainerSlotType, slot: Int, item: Item) {
        this.setItem(player, containerSlotType, slot, item, true)
    }

    private fun setItem(
        player: Player,
        containerSlotType: ContainerSlotType,
        slot: Int,
        item: Item,
        sendContent: Boolean,
    ) {
        when (containerSlotType) {
            ContainerSlotType.CURSOR -> player.getCursorInventory().setItem(slot, item, sendContent)
            ContainerSlotType.OFFHAND -> player.getOffHandInventory().setItem(slot, item, sendContent)
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory.setItem(
                slot,
                item,
                sendContent,
            )

            ContainerSlotType.ARMOR -> player.armorInventory.setItem(slot, item, sendContent)
            ContainerSlotType.LEVEL_ENTITY, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_RESULT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_MATERIAL -> player.currentInventory?.setItem(
                slot,
                item,
                sendContent,
            )

            ContainerSlotType.CRAFTING_INPUT -> player.getCraftingGridInventory().setItem(slot, item, sendContent)
            ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_RESULT -> player.getCartographyTableInventory().setItem(
                slot,
                item,
                sendContent,
            )

            ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT -> player.getSmithingTableInventory().setItem(
                slot,
                item,
                sendContent,
            )

            ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT -> player.getAnvilInventory().setItem(
                slot,
                item,
                sendContent,
            )

            ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT -> player.getStoneCutterInventory().setItem(
                slot,
                item,
                sendContent,
            )

            ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT -> player.getGrindstoneInventory().setItem(
                slot,
                item,
                sendContent,
            )

            else -> {}
        }
    }
}
