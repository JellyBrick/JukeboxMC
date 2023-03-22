package org.jukeboxmc.blockentity

import com.nukkitx.protocol.bedrock.packet.ContainerSetDataPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.FurnaceInventory
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.inventory.WindowId
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import java.time.Duration
import kotlin.math.ceil

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class SmeltingComponent(
    block: Block,
    blockEntityType: BlockEntityType,
) : BlockEntity(block, blockEntityType) {
    private var cookTime: Short = 0
    private var burnTime: Short = 0
    private var burnDuration: Short = 0
    private var output: Item? = null
    private var inventory: Inventory? = null
    fun initInventory(inventory: Inventory?) {
        this.inventory = inventory
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        sendDataProperties(player)
        return true
    }

    override fun update(currentTick: Long) {
        val input = inventory!!.getItem(0)
        val fuelItem = inventory!!.getItem(1)
        val outputItem = inventory!!.getItem(2)
        if (input != null && input.type != ItemType.AIR && fuelItem != null && fuelItem.type != ItemType.AIR && outputItem.amount < 64) {
            checkForRecipe(input)
        }
        if (output != null && output!!.type != ItemType.AIR && input.type != ItemType.AIR && outputItem.amount < 64 && burnTime > 0) {
            cookTime++
            if (cookTime >= (if (inventory is FurnaceInventory) 200 else 100)) {
                val itemStack = inventory!!.getItem(2)
                if (itemStack.type != output!!.type) {
                    inventory!!.setItem(2, output!!.setAmount(1))
                } else {
                    itemStack.setAmount(itemStack.amount + 1)
                    inventory!!.setItem(2, itemStack)
                }
                inventory!!.setItem(0, input.decreaseAmount())
                cookTime = 0
                broadcastCookTime()
            } else if (cookTime % 20 == 0) {
                broadcastCookTime()
            }
        }
        if (burnDuration > 0) {
            burnTime--
            var didRefuel = false
            if (burnTime.toInt() == 0) {
                burnDuration = 0
                if (checkForRefuel()) {
                    didRefuel = true
                    broadcastFuelInfo()
                }
            }
            if (!didRefuel && (burnTime.toInt() == 0 || burnTime % 20 == 0)) {
                broadcastFuelInfo()
            }
        } else {
            if (checkForRefuel()) {
                broadcastFuelInfo()
            }
        }
    }

    private fun checkForRecipe(input: Item) {
        output = Server.instance.getCraftingManager().getSmeltingRecipe(input)?.output?.clone()
    }

    private fun checkForRefuel(): Boolean {
        if (canProduceOutput()) {
            val fuelItem = inventory!!.getItem(1)
            if (fuelItem is Burnable) {
                val duration: Duration = fuelItem.burnTime!!
                if (fuelItem.amount > 0) {
                    inventory!!.setItem(1, fuelItem.decreaseAmount())
                    val diff = if (inventory is FurnaceInventory) 1 else 2
                    burnDuration = ceil(duration.toMillis().toDouble() / diff.toDouble()).toInt().toShort()
                    burnTime = burnDuration
                    return true
                }
            }
        }
        return false
    }

    private fun canProduceOutput(): Boolean {
        if (output == null) {
            return false
        }
        val input = inventory!!.getItem(0)
        if (input.type == ItemType.AIR || input.amount == 0) {
            return false
        }
        val itemStack = inventory!!.getItem(2)
        return if (itemStack.type == output!!.type) {
            itemStack.amount <= itemStack.maxStackSize
        } else {
            true
        }
    }

    private fun sendTickProgress(player: Player) {
        val containerData = ContainerSetDataPacket()
        containerData.windowId = WindowId.OPEN_CONTAINER.id.toByte()
        containerData.property = CONTAINER_PROPERTY_TICK_COUNT
        containerData.value = cookTime.toInt()
        player.playerConnection.sendPacket(containerData)
    }

    private fun sendFuelInfo(player: Player) {
        var containerData = ContainerSetDataPacket()
        containerData.windowId = WindowId.OPEN_CONTAINER.id.toByte()
        containerData.property = CONTAINER_PROPERTY_LIT_TIME
        containerData.value = burnTime.toInt()
        player.playerConnection.sendPacket(containerData)
        containerData = ContainerSetDataPacket()
        containerData.windowId = WindowId.OPEN_CONTAINER.id.toByte()
        containerData.property = CONTAINER_PROPERTY_LIT_DURATION
        containerData.value = burnDuration.toInt()
        player.playerConnection.sendPacket(containerData)
    }

    private fun sendDataProperties(player: Player) {
        var containerData = ContainerSetDataPacket()
        containerData.windowId = WindowId.OPEN_CONTAINER.id.toByte()
        containerData.property = CONTAINER_PROPERTY_TICK_COUNT
        containerData.value = cookTime.toInt()
        player.playerConnection.sendPacket(containerData)
        containerData = ContainerSetDataPacket()
        containerData.windowId = WindowId.OPEN_CONTAINER.id.toByte()
        containerData.property = CONTAINER_PROPERTY_LIT_TIME
        containerData.value = burnTime.toInt()
        player.playerConnection.sendPacket(containerData)
        containerData = ContainerSetDataPacket()
        containerData.windowId = WindowId.OPEN_CONTAINER.id.toByte()
        containerData.property = CONTAINER_PROPERTY_LIT_DURATION
        containerData.value = burnDuration.toInt()
        player.playerConnection.sendPacket(containerData)
    }

    private fun broadcastCookTime() {
        for (viewer in inventory!!.viewer) {
            sendTickProgress(viewer)
        }
    }

    private fun broadcastFuelInfo() {
        for (viewer in inventory!!.viewer) {
            sendFuelInfo(viewer)
        }
    }

    companion object {
        private const val CONTAINER_PROPERTY_TICK_COUNT = 0
        private const val CONTAINER_PROPERTY_LIT_TIME = 1
        private const val CONTAINER_PROPERTY_LIT_DURATION = 2
    }
}
