package org.jukeboxmc.player

import java.util.Arrays
import lombok.Getter

/**
 * @author KCodeYT
 * @version 1.0
 */
class AdventureSettings(private val player: Player) {
    var walkSpeed = 0.1f
    var flySpeed = 0.05f
    private val values: MutableMap<Type, Boolean> = EnumMap<Type, Boolean>(
        Type::class.java
    )

    operator fun set(type: Type, value: Boolean): AdventureSettings {
        values[type] = value
        return this
    }

    operator fun get(type: Type): Boolean {
        val value = values[type]
        return value ?: type.getDefaultValue()
    }

    fun update() {
        val updateAbilitiesPacket = UpdateAbilitiesPacket()
        updateAbilitiesPacket.setUniqueEntityId(player.entityId)
        updateAbilitiesPacket.setCommandPermission(if (player.isOp) CommandPermission.OPERATOR else CommandPermission.NORMAL)
        updateAbilitiesPacket.setPlayerPermission(if (player.isOp && player.gameMode != GameMode.SPECTATOR) PlayerPermission.OPERATOR else PlayerPermission.MEMBER)
        val abilityLayer = AbilityLayer()
        abilityLayer.setLayerType(AbilityLayer.Type.BASE)
        abilityLayer.getAbilitiesSet().addAll(Arrays.asList<Ability>(*Ability.values()))
        for (type in Type.values()) {
            if (type.getAbility() != null && this[type]) {
                abilityLayer.getAbilityValues().add(type.getAbility())
            }
        }
        abilityLayer.getAbilityValues().add(Ability.WALK_SPEED)
        abilityLayer.getAbilityValues().add(Ability.FLY_SPEED)
        if (player.gameMode == GameMode.CREATIVE) {
            abilityLayer.getAbilityValues().add(Ability.INSTABUILD)
        }
        if (player.isOp) {
            abilityLayer.getAbilityValues().add(Ability.OPERATOR_COMMANDS)
        }
        abilityLayer.setWalkSpeed(walkSpeed)
        abilityLayer.setFlySpeed(flySpeed)
        updateAbilitiesPacket.getAbilityLayers().add(abilityLayer)
        val updateAdventureSettingsPacket = UpdateAdventureSettingsPacket()
        updateAdventureSettingsPacket.setAutoJump(this[Type.AUTO_JUMP])
        updateAdventureSettingsPacket.setImmutableWorld(this[Type.WORLD_IMMUTABLE])
        updateAdventureSettingsPacket.setNoMvP(this[Type.NO_MVP])
        updateAdventureSettingsPacket.setNoPvM(this[Type.NO_PVM])
        updateAdventureSettingsPacket.setShowNameTags(this[Type.SHOW_NAME_TAGS])
        player.playerConnection.sendPacket(updateAbilitiesPacket)
        player.playerConnection.sendPacket(updateAdventureSettingsPacket)
    }

    @Getter
    enum class Type {
        WORLD_IMMUTABLE(false), NO_PVM(false), NO_MVP(
            Ability.INVULNERABLE,
            false
        ),
        SHOW_NAME_TAGS(true), AUTO_JUMP(true), ALLOW_FLIGHT(Ability.MAY_FLY, false), NO_CLIP(
            Ability.NO_CLIP,
            false
        ),
        WORLD_BUILDER(Ability.WORLD_BUILDER, true), FLYING(Ability.FLYING, false), MUTED(Ability.MUTED, false), MINE(
            Ability.MINE,
            true
        ),
        DOORS_AND_SWITCHED(Ability.DOORS_AND_SWITCHES, true), OPEN_CONTAINERS(
            Ability.OPEN_CONTAINERS,
            true
        ),
        ATTACK_PLAYERS(Ability.ATTACK_PLAYERS, true), ATTACK_MOBS(
            Ability.ATTACK_MOBS,
            true
        ),
        OPERATOR(Ability.OPERATOR_COMMANDS, false), TELEPORT(Ability.TELEPORT, false), BUILD(Ability.BUILD, true);

        private val ability: Ability?
        private val defaultValue: Boolean

        constructor(ability: Ability?, defaultValue: Boolean) {
            this.ability = ability
            this.defaultValue = defaultValue
        }

        constructor(defaultValue: Boolean) {
            this.defaultValue = defaultValue
            ability = null
        }
    }
}