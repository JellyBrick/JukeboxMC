package org.jukeboxmc.blockentity

import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtMapBuilder
import org.cloudburstmc.nbt.NbtType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.BlastFurnaceInventory
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityBlastFurnace(
    block: Block,
    blockEntityType: BlockEntityType,
) : SmeltingComponent(block, blockEntityType), InventoryHolder {
    val blastFurnaceInventory: BlastFurnaceInventory = BlastFurnaceInventory(this)

    init {
        initInventory(blastFurnaceInventory)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        player.openInventory(blastFurnaceInventory, blockPosition)
        return super.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
    }

    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        val items = compound.getList("Items", NbtType.COMPOUND)
        for (nbtMap in items) {
            val item = toItem(nbtMap)
            val slot = nbtMap.getByte("Slot", 127.toByte())
            if (slot.toInt() == 127) {
                blastFurnaceInventory.addItem(item, false)
            } else {
                blastFurnaceInventory.setItem(slot.toInt(), item, false)
            }
        }
    }

    override fun toCompound(): NbtMapBuilder {
        val builder = super.toCompound()
        val itemsCompoundList: MutableList<NbtMap> = mutableListOf()
        for (slot in 0 until blastFurnaceInventory.size) {
            val itemCompound = NbtMap.builder()
            val item = blastFurnaceInventory.getItem(slot)
            itemCompound.putByte("Slot", slot.toByte())
            fromItem(item, itemCompound)
            itemsCompoundList.add(itemCompound.build())
        }
        builder.putList("Items", NbtType.COMPOUND, itemsCompoundList)
        return builder
    }
}
