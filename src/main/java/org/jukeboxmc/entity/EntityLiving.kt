package org.jukeboxmc.entity

import com.nukkitx.protocol.bedrock.data.entity.EntityData
import com.nukkitx.protocol.bedrock.data.entity.EntityEventType
import com.nukkitx.protocol.bedrock.packet.EntityEventPacket
import com.nukkitx.protocol.bedrock.packet.MobEffectPacket
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.Server
import org.jukeboxmc.entity.attribute.Attribute
import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.event.entity.EntityAddEffectEvent
import org.jukeboxmc.event.entity.EntityDamageByEntityEvent
import org.jukeboxmc.event.entity.EntityDamageEvent
import org.jukeboxmc.event.entity.EntityDamageEvent.DamageSource
import org.jukeboxmc.event.entity.EntityHealEvent
import org.jukeboxmc.event.entity.EntityRemoveEffectEvent
import org.jukeboxmc.player.Player
import org.jukeboxmc.potion.Effect
import org.jukeboxmc.potion.EffectType
import java.util.*
import kotlin.math.sqrt

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
    protected val attributes: MutableMap<AttributeType, Attribute> = EnumMap(AttributeType::class.java)
    protected val effects: MutableMap<EffectType, Effect> = EnumMap(EffectType::class.java)

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
        lastDamageEntity?.let {
            if (it.isDead) {
                lastDamageEntity = null
            }
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
        if (isDead || health <= 0) {
            return
        }
        if (attackCoolDown > 0) {
            attackCoolDown--
        }
        if (this.isOnLadder) {
            fallDistance = 0f
        }
        if (effects.isNotEmpty()) {
            effects.values.forEach {
                it.update(currentTick)
                if (it.canExecute()) {
                    it.apply(this)
                }
                it.setDuration(it.duration - 1)
                if (it.duration <= 0) {
                    removeEffect(it.effectType)
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
            damage = (damage - absorption).coerceAtLeast(0f)
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
            entityEventPacket.runtimeEntityId = entityId
            entityEventPacket.type = EntityEventType.HURT
            Server.instance.broadcastPacket(entityEventPacket)
        }
        if (event is EntityDamageByEntityEvent) {
            val damager = event.damager
            val diffX = this.x - damager.x
            val diffZ = this.z - damager.z
            var distance = sqrt(diffX * diffX + diffZ * diffZ)
            if (distance > 0.0) {
                val baseModifier = 0.3f
                distance = 1 / distance
                var velocity = velocity
                velocity = velocity.divide(2f, 2f, 2f)
                velocity = velocity.add(
                    diffX * distance * baseModifier,
                    baseModifier,
                    diffZ * distance * baseModifier,
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
            if (event is EntityDamageByEntityEvent) event.damager else null
        attackCoolDown = 10
        this.health = if (health <= 0) 0F else health
        return true
    }

    open fun applyArmorReduction(event: EntityDamageEvent, damageArmor: Boolean): Float {
        return event.damage
    }

    open fun applyFeatherFallingReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0F
    }

    open fun applyProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0F
    }

    open fun applyProjectileProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0F
    }

    open fun applyFireProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        return 0F
    }

    fun applyResistanceEffectReduction(event: EntityDamageEvent, damage: Float): Float {
        val entity = event.getEntity()
        if (entity is EntityLiving) {
            if (entity.hasEffect(EffectType.RESISTANCE)) {
                val amplifier = entity.getEffect<Effect>(EffectType.RESISTANCE)!!.amplifier
                return -(damage * 0.20f * amplifier + 1)
            }
        }
        return event.damage
    }

    public override fun fall() {
        var distanceReduce = 0.0f
        if (hasEffect(EffectType.JUMP_BOOST)) {
            val jumpAmplifier = getEffect<Effect>(EffectType.JUMP_BOOST)!!.amplifier
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
        entityEventPacket.runtimeEntityId = entityId
        entityEventPacket.type = EntityEventType.DEATH
        Server.instance.broadcastPacket(entityEventPacket)
        removeAllEffects()
        fireTicks = 0
        this.isBurning = false
    }

    fun addEffect(effect: Effect) {
        val addEffectEvent = EntityAddEffectEvent(this, effect)
        Server.instance.pluginManager.callEvent(addEffectEvent)
        if (addEffectEvent.isCancelled) {
            return
        }
        val oldEffect = getEffect<Effect>(addEffectEvent.effect.effectType)
        addEffectEvent.effect.apply(this)
        if (this is Player) {
            val mobEffectPacket = MobEffectPacket()
            mobEffectPacket.runtimeEntityId = entityId
            mobEffectPacket.effectId = addEffectEvent.effect.id
            mobEffectPacket.amplifier = addEffectEvent.effect.amplifier
            mobEffectPacket.isParticles = addEffectEvent.effect.isVisible
            mobEffectPacket.duration = addEffectEvent.effect.duration
            if (oldEffect != null) {
                mobEffectPacket.event = MobEffectPacket.Event.MODIFY
            } else {
                mobEffectPacket.event = MobEffectPacket.Event.ADD
            }
            playerConnection.sendPacket(mobEffectPacket)
        }
        effects[addEffectEvent.effect.effectType] = addEffectEvent.effect
        calculateEffectColor()
    }

    fun removeEffect(effectType: EffectType) {
        if (effects.containsKey(effectType)) {
            val effect = effects.getValue(effectType)
            val removeEffectEvent = EntityRemoveEffectEvent(this, effect)
            Server.instance.pluginManager.callEvent(removeEffectEvent)
            if (removeEffectEvent.isCancelled) {
                return
            }
            effects.remove(effectType)
            effect.remove(this)
            if (this is Player) {
                val mobEffectPacket = MobEffectPacket()
                mobEffectPacket.runtimeEntityId = entityId
                mobEffectPacket.event = MobEffectPacket.Event.REMOVE
                mobEffectPacket.effectId = effect.id
                playerConnection.sendPacket(mobEffectPacket)
            }
            calculateEffectColor()
        }
    }

    fun removeAllEffects() {
        effects.keys.forEach(::removeEffect)
    }

    fun getEffectAsEffect(effectType: EffectType): Effect? {
        return effects[effectType]
    }

    inline fun <reified T : Effect> getEffect(effectType: EffectType): T? {
        return getEffectAsEffect(effectType) as? T
    }

    fun hasEffect(effectType: EffectType): Boolean {
        return effects.containsKey(effectType)
    }

    private fun calculateEffectColor() {
        val color = IntArray(3)
        var count = 0
        for (effect in effects.values) {
            if (effect.isVisible) {
                val c = effect.color
                color[0] += c[0] * (effect.amplifier + 1)
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

    fun getAttribute(attributeType: AttributeType): Attribute {
        return attributes.getValue(attributeType)
    }

    fun setAttributes(attributes: AttributeType, value: Float) {
        val attribute = this.attributes[attributes] ?: return
        attribute.setCurrentValue(value)
    }

    fun getAttributeValue(attributeType: AttributeType): Float {
        return getAttribute(attributeType).getCurrentValue()
    }

    fun getAttributes(): Collection<Attribute> {
        return attributes.values
    }

    var health: Float
        get() = getAttributeValue(AttributeType.HEALTH)
        set(value) {
            var variableValue = value
            if (variableValue < 1) {
                variableValue = 0f
                killInternal()
            }
            val attribute = getAttribute(AttributeType.HEALTH)
            attribute.setCurrentValue(variableValue)
        }
    val maxHealth: Float
        get() = getAttribute(AttributeType.HEALTH).maxValue

    fun setHeal(value: Float) {
        health = 20f.coerceAtMost(0f.coerceAtLeast(value))
    }

    fun setHeal(value: Float, cause: EntityHealEvent.Cause) {
        val entityHealEvent = EntityHealEvent(this, health + value, cause)
        Server.instance.pluginManager.callEvent(entityHealEvent)
        if (entityHealEvent.isCancelled) {
            return
        }
        health = 20f.coerceAtMost(0f.coerceAtLeast(entityHealEvent.heal))
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
