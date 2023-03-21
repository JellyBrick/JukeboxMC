package org.jukeboxmc.entity.passiv

import com.nukkitx.protocol.bedrock.data.entity.EntityData
import org.jukeboxmc.block.Block
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityFallingBlock : EntityMoveable() {
    private var block: Block? = null
    override fun update(currentTick: Long) {
        if (this.isClosed() || this.isDead()) {
            return
        }
        super.update(currentTick)
        this.velocity.setY(this.velocity.getY() - gravity)
        this.move(this.velocity)
        val friction = 1 - drag
        this.velocity.setX(this.velocity.getX() * friction)
        this.velocity.setY(this.velocity.getY() * (1 - drag))
        this.velocity.setZ(this.velocity.getZ() * friction)
        val position: Vector = Vector(this.getX() - 0.5f, this.getY(), this.getZ() - 0.5f).round()
        if (this.onGround) {
            this.close()
            val world: World = this.getWorld()
            val replacedBlock = world.getBlock(position)
            if (replacedBlock!!.canBeReplaced(null)) {
                world.setBlock(position, block!!)
            } else if (replacedBlock.isTransparent) {
                for (drop in block!!.getDrops(null)) {
                    world.dropItem(drop, this.location, null).spawn()
                }
            }
        }
        this.updateMovement()
    }

    val name: String
        get() = "Falling Block"
    val width: Float
        get() = 0.98f
    val height: Float
        get() = 0.98f
    val drag: Float
        get() = 0.02f
    val gravity: Float
        get() = 0.04f
    val type: EntityType
        get() = EntityType.FALLING_BLOCK
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:falling_block")

    fun setBlock(block: Block?) {
        this.block = block
        this.metadata.setInt(EntityData.VARIANT, this.block.getRuntimeId())
    }

    fun getBlock(): Block? {
        return block
    }
}