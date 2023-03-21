package org.jukeboxmc.util

/**
 * @author LucGamesYT
 * @version 1.0
 */
object IdentifierMapping {
    private val identifierMap: MutableMap<Identifier, Identifier> = LinkedHashMap()
    fun init() {
        identifierMap[Identifier.Companion.fromString("minecraft:acacia_door")] =
            Identifier.Companion.fromString("minecraft:item.acacia_door")
        identifierMap[Identifier.Companion.fromString("minecraft:bed")] =
            Identifier.Companion.fromString("minecraft:item.bed")
        identifierMap[Identifier.Companion.fromString("minecraft:beetroot")] =
            Identifier.Companion.fromString("minecraft:item.beetroot")
        identifierMap[Identifier.Companion.fromString("minecraft:birch_door")] =
            Identifier.Companion.fromString("minecraft:item.birch_door")
        identifierMap[Identifier.Companion.fromString("minecraft:brewing_stand")] =
            Identifier.Companion.fromString("minecraft:item.brewing_stand")
        identifierMap[Identifier.Companion.fromString("minecraft:cake")] =
            Identifier.Companion.fromString("minecraft:item.cake")
        identifierMap[Identifier.Companion.fromString("minecraft:camera")] =
            Identifier.Companion.fromString("minecraft:item.camera")
        identifierMap[Identifier.Companion.fromString("minecraft:campfire")] =
            Identifier.Companion.fromString("minecraft:item.campfire")
        identifierMap[Identifier.Companion.fromString("minecraft:cauldron")] =
            Identifier.Companion.fromString("minecraft:item.cauldron")
        identifierMap[Identifier.Companion.fromString("minecraft:chain")] =
            Identifier.Companion.fromString("minecraft:item.chain")
        identifierMap[Identifier.Companion.fromString("minecraft:crimson_door")] =
            Identifier.Companion.fromString("minecraft:item.crimson_door")
        identifierMap[Identifier.Companion.fromString("minecraft:dark_oak_door")] =
            Identifier.Companion.fromString("minecraft:item.dark_oak_door")
        identifierMap[Identifier.Companion.fromString("minecraft:flower_pot")] =
            Identifier.Companion.fromString("minecraft:item.flower_pot")
        identifierMap[Identifier.Companion.fromString("minecraft:frame")] =
            Identifier.Companion.fromString("minecraft:item.frame")
        identifierMap[Identifier.Companion.fromString("minecraft:glow_frame")] =
            Identifier.Companion.fromString("minecraft:item.glow_frame")
        identifierMap[Identifier.Companion.fromString("minecraft:hopper")] =
            Identifier.Companion.fromString("minecraft:item.hopper")
        identifierMap[Identifier.Companion.fromString("minecraft:iron_door")] =
            Identifier.Companion.fromString("minecraft:item.iron_door")
        identifierMap[Identifier.Companion.fromString("minecraft:jungle_door")] =
            Identifier.Companion.fromString("minecraft:item.jungle_door")
        identifierMap[Identifier.Companion.fromString("minecraft:kelp")] =
            Identifier.Companion.fromString("minecraft:item.kelp")
        identifierMap[Identifier.Companion.fromString("minecraft:mangrove_door")] =
            Identifier.Companion.fromString("minecraft:item.mangrove_door")
        identifierMap[Identifier.Companion.fromString("minecraft:nether_sprouts")] =
            Identifier.Companion.fromString("minecraft:item.nether_sprouts")
        identifierMap[Identifier.Companion.fromString("minecraft:nether_wart")] =
            Identifier.Companion.fromString("minecraft:item.nether_wart")
        identifierMap[Identifier.Companion.fromString("minecraft:reeds")] =
            Identifier.Companion.fromString("minecraft:item.reeds")
        identifierMap[Identifier.Companion.fromString("minecraft:skull")] =
            Identifier.Companion.fromString("minecraft:item.skull")
        identifierMap[Identifier.Companion.fromString("minecraft:soul_campfire")] =
            Identifier.Companion.fromString("minecraft:item.soul_campfire")
        identifierMap[Identifier.Companion.fromString("minecraft:spruce_door")] =
            Identifier.Companion.fromString("minecraft:item.spruce_door")
        identifierMap[Identifier.Companion.fromString("minecraft:warped_door")] =
            Identifier.Companion.fromString("minecraft:item.warped_door")
        identifierMap[Identifier.Companion.fromString("minecraft:wheat")] =
            Identifier.Companion.fromString("minecraft:item.wheat")
        identifierMap[Identifier.Companion.fromString("minecraft:wooden_door")] =
            Identifier.Companion.fromString("minecraft:item.wooden_door")
    }

    fun toItemIdentifier(blockIdentifier: Identifier): Identifier? {
        return identifierMap[blockIdentifier]
    }

    fun mappingExists(blockIdentifier: Identifier): Boolean {
        return identifierMap.containsKey(blockIdentifier)
    }
}