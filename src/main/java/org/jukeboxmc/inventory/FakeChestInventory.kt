package org.jukeboxmc.inventory

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class FakeChestInventory : FakeInventory {
    constructor(holder: InventoryHolder?, holderId: Int, size: Int) : super(holder, holderId, size)
    constructor(customName: String?) : super(null, -1, 27) {
        this.customName = customName
    }

    constructor() : super(null, -1, 27)

    override val inventoryHolder: InventoryHolder?
        get() = null
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER
    override val type: InventoryType
        get() = InventoryType.FAKE_CHEST

    override fun onOpenChest(player: Player): List<Vector?> {
        val position = Vector(player.blockX, player.blockY + 2, player.blockZ)
        placeFakeChest(player, position)
        return listOf(position)
    }

    protected fun placeFakeChest(player: Player, position: Vector) {
        val updateBlockPacket = UpdateBlockPacket()
        updateBlockPacket.runtimeId =
            Block.Companion.create<Block>(BlockType.CHEST)
                .getRuntimeId()
        updateBlockPacket.blockPosition = position.toVector3i()
        updateBlockPacket.dataLayer = 0
        updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
        player.playerConnection.sendPacket(updateBlockPacket)
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.setBlockPosition(position.toVector3i())
        blockEntityDataPacket.setData(toChestNBT(position))
        player.playerConnection.sendPacket(blockEntityDataPacket)
    }

    private fun toChestNBT(position: Vector): NbtMap {
        return NbtMap.builder()
            .putString("id", "Chest")
            .putInt("x", position.blockX)
            .putInt("y", position.blockY)
            .putInt("z", position.blockZ)
            .putString("CustomName", if (customName == null) "Chest" else customName)
            .build()
    }
}