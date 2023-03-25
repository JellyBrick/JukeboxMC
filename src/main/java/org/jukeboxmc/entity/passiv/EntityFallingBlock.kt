package org.jukeboxmc.entity.passiv

import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes
import org.jukeboxmc.block.Block
import org.jukeboxmc.entity.EntityMoveable
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
        if (this.isClosed || this.isDead) {
            return
        }
        super.update(currentTick)
        this.velocity.y = this.velocity.y - gravity
        this.move(this.velocity)
        val friction = 1 - drag
        this.velocity.x = this.velocity.x * friction
        this.velocity.y = this.velocity.y * (1 - drag)
        this.velocity.z = this.velocity.z * friction
        val position: Vector = Vector(this.x - 0.5f, this.y, this.z - 0.5f).round()
        if (this.isOnGround) {
            this.close()
            val world: World = this.world
            val replacedBlock = world.getBlock(position)
            if (replacedBlock.canBeReplaced(null)) {
                world.setBlock(position, block!!)
            } else if (replacedBlock.isTransparent) {
                for (drop in block!!.getDrops(null)) {
                    world.dropItem(drop, this.location, null).spawn()
                }
            }
        }
        this.updateMovement()
    }

    override val name: String
        get() = "Falling Block"
    override val width: Float
        get() = 0.98f
    override val height: Float
        get() = 0.98f
    override var drag: Float = 0.02f
    override var gravity: Float = 0.04f
    override val type: EntityType
        get() = EntityType.FALLING_BLOCK
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:falling_block")

    fun setBlock(block: Block?) {
        this.block = block
        this.metadata.setInt(EntityDataTypes.VARIANT, this.block!!.runtimeId)
    }

    fun getBlock(): Block? {
        return block
    }
}
