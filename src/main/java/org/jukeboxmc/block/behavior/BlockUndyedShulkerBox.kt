package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityShulkerBox
import org.jukeboxmc.blockentity.BlockEntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemUndyedShulkerBox
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockUndyedShulkerBox : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemIndHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        val value =
            super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemIndHand, blockFace)
        if (value) {
            val blockEntityShulkerBox =
                BlockEntity.create<BlockEntityShulkerBox>(BlockEntityType.SHULKER_BOX, this).setUndyed(true)
                    .spawn() as BlockEntityShulkerBox
            if (itemIndHand.nbt != null && itemIndHand.nbt!!.containsKey("Items")) {
                val nbt = itemIndHand.nbt
                val items = nbt!!.getList("Items", NbtType.COMPOUND)
                for (nbtMap in items) {
                    val item = blockEntityShulkerBox.toItem(nbtMap)
                    val slot = nbtMap.getByte("Slot", 127.toByte())
                    if (slot.toInt() == 127) {
                        blockEntityShulkerBox.shulkerBoxInventory.addItem(item, false)
                    } else {
                        blockEntityShulkerBox.shulkerBoxInventory.setItem(slot.toInt(), item, false)
                    }
                }
            }
        }
        return value
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item,
    ): Boolean {
        val blockEntity: BlockEntityShulkerBox? = blockEntity as BlockEntityShulkerBox?
        if (blockEntity != null) {
            blockEntity.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            return true
        }
        return false
    }

    override val blockEntity: BlockEntity?
        get() = location.world?.getBlockEntity(location, location.dimension) as BlockEntityShulkerBox?

    override fun toItem(): ItemUndyedShulkerBox {
        val itemShulkerBox: ItemUndyedShulkerBox =
            Item.create<ItemUndyedShulkerBox>(ItemType.UNDYED_SHULKER_BOX)
        val blockEntity = blockEntity as BlockEntityShulkerBox? ?: return itemShulkerBox
        val shulkerBoxInventory = blockEntity.shulkerBoxInventory
        val builder = NbtMap.builder()
        val itemsCompoundList: MutableList<NbtMap> = ArrayList()
        for (slot in 0 until shulkerBoxInventory.size) {
            val item = shulkerBoxInventory.getItem(slot)
            if (item.type != ItemType.AIR) {
                val itemCompound = NbtMap.builder()
                itemCompound.putByte("Slot", slot.toByte())
                blockEntity.fromItem(item, itemCompound)
                itemsCompoundList.add(itemCompound.build())
            }
        }
        builder.putList("Items", NbtType.COMPOUND, itemsCompoundList)
        itemShulkerBox.setNbt(builder.build())
        return itemShulkerBox
    }
}
