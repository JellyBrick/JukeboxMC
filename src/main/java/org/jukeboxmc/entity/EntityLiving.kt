package org.jukeboxmc.entity

import com.nukkitx.protocol.bedrock.data.entity.EntityData
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.Server
import org.jukeboxmc.entity.attribute.Attribute
import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.event.entity.EntityDamageEvent
import org.jukeboxmc.event.entity.EntityDamageEvent.DamageSource
import org.jukeboxmc.event.entity.EntityHealEvent
import org.jukeboxmc.player.Player
import org.jukeboxmc.potion.Effect
import org.jukeboxmc.potion.EffectType

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class EntityLiving : Entity() {
    protected var deadTimer = 0
    protected var lastDamage = 0f
    protected var attackCoolDown: Byte = 0
    var lastDamageSource: DamageSource? = DamageSource.COMMAND
        protected set
    var lastDamageEntity: Entity? = null
        protected set
    protected val attributes: MutableMap<AttributeType, Attribute> = HashMap()
    protected val effects: MutableMap<EffectType?, Effect> = HashMap()

    init {
        addAttribute(AttributeType.HEALTH)
        addAttribute(AttributeType.ABSORPTION)
        addAttribute(AttributeType.ATTACK_DAMAGE)
        addAttribute(AttributeType.FOLLOW_RANGE)
        addAttribute(AttributeType.MOVEMENT)
        addAttribute(AttributeType.KNOCKBACK_RESISTENCE)
    }

    override fun update(currentTick: Long) {
        if (!(isDead || health <= 0)) {
            super.update(currentTick)
        }
        if (lastDamageEntity != null && lastDamageEntity!!.isDead()) {
            lastDamageEntity = null
        }
        if (health < 1) {
            if (deadTimer > 0 && deadTimer-- > 1) {
                this.despawn()
                isDead = true
                deadTimer = 0
            }
        } else {
            deadTimer = 0
        }
        if (isDead() || health <= 0) {
            return
        }
        if (attackCoolDown > 0) {
            attackCoolDown--
        }
        if (this.isOnLadder) {
            fallDistance = 0f
        }
        if (!effects.isEmpty()) {
            for (effect in effects.values) {
                effect.update(currentTick)
                if (effect.canExecute()) {
                    effect.apply(this)
                }
                effect.duration = effect.duration - 1
                if (effect.duration <= 0) {
                    removeEffect(effect.effectType)
                }
            }
        }
    }

    override fun damage(event: EntityDamageEvent): Boolean {
        if (health <= 0) {
            return false
        }
        if (hasEffect(EffectType.FIRE_RESISTANCE) &&
            (event.damageSource == DamageSource.FIRE || event.damageSource == DamageSource.ON_FIRE || event.damageSource == DamageSource.LAVA)
        ) {
            return false
        }
        var damage = applyArmorReduction(event, false)
        damage = applyFeatherFallingReduction(event, damage)
        damage = applyProtectionReduction(event, damage)
        damage = applyProjectileProtectionReduction(event, damage)
        damage = applyFireProtectionReduction(event, damage)
        damage = applyResistanceEffectReduction(event, damage)
        var absorption = absorption
        if (absorption > 0) {
            damage = Math.max(damage - absorption, 0f)
        }
        if (attackCoolDown > 0 && damage <= lastDamage) {
            return false
        }
        event.finalDamage = damage
        if (!super.damage(event)) {
            return false
        }
        var damageToBeDealt: Float
        if (damage != event.finalDamage) {
            damageToBeDealt = event.finalDamage
        } else {
            damageToBeDealt = applyArmorReduction(event, true)
            damageToBeDealt = applyFeatherFallingReduction(event, damageToBeDealt)
            damageToBeDealt = applyProtectionReduction(event, damageToBeDealt)
            damageToBeDealt = applyProjectileProtectionReduction(event, damageToBeDealt)
            damageToBeDealt = applyFireProtectionReduction(event, damageToBeDealt)
            damageToBeDealt = applyResistanceEffectReduction(event, damageToBeDealt)
            absorption = this.absorption
            if (absorption > 0) {
                val oldDamage = damageToBeDealt
                damageToBeDealt = Math.max(damage - absorption, 0f)
                this.absorption = absorption - (oldDamage - damageToBeDealt)
            }
        }
        val health = health - damageToBeDealt
        if (health > 0) {
            val entityEventPacket = EntityEventPacket()
            entityEventPacket.setRuntimeEntityId(entityId)
            entityEventPacket.setType(EntityEventType.HURT)
            Server.Companion.getInstance().broadcastPacket(entityEventPacket)
        }
        if (event is EntityDamageByEntityEvent) {
            val damager: Entity = event.getDamager()
            val diffX = this.x - damager.x
            val diffZ = this.z - damager.z
            var distance = Math.sqrt((diffX * diffX + diffZ * diffZ).toDouble()).toFloat()
            if (distance > 0.0) {
                val baseModifier = 0.3f
                distance = 1 / distance
                var velocity = getVelocity()
                velocity = velocity!!.divide(2f, 2f, 2f)
                velocity = velocity.add(
                    diffX * distance * baseModifier,
                    baseModifier,
                    diffZ * distance * baseModifier
                )
                if (velocity.y > baseModifier) {
                    velocity.y = baseModifier
                }
                this.setVelocity(velocity, true)
            }
        }
        lastDamage = damage
        lastDamageSource = event.damageSource
        lastDamageEntity =
            if (event is EntityDamageByEntityEvent) (event as EntityDamageByEntityEvent).getDamager() else null
        attackCoolDown = 10
        this.health = if (health <= 0) 0 else health
        return true
    }

    open fun applyArmorReduction(event: EntityDamageEvent, damageArmor: Boolean): Float {
        return event.damage
    }

    open fun applyFeatherFallingReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0
    }

    open fun applyProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0
    }

    open fun applyProjectileProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0
    }

    open fun applyFireProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0
    }

    fun applyResistanceEffectReduction(event: EntityDamageEvent, damage: Float): Float {
        if (event.entity is EntityLiving) {
            if (entityLiving.hasEffect(EffectType.RESISTANCE)) {
                val amplifier: Int = entityLiving.getEffect<Effect>(EffectType.RESISTANCE).getAmplifier()
                return -(damage * 0.20f * amplifier + 1)
            }
        }
        return event.damage
    }

    public override fun fall() {
        var distanceReduce = 0.0f
        if (hasEffect(EffectType.JUMP_BOOST)) {
            val jumpAmplifier = getEffect<Effect>(EffectType.JUMP_BOOST).getAmplifier()
            if (jumpAmplifier != -1) {
                distanceReduce = (jumpAmplifier + 1).toFloat()
            }
        }
        val damage = FastMath.floor((fallDistance - 3f - distanceReduce).toDouble()).toFloat()
        if (damage > 0) {
            damage(EntityDamageEvent(this, damage, DamageSource.FALL))
        }
    }

    protected open fun killInternal() {
        deadTimer = 20
        val entityEventPacket = EntityEventPacket()
        entityEventPacket.setRuntimeEntityId(entityId)
        entityEventPacket.setType(EntityEventType.DEATH)
        Server.Companion.getInstance().broadcastPacket(entityEventPacket)
        removeAllEffects()
        fireTicks = 0
        this.isBurning = false
    }

    fun addEffect(effect: Effect?) {
        val addEffectEvent = EntityAddEffectEvent(this, effect)
        Server.Companion.getInstance().getPluginManager().callEvent(addEffectEvent)
        if (addEffectEvent.isCancelled()) {
            return
        }
        val oldEffect = getEffect<Effect>(addEffectEvent.getEffect().getEffectType())
        addEffectEvent.getEffect().apply(this)
        if (this is Player) {
            val mobEffectPacket = MobEffectPacket()
            mobEffectPacket.setRuntimeEntityId(entityId)
            mobEffectPacket.setEffectId(addEffectEvent.getEffect().getId())
            mobEffectPacket.setAmplifier(addEffectEvent.getEffect().getAmplifier())
            mobEffectPacket.setParticles(addEffectEvent.getEffect().isVisible())
            mobEffectPacket.setDuration(addEffectEvent.getEffect().getDuration())
            if (oldEffect != null) {
                mobEffectPacket.setEvent(MobEffectPacket.Event.MODIFY)
            } else {
                mobEffectPacket.setEvent(MobEffectPacket.Event.ADD)
            }
            player.getPlayerConnection().sendPacket(mobEffectPacket)
        }
        effects[addEffectEvent.getEffect().getEffectType()] = addEffectEvent.getEffect()
        calculateEffectColor()
    }

    fun removeEffect(effectType: EffectType?) {
        if (effects.containsKey(effectType)) {
            val effect = effects[effectType]
            val removeEffectEvent = EntityRemoveEffectEvent(this, effect)
            Server.Companion.getInstance().getPluginManager().callEvent(removeEffectEvent)
            if (removeEffectEvent.isCancelled()) {
                return
            }
            effects.remove(effectType)
            effect!!.remove(this)
            if (this is Player) {
                val mobEffectPacket = MobEffectPacket()
                mobEffectPacket.setRuntimeEntityId(entityId)
                mobEffectPacket.setEvent(MobEffectPacket.Event.REMOVE)
                mobEffectPacket.setEffectId(effect.id)
                player.getPlayerConnection().sendPacket(mobEffectPacket)
            }
            calculateEffectColor()
        }
    }

    fun removeAllEffects() {
        for (effectType in effects.keys) {
            removeEffect(effectType)
        }
    }

    fun <T : Effect?> getEffect(effectType: EffectType?): T? {
        return effects[effectType] as T?
    }

    fun hasEffect(effectType: EffectType?): Boolean {
        return effects.containsKey(effectType)
    }

    private fun calculateEffectColor() {
        val color = IntArray(3)
        var count = 0
        for (effect in effects.values) {
            if (effect.isVisible) {
                val c = effect.color
                color[0] += c!![0] * (effect.amplifier + 1)
                color[1] += c[1] * (effect.amplifier + 1)
                color[2] += c[2] * (effect.amplifier + 1)
                count += effect.amplifier + 1
            }
        }
        if (count > 0) {
            val r = color[0] / count and 0xff
            val g = color[1] / count and 0xff
            val b = color[2] / count and 0xff
            this.updateMetadata(metadata.setInt(EntityData.EFFECT_COLOR, (r shl 16) + (g shl 8) + b))
            this.updateMetadata(metadata.setByte(EntityData.EFFECT_AMBIENT, 0.toByte()))
        } else {
            this.updateMetadata(metadata.setInt(EntityData.EFFECT_COLOR, 0))
            this.updateMetadata(metadata.setByte(EntityData.EFFECT_AMBIENT, 0.toByte()))
        }
    }

    fun addAttribute(attributeType: AttributeType) {
        attributes[attributeType] = attributeType.attribute
    }

    fun getAttribute(attributeType: AttributeType): Attribute? {
        return attributes[attributeType]
    }

    fun setAttributes(attributes: AttributeType, value: Float) {
        val attribute = this.attributes[attributes] ?: return
        attribute.currentValue = value
    }

    fun getAttributeValue(attributeType: AttributeType): Float {
        return getAttribute(attributeType)!!.currentValue
    }

    fun getAttributes(): Collection<Attribute> {
        return attributes.values
    }

    var health: Float
        get() = getAttributeValue(AttributeType.HEALTH)
        set(value) {
            var value = value
            if (value < 1) {
                value = 0f
                killInternal()
            }
            val attribute = getAttribute(AttributeType.HEALTH)
            attribute!!.currentValue = value
        }
    val maxHealth: Float
        get() = getAttribute(AttributeType.HEALTH).getMaxValue()

    fun setHeal(value: Float) {
        health = Math.min(20f, Math.max(0f, value))
    }

    fun setHeal(value: Float, cause: EntityHealEvent.Cause?) {
        val entityHealEvent = EntityHealEvent(this, health + value, cause)
        Server.Companion.getInstance().getPluginManager().callEvent(entityHealEvent)
        if (entityHealEvent.isCancelled()) {
            return
        }
        health = Math.min(20f, Math.max(0f, entityHealEvent.getHeal()))
    }

    var absorption: Float
        get() = getAttributeValue(AttributeType.ABSORPTION)
        set(value) {
            setAttributes(AttributeType.ABSORPTION, value)
        }
    var attackDamage: Float
        get() = getAttributeValue(AttributeType.ATTACK_DAMAGE)
        set(value) {
            setAttributes(AttributeType.ATTACK_DAMAGE, value)
        }
    var followRange: Float
        get() = getAttributeValue(AttributeType.FOLLOW_RANGE)
        set(value) {
            setAttributes(AttributeType.FOLLOW_RANGE, value)
        }
    var movement: Float
        get() = getAttributeValue(AttributeType.MOVEMENT)
        set(value) {
            setAttributes(AttributeType.MOVEMENT, value)
        }
    var knockbackResistence: Float
        get() = getAttributeValue(AttributeType.KNOCKBACK_RESISTENCE)
        set(value) {
            setAttributes(AttributeType.KNOCKBACK_RESISTENCE, value)
        }
}