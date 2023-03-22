package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import com.nukkitx.nbt.NbtType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.inventory.ShulkerBoxInventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityShulkerBox(
    block: Block,
    blockEntityType: BlockEntityType,
) : BlockEntity(block, blockEntityType), InventoryHolder {
    var facing: Byte = 1
        private set
    var isUndyed = false
        private set
    val shulkerBoxInventory: ShulkerBoxInventory

    init {
        shulkerBoxInventory = ShulkerBoxInventory(this)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        player.openInventory(shulkerBoxInventory, blockPosition)
        return true
    }

    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        val items = compound.getList("Items", NbtType.COMPOUND)
        for (nbtMap in items) {
            val item = toItem(nbtMap)
            val slot = nbtMap.getByte("Slot", 127.toByte())
            if (slot.toInt() == 127) {
                shulkerBoxInventory.addItem(item, false)
            } else {
                shulkerBoxInventory.setItem(slot.toInt(), item, false)
            }
        }
        facing = compound.getByte("facing")
        isUndyed = compound.getBoolean("isUndyed")
    }

    override fun toCompound(): NbtMapBuilder {
        val builder = super.toCompound()
        val itemsCompoundList: MutableList<NbtMap> = ArrayList()
        for (slot in 0 until shulkerBoxInventory.size) {
            val itemCompound = NbtMap.builder()
            val item = shulkerBoxInventory.getItem(slot)
            itemCompound.putByte("Slot", slot.toByte())
            fromItem(item, itemCompound)
            itemsCompoundList.add(itemCompound.build())
        }
        builder.putList("Items", NbtType.COMPOUND, itemsCompoundList)
        builder.putByte("facing", facing)
        builder.putBoolean("isUndyed", isUndyed)
        return builder
    }

    fun setFacing(facing: Byte): BlockEntityShulkerBox {
        this.facing = facing
        return this
    }

    fun setUndyed(undyed: Boolean): BlockEntityShulkerBox {
        isUndyed = undyed
        return this
    }
}
