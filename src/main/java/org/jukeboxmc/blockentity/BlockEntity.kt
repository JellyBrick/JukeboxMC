package org.jukeboxmc.blockentity

import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtMapBuilder
import org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Dimension

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BlockEntity(val block: Block, val blockEntityType: BlockEntityType) {
    protected val dimension: Dimension = block.location.dimension
    protected var isMovable = true
    var isSpawned = false

    open fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        return false
    }

    open fun update(currentTick: Long) {}
    open fun fromCompound(compound: NbtMap) {
        isMovable = compound.getBoolean("isMovable", true)
    }

    open fun toCompound(): NbtMapBuilder {
        val compound = NbtMap.builder()
        val position: Vector = block.location
        compound.putString("id", BlockEntityRegistry.getBlockEntityType(blockEntityType))
        compound.putInt("x", position.blockX)
        compound.putInt("y", position.blockY)
        compound.putInt("z", position.blockZ)
        compound.putBoolean("isMovable", isMovable)
        compound.putString("CustomName", "")
        return compound
    }

    fun fromItem(item: Item, builder: NbtMapBuilder) {
        builder.putString("Name", item.identifier.fullName)
        builder.putShort("Damage", item.meta.toShort())
        builder.putByte("Count", item.amount.toByte())
        if (item.nbt != null) {
            val nbt = item.nbt
            builder.putCompound("tag", nbt)
        }
    }

    fun toItem(compound: NbtMap?): Item {
        if (compound == null) {
            return Item.create(ItemType.AIR)
        }
        val data = compound.getShort("Damage", 0.toShort())
        val amount = compound.getByte("Count", 0.toByte())
        val name = compound.getString("Name", null)
        val tag = compound.getCompound("tag", NbtMap.EMPTY)
        return if (name != null) {
            Item.create<Item>(
                Identifier.fromString(
                    name,
                ),
            ).setAmount(amount.toInt()).setMeta(data.toInt()).setNbt(tag)
        } else {
            Item.create(ItemType.AIR)
        }
    }

    fun spawn(): BlockEntity {
        val world = block.location.world
        val location: Vector = block.location
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.blockPosition = location.toVector3i()
        blockEntityDataPacket.data = toCompound().build()
        world.setBlockEntity(location, this, location.dimension)
        world.sendDimensionPacket(blockEntityDataPacket, location.dimension)
        Server.instance.broadcastPacket(blockEntityDataPacket)
        isSpawned = true
        return this
    }

    fun update(player: Player) {
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.blockPosition = block.location.toVector3i()
        blockEntityDataPacket.data = toCompound().build()
        player.playerConnection.sendPacket(blockEntityDataPacket)
    }

    companion object {
        fun createBlockEntity(blockEntityType: BlockEntityType, block: Block): BlockEntity {
            val constructor = BlockEntityRegistry.getBlockEntityClass(blockEntityType).getConstructor(
                Block::class.java,
                BlockEntityType::class.java,
            )
            constructor.isAccessible = true
            return constructor.newInstance(block, blockEntityType)
        }

        inline fun <reified T : BlockEntity> create(blockEntityType: BlockEntityType, block: Block): T =
            createBlockEntity(blockEntityType, block) as T
    }
}
