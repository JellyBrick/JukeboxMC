package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import com.nukkitx.nbt.NbtType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.ChestInventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Utils

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityChest(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType),
    InventoryHolder {
    val realInventory: ChestInventory
    private var doubleChestInventory: DoubleChestInventory? = null
    private var pairPosition: Vector? = null
    private var findable = false
    private var setPaired = false

    init {
        realInventory = ChestInventory(this)
    }

    override fun update(currentTick: Long) {
        if (isPaired && !setPaired && isSpawned) {
            val paired = getPaired() ?: return
            if (paired.isSpawned) {
                pair(paired)
            }
        }
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?
    ): Boolean {
        val chestInventory: ContainerInventory? = getChestInventory()
        player.openInventory(chestInventory, blockPosition)
        return true
    }

    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        val items = compound.getList("Items", NbtType.COMPOUND)
        for (nbtMap in items) {
            val item = toItem(nbtMap)
            val slot = nbtMap.getByte("Slot", 127.toByte())
            if (slot.toInt() == 127) {
                realInventory.addItem(item!!, false)
            } else {
                realInventory.setItem(slot.toInt(), item!!, false)
            }
        }
        pairPosition = Vector(compound.getInt("pairx", 0), getBlock().location.blockY, compound.getInt("pairz", 0))
        findable = compound.getBoolean("Findable", false)
    }

    override fun toCompound(): NbtMapBuilder {
        val builder = super.toCompound()
        val itemsCompoundList: MutableList<NbtMap> = ArrayList()
        for (slot in 0 until realInventory.size) {
            val itemCompound = NbtMap.builder()
            val item = realInventory.getItem(slot)
            itemCompound.putByte("Slot", slot.toByte())
            fromItem(item!!, itemCompound)
            itemsCompoundList.add(itemCompound.build())
        }
        builder!!.putList("Items", NbtType.COMPOUND, itemsCompoundList)
        if (pairPosition != null) {
            builder.putInt("pairx", pairPosition.getBlockX())
            builder.putInt("pairz", pairPosition.getBlockZ())
        }
        builder.putBoolean("Findable", findable)
        return builder
    }

    val isPaired: Boolean
        get() = if (findable) {
            pairPosition != null
        } else false

    fun pair(other: BlockEntityChest) {
        val otherBP: Vector? = other.getBlock().location
        val otherL = Utils.toLong(otherBP.getBlockX(), otherBP.getBlockZ())
        val thisBP: Vector? = getBlock().location
        val thisL = Utils.toLong(thisBP.getBlockX(), thisBP.getBlockZ())
        if (otherL > thisL) {
            doubleChestInventory = DoubleChestInventory(this, other.getChestInventory(), realInventory)
        } else {
            doubleChestInventory = DoubleChestInventory(this, realInventory, other.getChestInventory())
        }
        other.doubleChestInventory = doubleChestInventory
        other.setPair(thisBP!!)
        setPair(otherBP!!)
    }

    fun unpair() {
        val other = getPaired()
        if (other != null) {
            other.doubleChestInventory = null
            other.resetPair()
        }
        doubleChestInventory = null
        resetPair()
    }

    private fun resetPair() {
        findable = false
        pairPosition = null
    }

    private fun getPaired(): BlockEntityChest? {
        if (pairPosition != null && isPaired) {
            val loadedChunk =
                getBlock().world.getLoadedChunk(pairPosition.getChunkX(), pairPosition.getChunkZ(), dimension)
            if (loadedChunk != null) {
                val blockEntity = loadedChunk.getBlockEntity(
                    pairPosition.getBlockX(),
                    pairPosition.getBlockY(),
                    pairPosition.getBlockZ()
                )
                if (blockEntity is BlockEntityChest) {
                    return blockEntity
                }
            }
        }
        return null
    }

    private fun setPair(otherBP: Vector) {
        findable = true
        setPaired = true
        pairPosition = Vector(otherBP.blockX, getBlock().location.blockY, otherBP.blockZ)
    }

    fun getChestInventory(): ChestInventory? {
        return if (doubleChestInventory != null) doubleChestInventory else realInventory
    }

    fun setDoubleChestInventory(doubleChestInventory: DoubleChestInventory?) {
        this.doubleChestInventory = doubleChestInventory
    }
}