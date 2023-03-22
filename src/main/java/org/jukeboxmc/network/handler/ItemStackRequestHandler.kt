package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType
import com.nukkitx.protocol.bedrock.data.inventory.ItemStackRequest
import com.nukkitx.protocol.bedrock.data.inventory.StackRequestSlotInfoData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.ConsumeStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftCreativeStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftRecipeOptionalStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftRecipeStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftResultsDeprecatedStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.DestroyStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.DropStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.PlaceStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.SwapStackRequestActionData
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.TakeStackRequestActionData
import com.nukkitx.protocol.bedrock.packet.ItemStackRequestPacket
import com.nukkitx.protocol.bedrock.packet.ItemStackResponsePacket
import com.nukkitx.protocol.bedrock.packet.ItemStackResponsePacket.ContainerEntry
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

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStackRequestHandler : PacketHandler<ItemStackRequestPacket> {
    override fun handle(packet: ItemStackRequestPacket, server: Server, player: Player) {
        val responses: MutableList<ItemStackResponsePacket.Response> = LinkedList<ItemStackResponsePacket.Response>()
        for (request in packet.requests) {
            val itemEntryMap: MutableMap<Int, MutableList<ItemStackResponsePacket.ItemEntry>> = HashMap()
            for (action in request.actions) {
                when (action.type) {
                    StackRequestActionType.CONSUME -> {
                        val itemEntry: ItemStackResponsePacket.ItemEntry =
                            handleConsumeAction(player, action as ConsumeStackRequestActionData, request)[0]
                        if (!itemEntryMap.containsKey(request.requestId)) {
                            itemEntryMap[request.requestId] =
                                object : LinkedList<ItemStackResponsePacket.ItemEntry>() {
                                    init {
                                        add(itemEntry)
                                    }
                                }
                        } else {
                            itemEntryMap[request.requestId]!!.add(itemEntry)
                        }
                    }

                    StackRequestActionType.CRAFT_CREATIVE -> handleCraftCreativeAction(
                        player,
                        action as CraftCreativeStackRequestActionData,
                    )

                    StackRequestActionType.CRAFT_RECIPE -> responses.addAll(
                        handleCraftRecipeAction(
                            player,
                            action as CraftRecipeStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.TAKE -> responses.addAll(
                        handleTakeStackRequestAction(
                            player,
                            action as TakeStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.PLACE -> responses.addAll(
                        handlePlaceAction(
                            player,
                            action as PlaceStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.DESTROY -> responses.addAll(
                        handleDestroyAction(
                            player,
                            action as DestroyStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.SWAP -> responses.addAll(
                        handleSwapAction(
                            player,
                            action as SwapStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.DROP -> responses.addAll(
                        handleDropItemAction(
                            player,
                            action as DropStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.CRAFT_RECIPE_OPTIONAL -> responses.addAll(
                        handleCraftRecipeOptionalAction(
                            player,
                            action as CraftRecipeOptionalStackRequestActionData,
                            request,
                        ),
                    )

                    StackRequestActionType.CRAFT_RESULTS_DEPRECATED -> {
                        // responses.addAll( this.handleCraftResult( player, (CraftResultsDeprecatedStackRequestActionData) action, requestId ) );
                    }

                    else -> server.logger.info("Unhandelt Action: " + action.javaClass.simpleName + " : " + action.type)
                }
            }
            val itemStackResponsePacket = ItemStackResponsePacket()
            val containerEntryMap: MutableMap<Int, MutableList<ContainerEntry>> = HashMap()
            if (itemEntryMap.isNotEmpty()) {
                for (respons in responses) {
                    containerEntryMap[respons.requestId] = respons.containers
                }
                if (containerEntryMap.containsKey(request.requestId)) {
                    containerEntryMap[request.requestId]?.add(
                        0,
                        ContainerEntry(
                            ContainerSlotType.CRAFTING_INPUT,
                            itemEntryMap[request.requestId],
                        ),
                    )
                }
                for ((key, value) in containerEntryMap) {
                    itemStackResponsePacket.entries
                        .add(ItemStackResponsePacket.Response(ItemStackResponsePacket.ResponseStatus.OK, key, value))
                }
            } else {
                itemStackResponsePacket.entries.addAll(responses)
            }
            player.playerConnection.sendPacket(itemStackResponsePacket)
        }
    }

    private fun handleCraftRecipeOptionalAction(
        player: Player,
        action: CraftRecipeOptionalStackRequestActionData,
        request: ItemStackRequest,
    ): Collection<ItemStackResponsePacket.Response> {
        return emptyList<ItemStackResponsePacket.Response>()
    }

    private fun handleCraftResult(
        player: Player,
        action: CraftResultsDeprecatedStackRequestActionData,
        requestId: Int,
    ): Collection<ItemStackResponsePacket.Response> {
        val craftingGridInventory: CraftingGridInventory = player.getCraftingGridInventory()
        val itemEntries: MutableList<ItemStackResponsePacket.ItemEntry> =
            LinkedList<ItemStackResponsePacket.ItemEntry>()
        for (slot in craftingGridInventory.offset until craftingGridInventory.size + craftingGridInventory.offset) {
            val item: Item = craftingGridInventory.getItem(slot)
            itemEntries.add(
                ItemStackResponsePacket.ItemEntry(
                    slot.toByte(),
                    slot.toByte(),
                    item.amount.toByte(),
                    item.stackNetworkId,
                    item.displayname,
                    item.durability,
                ),
            )
        }
        val containerEntryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        containerEntryList.add(ContainerEntry(ContainerSlotType.CRAFTING_INPUT, itemEntries))
        return listOf(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                requestId,
                containerEntryList,
            ),
        )
    }

    private fun handleConsumeAction(
        player: Player,
        action: ConsumeStackRequestActionData,
        request: ItemStackRequest,
    ): List<ItemStackResponsePacket.ItemEntry> {
        val amount: Byte = action.getCount()
        val source: StackRequestSlotInfoData = action.source
        var sourceItem = getItem(player, source.container, source.slot.toInt())
        if (sourceItem == null) {
            sourceItem = Item.create<Item>(ItemType.AIR)
        } else {
            sourceItem.setAmount(sourceItem.amount - amount)
        }
        if (sourceItem.amount <= 0) {
            sourceItem = Item.create<Item>(ItemType.AIR)
        }
        this.setItem(player, source.container, source.slot.toInt(), sourceItem)
        val containerEntryList: MutableList<ItemStackResponsePacket.ItemEntry> =
            LinkedList<ItemStackResponsePacket.ItemEntry>()
        containerEntryList.add(
            ItemStackResponsePacket.ItemEntry(
                source.slot,
                source.slot,
                sourceItem.amount.toByte(),
                sourceItem.stackNetworkId,
                sourceItem.displayname,
                sourceItem.durability,
            ),
        )
        return containerEntryList
    }

    private fun handleCraftRecipeAction(
        player: Player,
        action: CraftRecipeStackRequestActionData,
        request: ItemStackRequest,
    ): List<ItemStackResponsePacket.Response> {
        val resultItem: List<Item> =
            Server.instance.getCraftingManager().getResultItem(action.recipeNetworkId)
        player.getCraftingGridInventory().setItem(0, resultItem[0])
        return emptyList()
    }

    private fun handleCraftCreativeAction(player: Player, actionData: CraftCreativeStackRequestActionData) {
        val itemData = CreativeItems.creativeItems[actionData.creativeItemNetworkId - 1]
        val item: Item = Item.create(itemData)
        item.setAmount(item.maxStackSize)
        player.getCreativeItemCacheInventory().setItem(0, item)
    }

    private fun handlePlaceAction(
        player: Player,
        actionData: PlaceStackRequestActionData,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = actionData.count
        val source: StackRequestSlotInfoData = actionData.source
        val destination: StackRequestSlotInfoData = actionData.destination
        val containerEntryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        var sourceItem = getItem(player, source.container, source.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot.toInt()}")
            return emptyList()
        }
        var destinationItem = getItem(player, destination.container, destination.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown destination item slot ${destination.slot}")
            return emptyList()
        }
        if (source.container == ContainerSlotType.CREATIVE_OUTPUT) {
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
                source.slot.toInt(),
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled) {
                sourceInventory.setItem(source.slot.toInt(), sourceItem)
                return listOf(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ContainerEntry>(),
                    ),
                )
            }
            sourceItem.setStackNetworkId(Item.stackNetworkCount++)
            if (destinationItem.type != ItemType.AIR) {
                sourceItem.setAmount((destinationItem.amount + sourceItem.amount).coerceAtMost(sourceItem.maxStackSize))
            }
            this.setItem(player, destination.container, destination.slot.toInt(), sourceItem)
            containerEntryList.add(
                ContainerEntry(
                    destination.container,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            destination.slot,
                            destination.slot,
                            sourceItem.amount.toByte(),
                            sourceItem.stackNetworkId,
                            sourceItem.displayname,
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
                source.slot.toInt(),
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled || sourceInventory.type == InventoryType.ARMOR && sourceItem.getEnchantment(
                    EnchantmentType.CURSE_OF_BINDING,
                ) != null
            ) {
                sourceInventory.setItem(source.slot.toInt(), sourceItem)
                return listOf(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ContainerEntry>(),
                    ),
                )
            }
            if (destinationItem == sourceItem && sourceItem.amount > 0) {
                sourceItem.setAmount(sourceItem.amount - amount)
                if (sourceItem.amount <= 0) {
                    sourceItem = Item.create<Item>(ItemType.AIR)
                }
                destinationItem.setAmount(destinationItem.amount + amount)
            } else if (destinationItem.type == ItemType.AIR) {
                if (sourceItem.amount == amount.toInt()) {
                    destinationItem = sourceItem.clone()
                    sourceItem = Item.create<Item>(ItemType.AIR)
                } else {
                    destinationItem = sourceItem.clone()
                    destinationItem.setAmount(amount.toInt())
                    destinationItem.setStackNetworkId(Item.stackNetworkCount++)
                    sourceItem.setAmount(sourceItem.amount - amount)
                }
            }
            this.setItem(player, source.container, source.slot.toInt(), sourceItem)
            this.setItem(player, destination.container, destination.slot.toInt(), destinationItem)
            val finalSourceItem = sourceItem
            containerEntryList.add(
                ContainerEntry(
                    source.container,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            source.slot,
                            source.slot,
                            finalSourceItem.amount.toByte(),
                            finalSourceItem.stackNetworkId,
                            finalSourceItem.displayname,
                            finalSourceItem.durability,
                        ),
                    ),
                ),
            )
            val finalDestinationItem = destinationItem
            containerEntryList.add(
                ContainerEntry(
                    destination.container,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            destination.slot,
                            destination.slot,
                            finalDestinationItem.amount.toByte(),
                            finalDestinationItem.stackNetworkId,
                            finalDestinationItem.displayname,
                            finalDestinationItem.durability,
                        ),
                    ),
                ),
            )
        }
        return listOf(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.requestId,
                containerEntryList,
            ),
        )
    }

    private fun handleTakeStackRequestAction(
        player: Player,
        actionData: TakeStackRequestActionData,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = actionData.count
        val source: StackRequestSlotInfoData = actionData.source
        val destination: StackRequestSlotInfoData = actionData.destination
        val entryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        var sourceItem = getItem(player, source.container, source.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot.toInt()}")
            return emptyList()
        }
        var destinationItem = getItem(player, destination.container, destination.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown destination item slot ${destination.slot.toInt()}")
            return emptyList()
        }
        if (source.container == ContainerSlotType.CREATIVE_OUTPUT) {
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
                source.slot.toInt(),
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled) {
                sourceInventory.setItem(source.slot.toInt(), sourceItem)
                return java.util.List.of(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ContainerEntry>(),
                    ),
                )
            }
            sourceItem.setStackNetworkId(Item.stackNetworkCount++)
            if (destinationItem.type != ItemType.AIR) {
                sourceItem.setAmount(Math.min(destinationItem.amount + sourceItem.amount, sourceItem.maxStackSize))
            }
            this.setItem(player, destination.container, destination.slot.toInt(), sourceItem)
            entryList.add(
                ContainerEntry(
                    destination.container,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            destination.slot,
                            destination.slot,
                            sourceItem.amount.toByte(),
                            sourceItem.stackNetworkId,
                            sourceItem.displayname,
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
                source.slot.toInt(),
            )
            Server.instance.pluginManager.callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled || sourceInventory.type == InventoryType.ARMOR && sourceItem.getEnchantment(
                    EnchantmentType.CURSE_OF_BINDING,
                ) != null
            ) {
                sourceInventory.setItem(source.slot.toInt(), sourceItem)
                return listOf(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.requestId,
                        emptyList<ContainerEntry>(),
                    ),
                )
            }
            if (destinationItem == sourceItem && sourceItem.amount > 0) {
                sourceItem.setAmount(sourceItem.amount - amount)
                if (sourceItem.amount <= 0) {
                    sourceItem = Item.create<Item>(ItemType.AIR)
                }
                destinationItem.setAmount(destinationItem.amount + amount)
            } else if (destinationItem.type == ItemType.AIR) {
                if (sourceItem.amount == amount.toInt()) {
                    destinationItem = sourceItem.clone()
                    sourceItem = Item.create<Item>(ItemType.AIR)
                } else {
                    destinationItem = sourceItem.clone()
                    destinationItem.setAmount(amount.toInt())
                    destinationItem.setStackNetworkId(Item.stackNetworkCount++)
                    sourceItem.setAmount(sourceItem.amount - amount)
                }
            }
            this.setItem(player, source.container, source.slot.toInt(), sourceItem)
            this.setItem(player, destination.container, destination.slot.toInt(), destinationItem)
            val finalSourceItem = sourceItem
            entryList.add(
                ContainerEntry(
                    source.container,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            source.slot,
                            source.slot,
                            finalSourceItem.amount.toByte(),
                            finalSourceItem.stackNetworkId,
                            finalSourceItem.displayname,
                            finalSourceItem.durability,
                        ),
                    ),
                ),
            )
            val finalDestinationItem = destinationItem
            entryList.add(
                ContainerEntry(
                    destination.container,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            destination.slot,
                            destination.slot,
                            finalDestinationItem.amount.toByte(),
                            finalDestinationItem.stackNetworkId,
                            finalDestinationItem.displayname,
                            finalDestinationItem.durability,
                        ),
                    ),
                ),
            )
        }
        return listOf(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.requestId,
                entryList,
            ),
        )
    }

    private fun handleSwapAction(
        player: Player,
        actionData: SwapStackRequestActionData,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponsePacket.Response> {
        val source: StackRequestSlotInfoData = actionData.source
        val destination: StackRequestSlotInfoData = actionData.destination
        val sourceItem = getItem(player, source.container, source.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot.toInt()}")
            return emptyList()
        }
        val destinationItem = getItem(player, destination.container, destination.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown destination item slot ${destination.slot.toInt()}")
            return emptyList()
        }
        this.setItem(player, source.container, source.slot.toInt(), destinationItem)
        this.setItem(player, destination.container, destination.slot.toInt(), sourceItem)
        val containerEntryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        containerEntryList.add(
            ContainerEntry(
                destination.container,
                listOf(
                    ItemStackResponsePacket.ItemEntry(
                        destination.slot,
                        destination.slot,
                        sourceItem.amount.toByte(),
                        sourceItem.stackNetworkId,
                        sourceItem.displayname,
                        sourceItem.durability,
                    ),
                ),
            ),
        )
        containerEntryList.add(
            ContainerEntry(
                source.container,
                listOf(
                    ItemStackResponsePacket.ItemEntry(
                        source.slot,
                        source.slot,
                        destinationItem.amount.toByte(),
                        destinationItem.stackNetworkId,
                        destinationItem.displayname,
                        destinationItem.durability,
                    ),
                ),
            ),
        )
        return listOf(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.requestId,
                containerEntryList,
            ),
        )
    }

    private fun handleDestroyAction(
        player: Player,
        actionData: DestroyStackRequestActionData,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = actionData.count
        val source: StackRequestSlotInfoData = actionData.source
        var sourceItem = getItem(player, source.container, source.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot.toInt()}")
            return emptyList()
        }
        sourceItem.setAmount(sourceItem.amount - amount)
        if (sourceItem.amount <= 0) {
            sourceItem = Item.create<Item>(ItemType.AIR)
            this.setItem(player, source.container, source.slot.toInt(), sourceItem)
        }
        val finalSourceItem = sourceItem
        return listOf(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.requestId,
                listOf(
                    ContainerEntry(
                        source.container,
                        listOf(
                            ItemStackResponsePacket.ItemEntry(
                                source.slot,
                                source.slot,
                                finalSourceItem.amount.toByte(),
                                finalSourceItem.stackNetworkId,
                                finalSourceItem.displayname,
                                finalSourceItem.durability,
                            ),
                        ),
                    ),
                ),
            ),
        )
    }

    private fun handleDropItemAction(
        player: Player,
        dropStackRequestActionData: DropStackRequestActionData,
        itemStackRequest: ItemStackRequest,
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = dropStackRequestActionData.count
        val source: StackRequestSlotInfoData = dropStackRequestActionData.source
        val sourceInventory = getInventory(player, source.container) ?: run {
            Server.instance.logger.debug("Unknown source inventory ${source.container.name}")
            return emptyList()
        }
        var sourceItem = getItem(player, source.container, source.slot.toInt()) ?: run {
            Server.instance.logger.debug("Unknown source item slot ${source.slot.toInt()}")
            return emptyList()
        }
        val playerDropItemEvent = PlayerDropItemEvent(player, sourceItem)
        Server.instance.pluginManager.callEvent(playerDropItemEvent)
        if (playerDropItemEvent.isCancelled || sourceInventory.type == InventoryType.ARMOR && sourceItem.getEnchantment(
                EnchantmentType.CURSE_OF_BINDING,
            ) != null
        ) {
            sourceInventory.setItem(source.slot.toInt(), sourceItem)
            return listOf(
                ItemStackResponsePacket.Response(
                    ItemStackResponsePacket.ResponseStatus.ERROR,
                    itemStackRequest.requestId,
                    emptyList<ContainerEntry>(),
                ),
            )
        }
        sourceItem.setAmount(sourceItem.amount - amount)
        val dropItem = sourceItem.clone()
        dropItem.setAmount(amount.toInt())
        if (sourceItem.amount <= 0) {
            sourceItem = Item.create<Item>(ItemType.AIR)
        }
        this.setItem(player, source.container, source.slot.toInt(), sourceItem)
        val entityItem = player.world?.dropItem(
            dropItem,
            player.location.add(0f, player.eyeHeight, 0f),
            player.location.direction.multiply(0.4f, 0.4f, 0.4f),
        )!!
        entityItem.playerHasThrown = player
        entityItem.spawn()
        val finalSourceItem = sourceItem
        return listOf(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.requestId,
                listOf(
                    ContainerEntry(
                        source.container,
                        listOf(
                            ItemStackResponsePacket.ItemEntry(
                                source.slot,
                                source.slot,
                                finalSourceItem.amount.toByte(),
                                finalSourceItem.stackNetworkId,
                                finalSourceItem.displayname,
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
    ): ItemStackResponsePacket.Response {
        Server.instance.scheduler.scheduleDelayed(
            Runnable {
                sourceInventory.setItem(sourceSlot, sourceItem)
                sourceInventory.sendContents(sourceSlot, player)
                destinationInventory.setItem(destinationSlot, destinationItem)
                destinationInventory.sendContents(destinationSlot, player)
            },
            20,
        )
        return ItemStackResponsePacket.Response(
            ItemStackResponsePacket.ResponseStatus.OK,
            requestId,
            listOf(
                ContainerEntry(
                    ContainerSlotType.HOTBAR,
                    listOf(
                        ItemStackResponsePacket.ItemEntry(
                            sourceSlot.toByte(),
                            sourceSlot.toByte(),
                            sourceItem.amount.toByte(),
                            sourceItem.stackNetworkId,
                            sourceItem.displayname,
                            sourceItem.durability,
                        ),
                    ),
                ),
                ContainerEntry(
                    ContainerSlotType.CURSOR,
                    listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            destinationSlot.toByte(),
                            destinationSlot.toByte(),
                            destinationItem.amount.toByte(),
                            destinationItem.stackNetworkId,
                            destinationItem.displayname,
                            destinationItem.durability,
                        ),
                    ),
                ),
            ),
        )
    }

    private fun getInventory(player: Player, containerSlotType: ContainerSlotType): Inventory? {
        return when (containerSlotType) {
            ContainerSlotType.CREATIVE_OUTPUT -> player.getCreativeItemCacheInventory()
            ContainerSlotType.CURSOR -> player.getCursorInventory()
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory
            ContainerSlotType.ARMOR -> player.armorInventory
            ContainerSlotType.CONTAINER, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_OUTPUT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_LAPIS -> player.currentInventory
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
            ContainerSlotType.CREATIVE_OUTPUT -> player.getCreativeItemCacheInventory().getItem(0)
            ContainerSlotType.CURSOR -> player.getCursorInventory().getItem(slot)
            ContainerSlotType.OFFHAND -> player.getOffHandInventory().getItem(slot)
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory.getItem(
                slot,
            )

            ContainerSlotType.ARMOR -> player.armorInventory.getItem(slot)
            ContainerSlotType.CONTAINER, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_OUTPUT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_LAPIS -> player.currentInventory?.getItem(
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
            ContainerSlotType.CONTAINER, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_OUTPUT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_LAPIS -> player.currentInventory?.setItem(
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
