package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Inventory {
    val viewer: MutableSet<Player>
    protected var holder: InventoryHolder?
    private var _size: Int
    open var size: Int
        protected set(arg) {
            _size = arg
        }
        get() = _size
    protected open var contents: Array<Item>
    var holderId: Long = -1
        protected set

    constructor(holder: InventoryHolder, size: Int) {
        this.holder = holder
        this._size = size
        viewer = LinkedHashSet()
        if (holder is Player) {
            holderId = holder.entityId
        }
        this.contents = Array(size) { Item.create(ItemType.AIR) }
    }

    constructor(holder: InventoryHolder?, holderId: Int, size: Int) {
        this.holder = holder
        this._size = size
        viewer = LinkedHashSet()
        this.holderId = holderId.toLong()
        this.contents = Array(size) { Item.create(ItemType.AIR) }
    }

    abstract fun sendContents(player: Player)
    abstract fun sendContents(slot: Int, player: Player)
    abstract val inventoryHolder: InventoryHolder?
    abstract val type: InventoryType
    open val windowTypeId: ContainerType
        get() = ContainerType.INVENTORY

    fun addViewer(player: Player) {
        viewer.add(player)
    }

    open fun removeViewer(player: Player) {
        viewer.removeIf { target: Player -> target.uuid == player.uuid }
    }

    open fun setItem(slot: Int, item: Item) {
        this.setItem(slot, item, true)
    }

    open fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        if (slot < 0 || slot >= size) {
            return
        }
        if (item.amount <= 0 || item.type == ItemType.AIR) {
            contents[slot] = Item.create(ItemType.AIR)
        } else {
            contents[slot] = item.clone()
        }
        if (sendContent) {
            for (player in viewer) {
                this.sendContents(slot, player)
            }
        }
    }

    /**
     * @return the **cloned** [Item] in the specified slot.
     *
     * If you want to change item state, use [setItem] instead.
     */
    open fun getItem(slot: Int): Item {
        return contents[slot].clone()
    }

    fun canAddItem(item: Item): Boolean {
        var amount = item.amount
        for (content in contents) {
            if (content.type == ItemType.AIR) {
                return true
            } else if (content == item) {
                if (content.amount + item.amount <= content.maxStackSize) {
                    return true
                } else {
                    amount -= content.maxStackSize - content.amount
                    if (amount <= 0) {
                        return true
                    }
                }
            }
        }
        return false
    }

    @JvmOverloads
    fun addItem(item: Item, sendContents: Boolean = true): Boolean {
        if (canAddItem(item)) {
            val clone = item.clone()
            val contents = contents
            for (i in contents.indices) {
                if (contents[i] == clone && contents[i].amount <= contents[i].maxStackSize) {
                    if (contents[i].amount + clone.amount <= contents[i].maxStackSize) {
                        contents[i].setAmount(contents[i].amount + clone.amount)
                        clone.setAmount(0)
                    } else {
                        val amountToDecrease = contents[i].maxStackSize - contents[i].amount
                        contents[i].setAmount(contents[i].maxStackSize)
                        clone.setAmount(clone.amount - amountToDecrease)
                    }
                    this.setItem(i, contents[i], sendContents)
                    if (clone.amount == 0) {
                        return true
                    }
                }
            }
            for (i in contents.indices) {
                if (contents[i].type == ItemType.AIR) {
                    this.setItem(i, clone, sendContents)
                    return true
                }
            }
        } else {
            return false
        }
        return true
    }

    fun addItem(item: Item, slot: Int): Boolean {
        if (canAddItem(item)) {
            val clone = item.clone()
            if (contents[slot] == clone && contents[slot].amount <= contents[slot].maxStackSize) {
                if (contents[slot].amount + clone.amount <= contents[slot].maxStackSize) {
                    contents[slot].setAmount(contents[slot].amount + clone.amount)
                    clone.setAmount(0)
                } else {
                    val amountToDecrease = contents[slot].maxStackSize - contents[slot].amount
                    contents[slot].setAmount(contents[slot].maxStackSize)
                    clone.setAmount(clone.amount - amountToDecrease)
                }
                this.setItem(slot, contents[slot])
                if (clone.amount == 0) {
                    return true
                }
            }
            if (contents[slot].type == ItemType.AIR) {
                this.setItem(slot, clone)
                return true
            }
        } else {
            return false
        }
        return true
    }

    fun removeItem(slot: Int, item: Item) {
        val content = getItem(slot)
        if (content.type != ItemType.AIR) {
            if (content.type == item.type && content.meta == item.meta) {
                content.setAmount(content.amount - item.amount)
                if (content.amount <= 0) {
                    this.setItem(slot, Item.create(ItemType.AIR))
                } else {
                    this.setItem(slot, content)
                }
            }
        }
    }

    fun removeItem(itemType: ItemType, meta: Int, amount: Int) {
        for (i in 0 until size) {
            val content = getItem(i)
            if (content.type != ItemType.AIR) {
                if (content.type == itemType && content.meta == meta) {
                    content.setAmount(content.amount - amount)
                    if (content.amount <= 0) {
                        this.setItem(i, Item.create(ItemType.AIR))
                    } else {
                        this.setItem(i, content)
                    }
                    break
                }
            }
        }
    }

    fun removeItem(itemType: ItemType, amount: Int) {
        this.removeItem(itemType, 0, amount)
    }

    /**
     * @return the **cloned** contents of this inventory.
     */
    fun getContents(): Array<Item> = contents.clone()

    fun clear(slot: Int) {
        this.setItem(slot, Item.create(ItemType.AIR))
    }

    open fun clear() {
        for (i in 0 until size) {
            val item = getItem(i)
            if (item.type != ItemType.AIR) {
                this.clear(i)
            }
        }
    }

    operator fun contains(item: Item): Boolean {
        for (content in contents) {
            if (content.identifier == item.identifier && content.runtimeId == item.runtimeId &&
                (content.blockRuntimeId == item.blockRuntimeId || content.blockRuntimeId == 0 || item.blockRuntimeId == 0) && content.meta == item.meta && content.displayName == item.displayName &&
                content.entchantments.containsAll(item.entchantments) && content.amount == item.amount
            ) {
                return true
            }
        }
        return false
    }

    val itemDataContents: List<ItemData>
        get() {
            val itemDataList: MutableList<ItemData> = mutableListOf()
            for (content in contents) {
                itemDataList.add(content.toItemData())
            }
            return itemDataList
        }
}
