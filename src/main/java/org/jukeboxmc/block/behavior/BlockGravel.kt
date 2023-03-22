package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.passiv.EntityFallingBlock
import org.jukeboxmc.event.block.FallingBlockEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Objects

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockGravel : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        world.setBlock(placePosition, this, 0)
        return true
    }

    override fun onUpdate(updateReason: UpdateReason): Long {
        if (updateReason == UpdateReason.NORMAL) {
            location.world?.scheduleBlockUpdate(location, 1)
        } else if (updateReason == UpdateReason.SCHEDULED) {
            val blockDown = location.block.clone().getSide(BlockFace.DOWN)
            if (blockDown.type == BlockType.AIR) {
                val entity =
                    Objects.requireNonNull<EntityFallingBlock>(Entity.create<EntityFallingBlock>(EntityType.FALLING_BLOCK))
                entity.location = location.add(0.5f, 0f, 0.5f)
                entity.setBlock(this)
                val fallingBlockEvent = FallingBlockEvent(this, entity)
                Server.instance.pluginManager.callEvent(fallingBlockEvent)
                if (fallingBlockEvent.isCancelled) {
                    return -1
                }
                location.world?.setBlock(location, create(BlockType.AIR))
                entity.spawn()
            }
        }
        return -1
    }
}
