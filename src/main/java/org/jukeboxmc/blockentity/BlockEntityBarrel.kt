package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import com.nukkitx.nbt.NbtType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.BarrelInventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityBarrel(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType),
    InventoryHolder {
    val barrelInventory: BarrelInventory

    init {
        barrelInventory = BarrelInventory(this)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?
    ): Boolean {
        player.openInventory(barrelInventory, blockPosition)
        return true
    }

    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        val items = compound.getList("Items", NbtType.COMPOUND)
        for (nbtMap in items) {
            val item = toItem(nbtMap)
            val slot = nbtMap.getByte("Slot", 127.toByte())
            if (slot.toInt() == 127) {
                barrelInventory.addItem(item!!, false)
            } else {
                barrelInventory.setItem(slot.toInt(), item!!, false)
            }
        }
    }

    override fun toCompound(): NbtMapBuilder {
        val builder = super.toCompound()
        val itemsCompoundList: MutableList<NbtMap> = ArrayList()
        for (slot in 0 until barrelInventory.size) {
            val itemCompound = NbtMap.builder()
            val item = barrelInventory.getItem(slot)
            itemCompound.putByte("Slot", slot.toByte())
            fromItem(item!!, itemCompound)
            itemsCompoundList.add(itemCompound.build())
        }
        builder!!.putList("Items", NbtType.COMPOUND, itemsCompoundList)
        return builder
    }
}