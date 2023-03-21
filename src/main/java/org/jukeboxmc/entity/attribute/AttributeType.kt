package org.jukeboxmc.entity.attribute

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class AttributeType(
    private val key: String,
    private val minValue: Float,
    private val maxValue: Float,
    private val defaultValue: Float
) {
    //Base Entity
    ABSORPTION("minecraft:absorption", 0, Float.MAX_VALUE, 0), KNOCKBACK_RESISTENCE(
        "minecraft:knockback_resistance",
        0,
        1,
        0
    ),
    HEALTH("minecraft:health", 0, 20, 20), MOVEMENT(
        "minecraft:movement",
        0.1f,
        1,
        0.1f
    ),
    FOLLOW_RANGE("minecraft:follow_range", 0, 2048, 16), ATTACK_DAMAGE(
        "minecraft:attack_damage",
        1,
        Float.MAX_VALUE,
        1
    ),
    LUCK("minecraft:luck", -1024, 1024, 0), FALL_DAMAGE("minecraft:fall_damage", 0, Float.MAX_VALUE, 1),  //Horse
    HORSE_JUMP_STRENGTH("minecraft:horse.jump_strength", 0, 20, 0.7f),  //Zombie
    ZOMBIE_SPAWN_REINFORCEMENTS("minecraft:zombie.spawn_reinforcements", 0, 1, 0),  //Player
    PLAYER_HUNGER("minecraft:player.hunger", 0, 20, 20), PLAYER_SATURATION(
        "minecraft:player.saturation",
        0,
        20,
        20
    ),
    PLAYER_EXHAUSTION("minecraft:player.exhaustion", 0, 5, 0.41f), PLAYER_LEVEL(
        "minecraft:player.level",
        0,
        24791,
        0
    ),
    PLAYER_EXPERIENCE("minecraft:player.experience", 0, 1, 0);

    val attribute: Attribute
        get() = Attribute(key, minValue, maxValue, defaultValue)
}