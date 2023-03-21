package org.jukeboxmc.util

/**
 * @author LucGamesYT
 * @version 1.0
 */
object IdentifierMapping {
    private val identifierMap: MutableMap<Identifier, Identifier> = LinkedHashMap()
    fun init() {
        identifierMap[Identifier.fromString("minecraft:acacia_door")] =
            Identifier.fromString("minecraft:item.acacia_door")
        identifierMap[Identifier.fromString("minecraft:bed")] =
            Identifier.fromString("minecraft:item.bed")
        identifierMap[Identifier.fromString("minecraft:beetroot")] =
            Identifier.fromString("minecraft:item.beetroot")
        identifierMap[Identifier.fromString("minecraft:birch_door")] =
            Identifier.fromString("minecraft:item.birch_door")
        identifierMap[Identifier.fromString("minecraft:brewing_stand")] =
            Identifier.fromString("minecraft:item.brewing_stand")
        identifierMap[Identifier.fromString("minecraft:cake")] =
            Identifier.fromString("minecraft:item.cake")
        identifierMap[Identifier.fromString("minecraft:camera")] =
            Identifier.fromString("minecraft:item.camera")
        identifierMap[Identifier.fromString("minecraft:campfire")] =
            Identifier.fromString("minecraft:item.campfire")
        identifierMap[Identifier.fromString("minecraft:cauldron")] =
            Identifier.fromString("minecraft:item.cauldron")
        identifierMap[Identifier.fromString("minecraft:chain")] =
            Identifier.fromString("minecraft:item.chain")
        identifierMap[Identifier.fromString("minecraft:crimson_door")] =
            Identifier.fromString("minecraft:item.crimson_door")
        identifierMap[Identifier.fromString("minecraft:dark_oak_door")] =
            Identifier.fromString("minecraft:item.dark_oak_door")
        identifierMap[Identifier.fromString("minecraft:flower_pot")] =
            Identifier.fromString("minecraft:item.flower_pot")
        identifierMap[Identifier.fromString("minecraft:frame")] =
            Identifier.fromString("minecraft:item.frame")
        identifierMap[Identifier.fromString("minecraft:glow_frame")] =
            Identifier.fromString("minecraft:item.glow_frame")
        identifierMap[Identifier.fromString("minecraft:hopper")] =
            Identifier.fromString("minecraft:item.hopper")
        identifierMap[Identifier.fromString("minecraft:iron_door")] =
            Identifier.fromString("minecraft:item.iron_door")
        identifierMap[Identifier.fromString("minecraft:jungle_door")] =
            Identifier.fromString("minecraft:item.jungle_door")
        identifierMap[Identifier.fromString("minecraft:kelp")] =
            Identifier.fromString("minecraft:item.kelp")
        identifierMap[Identifier.fromString("minecraft:mangrove_door")] =
            Identifier.fromString("minecraft:item.mangrove_door")
        identifierMap[Identifier.fromString("minecraft:nether_sprouts")] =
            Identifier.fromString("minecraft:item.nether_sprouts")
        identifierMap[Identifier.fromString("minecraft:nether_wart")] =
            Identifier.fromString("minecraft:item.nether_wart")
        identifierMap[Identifier.fromString("minecraft:reeds")] =
            Identifier.fromString("minecraft:item.reeds")
        identifierMap[Identifier.fromString("minecraft:skull")] =
            Identifier.fromString("minecraft:item.skull")
        identifierMap[Identifier.fromString("minecraft:soul_campfire")] =
            Identifier.fromString("minecraft:item.soul_campfire")
        identifierMap[Identifier.fromString("minecraft:spruce_door")] =
            Identifier.fromString("minecraft:item.spruce_door")
        identifierMap[Identifier.fromString("minecraft:warped_door")] =
            Identifier.fromString("minecraft:item.warped_door")
        identifierMap[Identifier.fromString("minecraft:wheat")] =
            Identifier.fromString("minecraft:item.wheat")
        identifierMap[Identifier.fromString("minecraft:wooden_door")] =
            Identifier.fromString("minecraft:item.wooden_door")
    }

    fun toItemIdentifier(blockIdentifier: Identifier): Identifier? {
        return identifierMap[blockIdentifier]
    }

    fun mappingExists(blockIdentifier: Identifier): Boolean {
        return identifierMap.containsKey(blockIdentifier)
    }
}
