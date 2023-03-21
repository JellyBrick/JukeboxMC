package org.jukeboxmc.entity.projectile

import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.entity.EntityData
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
        if (shooter.isDead || (shooter as Player).inventory.itemInHand.type != ItemType.FISHING_ROD) {
            close()
            if (shooter is Player) {
                player.setEntityFishingHook(null)
            }
        }
        if (isCollided && isInsideLiquid) {
            if (velocity != Vector(0f, 0.1f, 0f)) {
                this.setVelocity(Vector(0f, 0.1f, 0f))
            }
        } else if (isCollided) {
            if (!isReset && velocity.squaredLength() < 0.0025) {
                this.setVelocity(Vector.Companion.zero())
                isReset = true
            }
        }
    }

    override fun createSpawnPacket(): BedrockPacket {
        metadata.setLong(EntityData.OWNER_EID, if (shooter != null) shooter.entityId else -1)
        return super.createSpawnPacket()
    }

    val name: String
        get() = "Fishing Hook"
    val width: Float
        get() = 0.2f
    val height: Float
        get() = 0.2f
    val gravity: Float
        get() = 0.3f
    val drag: Float
        get() = 0.1f
    val type: EntityType
        get() = EntityType.FISHING_HOOK
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:fishing_hook")
    val isInsideLiquid: Boolean
        get() {
            val eyeLocation = getLocation().add(0f, this.eyeHeight, 0f)
            val block = eyeLocation.block
            if (block.type == BlockType.WATER || block.type == BlockType.FLOWING_WATER) {
                val yLiquid = (block!!.location!!.y + 1 + ((block as BlockLiquid).liquidDepth - 0.12)).toFloat()
                return eyeLocation.y < yLiquid
            }
            return false
        }
}