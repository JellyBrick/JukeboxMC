package org.jukeboxmc.player

import com.nukkitx.protocol.bedrock.data.Ability
import com.nukkitx.protocol.bedrock.data.AbilityLayer
import com.nukkitx.protocol.bedrock.data.PlayerPermission
import com.nukkitx.protocol.bedrock.data.command.CommandPermission
import com.nukkitx.protocol.bedrock.packet.UpdateAbilitiesPacket
import com.nukkitx.protocol.bedrock.packet.UpdateAdventureSettingsPacket
import java.util.EnumMap

/**
 * @author KCodeYT
 * @version 1.0
 */
class AdventureSettings(private val player: Player) {
    var walkSpeed = 0.1f
    var flySpeed = 0.05f
    private val values: MutableMap<Type, Boolean> = EnumMap(Type::class.java)

    operator fun set(type: Type, value: Boolean): AdventureSettings {
        values[type] = value
        return this
    }

    operator fun get(type: Type): Boolean {
        val value = values[type]
        return value ?: type.defaultValue
    }

    fun update() {
        val updateAbilitiesPacket = UpdateAbilitiesPacket()
        updateAbilitiesPacket.uniqueEntityId = player.entityId
        updateAbilitiesPacket.commandPermission =
            if (player.isOp) CommandPermission.OPERATOR else CommandPermission.NORMAL
        updateAbilitiesPacket.playerPermission =
            if (player.isOp && player.gameMode != GameMode.SPECTATOR) PlayerPermission.OPERATOR else PlayerPermission.MEMBER
        val abilityLayer = AbilityLayer()
        abilityLayer.layerType = AbilityLayer.Type.BASE
        abilityLayer.abilitiesSet.addAll(listOf(*Ability.values()))
        for (type in Type.values()) {
            if (type.ability != null && this[type]) {
                abilityLayer.abilityValues.add(type.ability)
            }
        }
        abilityLayer.abilityValues.add(Ability.WALK_SPEED)
        abilityLayer.abilityValues.add(Ability.FLY_SPEED)
        if (player.gameMode == GameMode.CREATIVE) {
            abilityLayer.abilityValues.add(Ability.INSTABUILD)
        }
        if (player.isOp) {
            abilityLayer.abilityValues.add(Ability.OPERATOR_COMMANDS)
        }
        abilityLayer.walkSpeed = walkSpeed
        abilityLayer.flySpeed = flySpeed
        updateAbilitiesPacket.abilityLayers.add(abilityLayer)
        val updateAdventureSettingsPacket = UpdateAdventureSettingsPacket()
        updateAdventureSettingsPacket.isAutoJump = this[Type.AUTO_JUMP]
        updateAdventureSettingsPacket.isImmutableWorld = this[Type.WORLD_IMMUTABLE]
        updateAdventureSettingsPacket.isNoMvP = this[Type.NO_MVP]
        updateAdventureSettingsPacket.isNoPvM = this[Type.NO_PVM]
        updateAdventureSettingsPacket.isShowNameTags = this[Type.SHOW_NAME_TAGS]
        player.playerConnection.sendPacket(updateAbilitiesPacket)
        player.playerConnection.sendPacket(updateAdventureSettingsPacket)
    }

    enum class Type {
        WORLD_IMMUTABLE(false),
        NO_PVM(false),
        NO_MVP(
            Ability.INVULNERABLE,
            false,
        ),
        SHOW_NAME_TAGS(true),
        AUTO_JUMP(true),
        ALLOW_FLIGHT(Ability.MAY_FLY, false),
        NO_CLIP(
            Ability.NO_CLIP,
            false,
        ),
        WORLD_BUILDER(Ability.WORLD_BUILDER, true),
        FLYING(Ability.FLYING, false),
        MUTED(Ability.MUTED, false),
        MINE(
            Ability.MINE,
            true,
        ),
        DOORS_AND_SWITCHED(Ability.DOORS_AND_SWITCHES, true),
        OPEN_CONTAINERS(
            Ability.OPEN_CONTAINERS,
            true,
        ),
        ATTACK_PLAYERS(Ability.ATTACK_PLAYERS, true),
        ATTACK_MOBS(
            Ability.ATTACK_MOBS,
            true,
        ),
        OPERATOR(Ability.OPERATOR_COMMANDS, false),
        TELEPORT(Ability.TELEPORT, false),
        BUILD(Ability.BUILD, true),
        ;

        val ability: Ability?
        val defaultValue: Boolean

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
