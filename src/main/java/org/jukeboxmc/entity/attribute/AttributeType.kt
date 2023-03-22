package org.jukeboxmc.entity.attribute

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class AttributeType(
    private val key: String,
    private val minValue: Float,
    private val maxValue: Float,
    private val defaultValue: Float,
) {
    // Base Entity
    ABSORPTION("minecraft:absorption", 0f, Float.MAX_VALUE, 0f), KNOCKBACK_RESISTENCE(
        "minecraft:knockback_resistance",
        0f,
        1f,
        0f,
    ),
    HEALTH("minecraft:health", 0f, 20f, 20f), MOVEMENT(
        "minecraft:movement",
        0.1f,
        1f,
        0.1f,
    ),
    FOLLOW_RANGE("minecraft:follow_range", 0f, 2048f, 16f), ATTACK_DAMAGE(
        "minecraft:attack_damage",
        1f,
        Float.MAX_VALUE,
        1f,
    ),
    LUCK("minecraft:luck", -1024f, 1024f, 0f), FALL_DAMAGE("minecraft:fall_damage", 0f, Float.MAX_VALUE, 1f), // Horse
    HORSE_JUMP_STRENGTH("minecraft:horse.jump_strength", 0f, 20f, 0.7f), // Zombie
    ZOMBIE_SPAWN_REINFORCEMENTS("minecraft:zombie.spawn_reinforcements", 0f, 1f, 0f), // Player
    PLAYER_HUNGER("minecraft:player.hunger", 0f, 20f, 20f), PLAYER_SATURATION(
        "minecraft:player.saturation",
        0f,
        20f,
        20f,
    ),
    PLAYER_EXHAUSTION("minecraft:player.exhaustion", 0f, 5f, 0.41f), PLAYER_LEVEL(
        "minecraft:player.level",
        0f,
        24791f,
        0f,
    ),
    PLAYER_EXPERIENCE("minecraft:player.experience", 0f, 1f, 0f),
    ;

    val attribute: Attribute
        get() = Attribute(key, minValue, maxValue, defaultValue)
}
