package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import java.util.Objects
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.SandType
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.passiv.EntityFallingBlock
import org.jukeboxmc.event.block.FallingBlockEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemSand
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSand : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace
    ): Boolean {
        world.setBlock(placePosition, this, 0)
        return true
    }

    override fun onUpdate(updateReason: UpdateReason): Long {
        val blockDown = location!!.block!!.clone().getSide(BlockFace.DOWN)
        if (blockDown.type == BlockType.AIR) {
            val entity =
                Objects.requireNonNull<EntityFallingBlock>(Entity.Companion.create<EntityFallingBlock>(EntityType.FALLING_BLOCK))
            entity.location = location!!.add(0.5f, 0f, 0.5f)
            entity.block = this
            val fallingBlockEvent = FallingBlockEvent(this, entity)
            Server.Companion.getInstance().getPluginManager().callEvent(fallingBlockEvent)
            if (fallingBlockEvent.isCancelled) {
                return -1
            }
            location.world.setBlock(location, Block.Companion.create<Block>(BlockType.AIR))
            entity.spawn()
        }
        return -1
    }

    override fun toItem(): Item {
        return Item.Companion.create<ItemSand>(ItemType.SAND).setSandType(sandType)
    }

    fun setSandType(sandType: SandType): BlockSand {
        return setState("sand_type", sandType.name.lowercase(Locale.getDefault()))
    }

    val sandType: SandType
        get() = if (stateExists("sand_type")) SandType.valueOf(getStringState("sand_type")) else SandType.NORMAL
}