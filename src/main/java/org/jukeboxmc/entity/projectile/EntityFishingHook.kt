package org.jukeboxmc.entity.projectile

import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLiquid
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityFishingHook : EntityProjectile() {
    private var isReset = false
    override fun update(currentTick: Long) {
        super.update(currentTick)
        if (shooter == null || shooter!!.isDead || (shooter as Player).inventory.itemInHand.type != ItemType.FISHING_ROD) {
            close()
            if (shooter is Player) {
                val player = shooter as Player
                player.entityFishingHook = null
            }
        }
        if (isCollided && isInsideLiquid) {
            if (velocity != Vector(0f, 0.1f, 0f)) {
                this.velocity = Vector(0f, 0.1f, 0f)
            }
        } else if (isCollided) {
            if (!isReset && velocity.squaredLength() < 0.0025) {
                this.velocity = Vector.zero()
                isReset = true
            }
        }
    }

    override fun createSpawnPacket(): BedrockPacket {
        metadata.setLong(EntityDataTypes.OWNER_EID, if (shooter != null) shooter!!.entityId else -1)
        return super.createSpawnPacket()
    }

    override val name: String
        get() = "Fishing Hook"
    override val width: Float
        get() = 0.2f
    override val height: Float
        get() = 0.2f
    override var gravity: Float = 0.3f
    override var drag: Float = 0.1f
    override val type: EntityType
        get() = EntityType.FISHING_HOOK
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:fishing_hook")
    val isInsideLiquid: Boolean
        get() {
            val eyeLocation = location.add(0f, this.eyeHeight, 0f)
            val block = eyeLocation.block
            if (block.type == BlockType.WATER || block.type == BlockType.FLOWING_WATER) {
                val yLiquid = (block.location.y + 1 + ((block as BlockLiquid).liquidDepth - 0.12)).toFloat()
                return eyeLocation.y < yLiquid
            }
            return false
        }
}
