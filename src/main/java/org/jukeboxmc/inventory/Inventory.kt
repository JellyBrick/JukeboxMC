package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import java.util.Arrays
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Inventory {
    protected val viewer: MutableSet<Player>
    protected var holder: InventoryHolder?
    open var size: Int
        protected set
    open var contents: Array<Item?>
        protected set
    var holderId: Long = -1
        protected set

    constructor(holder: InventoryHolder, size: Int) {
        this.holder = holder
        this.size = size
        viewer = LinkedHashSet()
        if (holder is Player) {
            holderId = holder.entityId
        }
        contents = arrayOfNulls(size)
        Arrays.fill(contents, Item.Companion.create<Item>(ItemType.AIR))
    }

    constructor(holder: InventoryHolder?, holderId: Int, size: Int) {
        this.holder = holder
        this.size = size
        viewer = LinkedHashSet()
        this.holderId = holderId.toLong()
        contents = arrayOfNulls(size)
        Arrays.fill(contents, Item.Companion.create<Item>(ItemType.AIR))
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

    fun getViewer(): Set<Player> {
        return viewer
    }

    open fun setItem(slot: Int, item: Item) {
        this.setItem(slot, item, true)
    }

    open fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        if (slot < 0 || slot >= size) {
            return
        }
        if (item.amount <= 0 || item.type == ItemType.AIR) {
            contents[slot] = Item.Companion.create<Item>(ItemType.AIR)
        } else {
            contents[slot] = item
        }
        if (sendContent) {
            for (player in viewer) {
                this.sendContents(slot, player)
            }
        }
    }

    open fun getItem(slot: Int): Item? {
        val content = contents[slot]
        return content ?: Item.Companion.create<Item>(ItemType.AIR)
    }

    fun canAddItem(item: Item): Boolean {
        var amount = item.amount
        for (content in contents) {
            if (content == null || content.type == ItemType.AIR) {
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
                if (contents[i] == clone && contents[i].getAmount() <= contents[i].getMaxStackSize()) {
                    if (contents[i].getAmount() + clone.amount <= contents[i].getMaxStackSize()) {
                        contents[i]!!.amount = contents[i].getAmount() + clone.amount
                        clone.amount = 0
                    } else {
                        val amountToDecrease = contents[i].getMaxStackSize() - contents[i].getAmount()
                        contents[i]!!.amount = contents[i].getMaxStackSize()
                        clone.amount = clone.amount - amountToDecrease
                    }
                    this.setItem(i, contents[i]!!, sendContents)
                    if (clone.amount == 0) {
                        return true
                    }
                }
            }
            for (i in contents.indices) {
                if (contents[i].getType() == ItemType.AIR) {
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
            if (contents[slot] == clone && contents[slot].getAmount() <= contents[slot].getMaxStackSize()) {
                if (contents[slot].getAmount() + clone.amount <= contents[slot].getMaxStackSize()) {
                    contents[slot]!!.amount = contents[slot].getAmount() + clone.amount
                    clone.amount = 0
                } else {
                    val amountToDecrease = contents[slot].getMaxStackSize() - contents[slot].getAmount()
                    contents[slot]!!.amount = contents[slot].getMaxStackSize()
                    clone.amount = clone.amount - amountToDecrease
                }
                this.setItem(slot, contents[slot]!!)
                if (clone.amount == 0) {
                    return true
                }
            }
            if (contents[slot].getType() == ItemType.AIR) {
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
        if (content != null && content.type != ItemType.AIR) {
            if (content.type == item.type && content.meta == item.meta) {
                content.amount = content.amount - item.amount
                if (content.amount <= 0) {
                    this.setItem(slot, Item.Companion.create<Item>(ItemType.AIR))
                } else {
                    this.setItem(slot, content)
                }
            }
        }
    }

    fun removeItem(itemType: ItemType, meta: Int, amount: Int) {
        for (i in 0 until size) {
            val content = getItem(i)
            if (content != null && content.type != ItemType.AIR) {
                if (content.type == itemType && content.meta == meta) {
                    content.amount = content.amount - amount
                    if (content.amount <= 0) {
                        this.setItem(i, Item.Companion.create<Item>(ItemType.AIR))
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

    fun clear(slot: Int) {
        this.setItem(slot, Item.Companion.create<Item>(ItemType.AIR))
    }

    open fun clear() {
        for (i in 0 until size) {
            val item = getItem(i)
            if (item != null && item.type != ItemType.AIR) {
                this.clear(i)
            }
        }
    }

    operator fun contains(item: Item): Boolean {
        for (content in contents) {
            if (content.getIdentifier() == item.identifier && content.getRuntimeId() == item.runtimeId &&
                (content.getBlockRuntimeId() == item.blockRuntimeId || content.getBlockRuntimeId() == 0 || item.blockRuntimeId == 0) && content.getMeta() == item.meta && content.getDisplayname() == item.displayname &&
                content.getEntchantments().containsAll(item.entchantments) && content.getAmount() == item.amount
            ) {
                return true
            }
        }
        return false
    }

    val itemDataContents: List<ItemData?>
        get() {
            val itemDataList: MutableList<ItemData?> = ArrayList()
            for (content in contents) {
                itemDataList.add(content!!.toItemData())
            }
            return itemDataList
        }
}