package org.jukeboxmc.network.handler

import java.util.Arrays
import java.util.LinkedList
import org.jukeboxmc.Server
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.CreativeItems

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStackRequestHandler : PacketHandler<ItemStackRequestPacket> {
    override fun handle(packet: ItemStackRequestPacket, server: Server, player: Player) {
        val responses: MutableList<ItemStackResponsePacket.Response> = LinkedList<ItemStackResponsePacket.Response>()
        for (request in packet.getRequests()) {
            val itemEntryMap: MutableMap<Int, MutableList<ItemStackResponsePacket.ItemEntry>> =
                HashMap<Int, MutableList<ItemStackResponsePacket.ItemEntry>>()
            for (action in request.getActions()) {
                when (action.getType()) {
                    StackRequestActionType.CONSUME -> {
                        val itemEntry: ItemStackResponsePacket.ItemEntry =
                            handleConsumeAction(player, action as ConsumeStackRequestActionData, request)[0]
                        if (!itemEntryMap.containsKey(request.getRequestId())) {
                            itemEntryMap[request.getRequestId()] =
                                object : LinkedList<ItemStackResponsePacket.ItemEntry?>() {
                                    init {
                                        add(itemEntry)
                                    }
                                }
                        } else {
                            itemEntryMap[request.getRequestId()]!!.add(itemEntry)
                        }
                    }

                    StackRequestActionType.CRAFT_CREATIVE -> handleCraftCreativeAction(
                        player,
                        action as CraftCreativeStackRequestActionData
                    )

                    StackRequestActionType.CRAFT_RECIPE -> responses.addAll(
                        handleCraftRecipeAction(
                            player,
                            action as CraftRecipeStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.TAKE -> responses.addAll(
                        handleTakeStackRequestAction(
                            player,
                            action as TakeStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.PLACE -> responses.addAll(
                        handlePlaceAction(
                            player,
                            action as PlaceStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.DESTROY -> responses.addAll(
                        handleDestroyAction(
                            player,
                            action as DestroyStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.SWAP -> responses.addAll(
                        handleSwapAction(
                            player,
                            action as SwapStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.DROP -> responses.addAll(
                        handleDropItemAction(
                            player,
                            action as DropStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.CRAFT_RECIPE_OPTIONAL -> responses.addAll(
                        handleCraftRecipeOptionalAction(
                            player,
                            action as CraftRecipeOptionalStackRequestActionData,
                            request
                        )
                    )

                    StackRequestActionType.CRAFT_RESULTS_DEPRECATED -> {
                        //responses.addAll( this.handleCraftResult( player, (CraftResultsDeprecatedStackRequestActionData) action, requestId ) );
                    }

                    else -> server.logger.info("Unhandelt Action: " + action.javaClass.getSimpleName() + " : " + action.getType())
                }
            }
            val itemStackResponsePacket = ItemStackResponsePacket()
            val containerEntryMap: MutableMap<Int, List<ContainerEntry>> = HashMap<Int, List<ContainerEntry>>()
            if (!itemEntryMap.isEmpty()) {
                for (respons in responses) {
                    containerEntryMap[respons.getRequestId()] = respons.getContainers()
                }
                if (containerEntryMap.containsKey(request.getRequestId())) {
                    containerEntryMap[request.getRequestId()].add(
                        0,
                        ContainerEntry(ContainerSlotType.CRAFTING_INPUT, itemEntryMap[request.getRequestId()])
                    )
                }
                for ((key, value) in containerEntryMap) {
                    itemStackResponsePacket.getEntries()
                        .add(ItemStackResponsePacket.Response(ItemStackResponsePacket.ResponseStatus.OK, key, value))
                }
            } else {
                itemStackResponsePacket.getEntries().addAll(responses)
            }
            player.playerConnection.sendPacket(itemStackResponsePacket)
        }
    }

    private fun handleCraftRecipeOptionalAction(
        player: Player,
        action: CraftRecipeOptionalStackRequestActionData,
        request: ItemStackRequest
    ): Collection<ItemStackResponsePacket.Response> {
        return emptyList<ItemStackResponsePacket.Response>()
    }

    private fun handleCraftResult(
        player: Player,
        action: CraftResultsDeprecatedStackRequestActionData,
        requestId: Int
    ): Collection<ItemStackResponsePacket.Response> {
        val craftingGridInventory: CraftingGridInventory? = player.craftingGridInventory
        val itemEntries: MutableList<ItemStackResponsePacket.ItemEntry> =
            LinkedList<ItemStackResponsePacket.ItemEntry>()
        for (slot in craftingGridInventory.getOffset() until craftingGridInventory.getSize() + craftingGridInventory.getOffset()) {
            val item: Item = craftingGridInventory.getItem(slot)
            itemEntries.add(
                ItemStackResponsePacket.ItemEntry(
                    slot.toByte(),
                    slot.toByte(),
                    item.amount.toByte(),
                    item.stackNetworkId,
                    item.displayname,
                    item.durability
                )
            )
        }
        val containerEntryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        containerEntryList.add(ContainerEntry(ContainerSlotType.CRAFTING_INPUT, itemEntries))
        return listOf<ItemStackResponsePacket.Response>(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                requestId,
                containerEntryList
            )
        )
    }

    private fun handleConsumeAction(
        player: Player,
        action: ConsumeStackRequestActionData,
        request: ItemStackRequest
    ): List<ItemStackResponsePacket.ItemEntry> {
        val amount: Byte = action.getCount()
        val source: StackRequestSlotInfoData = action.getSource()
        var sourceItem = getItem(player, source.getContainer(), source.getSlot().toInt())
        if (sourceItem == null) {
            sourceItem = Item.Companion.create<Item>(ItemType.AIR)
        } else {
            sourceItem.amount = sourceItem.amount - amount
        }
        if (sourceItem.amount <= 0) {
            sourceItem = Item.Companion.create<Item>(ItemType.AIR)
        }
        this.setItem(player, source.getContainer(), source.getSlot().toInt(), sourceItem)
        val containerEntryList: MutableList<ItemStackResponsePacket.ItemEntry> =
            LinkedList<ItemStackResponsePacket.ItemEntry>()
        containerEntryList.add(
            ItemStackResponsePacket.ItemEntry(
                source.getSlot(),
                source.getSlot(), sourceItem.amount.toByte(),
                sourceItem.stackNetworkId,
                sourceItem.displayname,
                sourceItem.durability
            )
        )
        return containerEntryList
    }

    private fun handleCraftRecipeAction(
        player: Player,
        action: CraftRecipeStackRequestActionData,
        request: ItemStackRequest
    ): List<ItemStackResponsePacket.Response> {
        val resultItem: List<Item> =
            Server.Companion.getInstance().getCraftingManager().getResultItem(action.getRecipeNetworkId())
        player.creativeItemCacheInventory.setItem(0, resultItem[0])
        return emptyList<ItemStackResponsePacket.Response>()
    }

    private fun handleCraftCreativeAction(player: Player, actionData: CraftCreativeStackRequestActionData) {
        val itemData = CreativeItems.getCreativeItems()[actionData.getCreativeItemNetworkId() - 1]
        val item: Item = Item.Companion.create<Item>(itemData)
        item.amount = item.maxStackSize
        player.creativeItemCacheInventory.setItem(0, item)
    }

    private fun handlePlaceAction(
        player: Player,
        actionData: PlaceStackRequestActionData,
        itemStackRequest: ItemStackRequest
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = actionData.getCount()
        val source: StackRequestSlotInfoData = actionData.getSource()
        val destination: StackRequestSlotInfoData = actionData.getDestination()
        val containerEntryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        var sourceItem = getItem(player, source.getContainer(), source.getSlot().toInt())
        var destinationItem = getItem(player, destination.getContainer(), destination.getSlot().toInt())
        if (source.getContainer() == ContainerSlotType.CREATIVE_OUTPUT) {
            val sourceInventory = getInventory(player, source.getContainer())
            val destinationInventory = getInventory(player, destination.getContainer())
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.getSlot().toInt()
            )
            Server.Companion.getInstance().getPluginManager().callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled()) {
                sourceInventory!!.setItem(source.getSlot().toInt(), sourceItem!!)
                return java.util.List.of<ItemStackResponsePacket.Response>(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.getRequestId(),
                        emptyList<ContainerEntry>()
                    )
                )
            }
            sourceItem!!.stackNetworkId = Item.Companion.stackNetworkCount++
            if (destinationItem.getType() != ItemType.AIR) {
                sourceItem.amount = Math.min(destinationItem.getAmount() + sourceItem.amount, sourceItem.maxStackSize)
            }
            this.setItem(player, destination.getContainer(), destination.getSlot().toInt(), sourceItem)
            containerEntryList.add(
                ContainerEntry(
                    destination.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            destination.getSlot(),
                            destination.getSlot(), sourceItem.amount.toByte(),
                            sourceItem.stackNetworkId,
                            sourceItem.displayname,
                            sourceItem.durability
                        )
                    )
                )
            )
        } else {
            val sourceInventory = getInventory(player, source.getContainer())
            val destinationInventory = getInventory(player, destination.getContainer())
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.getSlot().toInt()
            )
            Server.Companion.getInstance().getPluginManager().callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled() || sourceInventory.getType() == InventoryType.ARMOR && sourceItem!!.getEnchantment(
                    EnchantmentType.CURSE_OF_BINDING
                ) != null
            ) {
                sourceInventory!!.setItem(source.getSlot().toInt(), sourceItem!!)
                return java.util.List.of<ItemStackResponsePacket.Response>(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.getRequestId(),
                        emptyList<ContainerEntry>()
                    )
                )
            }
            if (destinationItem == sourceItem && sourceItem.getAmount() > 0) {
                sourceItem!!.amount = sourceItem.amount - amount
                if (sourceItem.amount <= 0) {
                    sourceItem = Item.Companion.create<Item>(ItemType.AIR)
                }
                destinationItem!!.amount = destinationItem.amount + amount
            } else if (destinationItem.getType() == ItemType.AIR) {
                if (sourceItem.getAmount() == amount.toInt()) {
                    destinationItem = sourceItem!!.clone()
                    sourceItem = Item.Companion.create<Item>(ItemType.AIR)
                } else {
                    destinationItem = sourceItem!!.clone()
                    destinationItem.amount = amount.toInt()
                    destinationItem.stackNetworkId = Item.Companion.stackNetworkCount++
                    sourceItem.amount = sourceItem.amount - amount
                }
            }
            this.setItem(player, source.getContainer(), source.getSlot().toInt(), sourceItem)
            this.setItem(player, destination.getContainer(), destination.getSlot().toInt(), destinationItem)
            val finalSourceItem = sourceItem
            containerEntryList.add(
                ContainerEntry(
                    source.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            source.getSlot(),
                            source.getSlot(), finalSourceItem.getAmount().toByte(),
                            finalSourceItem.getStackNetworkId(),
                            finalSourceItem.getDisplayname(),
                            finalSourceItem.getDurability()
                        )
                    )
                )
            )
            val finalDestinationItem = destinationItem
            containerEntryList.add(
                ContainerEntry(
                    destination.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            destination.getSlot(),
                            destination.getSlot(), finalDestinationItem.getAmount().toByte(),
                            finalDestinationItem.getStackNetworkId(),
                            finalDestinationItem.getDisplayname(),
                            finalDestinationItem.getDurability()
                        )
                    )
                )
            )
        }
        return listOf<ItemStackResponsePacket.Response>(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.getRequestId(),
                containerEntryList
            )
        )
    }

    private fun handleTakeStackRequestAction(
        player: Player,
        actionData: TakeStackRequestActionData,
        itemStackRequest: ItemStackRequest
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = actionData.getCount()
        val source: StackRequestSlotInfoData = actionData.getSource()
        val destination: StackRequestSlotInfoData = actionData.getDestination()
        val entryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        var sourceItem = getItem(player, source.getContainer(), source.getSlot().toInt())
        var destinationItem = getItem(player, destination.getContainer(), destination.getSlot().toInt())
        if (source.getContainer() == ContainerSlotType.CREATIVE_OUTPUT) {
            val sourceInventory = getInventory(player, source.getContainer())
            val destinationInventory = getInventory(player, destination.getContainer())
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.getSlot().toInt()
            )
            Server.Companion.getInstance().getPluginManager().callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled()) {
                sourceInventory!!.setItem(source.getSlot().toInt(), sourceItem!!)
                return java.util.List.of<ItemStackResponsePacket.Response>(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.getRequestId(),
                        emptyList<ContainerEntry>()
                    )
                )
            }
            sourceItem!!.stackNetworkId = Item.Companion.stackNetworkCount++
            if (destinationItem.getType() != ItemType.AIR) {
                sourceItem.amount = Math.min(destinationItem.getAmount() + sourceItem.amount, sourceItem.maxStackSize)
            }
            this.setItem(player, destination.getContainer(), destination.getSlot().toInt(), sourceItem)
            entryList.add(
                ContainerEntry(
                    destination.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            destination.getSlot(),
                            destination.getSlot(), sourceItem.amount.toByte(),
                            sourceItem.stackNetworkId,
                            sourceItem.displayname,
                            sourceItem.durability
                        )
                    )
                )
            )
        } else {
            val sourceInventory = getInventory(player, source.getContainer())
            val destinationInventory = getInventory(player, destination.getContainer())
            val inventoryClickEvent = InventoryClickEvent(
                sourceInventory,
                destinationInventory,
                player,
                sourceItem,
                destinationItem,
                source.getSlot().toInt()
            )
            Server.Companion.getInstance().getPluginManager().callEvent(inventoryClickEvent)
            if (inventoryClickEvent.isCancelled() || sourceInventory.getType() == InventoryType.ARMOR && sourceItem!!.getEnchantment(
                    EnchantmentType.CURSE_OF_BINDING
                ) != null
            ) {
                sourceInventory!!.setItem(source.getSlot().toInt(), sourceItem!!)
                return java.util.List.of<ItemStackResponsePacket.Response>(
                    ItemStackResponsePacket.Response(
                        ItemStackResponsePacket.ResponseStatus.ERROR,
                        itemStackRequest.getRequestId(),
                        emptyList<ContainerEntry>()
                    )
                )
            }
            if (destinationItem == sourceItem && sourceItem.getAmount() > 0) {
                sourceItem!!.amount = sourceItem.amount - amount
                if (sourceItem.amount <= 0) {
                    sourceItem = Item.Companion.create<Item>(ItemType.AIR)
                }
                destinationItem!!.amount = destinationItem.amount + amount
            } else if (destinationItem.getType() == ItemType.AIR) {
                if (sourceItem.getAmount() == amount.toInt()) {
                    destinationItem = sourceItem!!.clone()
                    sourceItem = Item.Companion.create<Item>(ItemType.AIR)
                } else {
                    destinationItem = sourceItem!!.clone()
                    destinationItem.amount = amount.toInt()
                    destinationItem.stackNetworkId = Item.Companion.stackNetworkCount++
                    sourceItem.amount = sourceItem.amount - amount
                }
            }
            this.setItem(player, source.getContainer(), source.getSlot().toInt(), sourceItem)
            this.setItem(player, destination.getContainer(), destination.getSlot().toInt(), destinationItem)
            val finalSourceItem = sourceItem
            entryList.add(
                ContainerEntry(
                    source.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            source.getSlot(),
                            source.getSlot(), finalSourceItem.getAmount().toByte(),
                            finalSourceItem.getStackNetworkId(),
                            finalSourceItem.getDisplayname(),
                            finalSourceItem.getDurability()
                        )
                    )
                )
            )
            val finalDestinationItem = destinationItem
            entryList.add(
                ContainerEntry(
                    destination.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            destination.getSlot(),
                            destination.getSlot(), finalDestinationItem.getAmount().toByte(),
                            finalDestinationItem.getStackNetworkId(),
                            finalDestinationItem.getDisplayname(),
                            finalDestinationItem.getDurability()
                        )
                    )
                )
            )
        }
        return listOf<ItemStackResponsePacket.Response>(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.getRequestId(),
                entryList
            )
        )
    }

    private fun handleSwapAction(
        player: Player,
        actionData: SwapStackRequestActionData,
        itemStackRequest: ItemStackRequest
    ): List<ItemStackResponsePacket.Response> {
        val source: StackRequestSlotInfoData = actionData.getSource()
        val destination: StackRequestSlotInfoData = actionData.getDestination()
        val sourceItem = getItem(player, source.getContainer(), source.getSlot().toInt())
        val destinationItem = getItem(player, destination.getContainer(), destination.getSlot().toInt())
        this.setItem(player, source.getContainer(), source.getSlot().toInt(), destinationItem)
        this.setItem(player, destination.getContainer(), destination.getSlot().toInt(), sourceItem)
        val containerEntryList: MutableList<ContainerEntry> = LinkedList<ContainerEntry>()
        containerEntryList.add(
            ContainerEntry(
                destination.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                    ItemStackResponsePacket.ItemEntry(
                        destination.getSlot(),
                        destination.getSlot(), sourceItem.getAmount().toByte(),
                        sourceItem.getStackNetworkId(),
                        sourceItem.getDisplayname(),
                        sourceItem.getDurability()
                    )
                )
            )
        )
        containerEntryList.add(
            ContainerEntry(
                source.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                    ItemStackResponsePacket.ItemEntry(
                        source.getSlot(),
                        source.getSlot(), destinationItem.getAmount().toByte(),
                        destinationItem.getStackNetworkId(),
                        destinationItem.getDisplayname(),
                        destinationItem.getDurability()
                    )
                )
            )
        )
        return listOf<ItemStackResponsePacket.Response>(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK,
                itemStackRequest.getRequestId(),
                containerEntryList
            )
        )
    }

    private fun handleDestroyAction(
        player: Player,
        actionData: DestroyStackRequestActionData,
        itemStackRequest: ItemStackRequest
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = actionData.getCount()
        val source: StackRequestSlotInfoData = actionData.getSource()
        var sourceItem = getItem(player, source.getContainer(), source.getSlot().toInt())
        sourceItem!!.amount = sourceItem.amount - amount
        if (sourceItem.amount <= 0) {
            sourceItem = Item.Companion.create<Item>(ItemType.AIR)
            this.setItem(player, source.getContainer(), source.getSlot().toInt(), sourceItem)
        }
        val finalSourceItem = sourceItem
        return listOf<ItemStackResponsePacket.Response>(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK, itemStackRequest.getRequestId(), listOf<ContainerEntry>(
                    ContainerEntry(
                        source.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                            ItemStackResponsePacket.ItemEntry(
                                source.getSlot(),
                                source.getSlot(), finalSourceItem.amount.toByte(),
                                finalSourceItem.stackNetworkId,
                                finalSourceItem.displayname,
                                finalSourceItem.durability
                            )
                        )
                    )
                )
            )
        )
    }

    private fun handleDropItemAction(
        player: Player,
        dropStackRequestActionData: DropStackRequestActionData,
        itemStackRequest: ItemStackRequest
    ): List<ItemStackResponsePacket.Response> {
        val amount: Byte = dropStackRequestActionData.getCount()
        val source: StackRequestSlotInfoData = dropStackRequestActionData.getSource()
        val sourceInventory = getInventory(player, source.getContainer())
        var sourceItem = getItem(player, source.getContainer(), source.getSlot().toInt())
        val playerDropItemEvent = PlayerDropItemEvent(player, sourceItem)
        Server.Companion.getInstance().getPluginManager().callEvent(playerDropItemEvent)
        if (playerDropItemEvent.isCancelled() || sourceInventory.getType() == InventoryType.ARMOR && sourceItem!!.getEnchantment(
                EnchantmentType.CURSE_OF_BINDING
            ) != null
        ) {
            sourceInventory!!.setItem(source.getSlot().toInt(), sourceItem!!)
            return java.util.List.of<ItemStackResponsePacket.Response>(
                ItemStackResponsePacket.Response(
                    ItemStackResponsePacket.ResponseStatus.ERROR,
                    itemStackRequest.getRequestId(),
                    emptyList<ContainerEntry>()
                )
            )
        }
        sourceItem!!.amount = sourceItem.amount - amount
        val dropItem = sourceItem.clone()
        dropItem.amount = amount.toInt()
        if (sourceItem.amount <= 0) {
            sourceItem = Item.Companion.create<Item>(ItemType.AIR)
        }
        this.setItem(player, source.getContainer(), source.getSlot().toInt(), sourceItem)
        val entityItem = player.world.dropItem(
            dropItem,
            player.location.add(0f, player.eyeHeight, 0f),
            player.location.direction.multiply(0.4f, 0.4f, 0.4f)
        )
        entityItem.playerHasThrown = player
        entityItem.spawn()
        val finalSourceItem = sourceItem
        return listOf<ItemStackResponsePacket.Response>(
            ItemStackResponsePacket.Response(
                ItemStackResponsePacket.ResponseStatus.OK, itemStackRequest.getRequestId(), listOf<ContainerEntry>(
                    ContainerEntry(
                        source.getContainer(), listOf<ItemStackResponsePacket.ItemEntry>(
                            ItemStackResponsePacket.ItemEntry(
                                source.getSlot(),
                                source.getSlot(), finalSourceItem.amount.toByte(),
                                finalSourceItem.stackNetworkId,
                                finalSourceItem.displayname,
                                finalSourceItem.durability
                            )
                        )
                    )
                )
            )
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
        destinationItem: Item
    ): ItemStackResponsePacket.Response {
        Server.Companion.getInstance().getScheduler().scheduleDelayed(Runnable {
            sourceInventory.setItem(sourceSlot, sourceItem)
            sourceInventory.sendContents(sourceSlot, player)
            destinationInventory.setItem(destinationSlot, destinationItem)
            destinationInventory.sendContents(destinationSlot, player)
        }, 20)
        return ItemStackResponsePacket.Response(
            ItemStackResponsePacket.ResponseStatus.OK, requestId, Arrays.asList<ContainerEntry>(
                ContainerEntry(
                    ContainerSlotType.HOTBAR, listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            sourceSlot.toByte(), sourceSlot.toByte(), sourceItem.amount.toByte(),
                            sourceItem.stackNetworkId,
                            sourceItem.displayname,
                            sourceItem.durability
                        )
                    )
                ),
                ContainerEntry(
                    ContainerSlotType.CURSOR, listOf<ItemStackResponsePacket.ItemEntry>(
                        ItemStackResponsePacket.ItemEntry(
                            destinationSlot.toByte(), destinationSlot.toByte(), destinationItem.amount.toByte(),
                            destinationItem.stackNetworkId,
                            destinationItem.displayname,
                            destinationItem.durability
                        )
                    )
                )
            )
        )
    }

    private fun getInventory(player: Player, containerSlotType: ContainerSlotType): Inventory? {
        return when (containerSlotType) {
            ContainerSlotType.CREATIVE_OUTPUT -> player.creativeItemCacheInventory
            ContainerSlotType.CURSOR -> player.cursorInventory
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory
            ContainerSlotType.ARMOR -> player.armorInventory
            ContainerSlotType.CONTAINER, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_OUTPUT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_LAPIS -> player.currentInventory
            ContainerSlotType.CRAFTING_INPUT -> player.craftingGridInventory
            ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_RESULT -> player.cartographyTableInventory
            ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT -> player.smithingTableInventory
            ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT -> player.anvilInventory
            ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT -> player.stoneCutterInventory
            ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT -> player.grindstoneInventory
            else -> null
        }
    }

    private fun getItem(player: Player, containerSlotType: ContainerSlotType, slot: Int): Item? {
        return when (containerSlotType) {
            ContainerSlotType.CREATIVE_OUTPUT -> player.creativeItemCacheInventory.getItem(0)
            ContainerSlotType.CURSOR -> player.cursorInventory.getItem(slot)
            ContainerSlotType.OFFHAND -> player.offHandInventory.getItem(slot)
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory.getItem(
                slot
            )

            ContainerSlotType.ARMOR -> player.armorInventory.getItem(slot)
            ContainerSlotType.CONTAINER, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_OUTPUT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_LAPIS -> player.currentInventory.getItem(
                slot
            )

            ContainerSlotType.CRAFTING_INPUT -> player.craftingGridInventory.getItem(slot)
            ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_RESULT -> player.cartographyTableInventory.getItem(
                slot
            )

            ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT -> player.smithingTableInventory.getItem(
                slot
            )

            ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT -> player.anvilInventory.getItem(
                slot
            )

            ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT -> player.stoneCutterInventory.getItem(
                slot
            )

            ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT -> player.grindstoneInventory.getItem(
                slot
            )

            else -> null
        }
    }

    private fun setItem(player: Player, containerSlotType: ContainerSlotType, slot: Int, item: Item?) {
        this.setItem(player, containerSlotType, slot, item, true)
    }

    private fun setItem(
        player: Player,
        containerSlotType: ContainerSlotType,
        slot: Int,
        item: Item?,
        sendContent: Boolean
    ) {
        when (containerSlotType) {
            ContainerSlotType.CURSOR -> player.cursorInventory.setItem(slot, item, sendContent)
            ContainerSlotType.OFFHAND -> player.offHandInventory.setItem(slot, item, sendContent)
            ContainerSlotType.INVENTORY, ContainerSlotType.HOTBAR, ContainerSlotType.HOTBAR_AND_INVENTORY -> player.inventory.setItem(
                slot,
                item,
                sendContent
            )

            ContainerSlotType.ARMOR -> player.armorInventory.setItem(slot, item, sendContent)
            ContainerSlotType.CONTAINER, ContainerSlotType.BARREL, ContainerSlotType.BREWING_RESULT, ContainerSlotType.BREWING_FUEL, ContainerSlotType.BREWING_INPUT, ContainerSlotType.FURNACE_FUEL, ContainerSlotType.FURNACE_INGREDIENT, ContainerSlotType.FURNACE_OUTPUT, ContainerSlotType.BLAST_FURNACE_INGREDIENT, ContainerSlotType.ENCHANTING_INPUT, ContainerSlotType.ENCHANTING_LAPIS -> player.currentInventory.setItem(
                slot,
                item,
                sendContent
            )

            ContainerSlotType.CRAFTING_INPUT -> player.craftingGridInventory.setItem(slot, item, sendContent)
            ContainerSlotType.CARTOGRAPHY_ADDITIONAL, ContainerSlotType.CARTOGRAPHY_INPUT, ContainerSlotType.CARTOGRAPHY_RESULT -> player.cartographyTableInventory.setItem(
                slot,
                item,
                sendContent
            )

            ContainerSlotType.SMITHING_TABLE_INPUT, ContainerSlotType.SMITHING_TABLE_MATERIAL, ContainerSlotType.SMITHING_TABLE_RESULT -> player.smithingTableInventory.setItem(
                slot,
                item,
                sendContent
            )

            ContainerSlotType.ANVIL_INPUT, ContainerSlotType.ANVIL_MATERIAL, ContainerSlotType.ANVIL_RESULT -> player.anvilInventory.setItem(
                slot,
                item,
                sendContent
            )

            ContainerSlotType.STONECUTTER_INPUT, ContainerSlotType.STONECUTTER_RESULT -> player.stoneCutterInventory.setItem(
                slot,
                item,
                sendContent
            )

            ContainerSlotType.GRINDSTONE_ADDITIONAL, ContainerSlotType.GRINDSTONE_INPUT, ContainerSlotType.GRINDSTONE_RESULT -> player.grindstoneInventory.setItem(
                slot,
                item,
                sendContent
            )

            else -> {}
        }
    }
}