package org.jukeboxmc.inventory

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket
import org.jukeboxmc.Server
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FakeDoubleChestInventory : FakeChestInventory {
    constructor() : super(null, -1, 27 * 2)
    constructor(customName: String?) : super(null, -1, 27 * 2) {
        this.customName = customName
    }

    override val type: InventoryType
        get() = InventoryType.FAKE_DOUBLE_CHEST

    override fun addViewer(player: Player, position: Vector, windowId: Byte) {
        viewer.add(player)
        if (openInventory.put(player, this) != null) {
            return
        }
        val blockPosition = onOpenChest(player)
        blockPositions[player] = blockPosition
        Server.instance.scheduler.scheduleDelayed(
            {
                val vector = blockPosition[0]
                val containerOpenPacket = ContainerOpenPacket()
                containerOpenPacket.id = windowId
                containerOpenPacket.uniqueEntityId = holderId
                containerOpenPacket.type = this.windowTypeId
                containerOpenPacket.blockPosition = vector.toVector3i()
                player.playerConnection.sendPacket(containerOpenPacket)
                this.sendContents(player)
            },
            3,
        )
    }

    override fun onOpenChest(player: Player): List<Vector> {
        val positionA = Vector(player.blockX, player.blockY + 2, player.blockZ)
        val positionB = positionA.add(1f, 0f, 0f)
        placeFakeChest(player, positionA)
        placeFakeChest(player, positionB)
        pair(player, positionA, positionB)
        pair(player, positionB, positionA)
        return listOf(positionA, positionB)
    }

    private fun pair(player: Player, positionA: Vector, positionB: Vector) {
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.blockPosition = positionA.toVector3i()
        blockEntityDataPacket.data = this.toChestNBT(positionA, positionB)
        player.playerConnection.sendPacket(blockEntityDataPacket)
    }

    private fun toChestNBT(positionA: Vector, positionB: Vector): NbtMap {
        return NbtMap.builder()
            .putString("id", "Chest")
            .putInt("x", positionA.blockX)
            .putInt("y", positionA.blockY)
            .putInt("z", positionA.blockZ)
            .putInt("pairx", positionB.blockX)
            .putInt("pairz", positionB.blockZ)
            .putString("CustomName", if (customName == null) "Chest" else customName)
            .build()
    }
}
