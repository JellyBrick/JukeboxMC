package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import java.lang.reflect.InvocationTargetException
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
    protected val dimension: Dimension?
    protected var isMovable = true
    var isSpawned = false

    init {
        dimension = block.location.dimension
    }

    open fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?
    ): Boolean {
        return false
    }

    open fun update(currentTick: Long) {}
    open fun fromCompound(compound: NbtMap) {
        isMovable = compound.getBoolean("isMovable", true)
    }

    open fun toCompound(): NbtMapBuilder {
        val compound = NbtMap.builder()
        val position: Vector? = block.location
        compound.putString("id", BlockEntityRegistry.getBlockEntityType(blockEntityType))
        compound.putInt("x", position.getBlockX())
        compound.putInt("y", position.getBlockY())
        compound.putInt("z", position.getBlockZ())
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
            return Item.Companion.create<Item>(ItemType.AIR)
        }
        val data = compound.getShort("Damage", 0.toShort())
        val amount = compound.getByte("Count", 0.toByte())
        val name = compound.getString("Name", null)
        val tag = compound.getCompound("tag", NbtMap.EMPTY)
        return if (name != null) {
            Item.Companion.create<Item>(
                Identifier.Companion.fromString(
                    name
                )
            ).setAmount(amount.toInt()).setMeta(data.toInt()).setNbt(tag)
        } else Item.Companion.create<Item>(ItemType.AIR)
    }

    fun spawn(): BlockEntity {
        val world = block.location.world
        val location: Vector? = block.location
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.setBlockPosition(location!!.toVector3i())
        blockEntityDataPacket.setData(toCompound().build())
        world!!.setBlockEntity(location, this, location.dimension)
        world.sendDimensionPacket(blockEntityDataPacket, location.dimension)
        Server.Companion.getInstance().broadcastPacket(blockEntityDataPacket)
        isSpawned = true
        return this
    }

    fun update(player: Player) {
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.setBlockPosition(block.location!!.toVector3i())
        blockEntityDataPacket.setData(toCompound().build())
        player.playerConnection.sendPacket(blockEntityDataPacket)
    }

    companion object {
        fun <T : BlockEntity?> create(blockEntityType: BlockEntityType?, block: Block?): T {
            return try {
                val constructor = BlockEntityRegistry.getBlockEntityClass(blockEntityType).getConstructor(
                    Block::class.java, BlockEntityType::class.java
                )
                constructor.isAccessible = true
                constructor.newInstance(block, blockEntityType) as T
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            }
        }
    }
}