package org.jukeboxmc.entity.projectile

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.entity.EntityData
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.entity.EntityDamageEvent
import org.jukeboxmc.event.entity.EntityDamageEvent.DamageSource
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.world.Sound

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class EntityProjectile : EntityMoveable() {
    protected var shooter: EntityLiving? = null
    protected var hitEntity: Entity? = null
    var hadCollision = false
    override fun update(currentTick: Long) {
        if (this.isClosed()) {
            return
        }
        super.update(currentTick)
        if (!this.isDead) {
            if (hitEntity != null) {
                this.location = hitEntity!!.location!!
                    .add(0f, hitEntity.getEyeHeight() + this.getHeight(), 0f)
            } else {
                val location: Location = this.location
                if (!this.isCollided) {
                    this.velocity.setY(this.velocity.getY() - this.getGravity())
                }
                val moveVector: Vector = Vector(
                    location.x + this.velocity.getX(),
                    location.y + this.velocity.getY(),
                    location.z + this.velocity.getZ()
                )
                val nearbyEntities = location.world.getNearbyEntities(
                    this.boundingBox.addCoordinates(
                        this.velocity.getX(),
                        this.velocity.getY(),
                        this.velocity.getZ()
                    ).expand(1f, 1f, 1f), location.dimension, this
                )
                var nearDistance = Int.MAX_VALUE.toFloat()
                var hitEntity: Entity? = null
                for (entity in nearbyEntities) {
                    if (entity === shooter && this.age < 20) {
                        continue
                    }
                    val axisAlignedBB = entity.boundingBox.grow(0.3f, 0.3f, 0.3f)
                    val onLineVector = axisAlignedBB.calculateIntercept(location, moveVector) ?: continue
                    val distance = location.distanceSquared(onLineVector)
                    if (distance < nearDistance) {
                        nearDistance = distance
                        hitEntity = entity
                    }
                }
                if (hitEntity != null) {
                    val projectileHitEntityEvent = ProjectileHitEntityEvent(hitEntity, this)
                    this.getWorld().getServer().getPluginManager().callEvent(projectileHitEntityEvent)
                    if (!projectileHitEntityEvent.isCancelled()) {
                        val damage = damage
                        val event = EntityDamageByEntityEvent(hitEntity, shooter, damage, DamageSource.PROJECTILE)
                        if (hitEntity.damage(event)) {
                            applyCustomKnockback(hitEntity)
                            applyCustomDamageEffects(hitEntity)
                            if (this is EntityArrow) {
                                if (shooter is Player) {
                                    player.playSound(Sound.RANDOM_BOWHIT)
                                }
                            }
                            this.updateMetadata(this.metadata.setLong(EntityData.TARGET_EID, hitEntity.entityId))
                        }
                        onCollidedWithEntity(hitEntity)
                        this.hitEntity = hitEntity
                        this.updateMovement()
                        return
                    }
                }
                this.move(this.velocity)
                if (this.isCollided && !hadCollision) {
                    hadCollision = true
                    this.velocity.setX(0f)
                    this.velocity.setY(0f)
                    this.velocity.setZ(0f)
                    (this as? EntityArrow)?.world?.playSound(this.location, SoundEvent.BOW_HIT)
                    this.updateMovement()
                    return
                } else if (!this.isCollided && hadCollision) {
                    hadCollision = false
                }
                if (!hadCollision || Math.abs(this.velocity.getX()) > 0.00001 || Math.abs(this.velocity.getY()) > 0.00001 || Math.abs(
                        this.velocity.getZ()
                    ) > 0.00001
                ) {
                    val f =
                        Math.sqrt((this.velocity.getX() * this.velocity.getX() + this.velocity.getZ() * this.velocity.getZ()).toDouble())
                    this.setYaw(
                        (Math.atan2(
                            this.velocity.getX().toDouble(),
                            this.velocity.getZ().toDouble()
                        ) * 180 / Math.PI).toFloat()
                    )
                    this.setPitch((Math.atan2(this.velocity.getY().toDouble(), f) * 180 / Math.PI).toFloat())
                }
                this.updateMovement()
            }
        }
    }

    override fun damage(event: EntityDamageEvent): Boolean {
        return event.damageSource == DamageSource.VOID && super.damage(event)
    }

    override fun canCollideWith(entity: Entity?): Boolean {
        return entity is EntityLiving && !this.onGround
    }

    open val damage: Float
        get() = 0

    protected open fun applyCustomDamageEffects(hitEntity: Entity) {}
    protected open fun applyCustomKnockback(hitEntity: Entity) {}
    open fun onCollidedWithEntity(entity: Entity?) {}
    fun getShooter(): EntityLiving? {
        return if (shooter.isDead()) null else shooter
    }

    fun setShooter(shooter: EntityLiving) {
        this.shooter = shooter
        this.metadata.setLong(EntityData.OWNER_EID, shooter.getEntityId())
    }
}