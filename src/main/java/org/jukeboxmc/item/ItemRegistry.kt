package org.jukeboxmc.item

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.cloudburstmc.protocol.bedrock.data.defintions.ItemDefinition
import org.jukeboxmc.Bootstrap
import org.jukeboxmc.item.behavior.ItemAcaciaSign
import org.jukeboxmc.item.behavior.ItemAir
import org.jukeboxmc.item.behavior.ItemAnvil
import org.jukeboxmc.item.behavior.ItemApple
import org.jukeboxmc.item.behavior.ItemBakedPotato
import org.jukeboxmc.item.behavior.ItemBamboo
import org.jukeboxmc.item.behavior.ItemBanner
import org.jukeboxmc.item.behavior.ItemBed
import org.jukeboxmc.item.behavior.ItemBeef
import org.jukeboxmc.item.behavior.ItemBeehive
import org.jukeboxmc.item.behavior.ItemBeetroot
import org.jukeboxmc.item.behavior.ItemBeetrootSoup
import org.jukeboxmc.item.behavior.ItemBirchSign
import org.jukeboxmc.item.behavior.ItemBlackWool
import org.jukeboxmc.item.behavior.ItemBlazeRod
import org.jukeboxmc.item.behavior.ItemBoat
import org.jukeboxmc.item.behavior.ItemBookshelf
import org.jukeboxmc.item.behavior.ItemBow
import org.jukeboxmc.item.behavior.ItemBowl
import org.jukeboxmc.item.behavior.ItemBread
import org.jukeboxmc.item.behavior.ItemBrewingStand
import org.jukeboxmc.item.behavior.ItemBrownWool
import org.jukeboxmc.item.behavior.ItemBucket
import org.jukeboxmc.item.behavior.ItemCampfire
import org.jukeboxmc.item.behavior.ItemCandle
import org.jukeboxmc.item.behavior.ItemCarpet
import org.jukeboxmc.item.behavior.ItemCarrot
import org.jukeboxmc.item.behavior.ItemChainBoots
import org.jukeboxmc.item.behavior.ItemChainChestplate
import org.jukeboxmc.item.behavior.ItemChainHelmet
import org.jukeboxmc.item.behavior.ItemChainLeggings
import org.jukeboxmc.item.behavior.ItemCharcoal
import org.jukeboxmc.item.behavior.ItemChest
import org.jukeboxmc.item.behavior.ItemChicken
import org.jukeboxmc.item.behavior.ItemCoal
import org.jukeboxmc.item.behavior.ItemCoalBlock
import org.jukeboxmc.item.behavior.ItemCod
import org.jukeboxmc.item.behavior.ItemConcrete
import org.jukeboxmc.item.behavior.ItemConcretePowder
import org.jukeboxmc.item.behavior.ItemCookedBeef
import org.jukeboxmc.item.behavior.ItemCookedChicken
import org.jukeboxmc.item.behavior.ItemCookedCod
import org.jukeboxmc.item.behavior.ItemCookedMutton
import org.jukeboxmc.item.behavior.ItemCookedPorkchop
import org.jukeboxmc.item.behavior.ItemCookedRabbit
import org.jukeboxmc.item.behavior.ItemCookedSalmon
import org.jukeboxmc.item.behavior.ItemCookie
import org.jukeboxmc.item.behavior.ItemCoral
import org.jukeboxmc.item.behavior.ItemCoralFan
import org.jukeboxmc.item.behavior.ItemCoralFanDead
import org.jukeboxmc.item.behavior.ItemCraftingTable
import org.jukeboxmc.item.behavior.ItemCrimsonSign
import org.jukeboxmc.item.behavior.ItemCyanWool
import org.jukeboxmc.item.behavior.ItemDarkOakSign
import org.jukeboxmc.item.behavior.ItemDaylightSensor
import org.jukeboxmc.item.behavior.ItemDiamondAxe
import org.jukeboxmc.item.behavior.ItemDiamondBoots
import org.jukeboxmc.item.behavior.ItemDiamondChestplate
import org.jukeboxmc.item.behavior.ItemDiamondHelmet
import org.jukeboxmc.item.behavior.ItemDiamondHoe
import org.jukeboxmc.item.behavior.ItemDiamondLeggings
import org.jukeboxmc.item.behavior.ItemDiamondPickaxe
import org.jukeboxmc.item.behavior.ItemDiamondShovel
import org.jukeboxmc.item.behavior.ItemDiamondSword
import org.jukeboxmc.item.behavior.ItemDirt
import org.jukeboxmc.item.behavior.ItemDoor
import org.jukeboxmc.item.behavior.ItemDoublePlant
import org.jukeboxmc.item.behavior.ItemDriedKelp
import org.jukeboxmc.item.behavior.ItemEgg
import org.jukeboxmc.item.behavior.ItemEnchantedGoldenApple
import org.jukeboxmc.item.behavior.ItemFence
import org.jukeboxmc.item.behavior.ItemFishingRod
import org.jukeboxmc.item.behavior.ItemGlowBerries
import org.jukeboxmc.item.behavior.ItemGoldenApple
import org.jukeboxmc.item.behavior.ItemGoldenAxe
import org.jukeboxmc.item.behavior.ItemGoldenBoots
import org.jukeboxmc.item.behavior.ItemGoldenCarrot
import org.jukeboxmc.item.behavior.ItemGoldenChestplate
import org.jukeboxmc.item.behavior.ItemGoldenHelmet
import org.jukeboxmc.item.behavior.ItemGoldenHoe
import org.jukeboxmc.item.behavior.ItemGoldenLeggings
import org.jukeboxmc.item.behavior.ItemGoldenPickaxe
import org.jukeboxmc.item.behavior.ItemGoldenShovel
import org.jukeboxmc.item.behavior.ItemGoldenSword
import org.jukeboxmc.item.behavior.ItemGrayWool
import org.jukeboxmc.item.behavior.ItemGreenWool
import org.jukeboxmc.item.behavior.ItemHopper
import org.jukeboxmc.item.behavior.ItemInfestedStone
import org.jukeboxmc.item.behavior.ItemIronAxe
import org.jukeboxmc.item.behavior.ItemIronBoots
import org.jukeboxmc.item.behavior.ItemIronChestplate
import org.jukeboxmc.item.behavior.ItemIronHelmet
import org.jukeboxmc.item.behavior.ItemIronHoe
import org.jukeboxmc.item.behavior.ItemIronLeggings
import org.jukeboxmc.item.behavior.ItemIronPickaxe
import org.jukeboxmc.item.behavior.ItemIronShovel
import org.jukeboxmc.item.behavior.ItemIronSword
import org.jukeboxmc.item.behavior.ItemJukebox
import org.jukeboxmc.item.behavior.ItemJungleSign
import org.jukeboxmc.item.behavior.ItemLadder
import org.jukeboxmc.item.behavior.ItemLavaBucket
import org.jukeboxmc.item.behavior.ItemLeatherBoots
import org.jukeboxmc.item.behavior.ItemLeatherChestplate
import org.jukeboxmc.item.behavior.ItemLeatherHelmet
import org.jukeboxmc.item.behavior.ItemLeatherLeggings
import org.jukeboxmc.item.behavior.ItemLeaves
import org.jukeboxmc.item.behavior.ItemLeaves2
import org.jukeboxmc.item.behavior.ItemLightBlueWool
import org.jukeboxmc.item.behavior.ItemLimeWool
import org.jukeboxmc.item.behavior.ItemLog
import org.jukeboxmc.item.behavior.ItemLog2
import org.jukeboxmc.item.behavior.ItemMagentaWool
import org.jukeboxmc.item.behavior.ItemMangroveFence
import org.jukeboxmc.item.behavior.ItemMangroveFenceGate
import org.jukeboxmc.item.behavior.ItemMangroveSign
import org.jukeboxmc.item.behavior.ItemMelonSlice
import org.jukeboxmc.item.behavior.ItemMushroomStew
import org.jukeboxmc.item.behavior.ItemMutton
import org.jukeboxmc.item.behavior.ItemNetherWart
import org.jukeboxmc.item.behavior.ItemNetheriteAxe
import org.jukeboxmc.item.behavior.ItemNetheriteBoots
import org.jukeboxmc.item.behavior.ItemNetheriteChestplate
import org.jukeboxmc.item.behavior.ItemNetheriteHelmet
import org.jukeboxmc.item.behavior.ItemNetheriteHoe
import org.jukeboxmc.item.behavior.ItemNetheriteLeggings
import org.jukeboxmc.item.behavior.ItemNetheritePickaxe
import org.jukeboxmc.item.behavior.ItemNetheriteShovel
import org.jukeboxmc.item.behavior.ItemNetheriteSword
import org.jukeboxmc.item.behavior.ItemNoteblock
import org.jukeboxmc.item.behavior.ItemOakSign
import org.jukeboxmc.item.behavior.ItemOrangeWool
import org.jukeboxmc.item.behavior.ItemPinkWool
import org.jukeboxmc.item.behavior.ItemPlanks
import org.jukeboxmc.item.behavior.ItemPoisonousPotato
import org.jukeboxmc.item.behavior.ItemPorkchop
import org.jukeboxmc.item.behavior.ItemPotato
import org.jukeboxmc.item.behavior.ItemPowderSnowBucket
import org.jukeboxmc.item.behavior.ItemPrismarine
import org.jukeboxmc.item.behavior.ItemPufferfish
import org.jukeboxmc.item.behavior.ItemPumpkinPie
import org.jukeboxmc.item.behavior.ItemPurpleWool
import org.jukeboxmc.item.behavior.ItemPurpurBlock
import org.jukeboxmc.item.behavior.ItemQuartzBlock
import org.jukeboxmc.item.behavior.ItemRabbit
import org.jukeboxmc.item.behavior.ItemRabbitStew
import org.jukeboxmc.item.behavior.ItemRedFlower
import org.jukeboxmc.item.behavior.ItemRedSandstone
import org.jukeboxmc.item.behavior.ItemRedWool
import org.jukeboxmc.item.behavior.ItemSalmon
import org.jukeboxmc.item.behavior.ItemSand
import org.jukeboxmc.item.behavior.ItemSandstone
import org.jukeboxmc.item.behavior.ItemSapling
import org.jukeboxmc.item.behavior.ItemShulkerBox
import org.jukeboxmc.item.behavior.ItemSilverWool
import org.jukeboxmc.item.behavior.ItemSkull
import org.jukeboxmc.item.behavior.ItemSnowball
import org.jukeboxmc.item.behavior.ItemSoulCampfire
import org.jukeboxmc.item.behavior.ItemSpruceSign
import org.jukeboxmc.item.behavior.ItemStainedGlass
import org.jukeboxmc.item.behavior.ItemStainedGlassPane
import org.jukeboxmc.item.behavior.ItemStainedHardenedClay
import org.jukeboxmc.item.behavior.ItemStairs
import org.jukeboxmc.item.behavior.ItemStick
import org.jukeboxmc.item.behavior.ItemStone
import org.jukeboxmc.item.behavior.ItemStoneAxe
import org.jukeboxmc.item.behavior.ItemStoneHoe
import org.jukeboxmc.item.behavior.ItemStonePickaxe
import org.jukeboxmc.item.behavior.ItemStoneShovel
import org.jukeboxmc.item.behavior.ItemStoneSlab
import org.jukeboxmc.item.behavior.ItemStoneSlab2
import org.jukeboxmc.item.behavior.ItemStoneSlab3
import org.jukeboxmc.item.behavior.ItemStoneSlab4
import org.jukeboxmc.item.behavior.ItemStoneSword
import org.jukeboxmc.item.behavior.ItemStonebrick
import org.jukeboxmc.item.behavior.ItemSugarCane
import org.jukeboxmc.item.behavior.ItemSweetBerries
import org.jukeboxmc.item.behavior.ItemTallGrass
import org.jukeboxmc.item.behavior.ItemTrappedChest
import org.jukeboxmc.item.behavior.ItemTropicalFish
import org.jukeboxmc.item.behavior.ItemTurtleHelmet
import org.jukeboxmc.item.behavior.ItemUndyedShulkerBox
import org.jukeboxmc.item.behavior.ItemWall
import org.jukeboxmc.item.behavior.ItemWarpedSign
import org.jukeboxmc.item.behavior.ItemWaterBucket
import org.jukeboxmc.item.behavior.ItemWhiteWool
import org.jukeboxmc.item.behavior.ItemWood
import org.jukeboxmc.item.behavior.ItemWoodenAxe
import org.jukeboxmc.item.behavior.ItemWoodenButton
import org.jukeboxmc.item.behavior.ItemWoodenDoor
import org.jukeboxmc.item.behavior.ItemWoodenFenceGate
import org.jukeboxmc.item.behavior.ItemWoodenHoe
import org.jukeboxmc.item.behavior.ItemWoodenPickaxe
import org.jukeboxmc.item.behavior.ItemWoodenPressurePlate
import org.jukeboxmc.item.behavior.ItemWoodenShovel
import org.jukeboxmc.item.behavior.ItemWoodenSlab
import org.jukeboxmc.item.behavior.ItemWoodenSword
import org.jukeboxmc.item.behavior.ItemWoodenTrapdoor
import org.jukeboxmc.item.behavior.ItemYellowWool
import org.jukeboxmc.network.registry.SimpleDefinitionRegistry
import org.jukeboxmc.util.Identifier
import java.io.InputStreamReader
import java.util.stream.Collectors

/**
 * @author LucGamesYT
 * @version 1.0
 */
object ItemRegistry {
    private val ITEMS: MutableMap<ItemType, ItemRegistryData> = LinkedHashMap()
    private val ITEM_PROPERTIES: MutableMap<Identifier, ItemProperties> = LinkedHashMap()
    private val ITEMCLASS_FROM_ITEMTYPE: MutableMap<ItemType, Class<out Item>> = LinkedHashMap()
    private val ITEMTYPE_FROM_IDENTIFIER: MutableMap<Identifier, ItemType> = LinkedHashMap()
    private val itemDefinitionRegistry =
        SimpleDefinitionRegistry.getRegistry<ItemDefinition, SimpleDefinitionRegistry<ItemDefinition>>()

    fun init() {
        register(ItemType.ACACIA_BOAT, ItemRegistryData(Identifier.fromString("minecraft:acacia_boat")))
        register(
            ItemType.ACACIA_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.ACACIA_CHEST_BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_chest_boat")),
        )
        register(
            ItemType.ACACIA_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.ACACIA_FENCE_GATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:acacia_fence_gate"),
                ItemWoodenFenceGate::class.java,
            ),
        )
        register(
            ItemType.ACACIA_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:acacia_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.ACACIA_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_sign"), ItemAcaciaSign::class.java),
        )
        register(
            ItemType.ACACIA_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.ACACIA_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_standing_sign")),
        )
        register(
            ItemType.ACACIA_TRAPDOOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:acacia_trapdoor"),
                ItemWoodenTrapdoor::class.java,
            ),
        )
        register(
            ItemType.ACACIA_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:acacia_wall_sign")),
        )
        register(ItemType.ACTIVATOR_RAIL, ItemRegistryData(Identifier.fromString("minecraft:activator_rail")))
        register(
            ItemType.AGENT_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:agent_spawn_egg")),
        )
        register(ItemType.AIR, ItemRegistryData(Identifier.fromString("minecraft:air"), ItemAir::class.java))
        register(
            ItemType.ALLAY_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:allay_spawn_egg")),
        )
        register(ItemType.ALLOW, ItemRegistryData(Identifier.fromString("minecraft:allow")))
        register(ItemType.AMETHYST_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:amethyst_block")))
        register(
            ItemType.AMETHYST_CLUSTER,
            ItemRegistryData(Identifier.fromString("minecraft:amethyst_cluster")),
        )
        register(ItemType.AMETHYST_SHARD, ItemRegistryData(Identifier.fromString("minecraft:amethyst_shard")))
        register(ItemType.ANCIENT_DEBRIS, ItemRegistryData(Identifier.fromString("minecraft:ancient_debris")))
        register(
            ItemType.ANDESITE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:andesite_stairs")),
        )
        register(
            ItemType.ANVIL,
            ItemRegistryData(Identifier.fromString("minecraft:anvil"), ItemAnvil::class.java),
        )
        register(
            ItemType.APPLE,
            ItemRegistryData(Identifier.fromString("minecraft:apple"), ItemApple::class.java),
        )
        register(ItemType.ARMOR_STAND, ItemRegistryData(Identifier.fromString("minecraft:armor_stand")))
        register(ItemType.ARROW, ItemRegistryData(Identifier.fromString("minecraft:arrow")))
        register(
            ItemType.AXOLOTL_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:axolotl_bucket"), ItemBucket::class.java),
        )
        register(
            ItemType.AXOLOTL_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:axolotl_spawn_egg")),
        )
        register(ItemType.AZALEA, ItemRegistryData(Identifier.fromString("minecraft:azalea")))
        register(ItemType.AZALEA_LEAVES, ItemRegistryData(Identifier.fromString("minecraft:azalea_leaves")))
        register(
            ItemType.AZALEA_LEAVES_FLOWERED,
            ItemRegistryData(Identifier.fromString("minecraft:azalea_leaves_flowered")),
        )
        register(
            ItemType.BAKED_POTATO,
            ItemRegistryData(Identifier.fromString("minecraft:baked_potato"), ItemBakedPotato::class.java),
        )
        register(ItemType.BALLOON, ItemRegistryData(Identifier.fromString("minecraft:balloon")))
        register(
            ItemType.BAMBOO,
            ItemRegistryData(Identifier.fromString("minecraft:bamboo"), ItemBamboo::class.java),
        )
        register(ItemType.BAMBOO_SAPLING, ItemRegistryData(Identifier.fromString("minecraft:bamboo_sapling")))
        register(
            ItemType.BANNER,
            ItemRegistryData(Identifier.fromString("minecraft:banner"), ItemBanner::class.java),
        )
        register(ItemType.BANNER_PATTERN, ItemRegistryData(Identifier.fromString("minecraft:banner_pattern")))
        register(ItemType.BARREL, ItemRegistryData(Identifier.fromString("minecraft:barrel")))
        register(ItemType.BARRIER, ItemRegistryData(Identifier.fromString("minecraft:barrier")))
        register(ItemType.BASALT, ItemRegistryData(Identifier.fromString("minecraft:basalt")))
        register(ItemType.BAT_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:bat_spawn_egg")))
        register(ItemType.BEACON, ItemRegistryData(Identifier.fromString("minecraft:beacon")))
        register(ItemType.BED, ItemRegistryData(Identifier.fromString("minecraft:bed"), ItemBed::class.java))
        register(ItemType.BEDROCK, ItemRegistryData(Identifier.fromString("minecraft:bedrock")))
        register(ItemType.BEE_NEST, ItemRegistryData(Identifier.fromString("minecraft:bee_nest")))
        register(ItemType.BEE_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:bee_spawn_egg")))
        register(
            ItemType.BEEF,
            ItemRegistryData(Identifier.fromString("minecraft:beef"), ItemBeef::class.java),
        )
        register(
            ItemType.BEEHIVE,
            ItemRegistryData(Identifier.fromString("minecraft:beehive"), ItemBeehive::class.java),
        )
        register(
            ItemType.BEETROOT,
            ItemRegistryData(Identifier.fromString("minecraft:beetroot"), ItemBeetroot::class.java),
        )
        register(ItemType.BEETROOT_SEEDS, ItemRegistryData(Identifier.fromString("minecraft:beetroot_seeds")))
        register(
            ItemType.BEETROOT_SOUP,
            ItemRegistryData(Identifier.fromString("minecraft:beetroot_soup"), ItemBeetrootSoup::class.java),
        )
        register(ItemType.BELL, ItemRegistryData(Identifier.fromString("minecraft:bell")))
        register(ItemType.BIG_DRIPLEAF, ItemRegistryData(Identifier.fromString("minecraft:big_dripleaf")))
        register(ItemType.BIRCH_BOAT, ItemRegistryData(Identifier.fromString("minecraft:birch_boat")))
        register(
            ItemType.BIRCH_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:birch_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.BIRCH_CHEST_BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:birch_chest_boat")),
        )
        register(
            ItemType.BIRCH_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:birch_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.BIRCH_FENCE_GATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:birch_fence_gate"),
                ItemWoodenFenceGate::class.java,
            ),
        )
        register(
            ItemType.BIRCH_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:birch_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.BIRCH_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:birch_sign"), ItemBirchSign::class.java),
        )
        register(
            ItemType.BIRCH_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:birch_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.BIRCH_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:birch_standing_sign")),
        )
        register(
            ItemType.BIRCH_TRAPDOOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:birch_trapdoor"),
                ItemWoodenTrapdoor::class.java,
            ),
        )
        register(
            ItemType.BIRCH_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:birch_wall_sign")),
        )
        register(
            ItemType.BLACK_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:black_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.BLACK_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:black_candle_cake")),
        )
        register(ItemType.BLACK_DYE, ItemRegistryData(Identifier.fromString("minecraft:black_dye")))
        register(
            ItemType.BLACK_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:black_glazed_terracotta")),
        )
        register(ItemType.BLACKSTONE, ItemRegistryData(Identifier.fromString("minecraft:blackstone")))
        register(
            ItemType.BLACKSTONE_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:blackstone_double_slab")),
        )
        register(
            ItemType.BLACKSTONE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:blackstone_slab")),
        )
        register(
            ItemType.BLACKSTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:blackstone_stairs")),
        )
        register(
            ItemType.BLACKSTONE_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:blackstone_wall")),
        )
        register(ItemType.BLAST_FURNACE, ItemRegistryData(Identifier.fromString("minecraft:blast_furnace")))
        register(ItemType.BLAZE_POWDER, ItemRegistryData(Identifier.fromString("minecraft:blaze_powder")))
        register(
            ItemType.BLAZE_ROD,
            ItemRegistryData(Identifier.fromString("minecraft:blaze_rod"), ItemBlazeRod::class.java),
        )
        register(
            ItemType.BLAZE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:blaze_spawn_egg")),
        )
        register(ItemType.BLEACH, ItemRegistryData(Identifier.fromString("minecraft:bleach")))
        register(
            ItemType.BLUE_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:blue_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.BLUE_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:blue_candle_cake")),
        )
        register(ItemType.BLUE_DYE, ItemRegistryData(Identifier.fromString("minecraft:blue_dye")))
        register(
            ItemType.BLUE_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:blue_glazed_terracotta")),
        )
        register(ItemType.BLUE_ICE, ItemRegistryData(Identifier.fromString("minecraft:blue_ice")))
        register(
            ItemType.BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:boat"), ItemBoat::class.java),
        )
        register(ItemType.BONE, ItemRegistryData(Identifier.fromString("minecraft:bone")))
        register(ItemType.BONE_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:bone_block")))
        register(ItemType.BONE_MEAL, ItemRegistryData(Identifier.fromString("minecraft:bone_meal")))
        register(ItemType.BOOK, ItemRegistryData(Identifier.fromString("minecraft:book")))
        register(
            ItemType.BOOKSHELF,
            ItemRegistryData(Identifier.fromString("minecraft:bookshelf"), ItemBookshelf::class.java),
        )
        register(ItemType.BORDER_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:border_block")))
        register(
            ItemType.BORDURE_INDENTED_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:bordure_indented_banner_pattern")),
        )
        register(ItemType.BOW, ItemRegistryData(Identifier.fromString("minecraft:bow"), ItemBow::class.java))
        register(
            ItemType.BOWL,
            ItemRegistryData(Identifier.fromString("minecraft:bowl"), ItemBowl::class.java),
        )
        register(
            ItemType.BREAD,
            ItemRegistryData(Identifier.fromString("minecraft:bread"), ItemBread::class.java),
        )
        register(
            ItemType.BREWING_STAND,
            ItemRegistryData(Identifier.fromString("minecraft:brewing_stand"), ItemBrewingStand::class.java),
        )
        register(ItemType.BRICK, ItemRegistryData(Identifier.fromString("minecraft:brick")))
        register(ItemType.BRICK_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:brick_block")))
        register(ItemType.BRICK_STAIRS, ItemRegistryData(Identifier.fromString("minecraft:brick_stairs")))
        register(
            ItemType.BROWN_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:brown_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.BROWN_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:brown_candle_cake")),
        )
        register(ItemType.BROWN_DYE, ItemRegistryData(Identifier.fromString("minecraft:brown_dye")))
        register(
            ItemType.BROWN_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:brown_glazed_terracotta")),
        )
        register(ItemType.BROWN_MUSHROOM, ItemRegistryData(Identifier.fromString("minecraft:brown_mushroom")))
        register(
            ItemType.BROWN_MUSHROOM_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:brown_mushroom_block")),
        )
        register(ItemType.BUBBLE_COLUMN, ItemRegistryData(Identifier.fromString("minecraft:bubble_column")))
        register(
            ItemType.BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:bucket"), ItemBucket::class.java),
        )
        register(
            ItemType.BUDDING_AMETHYST,
            ItemRegistryData(Identifier.fromString("minecraft:budding_amethyst")),
        )
        register(ItemType.CACTUS, ItemRegistryData(Identifier.fromString("minecraft:cactus")))
        register(ItemType.CAKE, ItemRegistryData(Identifier.fromString("minecraft:cake")))
        register(ItemType.CALCITE, ItemRegistryData(Identifier.fromString("minecraft:calcite")))
        register(ItemType.CAMERA, ItemRegistryData(Identifier.fromString("minecraft:camera")))
        register(
            ItemType.CAMPFIRE,
            ItemRegistryData(Identifier.fromString("minecraft:campfire"), ItemCampfire::class.java),
        )
        register(
            ItemType.CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:candle"), ItemCandle::class.java),
        )
        register(ItemType.CANDLE_CAKE, ItemRegistryData(Identifier.fromString("minecraft:candle_cake")))
        register(
            ItemType.CARPET,
            ItemRegistryData(Identifier.fromString("minecraft:carpet"), ItemCarpet::class.java),
        )
        register(
            ItemType.CARROT,
            ItemRegistryData(Identifier.fromString("minecraft:carrot"), ItemCarrot::class.java),
        )
        register(
            ItemType.CARROT_ON_A_STICK,
            ItemRegistryData(Identifier.fromString("minecraft:carrot_on_a_stick")),
        )
        register(ItemType.CARROTS, ItemRegistryData(Identifier.fromString("minecraft:carrots")))
        register(
            ItemType.CARTOGRAPHY_TABLE,
            ItemRegistryData(Identifier.fromString("minecraft:cartography_table")),
        )
        register(ItemType.CARVED_PUMPKIN, ItemRegistryData(Identifier.fromString("minecraft:carved_pumpkin")))
        register(ItemType.CAT_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:cat_spawn_egg")))
        register(ItemType.CAULDRON, ItemRegistryData(Identifier.fromString("minecraft:cauldron")))
        register(
            ItemType.CAVE_SPIDER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:cave_spider_spawn_egg")),
        )
        register(ItemType.CAVE_VINES, ItemRegistryData(Identifier.fromString("minecraft:cave_vines")))
        register(
            ItemType.CAVE_VINES_BODY_WITH_BERRIES,
            ItemRegistryData(Identifier.fromString("minecraft:cave_vines_body_with_berries")),
        )
        register(
            ItemType.CAVE_VINES_HEAD_WITH_BERRIES,
            ItemRegistryData(Identifier.fromString("minecraft:cave_vines_head_with_berries")),
        )
        register(ItemType.CHAIN, ItemRegistryData(Identifier.fromString("minecraft:chain")))
        register(
            ItemType.CHAIN_COMMAND_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:chain_command_block")),
        )
        register(
            ItemType.CHAINMAIL_BOOTS,
            ItemRegistryData(Identifier.fromString("minecraft:chainmail_boots"), ItemChainBoots::class.java),
        )
        register(
            ItemType.CHAINMAIL_CHESTPLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:chainmail_chestplate"),
                ItemChainChestplate::class.java,
            ),
        )
        register(
            ItemType.CHAINMAIL_HELMET,
            ItemRegistryData(Identifier.fromString("minecraft:chainmail_helmet"), ItemChainHelmet::class.java),
        )
        register(
            ItemType.CHAINMAIL_LEGGINGS,
            ItemRegistryData(
                Identifier.fromString("minecraft:chainmail_leggings"),
                ItemChainLeggings::class.java,
            ),
        )
        register(
            ItemType.CHARCOAL,
            ItemRegistryData(Identifier.fromString("minecraft:charcoal"), ItemCharcoal::class.java),
        )
        register(ItemType.CHEMICAL_HEAT, ItemRegistryData(Identifier.fromString("minecraft:chemical_heat")))
        register(
            ItemType.CHEMISTRY_TABLE,
            ItemRegistryData(Identifier.fromString("minecraft:chemistry_table")),
        )
        register(
            ItemType.CHEST,
            ItemRegistryData(Identifier.fromString("minecraft:chest"), ItemChest::class.java),
        )
        register(ItemType.CHEST_BOAT, ItemRegistryData(Identifier.fromString("minecraft:chest_boat")))
        register(ItemType.CHEST_MINECART, ItemRegistryData(Identifier.fromString("minecraft:chest_minecart")))
        register(
            ItemType.CHICKEN,
            ItemRegistryData(Identifier.fromString("minecraft:chicken"), ItemChicken::class.java),
        )
        register(
            ItemType.CHICKEN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:chicken_spawn_egg")),
        )
        register(
            ItemType.CHISELED_DEEPSLATE,
            ItemRegistryData(Identifier.fromString("minecraft:chiseled_deepslate")),
        )
        register(
            ItemType.CHISELED_NETHER_BRICKS,
            ItemRegistryData(Identifier.fromString("minecraft:chiseled_nether_bricks")),
        )
        register(
            ItemType.CHISELED_POLISHED_BLACKSTONE,
            ItemRegistryData(Identifier.fromString("minecraft:chiseled_polished_blackstone")),
        )
        register(ItemType.CHORUS_FLOWER, ItemRegistryData(Identifier.fromString("minecraft:chorus_flower")))
        register(ItemType.CHORUS_FRUIT, ItemRegistryData(Identifier.fromString("minecraft:chorus_fruit")))
        register(ItemType.CHORUS_PLANT, ItemRegistryData(Identifier.fromString("minecraft:chorus_plant")))
        register(ItemType.CLAY, ItemRegistryData(Identifier.fromString("minecraft:clay")))
        register(ItemType.CLAY_BALL, ItemRegistryData(Identifier.fromString("minecraft:clay_ball")))
        register(
            ItemType.CLIENT_REQUEST_PLACEHOLDER_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:client_request_placeholder_block")),
        )
        register(ItemType.CLOCK, ItemRegistryData(Identifier.fromString("minecraft:clock")))
        register(
            ItemType.COAL,
            ItemRegistryData(Identifier.fromString("minecraft:coal"), ItemCoal::class.java),
        )
        register(
            ItemType.COAL_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:coal_block"), ItemCoalBlock::class.java),
        )
        register(ItemType.COAL_ORE, ItemRegistryData(Identifier.fromString("minecraft:coal_ore")))
        register(
            ItemType.COBBLED_DEEPSLATE,
            ItemRegistryData(Identifier.fromString("minecraft:cobbled_deepslate")),
        )
        register(
            ItemType.COBBLED_DEEPSLATE_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:cobbled_deepslate_double_slab")),
        )
        register(
            ItemType.COBBLED_DEEPSLATE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:cobbled_deepslate_slab")),
        )
        register(
            ItemType.COBBLED_DEEPSLATE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:cobbled_deepslate_stairs")),
        )
        register(
            ItemType.COBBLED_DEEPSLATE_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:cobbled_deepslate_wall")),
        )
        register(ItemType.COBBLESTONE, ItemRegistryData(Identifier.fromString("minecraft:cobblestone")))
        register(
            ItemType.COBBLESTONE_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:cobblestone_wall"), ItemWall::class.java),
        )
        register(ItemType.COCOA, ItemRegistryData(Identifier.fromString("minecraft:cocoa")))
        register(ItemType.COCOA_BEANS, ItemRegistryData(Identifier.fromString("minecraft:cocoa_beans")))
        register(ItemType.COD, ItemRegistryData(Identifier.fromString("minecraft:cod"), ItemCod::class.java))
        register(
            ItemType.COD_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:cod_bucket"), ItemBucket::class.java),
        )
        register(ItemType.COD_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:cod_spawn_egg")))
        register(
            ItemType.COLORED_TORCH_BP,
            ItemRegistryData(Identifier.fromString("minecraft:colored_torch_bp")),
        )
        register(
            ItemType.COLORED_TORCH_RG,
            ItemRegistryData(Identifier.fromString("minecraft:colored_torch_rg")),
        )
        register(ItemType.COMMAND_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:command_block")))
        register(
            ItemType.COMMAND_BLOCK_MINECART,
            ItemRegistryData(Identifier.fromString("minecraft:command_block_minecart")),
        )
        register(ItemType.COMPARATOR, ItemRegistryData(Identifier.fromString("minecraft:comparator")))
        register(ItemType.COMPASS, ItemRegistryData(Identifier.fromString("minecraft:compass")))
        register(ItemType.COMPOSTER, ItemRegistryData(Identifier.fromString("minecraft:composter")))
        register(ItemType.COMPOUND, ItemRegistryData(Identifier.fromString("minecraft:compound")))
        register(
            ItemType.CONCRETE,
            ItemRegistryData(Identifier.fromString("minecraft:concrete"), ItemConcrete::class.java),
        )
        register(
            ItemType.CONCRETE_POWDER,
            ItemRegistryData(
                Identifier.fromString("minecraft:concrete_powder"),
                ItemConcretePowder::class.java,
            ),
        )
        register(ItemType.CONDUIT, ItemRegistryData(Identifier.fromString("minecraft:conduit")))
        register(
            ItemType.COOKED_BEEF,
            ItemRegistryData(Identifier.fromString("minecraft:cooked_beef"), ItemCookedBeef::class.java),
        )
        register(
            ItemType.COOKED_CHICKEN,
            ItemRegistryData(Identifier.fromString("minecraft:cooked_chicken"), ItemCookedChicken::class.java),
        )
        register(
            ItemType.COOKED_COD,
            ItemRegistryData(Identifier.fromString("minecraft:cooked_cod"), ItemCookedCod::class.java),
        )
        register(
            ItemType.COOKED_MUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:cooked_mutton"), ItemCookedMutton::class.java),
        )
        register(
            ItemType.COOKED_PORKCHOP,
            ItemRegistryData(
                Identifier.fromString("minecraft:cooked_porkchop"),
                ItemCookedPorkchop::class.java,
            ),
        )
        register(
            ItemType.COOKED_RABBIT,
            ItemRegistryData(Identifier.fromString("minecraft:cooked_rabbit"), ItemCookedRabbit::class.java),
        )
        register(
            ItemType.COOKED_SALMON,
            ItemRegistryData(Identifier.fromString("minecraft:cooked_salmon"), ItemCookedSalmon::class.java),
        )
        register(
            ItemType.COOKIE,
            ItemRegistryData(Identifier.fromString("minecraft:cookie"), ItemCookie::class.java),
        )
        register(ItemType.COPPER_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:copper_block")))
        register(ItemType.COPPER_INGOT, ItemRegistryData(Identifier.fromString("minecraft:copper_ingot")))
        register(ItemType.COPPER_ORE, ItemRegistryData(Identifier.fromString("minecraft:copper_ore")))
        register(
            ItemType.CORAL,
            ItemRegistryData(Identifier.fromString("minecraft:coral"), ItemCoral::class.java),
        )
        register(ItemType.CORAL_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:coral_block")))
        register(
            ItemType.CORAL_FAN,
            ItemRegistryData(Identifier.fromString("minecraft:coral_fan"), ItemCoralFan::class.java),
        )
        register(
            ItemType.CORAL_FAN_DEAD,
            ItemRegistryData(Identifier.fromString("minecraft:coral_fan_dead"), ItemCoralFanDead::class.java),
        )
        register(ItemType.CORAL_FAN_HANG, ItemRegistryData(Identifier.fromString("minecraft:coral_fan_hang")))
        register(
            ItemType.CORAL_FAN_HANG2,
            ItemRegistryData(Identifier.fromString("minecraft:coral_fan_hang2")),
        )
        register(
            ItemType.CORAL_FAN_HANG3,
            ItemRegistryData(Identifier.fromString("minecraft:coral_fan_hang3")),
        )
        register(ItemType.COW_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:cow_spawn_egg")))
        register(
            ItemType.CRACKED_DEEPSLATE_BRICKS,
            ItemRegistryData(Identifier.fromString("minecraft:cracked_deepslate_bricks")),
        )
        register(
            ItemType.CRACKED_DEEPSLATE_TILES,
            ItemRegistryData(Identifier.fromString("minecraft:cracked_deepslate_tiles")),
        )
        register(
            ItemType.CRACKED_NETHER_BRICKS,
            ItemRegistryData(Identifier.fromString("minecraft:cracked_nether_bricks")),
        )
        register(
            ItemType.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            ItemRegistryData(Identifier.fromString("minecraft:cracked_polished_blackstone_bricks")),
        )
        register(
            ItemType.CRAFTING_TABLE,
            ItemRegistryData(Identifier.fromString("minecraft:crafting_table"), ItemCraftingTable::class.java),
        )
        register(
            ItemType.CREEPER_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:creeper_banner_pattern")),
        )
        register(
            ItemType.CREEPER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:creeper_spawn_egg")),
        )
        register(ItemType.CRIMSON_BUTTON, ItemRegistryData(Identifier.fromString("minecraft:crimson_button")))
        register(
            ItemType.CRIMSON_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_door"), ItemDoor::class.java),
        )
        register(
            ItemType.CRIMSON_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_double_slab")),
        )
        register(ItemType.CRIMSON_FENCE, ItemRegistryData(Identifier.fromString("minecraft:crimson_fence")))
        register(
            ItemType.CRIMSON_FENCE_GATE,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_fence_gate")),
        )
        register(ItemType.CRIMSON_FUNGUS, ItemRegistryData(Identifier.fromString("minecraft:crimson_fungus")))
        register(ItemType.CRIMSON_HYPHAE, ItemRegistryData(Identifier.fromString("minecraft:crimson_hyphae")))
        register(ItemType.CRIMSON_NYLIUM, ItemRegistryData(Identifier.fromString("minecraft:crimson_nylium")))
        register(ItemType.CRIMSON_PLANKS, ItemRegistryData(Identifier.fromString("minecraft:crimson_planks")))
        register(
            ItemType.CRIMSON_PRESSURE_PLATE,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_pressure_plate")),
        )
        register(ItemType.CRIMSON_ROOTS, ItemRegistryData(Identifier.fromString("minecraft:crimson_roots")))
        register(
            ItemType.CRIMSON_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_sign"), ItemCrimsonSign::class.java),
        )
        register(ItemType.CRIMSON_SLAB, ItemRegistryData(Identifier.fromString("minecraft:crimson_slab")))
        register(
            ItemType.CRIMSON_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.CRIMSON_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_standing_sign")),
        )
        register(ItemType.CRIMSON_STEM, ItemRegistryData(Identifier.fromString("minecraft:crimson_stem")))
        register(
            ItemType.CRIMSON_TRAPDOOR,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_trapdoor")),
        )
        register(
            ItemType.CRIMSON_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:crimson_wall_sign")),
        )
        register(ItemType.CROSSBOW, ItemRegistryData(Identifier.fromString("minecraft:crossbow")))
        register(
            ItemType.CRYING_OBSIDIAN,
            ItemRegistryData(Identifier.fromString("minecraft:crying_obsidian")),
        )
        register(ItemType.CUT_COPPER, ItemRegistryData(Identifier.fromString("minecraft:cut_copper")))
        register(
            ItemType.CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:cut_copper_slab")),
        )
        register(
            ItemType.CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:cut_copper_stairs")),
        )
        register(
            ItemType.CYAN_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:cyan_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.CYAN_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:cyan_candle_cake")),
        )
        register(ItemType.CYAN_DYE, ItemRegistryData(Identifier.fromString("minecraft:cyan_dye")))
        register(
            ItemType.CYAN_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:cyan_glazed_terracotta")),
        )
        register(ItemType.DARK_OAK_BOAT, ItemRegistryData(Identifier.fromString("minecraft:dark_oak_boat")))
        register(
            ItemType.DARK_OAK_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:dark_oak_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.DARK_OAK_CHEST_BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:dark_oak_chest_boat")),
        )
        register(
            ItemType.DARK_OAK_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:dark_oak_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.DARK_OAK_FENCE_GATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:dark_oak_fence_gate"),
                ItemWoodenFenceGate::class.java,
            ),
        )
        register(
            ItemType.DARK_OAK_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:dark_oak_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.DARK_OAK_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:dark_oak_sign"), ItemDarkOakSign::class.java),
        )
        register(
            ItemType.DARK_OAK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:dark_oak_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.DARK_OAK_TRAPDOOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:dark_oak_trapdoor"),
                ItemWoodenTrapdoor::class.java,
            ),
        )
        register(
            ItemType.DARK_PRISMARINE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:dark_prismarine_stairs")),
        )
        register(
            ItemType.DARKOAK_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:darkoak_standing_sign")),
        )
        register(
            ItemType.DARKOAK_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:darkoak_wall_sign")),
        )
        register(
            ItemType.DAYLIGHT_DETECTOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:daylight_detector"),
                ItemDaylightSensor::class.java,
            ),
        )
        register(
            ItemType.DAYLIGHT_DETECTOR_INVERTED,
            ItemRegistryData(
                Identifier.fromString("minecraft:daylight_detector_inverted"),
                ItemDaylightSensor::class.java,
            ),
        )
        register(ItemType.DEADBUSH, ItemRegistryData(Identifier.fromString("minecraft:deadbush")))
        register(ItemType.DEEPSLATE, ItemRegistryData(Identifier.fromString("minecraft:deepslate")))
        register(
            ItemType.DEEPSLATE_BRICK_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_brick_double_slab")),
        )
        register(
            ItemType.DEEPSLATE_BRICK_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_brick_slab")),
        )
        register(
            ItemType.DEEPSLATE_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_brick_stairs")),
        )
        register(
            ItemType.DEEPSLATE_BRICK_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_brick_wall")),
        )
        register(
            ItemType.DEEPSLATE_BRICKS,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_bricks")),
        )
        register(
            ItemType.DEEPSLATE_COAL_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_coal_ore")),
        )
        register(
            ItemType.DEEPSLATE_COPPER_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_copper_ore")),
        )
        register(
            ItemType.DEEPSLATE_DIAMOND_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_diamond_ore")),
        )
        register(
            ItemType.DEEPSLATE_EMERALD_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_emerald_ore")),
        )
        register(
            ItemType.DEEPSLATE_GOLD_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_gold_ore")),
        )
        register(
            ItemType.DEEPSLATE_IRON_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_iron_ore")),
        )
        register(
            ItemType.DEEPSLATE_LAPIS_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_lapis_ore")),
        )
        register(
            ItemType.DEEPSLATE_REDSTONE_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_redstone_ore")),
        )
        register(
            ItemType.DEEPSLATE_TILE_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_tile_double_slab")),
        )
        register(
            ItemType.DEEPSLATE_TILE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_tile_slab")),
        )
        register(
            ItemType.DEEPSLATE_TILE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_tile_stairs")),
        )
        register(
            ItemType.DEEPSLATE_TILE_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_tile_wall")),
        )
        register(
            ItemType.DEEPSLATE_TILES,
            ItemRegistryData(Identifier.fromString("minecraft:deepslate_tiles")),
        )
        register(ItemType.DENY, ItemRegistryData(Identifier.fromString("minecraft:deny")))
        register(ItemType.DETECTOR_RAIL, ItemRegistryData(Identifier.fromString("minecraft:detector_rail")))
        register(ItemType.DIAMOND, ItemRegistryData(Identifier.fromString("minecraft:diamond")))
        register(
            ItemType.DIAMOND_AXE,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_axe"), ItemDiamondAxe::class.java),
        )
        register(ItemType.DIAMOND_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:diamond_block")))
        register(
            ItemType.DIAMOND_BOOTS,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_boots"), ItemDiamondBoots::class.java),
        )
        register(
            ItemType.DIAMOND_CHESTPLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:diamond_chestplate"),
                ItemDiamondChestplate::class.java,
            ),
        )
        register(
            ItemType.DIAMOND_HELMET,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_helmet"), ItemDiamondHelmet::class.java),
        )
        register(
            ItemType.DIAMOND_HOE,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_hoe"), ItemDiamondHoe::class.java),
        )
        register(
            ItemType.DIAMOND_HORSE_ARMOR,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_horse_armor")),
        )
        register(
            ItemType.DIAMOND_LEGGINGS,
            ItemRegistryData(
                Identifier.fromString("minecraft:diamond_leggings"),
                ItemDiamondLeggings::class.java,
            ),
        )
        register(ItemType.DIAMOND_ORE, ItemRegistryData(Identifier.fromString("minecraft:diamond_ore")))
        register(
            ItemType.DIAMOND_PICKAXE,
            ItemRegistryData(
                Identifier.fromString("minecraft:diamond_pickaxe"),
                ItemDiamondPickaxe::class.java,
            ),
        )
        register(
            ItemType.DIAMOND_SHOVEL,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_shovel"), ItemDiamondShovel::class.java),
        )
        register(
            ItemType.DIAMOND_SWORD,
            ItemRegistryData(Identifier.fromString("minecraft:diamond_sword"), ItemDiamondSword::class.java),
        )
        register(ItemType.DIORITE_STAIRS, ItemRegistryData(Identifier.fromString("minecraft:diorite_stairs")))
        register(
            ItemType.DIRT,
            ItemRegistryData(Identifier.fromString("minecraft:dirt"), ItemDirt::class.java),
        )
        register(
            ItemType.DIRT_WITH_ROOTS,
            ItemRegistryData(Identifier.fromString("minecraft:dirt_with_roots")),
        )
        register(
            ItemType.DISC_FRAGMENT_5,
            ItemRegistryData(Identifier.fromString("minecraft:disc_fragment_5")),
        )
        register(ItemType.DISPENSER, ItemRegistryData(Identifier.fromString("minecraft:dispenser")))
        register(
            ItemType.DOLPHIN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:dolphin_spawn_egg")),
        )
        register(
            ItemType.DONKEY_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:donkey_spawn_egg")),
        )
        register(
            ItemType.DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:double_cut_copper_slab")),
        )
        register(
            ItemType.DOUBLE_PLANT,
            ItemRegistryData(Identifier.fromString("minecraft:double_plant"), ItemDoublePlant::class.java),
        )
        register(
            ItemType.DOUBLE_STONE_BLOCK_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:double_stone_block_slab")),
        )
        register(
            ItemType.DOUBLE_STONE_BLOCK_SLAB2,
            ItemRegistryData(Identifier.fromString("minecraft:double_stone_block_slab2")),
        )
        register(
            ItemType.DOUBLE_STONE_BLOCK_SLAB3,
            ItemRegistryData(Identifier.fromString("minecraft:double_stone_block_slab3")),
        )
        register(
            ItemType.DOUBLE_STONE_BLOCK_SLAB4,
            ItemRegistryData(Identifier.fromString("minecraft:double_stone_block_slab4")),
        )
        register(
            ItemType.DOUBLE_WOODEN_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:double_wooden_slab")),
        )
        register(ItemType.DRAGON_BREATH, ItemRegistryData(Identifier.fromString("minecraft:dragon_breath")))
        register(ItemType.DRAGON_EGG, ItemRegistryData(Identifier.fromString("minecraft:dragon_egg")))
        register(
            ItemType.DRIED_KELP,
            ItemRegistryData(Identifier.fromString("minecraft:dried_kelp"), ItemDriedKelp::class.java),
        )
        register(
            ItemType.DRIED_KELP_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:dried_kelp_block")),
        )
        register(
            ItemType.DRIPSTONE_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:dripstone_block")),
        )
        register(ItemType.DROPPER, ItemRegistryData(Identifier.fromString("minecraft:dropper")))
        register(
            ItemType.DROWNED_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:drowned_spawn_egg")),
        )
        register(ItemType.DYE, ItemRegistryData(Identifier.fromString("minecraft:dye")))
        register(ItemType.ECHO_SHARD, ItemRegistryData(Identifier.fromString("minecraft:echo_shard")))
        register(ItemType.EGG, ItemRegistryData(Identifier.fromString("minecraft:egg"), ItemEgg::class.java))
        register(
            ItemType.ELDER_GUARDIAN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:elder_guardian_spawn_egg")),
        )
        register(ItemType.ELEMENT_0, ItemRegistryData(Identifier.fromString("minecraft:element_0")))
        register(ItemType.ELEMENT_1, ItemRegistryData(Identifier.fromString("minecraft:element_1")))
        register(ItemType.ELEMENT_10, ItemRegistryData(Identifier.fromString("minecraft:element_10")))
        register(ItemType.ELEMENT_100, ItemRegistryData(Identifier.fromString("minecraft:element_100")))
        register(ItemType.ELEMENT_101, ItemRegistryData(Identifier.fromString("minecraft:element_101")))
        register(ItemType.ELEMENT_102, ItemRegistryData(Identifier.fromString("minecraft:element_102")))
        register(ItemType.ELEMENT_103, ItemRegistryData(Identifier.fromString("minecraft:element_103")))
        register(ItemType.ELEMENT_104, ItemRegistryData(Identifier.fromString("minecraft:element_104")))
        register(ItemType.ELEMENT_105, ItemRegistryData(Identifier.fromString("minecraft:element_105")))
        register(ItemType.ELEMENT_106, ItemRegistryData(Identifier.fromString("minecraft:element_106")))
        register(ItemType.ELEMENT_107, ItemRegistryData(Identifier.fromString("minecraft:element_107")))
        register(ItemType.ELEMENT_108, ItemRegistryData(Identifier.fromString("minecraft:element_108")))
        register(ItemType.ELEMENT_109, ItemRegistryData(Identifier.fromString("minecraft:element_109")))
        register(ItemType.ELEMENT_11, ItemRegistryData(Identifier.fromString("minecraft:element_11")))
        register(ItemType.ELEMENT_110, ItemRegistryData(Identifier.fromString("minecraft:element_110")))
        register(ItemType.ELEMENT_111, ItemRegistryData(Identifier.fromString("minecraft:element_111")))
        register(ItemType.ELEMENT_112, ItemRegistryData(Identifier.fromString("minecraft:element_112")))
        register(ItemType.ELEMENT_113, ItemRegistryData(Identifier.fromString("minecraft:element_113")))
        register(ItemType.ELEMENT_114, ItemRegistryData(Identifier.fromString("minecraft:element_114")))
        register(ItemType.ELEMENT_115, ItemRegistryData(Identifier.fromString("minecraft:element_115")))
        register(ItemType.ELEMENT_116, ItemRegistryData(Identifier.fromString("minecraft:element_116")))
        register(ItemType.ELEMENT_117, ItemRegistryData(Identifier.fromString("minecraft:element_117")))
        register(ItemType.ELEMENT_118, ItemRegistryData(Identifier.fromString("minecraft:element_118")))
        register(ItemType.ELEMENT_12, ItemRegistryData(Identifier.fromString("minecraft:element_12")))
        register(ItemType.ELEMENT_13, ItemRegistryData(Identifier.fromString("minecraft:element_13")))
        register(ItemType.ELEMENT_14, ItemRegistryData(Identifier.fromString("minecraft:element_14")))
        register(ItemType.ELEMENT_15, ItemRegistryData(Identifier.fromString("minecraft:element_15")))
        register(ItemType.ELEMENT_16, ItemRegistryData(Identifier.fromString("minecraft:element_16")))
        register(ItemType.ELEMENT_17, ItemRegistryData(Identifier.fromString("minecraft:element_17")))
        register(ItemType.ELEMENT_18, ItemRegistryData(Identifier.fromString("minecraft:element_18")))
        register(ItemType.ELEMENT_19, ItemRegistryData(Identifier.fromString("minecraft:element_19")))
        register(ItemType.ELEMENT_2, ItemRegistryData(Identifier.fromString("minecraft:element_2")))
        register(ItemType.ELEMENT_20, ItemRegistryData(Identifier.fromString("minecraft:element_20")))
        register(ItemType.ELEMENT_21, ItemRegistryData(Identifier.fromString("minecraft:element_21")))
        register(ItemType.ELEMENT_22, ItemRegistryData(Identifier.fromString("minecraft:element_22")))
        register(ItemType.ELEMENT_23, ItemRegistryData(Identifier.fromString("minecraft:element_23")))
        register(ItemType.ELEMENT_24, ItemRegistryData(Identifier.fromString("minecraft:element_24")))
        register(ItemType.ELEMENT_25, ItemRegistryData(Identifier.fromString("minecraft:element_25")))
        register(ItemType.ELEMENT_26, ItemRegistryData(Identifier.fromString("minecraft:element_26")))
        register(ItemType.ELEMENT_27, ItemRegistryData(Identifier.fromString("minecraft:element_27")))
        register(ItemType.ELEMENT_28, ItemRegistryData(Identifier.fromString("minecraft:element_28")))
        register(ItemType.ELEMENT_29, ItemRegistryData(Identifier.fromString("minecraft:element_29")))
        register(ItemType.ELEMENT_3, ItemRegistryData(Identifier.fromString("minecraft:element_3")))
        register(ItemType.ELEMENT_30, ItemRegistryData(Identifier.fromString("minecraft:element_30")))
        register(ItemType.ELEMENT_31, ItemRegistryData(Identifier.fromString("minecraft:element_31")))
        register(ItemType.ELEMENT_32, ItemRegistryData(Identifier.fromString("minecraft:element_32")))
        register(ItemType.ELEMENT_33, ItemRegistryData(Identifier.fromString("minecraft:element_33")))
        register(ItemType.ELEMENT_34, ItemRegistryData(Identifier.fromString("minecraft:element_34")))
        register(ItemType.ELEMENT_35, ItemRegistryData(Identifier.fromString("minecraft:element_35")))
        register(ItemType.ELEMENT_36, ItemRegistryData(Identifier.fromString("minecraft:element_36")))
        register(ItemType.ELEMENT_37, ItemRegistryData(Identifier.fromString("minecraft:element_37")))
        register(ItemType.ELEMENT_38, ItemRegistryData(Identifier.fromString("minecraft:element_38")))
        register(ItemType.ELEMENT_39, ItemRegistryData(Identifier.fromString("minecraft:element_39")))
        register(ItemType.ELEMENT_4, ItemRegistryData(Identifier.fromString("minecraft:element_4")))
        register(ItemType.ELEMENT_40, ItemRegistryData(Identifier.fromString("minecraft:element_40")))
        register(ItemType.ELEMENT_41, ItemRegistryData(Identifier.fromString("minecraft:element_41")))
        register(ItemType.ELEMENT_42, ItemRegistryData(Identifier.fromString("minecraft:element_42")))
        register(ItemType.ELEMENT_43, ItemRegistryData(Identifier.fromString("minecraft:element_43")))
        register(ItemType.ELEMENT_44, ItemRegistryData(Identifier.fromString("minecraft:element_44")))
        register(ItemType.ELEMENT_45, ItemRegistryData(Identifier.fromString("minecraft:element_45")))
        register(ItemType.ELEMENT_46, ItemRegistryData(Identifier.fromString("minecraft:element_46")))
        register(ItemType.ELEMENT_47, ItemRegistryData(Identifier.fromString("minecraft:element_47")))
        register(ItemType.ELEMENT_48, ItemRegistryData(Identifier.fromString("minecraft:element_48")))
        register(ItemType.ELEMENT_49, ItemRegistryData(Identifier.fromString("minecraft:element_49")))
        register(ItemType.ELEMENT_5, ItemRegistryData(Identifier.fromString("minecraft:element_5")))
        register(ItemType.ELEMENT_50, ItemRegistryData(Identifier.fromString("minecraft:element_50")))
        register(ItemType.ELEMENT_51, ItemRegistryData(Identifier.fromString("minecraft:element_51")))
        register(ItemType.ELEMENT_52, ItemRegistryData(Identifier.fromString("minecraft:element_52")))
        register(ItemType.ELEMENT_53, ItemRegistryData(Identifier.fromString("minecraft:element_53")))
        register(ItemType.ELEMENT_54, ItemRegistryData(Identifier.fromString("minecraft:element_54")))
        register(ItemType.ELEMENT_55, ItemRegistryData(Identifier.fromString("minecraft:element_55")))
        register(ItemType.ELEMENT_56, ItemRegistryData(Identifier.fromString("minecraft:element_56")))
        register(ItemType.ELEMENT_57, ItemRegistryData(Identifier.fromString("minecraft:element_57")))
        register(ItemType.ELEMENT_58, ItemRegistryData(Identifier.fromString("minecraft:element_58")))
        register(ItemType.ELEMENT_59, ItemRegistryData(Identifier.fromString("minecraft:element_59")))
        register(ItemType.ELEMENT_6, ItemRegistryData(Identifier.fromString("minecraft:element_6")))
        register(ItemType.ELEMENT_60, ItemRegistryData(Identifier.fromString("minecraft:element_60")))
        register(ItemType.ELEMENT_61, ItemRegistryData(Identifier.fromString("minecraft:element_61")))
        register(ItemType.ELEMENT_62, ItemRegistryData(Identifier.fromString("minecraft:element_62")))
        register(ItemType.ELEMENT_63, ItemRegistryData(Identifier.fromString("minecraft:element_63")))
        register(ItemType.ELEMENT_64, ItemRegistryData(Identifier.fromString("minecraft:element_64")))
        register(ItemType.ELEMENT_65, ItemRegistryData(Identifier.fromString("minecraft:element_65")))
        register(ItemType.ELEMENT_66, ItemRegistryData(Identifier.fromString("minecraft:element_66")))
        register(ItemType.ELEMENT_67, ItemRegistryData(Identifier.fromString("minecraft:element_67")))
        register(ItemType.ELEMENT_68, ItemRegistryData(Identifier.fromString("minecraft:element_68")))
        register(ItemType.ELEMENT_69, ItemRegistryData(Identifier.fromString("minecraft:element_69")))
        register(ItemType.ELEMENT_7, ItemRegistryData(Identifier.fromString("minecraft:element_7")))
        register(ItemType.ELEMENT_70, ItemRegistryData(Identifier.fromString("minecraft:element_70")))
        register(ItemType.ELEMENT_71, ItemRegistryData(Identifier.fromString("minecraft:element_71")))
        register(ItemType.ELEMENT_72, ItemRegistryData(Identifier.fromString("minecraft:element_72")))
        register(ItemType.ELEMENT_73, ItemRegistryData(Identifier.fromString("minecraft:element_73")))
        register(ItemType.ELEMENT_74, ItemRegistryData(Identifier.fromString("minecraft:element_74")))
        register(ItemType.ELEMENT_75, ItemRegistryData(Identifier.fromString("minecraft:element_75")))
        register(ItemType.ELEMENT_76, ItemRegistryData(Identifier.fromString("minecraft:element_76")))
        register(ItemType.ELEMENT_77, ItemRegistryData(Identifier.fromString("minecraft:element_77")))
        register(ItemType.ELEMENT_78, ItemRegistryData(Identifier.fromString("minecraft:element_78")))
        register(ItemType.ELEMENT_79, ItemRegistryData(Identifier.fromString("minecraft:element_79")))
        register(ItemType.ELEMENT_8, ItemRegistryData(Identifier.fromString("minecraft:element_8")))
        register(ItemType.ELEMENT_80, ItemRegistryData(Identifier.fromString("minecraft:element_80")))
        register(ItemType.ELEMENT_81, ItemRegistryData(Identifier.fromString("minecraft:element_81")))
        register(ItemType.ELEMENT_82, ItemRegistryData(Identifier.fromString("minecraft:element_82")))
        register(ItemType.ELEMENT_83, ItemRegistryData(Identifier.fromString("minecraft:element_83")))
        register(ItemType.ELEMENT_84, ItemRegistryData(Identifier.fromString("minecraft:element_84")))
        register(ItemType.ELEMENT_85, ItemRegistryData(Identifier.fromString("minecraft:element_85")))
        register(ItemType.ELEMENT_86, ItemRegistryData(Identifier.fromString("minecraft:element_86")))
        register(ItemType.ELEMENT_87, ItemRegistryData(Identifier.fromString("minecraft:element_87")))
        register(ItemType.ELEMENT_88, ItemRegistryData(Identifier.fromString("minecraft:element_88")))
        register(ItemType.ELEMENT_89, ItemRegistryData(Identifier.fromString("minecraft:element_89")))
        register(ItemType.ELEMENT_9, ItemRegistryData(Identifier.fromString("minecraft:element_9")))
        register(ItemType.ELEMENT_90, ItemRegistryData(Identifier.fromString("minecraft:element_90")))
        register(ItemType.ELEMENT_91, ItemRegistryData(Identifier.fromString("minecraft:element_91")))
        register(ItemType.ELEMENT_92, ItemRegistryData(Identifier.fromString("minecraft:element_92")))
        register(ItemType.ELEMENT_93, ItemRegistryData(Identifier.fromString("minecraft:element_93")))
        register(ItemType.ELEMENT_94, ItemRegistryData(Identifier.fromString("minecraft:element_94")))
        register(ItemType.ELEMENT_95, ItemRegistryData(Identifier.fromString("minecraft:element_95")))
        register(ItemType.ELEMENT_96, ItemRegistryData(Identifier.fromString("minecraft:element_96")))
        register(ItemType.ELEMENT_97, ItemRegistryData(Identifier.fromString("minecraft:element_97")))
        register(ItemType.ELEMENT_98, ItemRegistryData(Identifier.fromString("minecraft:element_98")))
        register(ItemType.ELEMENT_99, ItemRegistryData(Identifier.fromString("minecraft:element_99")))
        register(ItemType.ELYTRA, ItemRegistryData(Identifier.fromString("minecraft:elytra")))
        register(ItemType.EMERALD, ItemRegistryData(Identifier.fromString("minecraft:emerald")))
        register(ItemType.EMERALD_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:emerald_block")))
        register(ItemType.EMERALD_ORE, ItemRegistryData(Identifier.fromString("minecraft:emerald_ore")))
        register(ItemType.EMPTY_MAP, ItemRegistryData(Identifier.fromString("minecraft:empty_map")))
        register(ItemType.ENCHANTED_BOOK, ItemRegistryData(Identifier.fromString("minecraft:enchanted_book")))
        register(
            ItemType.ENCHANTED_GOLDEN_APPLE,
            ItemRegistryData(
                Identifier.fromString("minecraft:enchanted_golden_apple"),
                ItemEnchantedGoldenApple::class.java,
            ),
        )
        register(
            ItemType.ENCHANTING_TABLE,
            ItemRegistryData(Identifier.fromString("minecraft:enchanting_table")),
        )
        register(
            ItemType.END_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:end_brick_stairs")),
        )
        register(ItemType.END_BRICKS, ItemRegistryData(Identifier.fromString("minecraft:end_bricks")))
        register(ItemType.END_CRYSTAL, ItemRegistryData(Identifier.fromString("minecraft:end_crystal")))
        register(ItemType.END_GATEWAY, ItemRegistryData(Identifier.fromString("minecraft:end_gateway")))
        register(ItemType.END_PORTAL, ItemRegistryData(Identifier.fromString("minecraft:end_portal")))
        register(
            ItemType.END_PORTAL_FRAME,
            ItemRegistryData(Identifier.fromString("minecraft:end_portal_frame")),
        )
        register(ItemType.END_ROD, ItemRegistryData(Identifier.fromString("minecraft:end_rod")))
        register(ItemType.END_STONE, ItemRegistryData(Identifier.fromString("minecraft:end_stone")))
        register(ItemType.ENDER_CHEST, ItemRegistryData(Identifier.fromString("minecraft:ender_chest")))
        register(ItemType.ENDER_EYE, ItemRegistryData(Identifier.fromString("minecraft:ender_eye")))
        register(ItemType.ENDER_PEARL, ItemRegistryData(Identifier.fromString("minecraft:ender_pearl")))
        register(
            ItemType.ENDERMAN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:enderman_spawn_egg")),
        )
        register(
            ItemType.ENDERMITE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:endermite_spawn_egg")),
        )
        register(
            ItemType.EVOKER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:evoker_spawn_egg")),
        )
        register(
            ItemType.EXPERIENCE_BOTTLE,
            ItemRegistryData(Identifier.fromString("minecraft:experience_bottle")),
        )
        register(ItemType.EXPOSED_COPPER, ItemRegistryData(Identifier.fromString("minecraft:exposed_copper")))
        register(
            ItemType.EXPOSED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:exposed_cut_copper")),
        )
        register(
            ItemType.EXPOSED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:exposed_cut_copper_slab")),
        )
        register(
            ItemType.EXPOSED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:exposed_cut_copper_stairs")),
        )
        register(
            ItemType.EXPOSED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:exposed_double_cut_copper_slab")),
        )
        register(ItemType.FARMLAND, ItemRegistryData(Identifier.fromString("minecraft:farmland")))
        register(ItemType.FEATHER, ItemRegistryData(Identifier.fromString("minecraft:feather")))
        register(
            ItemType.FENCE,
            ItemRegistryData(Identifier.fromString("minecraft:fence"), ItemFence::class.java),
        )
        register(
            ItemType.OAK_FENCE_GATE,
            ItemRegistryData(Identifier.fromString("minecraft:fence_gate"), ItemWoodenFenceGate::class.java),
        )
        register(
            ItemType.FERMENTED_SPIDER_EYE,
            ItemRegistryData(Identifier.fromString("minecraft:fermented_spider_eye")),
        )
        register(
            ItemType.FIELD_MASONED_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:field_masoned_banner_pattern")),
        )
        register(ItemType.FILLED_MAP, ItemRegistryData(Identifier.fromString("minecraft:filled_map")))
        register(ItemType.FIRE, ItemRegistryData(Identifier.fromString("minecraft:fire")))
        register(ItemType.FIRE_CHARGE, ItemRegistryData(Identifier.fromString("minecraft:fire_charge")))
        register(
            ItemType.FIREWORK_ROCKET,
            ItemRegistryData(Identifier.fromString("minecraft:firework_rocket")),
        )
        register(ItemType.FIREWORK_STAR, ItemRegistryData(Identifier.fromString("minecraft:firework_star")))
        register(
            ItemType.FISHING_ROD,
            ItemRegistryData(Identifier.fromString("minecraft:fishing_rod"), ItemFishingRod::class.java),
        )
        register(
            ItemType.FLETCHING_TABLE,
            ItemRegistryData(Identifier.fromString("minecraft:fletching_table")),
        )
        register(ItemType.FLINT, ItemRegistryData(Identifier.fromString("minecraft:flint")))
        register(
            ItemType.FLINT_AND_STEEL,
            ItemRegistryData(Identifier.fromString("minecraft:flint_and_steel")),
        )
        register(
            ItemType.FLOWER_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:flower_banner_pattern")),
        )
        register(ItemType.FLOWER_POT, ItemRegistryData(Identifier.fromString("minecraft:flower_pot")))
        register(
            ItemType.FLOWERING_AZALEA,
            ItemRegistryData(Identifier.fromString("minecraft:flowering_azalea")),
        )
        register(ItemType.FLOWING_LAVA, ItemRegistryData(Identifier.fromString("minecraft:flowing_lava")))
        register(ItemType.FLOWING_WATER, ItemRegistryData(Identifier.fromString("minecraft:flowing_water")))
        register(ItemType.FOX_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:fox_spawn_egg")))
        register(ItemType.FRAME, ItemRegistryData(Identifier.fromString("minecraft:frame")))
        register(ItemType.FROG_SPAWN, ItemRegistryData(Identifier.fromString("minecraft:frog_spawn")))
        register(ItemType.FROG_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:frog_spawn_egg")))
        register(ItemType.FROSTED_ICE, ItemRegistryData(Identifier.fromString("minecraft:frosted_ice")))
        register(ItemType.FURNACE, ItemRegistryData(Identifier.fromString("minecraft:furnace")))
        register(
            ItemType.GHAST_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:ghast_spawn_egg")),
        )
        register(ItemType.GHAST_TEAR, ItemRegistryData(Identifier.fromString("minecraft:ghast_tear")))
        register(
            ItemType.GILDED_BLACKSTONE,
            ItemRegistryData(Identifier.fromString("minecraft:gilded_blackstone")),
        )
        register(ItemType.GLASS, ItemRegistryData(Identifier.fromString("minecraft:glass")))
        register(ItemType.GLASS_BOTTLE, ItemRegistryData(Identifier.fromString("minecraft:glass_bottle")))
        register(ItemType.GLASS_PANE, ItemRegistryData(Identifier.fromString("minecraft:glass_pane")))
        register(
            ItemType.GLISTERING_MELON_SLICE,
            ItemRegistryData(Identifier.fromString("minecraft:glistering_melon_slice")),
        )
        register(
            ItemType.GLOBE_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:globe_banner_pattern")),
        )
        register(
            ItemType.GLOW_BERRIES,
            ItemRegistryData(Identifier.fromString("minecraft:glow_berries"), ItemGlowBerries::class.java),
        )
        register(ItemType.GLOW_FRAME, ItemRegistryData(Identifier.fromString("minecraft:glow_frame")))
        register(ItemType.GLOW_INK_SAC, ItemRegistryData(Identifier.fromString("minecraft:glow_ink_sac")))
        register(ItemType.GLOW_LICHEN, ItemRegistryData(Identifier.fromString("minecraft:glow_lichen")))
        register(
            ItemType.GLOW_SQUID_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:glow_squid_spawn_egg")),
        )
        register(ItemType.GLOW_STICK, ItemRegistryData(Identifier.fromString("minecraft:glow_stick")))
        register(
            ItemType.GLOWINGOBSIDIAN,
            ItemRegistryData(Identifier.fromString("minecraft:glowingobsidian")),
        )
        register(ItemType.GLOWSTONE, ItemRegistryData(Identifier.fromString("minecraft:glowstone")))
        register(ItemType.GLOWSTONE_DUST, ItemRegistryData(Identifier.fromString("minecraft:glowstone_dust")))
        register(ItemType.GOAT_HORN, ItemRegistryData(Identifier.fromString("minecraft:goat_horn")))
        register(ItemType.GOAT_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:goat_spawn_egg")))
        register(ItemType.GOLD_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:gold_block")))
        register(ItemType.GOLD_INGOT, ItemRegistryData(Identifier.fromString("minecraft:gold_ingot")))
        register(ItemType.GOLD_NUGGET, ItemRegistryData(Identifier.fromString("minecraft:gold_nugget")))
        register(ItemType.GOLD_ORE, ItemRegistryData(Identifier.fromString("minecraft:gold_ore")))
        register(
            ItemType.GOLDEN_APPLE,
            ItemRegistryData(Identifier.fromString("minecraft:golden_apple"), ItemGoldenApple::class.java),
        )
        register(
            ItemType.GOLDEN_AXE,
            ItemRegistryData(Identifier.fromString("minecraft:golden_axe"), ItemGoldenAxe::class.java),
        )
        register(
            ItemType.GOLDEN_BOOTS,
            ItemRegistryData(Identifier.fromString("minecraft:golden_boots"), ItemGoldenBoots::class.java),
        )
        register(
            ItemType.GOLDEN_CARROT,
            ItemRegistryData(Identifier.fromString("minecraft:golden_carrot"), ItemGoldenCarrot::class.java),
        )
        register(
            ItemType.GOLDEN_CHESTPLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:golden_chestplate"),
                ItemGoldenChestplate::class.java,
            ),
        )
        register(
            ItemType.GOLDEN_HELMET,
            ItemRegistryData(Identifier.fromString("minecraft:golden_helmet"), ItemGoldenHelmet::class.java),
        )
        register(
            ItemType.GOLDEN_HOE,
            ItemRegistryData(Identifier.fromString("minecraft:golden_hoe"), ItemGoldenHoe::class.java),
        )
        register(
            ItemType.GOLDEN_HORSE_ARMOR,
            ItemRegistryData(Identifier.fromString("minecraft:golden_horse_armor")),
        )
        register(
            ItemType.GOLDEN_LEGGINGS,
            ItemRegistryData(
                Identifier.fromString("minecraft:golden_leggings"),
                ItemGoldenLeggings::class.java,
            ),
        )
        register(
            ItemType.GOLDEN_PICKAXE,
            ItemRegistryData(Identifier.fromString("minecraft:golden_pickaxe"), ItemGoldenPickaxe::class.java),
        )
        register(ItemType.GOLDEN_RAIL, ItemRegistryData(Identifier.fromString("minecraft:golden_rail")))
        register(
            ItemType.GOLDEN_SHOVEL,
            ItemRegistryData(Identifier.fromString("minecraft:golden_shovel"), ItemGoldenShovel::class.java),
        )
        register(
            ItemType.GOLDEN_SWORD,
            ItemRegistryData(Identifier.fromString("minecraft:golden_sword"), ItemGoldenSword::class.java),
        )
        register(ItemType.GRANITE_STAIRS, ItemRegistryData(Identifier.fromString("minecraft:granite_stairs")))
        register(ItemType.GRASS, ItemRegistryData(Identifier.fromString("minecraft:grass")))
        register(ItemType.GRASS_PATH, ItemRegistryData(Identifier.fromString("minecraft:grass_path")))
        register(ItemType.GRAVEL, ItemRegistryData(Identifier.fromString("minecraft:gravel")))
        register(
            ItemType.GRAY_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:gray_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.GRAY_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:gray_candle_cake")),
        )
        register(ItemType.GRAY_DYE, ItemRegistryData(Identifier.fromString("minecraft:gray_dye")))
        register(
            ItemType.GRAY_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:gray_glazed_terracotta")),
        )
        register(
            ItemType.GREEN_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:green_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.GREEN_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:green_candle_cake")),
        )
        register(ItemType.GREEN_DYE, ItemRegistryData(Identifier.fromString("minecraft:green_dye")))
        register(
            ItemType.GREEN_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:green_glazed_terracotta")),
        )
        register(ItemType.GRINDSTONE, ItemRegistryData(Identifier.fromString("minecraft:grindstone")))
        register(
            ItemType.GUARDIAN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:guardian_spawn_egg")),
        )
        register(ItemType.GUNPOWDER, ItemRegistryData(Identifier.fromString("minecraft:gunpowder")))
        register(ItemType.HANGING_ROOTS, ItemRegistryData(Identifier.fromString("minecraft:hanging_roots")))
        register(ItemType.HARD_GLASS, ItemRegistryData(Identifier.fromString("minecraft:hard_glass")))
        register(
            ItemType.HARD_GLASS_PANE,
            ItemRegistryData(Identifier.fromString("minecraft:hard_glass_pane")),
        )
        register(
            ItemType.HARD_STAINED_GLASS,
            ItemRegistryData(Identifier.fromString("minecraft:hard_stained_glass")),
        )
        register(
            ItemType.HARD_STAINED_GLASS_PANE,
            ItemRegistryData(Identifier.fromString("minecraft:hard_stained_glass_pane")),
        )
        register(ItemType.HARDENED_CLAY, ItemRegistryData(Identifier.fromString("minecraft:hardened_clay")))
        register(ItemType.HAY_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:hay_block")))
        register(
            ItemType.HEART_OF_THE_SEA,
            ItemRegistryData(Identifier.fromString("minecraft:heart_of_the_sea")),
        )
        register(
            ItemType.HEAVY_WEIGHTED_PRESSURE_PLATE,
            ItemRegistryData(Identifier.fromString("minecraft:heavy_weighted_pressure_plate")),
        )
        register(
            ItemType.HOGLIN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:hoglin_spawn_egg")),
        )
        register(ItemType.HONEY_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:honey_block")))
        register(ItemType.HONEY_BOTTLE, ItemRegistryData(Identifier.fromString("minecraft:honey_bottle")))
        register(ItemType.HONEYCOMB, ItemRegistryData(Identifier.fromString("minecraft:honeycomb")))
        register(
            ItemType.HONEYCOMB_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:honeycomb_block")),
        )
        register(
            ItemType.HOPPER,
            ItemRegistryData(Identifier.fromString("minecraft:hopper"), ItemHopper::class.java),
        )
        register(
            ItemType.HOPPER_MINECART,
            ItemRegistryData(Identifier.fromString("minecraft:hopper_minecart")),
        )
        register(
            ItemType.HORSE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:horse_spawn_egg")),
        )
        register(ItemType.HUSK_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:husk_spawn_egg")))
        register(ItemType.ICE, ItemRegistryData(Identifier.fromString("minecraft:ice")))
        register(ItemType.ICE_BOMB, ItemRegistryData(Identifier.fromString("minecraft:ice_bomb")))
        register(
            ItemType.INFESTED_DEEPSLATE,
            ItemRegistryData(Identifier.fromString("minecraft:infested_deepslate")),
        )
        register(ItemType.INFO_UPDATE, ItemRegistryData(Identifier.fromString("minecraft:info_update")))
        register(ItemType.INFO_UPDATE2, ItemRegistryData(Identifier.fromString("minecraft:info_update2")))
        register(ItemType.INK_SAC, ItemRegistryData(Identifier.fromString("minecraft:ink_sac")))
        register(
            ItemType.INVISIBLE_BEDROCK,
            ItemRegistryData(Identifier.fromString("minecraft:invisible_bedrock")),
        )
        register(
            ItemType.IRON_AXE,
            ItemRegistryData(Identifier.fromString("minecraft:iron_axe"), ItemIronAxe::class.java),
        )
        register(ItemType.IRON_BARS, ItemRegistryData(Identifier.fromString("minecraft:iron_bars")))
        register(ItemType.IRON_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:iron_block")))
        register(
            ItemType.IRON_BOOTS,
            ItemRegistryData(Identifier.fromString("minecraft:iron_boots"), ItemIronBoots::class.java),
        )
        register(
            ItemType.IRON_CHESTPLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:iron_chestplate"),
                ItemIronChestplate::class.java,
            ),
        )
        register(
            ItemType.IRON_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:iron_door"), ItemDoor::class.java),
        )
        register(
            ItemType.IRON_HELMET,
            ItemRegistryData(Identifier.fromString("minecraft:iron_helmet"), ItemIronHelmet::class.java),
        )
        register(
            ItemType.IRON_HOE,
            ItemRegistryData(Identifier.fromString("minecraft:iron_hoe"), ItemIronHoe::class.java),
        )
        register(
            ItemType.IRON_HORSE_ARMOR,
            ItemRegistryData(Identifier.fromString("minecraft:iron_horse_armor")),
        )
        register(ItemType.IRON_INGOT, ItemRegistryData(Identifier.fromString("minecraft:iron_ingot")))
        register(
            ItemType.IRON_LEGGINGS,
            ItemRegistryData(Identifier.fromString("minecraft:iron_leggings"), ItemIronLeggings::class.java),
        )
        register(ItemType.IRON_NUGGET, ItemRegistryData(Identifier.fromString("minecraft:iron_nugget")))
        register(ItemType.IRON_ORE, ItemRegistryData(Identifier.fromString("minecraft:iron_ore")))
        register(
            ItemType.IRON_PICKAXE,
            ItemRegistryData(Identifier.fromString("minecraft:iron_pickaxe"), ItemIronPickaxe::class.java),
        )
        register(
            ItemType.IRON_SHOVEL,
            ItemRegistryData(Identifier.fromString("minecraft:iron_shovel"), ItemIronShovel::class.java),
        )
        register(
            ItemType.IRON_SWORD,
            ItemRegistryData(Identifier.fromString("minecraft:iron_sword"), ItemIronSword::class.java),
        )
        register(ItemType.IRON_TRAPDOOR, ItemRegistryData(Identifier.fromString("minecraft:iron_trapdoor")))
        register(ItemType.JIGSAW, ItemRegistryData(Identifier.fromString("minecraft:jigsaw")))
        register(
            ItemType.JUKEBOX,
            ItemRegistryData(Identifier.fromString("minecraft:jukebox"), ItemJukebox::class.java),
        )
        register(ItemType.JUNGLE_BOAT, ItemRegistryData(Identifier.fromString("minecraft:jungle_boat")))
        register(
            ItemType.JUNGLE_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.JUNGLE_CHEST_BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_chest_boat")),
        )
        register(
            ItemType.JUNGLE_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.JUNGLE_FENCE_GATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:jungle_fence_gate"),
                ItemWoodenFenceGate::class.java,
            ),
        )
        register(
            ItemType.JUNGLE_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:jungle_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.JUNGLE_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_sign"), ItemJungleSign::class.java),
        )
        register(
            ItemType.JUNGLE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.JUNGLE_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_standing_sign")),
        )
        register(
            ItemType.JUNGLE_TRAPDOOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:jungle_trapdoor"),
                ItemWoodenTrapdoor::class.java,
            ),
        )
        register(
            ItemType.JUNGLE_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:jungle_wall_sign")),
        )
        register(ItemType.KELP, ItemRegistryData(Identifier.fromString("minecraft:kelp")))
        register(
            ItemType.LADDER,
            ItemRegistryData(Identifier.fromString("minecraft:ladder"), ItemLadder::class.java),
        )
        register(ItemType.LANTERN, ItemRegistryData(Identifier.fromString("minecraft:lantern")))
        register(ItemType.LAPIS_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:lapis_block")))
        register(ItemType.LAPIS_LAZULI, ItemRegistryData(Identifier.fromString("minecraft:lapis_lazuli")))
        register(ItemType.LAPIS_ORE, ItemRegistryData(Identifier.fromString("minecraft:lapis_ore")))
        register(
            ItemType.LARGE_AMETHYST_BUD,
            ItemRegistryData(Identifier.fromString("minecraft:large_amethyst_bud")),
        )
        register(ItemType.LAVA, ItemRegistryData(Identifier.fromString("minecraft:lava")))
        register(
            ItemType.LAVA_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:lava_bucket"), ItemLavaBucket::class.java),
        )
        register(ItemType.LAVA_CAULDRON, ItemRegistryData(Identifier.fromString("minecraft:lava_cauldron")))
        register(ItemType.LEAD, ItemRegistryData(Identifier.fromString("minecraft:lead")))
        register(ItemType.LEATHER, ItemRegistryData(Identifier.fromString("minecraft:leather")))
        register(
            ItemType.LEATHER_BOOTS,
            ItemRegistryData(Identifier.fromString("minecraft:leather_boots"), ItemLeatherBoots::class.java),
        )
        register(
            ItemType.LEATHER_CHESTPLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:leather_chestplate"),
                ItemLeatherChestplate::class.java,
            ),
        )
        register(
            ItemType.LEATHER_HELMET,
            ItemRegistryData(Identifier.fromString("minecraft:leather_helmet"), ItemLeatherHelmet::class.java),
        )
        register(
            ItemType.LEATHER_HORSE_ARMOR,
            ItemRegistryData(Identifier.fromString("minecraft:leather_horse_armor")),
        )
        register(
            ItemType.LEATHER_LEGGINGS,
            ItemRegistryData(
                Identifier.fromString("minecraft:leather_leggings"),
                ItemLeatherLeggings::class.java,
            ),
        )
        register(
            ItemType.LEAVES,
            ItemRegistryData(Identifier.fromString("minecraft:leaves"), ItemLeaves::class.java),
        )
        register(
            ItemType.LEAVES2,
            ItemRegistryData(Identifier.fromString("minecraft:leaves2"), ItemLeaves2::class.java),
        )
        register(ItemType.LECTERN, ItemRegistryData(Identifier.fromString("minecraft:lectern")))
        register(ItemType.LEVER, ItemRegistryData(Identifier.fromString("minecraft:lever")))
        register(ItemType.LIGHT_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:light_block")))
        register(
            ItemType.LIGHT_BLUE_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:light_blue_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.LIGHT_BLUE_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:light_blue_candle_cake")),
        )
        register(ItemType.LIGHT_BLUE_DYE, ItemRegistryData(Identifier.fromString("minecraft:light_blue_dye")))
        register(
            ItemType.LIGHT_BLUE_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:light_blue_glazed_terracotta")),
        )
        register(
            ItemType.LIGHT_GRAY_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:light_gray_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.LIGHT_GRAY_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:light_gray_candle_cake")),
        )
        register(ItemType.LIGHT_GRAY_DYE, ItemRegistryData(Identifier.fromString("minecraft:light_gray_dye")))
        register(
            ItemType.LIGHT_WEIGHTED_PRESSURE_PLATE,
            ItemRegistryData(Identifier.fromString("minecraft:light_weighted_pressure_plate")),
        )
        register(ItemType.LIGHTNING_ROD, ItemRegistryData(Identifier.fromString("minecraft:lightning_rod")))
        register(
            ItemType.LIME_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:lime_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.LIME_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:lime_candle_cake")),
        )
        register(ItemType.LIME_DYE, ItemRegistryData(Identifier.fromString("minecraft:lime_dye")))
        register(
            ItemType.LIME_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:lime_glazed_terracotta")),
        )
        register(
            ItemType.LINGERING_POTION,
            ItemRegistryData(Identifier.fromString("minecraft:lingering_potion")),
        )
        register(
            ItemType.LIT_BLAST_FURNACE,
            ItemRegistryData(Identifier.fromString("minecraft:lit_blast_furnace")),
        )
        register(
            ItemType.LIT_DEEPSLATE_REDSTONE_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:lit_deepslate_redstone_ore")),
        )
        register(ItemType.LIT_FURNACE, ItemRegistryData(Identifier.fromString("minecraft:lit_furnace")))
        register(ItemType.LIT_PUMPKIN, ItemRegistryData(Identifier.fromString("minecraft:lit_pumpkin")))
        register(
            ItemType.LIT_REDSTONE_LAMP,
            ItemRegistryData(Identifier.fromString("minecraft:lit_redstone_lamp")),
        )
        register(
            ItemType.LIT_REDSTONE_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:lit_redstone_ore")),
        )
        register(ItemType.LIT_SMOKER, ItemRegistryData(Identifier.fromString("minecraft:lit_smoker")))
        register(
            ItemType.LLAMA_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:llama_spawn_egg")),
        )
        register(ItemType.LODESTONE, ItemRegistryData(Identifier.fromString("minecraft:lodestone")))
        register(
            ItemType.LODESTONE_COMPASS,
            ItemRegistryData(Identifier.fromString("minecraft:lodestone_compass")),
        )
        register(ItemType.LOG, ItemRegistryData(Identifier.fromString("minecraft:log"), ItemLog::class.java))
        register(
            ItemType.LOG2,
            ItemRegistryData(Identifier.fromString("minecraft:log2"), ItemLog2::class.java),
        )
        register(ItemType.LOOM, ItemRegistryData(Identifier.fromString("minecraft:loom")))
        register(
            ItemType.MAGENTA_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:magenta_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.MAGENTA_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:magenta_candle_cake")),
        )
        register(ItemType.MAGENTA_DYE, ItemRegistryData(Identifier.fromString("minecraft:magenta_dye")))
        register(
            ItemType.MAGENTA_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:magenta_glazed_terracotta")),
        )
        register(ItemType.MAGMA, ItemRegistryData(Identifier.fromString("minecraft:magma")))
        register(ItemType.MAGMA_CREAM, ItemRegistryData(Identifier.fromString("minecraft:magma_cream")))
        register(
            ItemType.MAGMA_CUBE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:magma_cube_spawn_egg")),
        )
        register(ItemType.MANGROVE_BOAT, ItemRegistryData(Identifier.fromString("minecraft:mangrove_boat")))
        register(
            ItemType.MANGROVE_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.MANGROVE_CHEST_BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_chest_boat")),
        )
        register(
            ItemType.MANGROVE_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.MANGROVE_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_double_slab")),
        )
        register(
            ItemType.MANGROVE_FENCE,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_fence"), ItemMangroveFence::class.java),
        )
        register(
            ItemType.MANGROVE_FENCE_GATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:mangrove_fence_gate"),
                ItemMangroveFenceGate::class.java,
            ),
        )
        register(
            ItemType.MANGROVE_LEAVES,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_leaves")),
        )
        register(ItemType.MANGROVE_LOG, ItemRegistryData(Identifier.fromString("minecraft:mangrove_log")))
        register(
            ItemType.MANGROVE_PLANKS,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_planks")),
        )
        register(
            ItemType.MANGROVE_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:mangrove_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.MANGROVE_PROPAGULE,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_propagule")),
        )
        register(ItemType.MANGROVE_ROOTS, ItemRegistryData(Identifier.fromString("minecraft:mangrove_roots")))
        register(
            ItemType.MANGROVE_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_sign"), ItemMangroveSign::class.java),
        )
        register(ItemType.MANGROVE_SLAB, ItemRegistryData(Identifier.fromString("minecraft:mangrove_slab")))
        register(
            ItemType.MANGROVE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.MANGROVE_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_standing_sign")),
        )
        register(
            ItemType.MANGROVE_TRAPDOOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:mangrove_trapdoor"),
                ItemWoodenTrapdoor::class.java,
            ),
        )
        register(
            ItemType.MANGROVE_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:mangrove_wall_sign")),
        )
        register(ItemType.MANGROVE_WOOD, ItemRegistryData(Identifier.fromString("minecraft:mangrove_wood")))
        register(ItemType.MEDICINE, ItemRegistryData(Identifier.fromString("minecraft:medicine")))
        register(
            ItemType.MEDIUM_AMETHYST_BUD,
            ItemRegistryData(Identifier.fromString("minecraft:medium_amethyst_bud")),
        )
        register(ItemType.MELON_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:melon_block")))
        register(ItemType.MELON_SEEDS, ItemRegistryData(Identifier.fromString("minecraft:melon_seeds")))
        register(
            ItemType.MELON_SLICE,
            ItemRegistryData(Identifier.fromString("minecraft:melon_slice"), ItemMelonSlice::class.java),
        )
        register(ItemType.MELON_STEM, ItemRegistryData(Identifier.fromString("minecraft:melon_stem")))
        register(
            ItemType.MILK_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:milk_bucket"), ItemBucket::class.java),
        )
        register(ItemType.MINECART, ItemRegistryData(Identifier.fromString("minecraft:minecart")))
        register(ItemType.MOB_SPAWNER, ItemRegistryData(Identifier.fromString("minecraft:mob_spawner")))
        register(
            ItemType.MOJANG_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:mojang_banner_pattern")),
        )
        register(
            ItemType.INFESTED_STONE,
            ItemRegistryData(Identifier.fromString("minecraft:monster_egg"), ItemInfestedStone::class.java),
        )
        register(
            ItemType.MOOSHROOM_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:mooshroom_spawn_egg")),
        )
        register(ItemType.MOSS_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:moss_block")))
        register(ItemType.MOSS_CARPET, ItemRegistryData(Identifier.fromString("minecraft:moss_carpet")))
        register(
            ItemType.MOSSY_COBBLESTONE,
            ItemRegistryData(Identifier.fromString("minecraft:mossy_cobblestone")),
        )
        register(
            ItemType.MOSSY_COBBLESTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:mossy_cobblestone_stairs")),
        )
        register(
            ItemType.MOSSY_STONE_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:mossy_stone_brick_stairs")),
        )
        register(ItemType.MOVING_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:moving_block")))
        register(ItemType.MUD, ItemRegistryData(Identifier.fromString("minecraft:mud")))
        register(
            ItemType.MUD_BRICK_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:mud_brick_double_slab")),
        )
        register(ItemType.MUD_BRICK_SLAB, ItemRegistryData(Identifier.fromString("minecraft:mud_brick_slab")))
        register(
            ItemType.MUD_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:mud_brick_stairs")),
        )
        register(ItemType.MUD_BRICK_WALL, ItemRegistryData(Identifier.fromString("minecraft:mud_brick_wall")))
        register(ItemType.MUD_BRICKS, ItemRegistryData(Identifier.fromString("minecraft:mud_bricks")))
        register(
            ItemType.MUDDY_MANGROVE_ROOTS,
            ItemRegistryData(Identifier.fromString("minecraft:muddy_mangrove_roots")),
        )
        register(ItemType.MULE_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:mule_spawn_egg")))
        register(
            ItemType.MUSHROOM_STEW,
            ItemRegistryData(Identifier.fromString("minecraft:mushroom_stew"), ItemMushroomStew::class.java),
        )
        register(ItemType.MUSIC_DISC_11, ItemRegistryData(Identifier.fromString("minecraft:music_disc_11")))
        register(ItemType.MUSIC_DISC_13, ItemRegistryData(Identifier.fromString("minecraft:music_disc_13")))
        register(ItemType.MUSIC_DISC_5, ItemRegistryData(Identifier.fromString("minecraft:music_disc_5")))
        register(
            ItemType.MUSIC_DISC_BLOCKS,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_blocks")),
        )
        register(ItemType.MUSIC_DISC_CAT, ItemRegistryData(Identifier.fromString("minecraft:music_disc_cat")))
        register(
            ItemType.MUSIC_DISC_CHIRP,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_chirp")),
        )
        register(ItemType.MUSIC_DISC_FAR, ItemRegistryData(Identifier.fromString("minecraft:music_disc_far")))
        register(
            ItemType.MUSIC_DISC_MALL,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_mall")),
        )
        register(
            ItemType.MUSIC_DISC_MELLOHI,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_mellohi")),
        )
        register(
            ItemType.MUSIC_DISC_OTHERSIDE,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_otherside")),
        )
        register(
            ItemType.MUSIC_DISC_PIGSTEP,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_pigstep")),
        )
        register(
            ItemType.MUSIC_DISC_STAL,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_stal")),
        )
        register(
            ItemType.MUSIC_DISC_STRAD,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_strad")),
        )
        register(
            ItemType.MUSIC_DISC_WAIT,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_wait")),
        )
        register(
            ItemType.MUSIC_DISC_WARD,
            ItemRegistryData(Identifier.fromString("minecraft:music_disc_ward")),
        )
        register(
            ItemType.MUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:mutton"), ItemMutton::class.java),
        )
        register(ItemType.MYCELIUM, ItemRegistryData(Identifier.fromString("minecraft:mycelium")))
        register(ItemType.NAME_TAG, ItemRegistryData(Identifier.fromString("minecraft:name_tag")))
        register(ItemType.NAUTILUS_SHELL, ItemRegistryData(Identifier.fromString("minecraft:nautilus_shell")))
        register(ItemType.NETHER_BRICK, ItemRegistryData(Identifier.fromString("minecraft:nether_brick")))
        register(
            ItemType.NETHER_BRICK_FENCE,
            ItemRegistryData(Identifier.fromString("minecraft:nether_brick_fence")),
        )
        register(
            ItemType.NETHER_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:nether_brick_stairs")),
        )
        register(
            ItemType.NETHER_GOLD_ORE,
            ItemRegistryData(Identifier.fromString("minecraft:nether_gold_ore")),
        )
        register(ItemType.NETHER_SPROUTS, ItemRegistryData(Identifier.fromString("minecraft:nether_sprouts")))
        register(ItemType.NETHER_STAR, ItemRegistryData(Identifier.fromString("minecraft:nether_star")))
        register(
            ItemType.NETHER_WART,
            ItemRegistryData(Identifier.fromString("minecraft:nether_wart"), ItemNetherWart::class.java),
        )
        register(
            ItemType.NETHER_WART_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:nether_wart_block")),
        )
        register(ItemType.NETHERBRICK, ItemRegistryData(Identifier.fromString("minecraft:netherbrick")))
        register(
            ItemType.NETHERITE_AXE,
            ItemRegistryData(Identifier.fromString("minecraft:netherite_axe"), ItemNetheriteAxe::class.java),
        )
        register(
            ItemType.NETHERITE_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:netherite_block")),
        )
        register(
            ItemType.NETHERITE_BOOTS,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_boots"),
                ItemNetheriteBoots::class.java,
            ),
        )
        register(
            ItemType.NETHERITE_CHESTPLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_chestplate"),
                ItemNetheriteChestplate::class.java,
            ),
        )
        register(
            ItemType.NETHERITE_HELMET,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_helmet"),
                ItemNetheriteHelmet::class.java,
            ),
        )
        register(
            ItemType.NETHERITE_HOE,
            ItemRegistryData(Identifier.fromString("minecraft:netherite_hoe"), ItemNetheriteHoe::class.java),
        )
        register(
            ItemType.NETHERITE_INGOT,
            ItemRegistryData(Identifier.fromString("minecraft:netherite_ingot")),
        )
        register(
            ItemType.NETHERITE_LEGGINGS,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_leggings"),
                ItemNetheriteLeggings::class.java,
            ),
        )
        register(
            ItemType.NETHERITE_PICKAXE,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_pickaxe"),
                ItemNetheritePickaxe::class.java,
            ),
        )
        register(
            ItemType.NETHERITE_SCRAP,
            ItemRegistryData(Identifier.fromString("minecraft:netherite_scrap")),
        )
        register(
            ItemType.NETHERITE_SHOVEL,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_shovel"),
                ItemNetheriteShovel::class.java,
            ),
        )
        register(
            ItemType.NETHERITE_SWORD,
            ItemRegistryData(
                Identifier.fromString("minecraft:netherite_sword"),
                ItemNetheriteSword::class.java,
            ),
        )
        register(ItemType.NETHERRACK, ItemRegistryData(Identifier.fromString("minecraft:netherrack")))
        register(ItemType.NETHERREACTOR, ItemRegistryData(Identifier.fromString("minecraft:netherreactor")))
        register(
            ItemType.NORMAL_STONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:normal_stone_stairs")),
        )
        register(
            ItemType.NOTEBLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:noteblock"), ItemNoteblock::class.java),
        )
        register(ItemType.NPC_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:npc_spawn_egg")))
        register(ItemType.OAK_BOAT, ItemRegistryData(Identifier.fromString("minecraft:oak_boat")))
        register(ItemType.OAK_CHEST_BOAT, ItemRegistryData(Identifier.fromString("minecraft:oak_chest_boat")))
        register(
            ItemType.OAK_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:oak_sign"), ItemOakSign::class.java),
        )
        register(
            ItemType.OAK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:oak_stairs"), ItemStairs::class.java),
        )
        register(ItemType.OBSERVER, ItemRegistryData(Identifier.fromString("minecraft:observer")))
        register(ItemType.OBSIDIAN, ItemRegistryData(Identifier.fromString("minecraft:obsidian")))
        register(
            ItemType.OCELOT_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:ocelot_spawn_egg")),
        )
        register(
            ItemType.OCHRE_FROGLIGHT,
            ItemRegistryData(Identifier.fromString("minecraft:ochre_froglight")),
        )
        register(
            ItemType.ORANGE_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:orange_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.ORANGE_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:orange_candle_cake")),
        )
        register(ItemType.ORANGE_DYE, ItemRegistryData(Identifier.fromString("minecraft:orange_dye")))
        register(
            ItemType.ORANGE_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:orange_glazed_terracotta")),
        )
        register(
            ItemType.OXIDIZED_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:oxidized_copper")),
        )
        register(
            ItemType.OXIDIZED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:oxidized_cut_copper")),
        )
        register(
            ItemType.OXIDIZED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:oxidized_cut_copper_slab")),
        )
        register(
            ItemType.OXIDIZED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:oxidized_cut_copper_stairs")),
        )
        register(
            ItemType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:oxidized_double_cut_copper_slab")),
        )
        register(ItemType.PACKED_ICE, ItemRegistryData(Identifier.fromString("minecraft:packed_ice")))
        register(ItemType.PACKED_MUD, ItemRegistryData(Identifier.fromString("minecraft:packed_mud")))
        register(ItemType.PAINTING, ItemRegistryData(Identifier.fromString("minecraft:painting")))
        register(
            ItemType.PANDA_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:panda_spawn_egg")),
        )
        register(ItemType.PAPER, ItemRegistryData(Identifier.fromString("minecraft:paper")))
        register(
            ItemType.PARROT_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:parrot_spawn_egg")),
        )
        register(
            ItemType.PEARLESCENT_FROGLIGHT,
            ItemRegistryData(Identifier.fromString("minecraft:pearlescent_froglight")),
        )
        register(
            ItemType.PHANTOM_MEMBRANE,
            ItemRegistryData(Identifier.fromString("minecraft:phantom_membrane")),
        )
        register(
            ItemType.PHANTOM_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:phantom_spawn_egg")),
        )
        register(ItemType.PIG_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:pig_spawn_egg")))
        register(
            ItemType.PIGLIN_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:piglin_banner_pattern")),
        )
        register(
            ItemType.PIGLIN_BRUTE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:piglin_brute_spawn_egg")),
        )
        register(
            ItemType.PIGLIN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:piglin_spawn_egg")),
        )
        register(
            ItemType.PILLAGER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:pillager_spawn_egg")),
        )
        register(
            ItemType.PINK_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:pink_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.PINK_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:pink_candle_cake")),
        )
        register(ItemType.PINK_DYE, ItemRegistryData(Identifier.fromString("minecraft:pink_dye")))
        register(
            ItemType.PINK_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:pink_glazed_terracotta")),
        )
        register(ItemType.PISTON, ItemRegistryData(Identifier.fromString("minecraft:piston")))
        register(
            ItemType.PISTON_ARM_COLLISION,
            ItemRegistryData(Identifier.fromString("minecraft:piston_arm_collision")),
        )
        register(
            ItemType.PLANKS,
            ItemRegistryData(Identifier.fromString("minecraft:planks"), ItemPlanks::class.java),
        )
        register(ItemType.PODZOL, ItemRegistryData(Identifier.fromString("minecraft:podzol")))
        register(
            ItemType.POINTED_DRIPSTONE,
            ItemRegistryData(Identifier.fromString("minecraft:pointed_dripstone")),
        )
        register(
            ItemType.POISONOUS_POTATO,
            ItemRegistryData(
                Identifier.fromString("minecraft:poisonous_potato"),
                ItemPoisonousPotato::class.java,
            ),
        )
        register(
            ItemType.POLAR_BEAR_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:polar_bear_spawn_egg")),
        )
        register(
            ItemType.POLISHED_ANDESITE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_andesite_stairs")),
        )
        register(
            ItemType.POLISHED_BASALT,
            ItemRegistryData(Identifier.fromString("minecraft:polished_basalt")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_brick_double_slab")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_BRICK_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_brick_slab")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_brick_stairs")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_BRICK_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_brick_wall")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_BRICKS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_bricks")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_button")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_double_slab")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_pressure_plate")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_slab")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_stairs")),
        )
        register(
            ItemType.POLISHED_BLACKSTONE_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:polished_blackstone_wall")),
        )
        register(
            ItemType.POLISHED_DEEPSLATE,
            ItemRegistryData(Identifier.fromString("minecraft:polished_deepslate")),
        )
        register(
            ItemType.POLISHED_DEEPSLATE_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:polished_deepslate_double_slab")),
        )
        register(
            ItemType.POLISHED_DEEPSLATE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:polished_deepslate_slab")),
        )
        register(
            ItemType.POLISHED_DEEPSLATE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_deepslate_stairs")),
        )
        register(
            ItemType.POLISHED_DEEPSLATE_WALL,
            ItemRegistryData(Identifier.fromString("minecraft:polished_deepslate_wall")),
        )
        register(
            ItemType.POLISHED_DIORITE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_diorite_stairs")),
        )
        register(
            ItemType.POLISHED_GRANITE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:polished_granite_stairs")),
        )
        register(
            ItemType.POPPED_CHORUS_FRUIT,
            ItemRegistryData(Identifier.fromString("minecraft:popped_chorus_fruit")),
        )
        register(
            ItemType.PORKCHOP,
            ItemRegistryData(Identifier.fromString("minecraft:porkchop"), ItemPorkchop::class.java),
        )
        register(ItemType.PORTAL, ItemRegistryData(Identifier.fromString("minecraft:portal")))
        register(
            ItemType.POTATO,
            ItemRegistryData(Identifier.fromString("minecraft:potato"), ItemPotato::class.java),
        )
        register(ItemType.POTATOES, ItemRegistryData(Identifier.fromString("minecraft:potatoes")))
        register(ItemType.POTION, ItemRegistryData(Identifier.fromString("minecraft:potion")))
        register(ItemType.POWDER_SNOW, ItemRegistryData(Identifier.fromString("minecraft:powder_snow")))
        register(
            ItemType.POWDER_SNOW_BUCKET,
            ItemRegistryData(
                Identifier.fromString("minecraft:powder_snow_bucket"),
                ItemPowderSnowBucket::class.java,
            ),
        )
        register(
            ItemType.POWERED_COMPARATOR,
            ItemRegistryData(Identifier.fromString("minecraft:powered_comparator")),
        )
        register(
            ItemType.POWERED_REPEATER,
            ItemRegistryData(Identifier.fromString("minecraft:powered_repeater")),
        )
        register(
            ItemType.PRISMARINE,
            ItemRegistryData(Identifier.fromString("minecraft:prismarine"), ItemPrismarine::class.java),
        )
        register(
            ItemType.PRISMARINE_BRICKS_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:prismarine_bricks_stairs")),
        )
        register(
            ItemType.PRISMARINE_CRYSTALS,
            ItemRegistryData(Identifier.fromString("minecraft:prismarine_crystals")),
        )
        register(
            ItemType.PRISMARINE_SHARD,
            ItemRegistryData(Identifier.fromString("minecraft:prismarine_shard")),
        )
        register(
            ItemType.PRISMARINE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:prismarine_stairs")),
        )
        register(
            ItemType.PUFFERFISH,
            ItemRegistryData(Identifier.fromString("minecraft:pufferfish"), ItemPufferfish::class.java),
        )
        register(
            ItemType.PUFFERFISH_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:pufferfish_bucket"), ItemBucket::class.java),
        )
        register(
            ItemType.PUFFERFISH_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:pufferfish_spawn_egg")),
        )
        register(ItemType.PUMPKIN, ItemRegistryData(Identifier.fromString("minecraft:pumpkin")))
        register(
            ItemType.PUMPKIN_PIE,
            ItemRegistryData(Identifier.fromString("minecraft:pumpkin_pie"), ItemPumpkinPie::class.java),
        )
        register(ItemType.PUMPKIN_SEEDS, ItemRegistryData(Identifier.fromString("minecraft:pumpkin_seeds")))
        register(ItemType.PUMPKIN_STEM, ItemRegistryData(Identifier.fromString("minecraft:pumpkin_stem")))
        register(
            ItemType.PURPLE_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:purple_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.PURPLE_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:purple_candle_cake")),
        )
        register(ItemType.PURPLE_DYE, ItemRegistryData(Identifier.fromString("minecraft:purple_dye")))
        register(
            ItemType.PURPLE_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:purple_glazed_terracotta")),
        )
        register(
            ItemType.PURPUR_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:purpur_block"), ItemPurpurBlock::class.java),
        )
        register(ItemType.PURPUR_STAIRS, ItemRegistryData(Identifier.fromString("minecraft:purpur_stairs")))
        register(ItemType.QUARTZ, ItemRegistryData(Identifier.fromString("minecraft:quartz")))
        register(
            ItemType.QUARTZ_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:quartz_block"), ItemQuartzBlock::class.java),
        )
        register(ItemType.QUARTZ_BRICKS, ItemRegistryData(Identifier.fromString("minecraft:quartz_bricks")))
        register(ItemType.QUARTZ_ORE, ItemRegistryData(Identifier.fromString("minecraft:quartz_ore")))
        register(ItemType.QUARTZ_STAIRS, ItemRegistryData(Identifier.fromString("minecraft:quartz_stairs")))
        register(
            ItemType.RABBIT,
            ItemRegistryData(Identifier.fromString("minecraft:rabbit"), ItemRabbit::class.java),
        )
        register(ItemType.RABBIT_FOOT, ItemRegistryData(Identifier.fromString("minecraft:rabbit_foot")))
        register(ItemType.RABBIT_HIDE, ItemRegistryData(Identifier.fromString("minecraft:rabbit_hide")))
        register(
            ItemType.RABBIT_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:rabbit_spawn_egg")),
        )
        register(
            ItemType.RABBIT_STEW,
            ItemRegistryData(Identifier.fromString("minecraft:rabbit_stew"), ItemRabbitStew::class.java),
        )
        register(ItemType.RAIL, ItemRegistryData(Identifier.fromString("minecraft:rail")))
        register(
            ItemType.RAPID_FERTILIZER,
            ItemRegistryData(Identifier.fromString("minecraft:rapid_fertilizer")),
        )
        register(
            ItemType.RAVAGER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:ravager_spawn_egg")),
        )
        register(ItemType.RAW_COPPER, ItemRegistryData(Identifier.fromString("minecraft:raw_copper")))
        register(
            ItemType.RAW_COPPER_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:raw_copper_block")),
        )
        register(ItemType.RAW_GOLD, ItemRegistryData(Identifier.fromString("minecraft:raw_gold")))
        register(ItemType.RAW_GOLD_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:raw_gold_block")))
        register(ItemType.RAW_IRON, ItemRegistryData(Identifier.fromString("minecraft:raw_iron")))
        register(ItemType.RAW_IRON_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:raw_iron_block")))
        register(
            ItemType.RECOVERY_COMPASS,
            ItemRegistryData(Identifier.fromString("minecraft:recovery_compass")),
        )
        register(
            ItemType.REEDS,
            ItemRegistryData(Identifier.fromString("minecraft:reeds"), ItemSugarCane::class.java),
        )
        register(
            ItemType.RED_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:red_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.RED_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:red_candle_cake")),
        )
        register(ItemType.RED_DYE, ItemRegistryData(Identifier.fromString("minecraft:red_dye")))
        register(
            ItemType.RED_FLOWER,
            ItemRegistryData(Identifier.fromString("minecraft:red_flower"), ItemRedFlower::class.java),
        )
        register(
            ItemType.RED_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:red_glazed_terracotta")),
        )
        register(ItemType.RED_MUSHROOM, ItemRegistryData(Identifier.fromString("minecraft:red_mushroom")))
        register(
            ItemType.RED_MUSHROOM_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:red_mushroom_block")),
        )
        register(
            ItemType.RED_NETHER_BRICK,
            ItemRegistryData(Identifier.fromString("minecraft:red_nether_brick")),
        )
        register(
            ItemType.RED_NETHER_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:red_nether_brick_stairs")),
        )
        register(
            ItemType.RED_SANDSTONE,
            ItemRegistryData(Identifier.fromString("minecraft:red_sandstone"), ItemRedSandstone::class.java),
        )
        register(
            ItemType.RED_SANDSTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:red_sandstone_stairs")),
        )
        register(ItemType.REDSTONE, ItemRegistryData(Identifier.fromString("minecraft:redstone")))
        register(ItemType.REDSTONE_BLOCK, ItemRegistryData(Identifier.fromString("minecraft:redstone_block")))
        register(ItemType.REDSTONE_LAMP, ItemRegistryData(Identifier.fromString("minecraft:redstone_lamp")))
        register(ItemType.REDSTONE_ORE, ItemRegistryData(Identifier.fromString("minecraft:redstone_ore")))
        register(ItemType.REDSTONE_TORCH, ItemRegistryData(Identifier.fromString("minecraft:redstone_torch")))
        register(ItemType.REDSTONE_WIRE, ItemRegistryData(Identifier.fromString("minecraft:redstone_wire")))
        register(
            ItemType.REINFORCED_DEEPSLATE,
            ItemRegistryData(Identifier.fromString("minecraft:reinforced_deepslate")),
        )
        register(ItemType.REPEATER, ItemRegistryData(Identifier.fromString("minecraft:repeater")))
        register(
            ItemType.REPEATING_COMMAND_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:repeating_command_block")),
        )
        register(ItemType.RESERVED6, ItemRegistryData(Identifier.fromString("minecraft:reserved6")))
        register(ItemType.RESPAWN_ANCHOR, ItemRegistryData(Identifier.fromString("minecraft:respawn_anchor")))
        register(ItemType.ROTTEN_FLESH, ItemRegistryData(Identifier.fromString("minecraft:rotten_flesh")))
        register(ItemType.SADDLE, ItemRegistryData(Identifier.fromString("minecraft:saddle")))
        register(
            ItemType.SALMON,
            ItemRegistryData(Identifier.fromString("minecraft:salmon"), ItemSalmon::class.java),
        )
        register(
            ItemType.SALMON_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:salmon_bucket"), ItemBucket::class.java),
        )
        register(
            ItemType.SALMON_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:salmon_spawn_egg")),
        )
        register(
            ItemType.SAND,
            ItemRegistryData(Identifier.fromString("minecraft:sand"), ItemSand::class.java),
        )
        register(
            ItemType.SANDSTONE,
            ItemRegistryData(Identifier.fromString("minecraft:sandstone"), ItemSandstone::class.java),
        )
        register(
            ItemType.SANDSTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:sandstone_stairs")),
        )
        register(
            ItemType.SAPLING,
            ItemRegistryData(Identifier.fromString("minecraft:sapling"), ItemSapling::class.java),
        )
        register(ItemType.SCAFFOLDING, ItemRegistryData(Identifier.fromString("minecraft:scaffolding")))
        register(ItemType.SCULK, ItemRegistryData(Identifier.fromString("minecraft:sculk")))
        register(ItemType.SCULK_CATALYST, ItemRegistryData(Identifier.fromString("minecraft:sculk_catalyst")))
        register(ItemType.SCULK_SENSOR, ItemRegistryData(Identifier.fromString("minecraft:sculk_sensor")))
        register(ItemType.SCULK_SHRIEKER, ItemRegistryData(Identifier.fromString("minecraft:sculk_shrieker")))
        register(ItemType.SCULK_VEIN, ItemRegistryData(Identifier.fromString("minecraft:sculk_vein")))
        register(ItemType.SCUTE, ItemRegistryData(Identifier.fromString("minecraft:scute")))
        register(ItemType.SEA_LANTERN, ItemRegistryData(Identifier.fromString("minecraft:sea_lantern")))
        register(ItemType.SEA_PICKLE, ItemRegistryData(Identifier.fromString("minecraft:sea_pickle")))
        register(ItemType.SEAGRASS, ItemRegistryData(Identifier.fromString("minecraft:seagrass")))
        register(ItemType.SHEARS, ItemRegistryData(Identifier.fromString("minecraft:shears")))
        register(
            ItemType.SHEEP_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:sheep_spawn_egg")),
        )
        register(ItemType.SHIELD, ItemRegistryData(Identifier.fromString("minecraft:shield")))
        register(ItemType.SHROOMLIGHT, ItemRegistryData(Identifier.fromString("minecraft:shroomlight")))
        register(
            ItemType.SHULKER_BOX,
            ItemRegistryData(Identifier.fromString("minecraft:shulker_box"), ItemShulkerBox::class.java),
        )
        register(ItemType.SHULKER_SHELL, ItemRegistryData(Identifier.fromString("minecraft:shulker_shell")))
        register(
            ItemType.SHULKER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:shulker_spawn_egg")),
        )
        register(
            ItemType.SILVER_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:silver_glazed_terracotta")),
        )
        register(
            ItemType.SILVERFISH_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:silverfish_spawn_egg")),
        )
        register(
            ItemType.SKELETON_HORSE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:skeleton_horse_spawn_egg")),
        )
        register(
            ItemType.SKELETON_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:skeleton_spawn_egg")),
        )
        register(
            ItemType.SKULL,
            ItemRegistryData(Identifier.fromString("minecraft:skull"), ItemSkull::class.java),
        )
        register(
            ItemType.SKULL_BANNER_PATTERN,
            ItemRegistryData(Identifier.fromString("minecraft:skull_banner_pattern")),
        )
        register(ItemType.SLIME, ItemRegistryData(Identifier.fromString("minecraft:slime")))
        register(ItemType.SLIME_BALL, ItemRegistryData(Identifier.fromString("minecraft:slime_ball")))
        register(
            ItemType.SLIME_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:slime_spawn_egg")),
        )
        register(
            ItemType.SMALL_AMETHYST_BUD,
            ItemRegistryData(Identifier.fromString("minecraft:small_amethyst_bud")),
        )
        register(
            ItemType.SMALL_DRIPLEAF_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:small_dripleaf_block")),
        )
        register(ItemType.SMITHING_TABLE, ItemRegistryData(Identifier.fromString("minecraft:smithing_table")))
        register(ItemType.SMOKER, ItemRegistryData(Identifier.fromString("minecraft:smoker")))
        register(ItemType.SMOOTH_BASALT, ItemRegistryData(Identifier.fromString("minecraft:smooth_basalt")))
        register(
            ItemType.SMOOTH_QUARTZ_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:smooth_quartz_stairs")),
        )
        register(
            ItemType.SMOOTH_RED_SANDSTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:smooth_red_sandstone_stairs")),
        )
        register(
            ItemType.SMOOTH_SANDSTONE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:smooth_sandstone_stairs")),
        )
        register(ItemType.SMOOTH_STONE, ItemRegistryData(Identifier.fromString("minecraft:smooth_stone")))
        register(ItemType.SNOW, ItemRegistryData(Identifier.fromString("minecraft:snow")))
        register(ItemType.SNOW_LAYER, ItemRegistryData(Identifier.fromString("minecraft:snow_layer")))
        register(
            ItemType.SNOWBALL,
            ItemRegistryData(Identifier.fromString("minecraft:snowball"), ItemSnowball::class.java),
        )
        register(
            ItemType.SOUL_CAMPFIRE,
            ItemRegistryData(Identifier.fromString("minecraft:soul_campfire"), ItemSoulCampfire::class.java),
        )
        register(ItemType.SOUL_FIRE, ItemRegistryData(Identifier.fromString("minecraft:soul_fire")))
        register(ItemType.SOUL_LANTERN, ItemRegistryData(Identifier.fromString("minecraft:soul_lantern")))
        register(ItemType.SOUL_SAND, ItemRegistryData(Identifier.fromString("minecraft:soul_sand")))
        register(ItemType.SOUL_SOIL, ItemRegistryData(Identifier.fromString("minecraft:soul_soil")))
        register(ItemType.SOUL_TORCH, ItemRegistryData(Identifier.fromString("minecraft:soul_torch")))
        register(ItemType.SPARKLER, ItemRegistryData(Identifier.fromString("minecraft:sparkler")))
        register(ItemType.SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:spawn_egg")))
        register(ItemType.SPIDER_EYE, ItemRegistryData(Identifier.fromString("minecraft:spider_eye")))
        register(
            ItemType.SPIDER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:spider_spawn_egg")),
        )
        register(ItemType.SPLASH_POTION, ItemRegistryData(Identifier.fromString("minecraft:splash_potion")))
        register(ItemType.SPONGE, ItemRegistryData(Identifier.fromString("minecraft:sponge")))
        register(ItemType.SPORE_BLOSSOM, ItemRegistryData(Identifier.fromString("minecraft:spore_blossom")))
        register(ItemType.SPRUCE_BOAT, ItemRegistryData(Identifier.fromString("minecraft:spruce_boat")))
        register(
            ItemType.SPRUCE_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.SPRUCE_CHEST_BOAT,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_chest_boat")),
        )
        register(
            ItemType.SPRUCE_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.SPRUCE_FENCE_GATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:spruce_fence_gate"),
                ItemWoodenFenceGate::class.java,
            ),
        )
        register(
            ItemType.SPRUCE_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:spruce_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.SPRUCE_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_sign"), ItemSpruceSign::class.java),
        )
        register(
            ItemType.SPRUCE_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.SPRUCE_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_standing_sign")),
        )
        register(
            ItemType.SPRUCE_TRAPDOOR,
            ItemRegistryData(
                Identifier.fromString("minecraft:spruce_trapdoor"),
                ItemWoodenTrapdoor::class.java,
            ),
        )
        register(
            ItemType.SPRUCE_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:spruce_wall_sign")),
        )
        register(ItemType.SPYGLASS, ItemRegistryData(Identifier.fromString("minecraft:spyglass")))
        register(
            ItemType.SQUID_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:squid_spawn_egg")),
        )
        register(
            ItemType.STAINED_GLASS,
            ItemRegistryData(Identifier.fromString("minecraft:stained_glass"), ItemStainedGlass::class.java),
        )
        register(
            ItemType.STAINED_GLASS_PANE,
            ItemRegistryData(
                Identifier.fromString("minecraft:stained_glass_pane"),
                ItemStainedGlassPane::class.java,
            ),
        )
        register(
            ItemType.STAINED_HARDENED_CLAY,
            ItemRegistryData(
                Identifier.fromString("minecraft:stained_hardened_clay"),
                ItemStainedHardenedClay::class.java,
            ),
        )
        register(
            ItemType.STANDING_BANNER,
            ItemRegistryData(Identifier.fromString("minecraft:standing_banner")),
        )
        register(ItemType.STANDING_SIGN, ItemRegistryData(Identifier.fromString("minecraft:standing_sign")))
        register(
            ItemType.STICK,
            ItemRegistryData(Identifier.fromString("minecraft:stick"), ItemStick::class.java),
        )
        register(ItemType.STICKY_PISTON, ItemRegistryData(Identifier.fromString("minecraft:sticky_piston")))
        register(
            ItemType.STICKY_PISTON_ARM_COLLISION,
            ItemRegistryData(Identifier.fromString("minecraft:sticky_piston_arm_collision")),
        )
        register(
            ItemType.STONE,
            ItemRegistryData(Identifier.fromString("minecraft:stone"), ItemStone::class.java),
        )
        register(
            ItemType.STONE_AXE,
            ItemRegistryData(Identifier.fromString("minecraft:stone_axe"), ItemStoneAxe::class.java),
        )
        register(
            ItemType.STONE_BLOCK_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:stone_block_slab"), ItemStoneSlab::class.java),
        )
        register(
            ItemType.STONE_BLOCK_SLAB2,
            ItemRegistryData(Identifier.fromString("minecraft:stone_block_slab2"), ItemStoneSlab2::class.java),
        )
        register(
            ItemType.STONE_BLOCK_SLAB3,
            ItemRegistryData(Identifier.fromString("minecraft:stone_block_slab3"), ItemStoneSlab3::class.java),
        )
        register(
            ItemType.STONE_BLOCK_SLAB4,
            ItemRegistryData(Identifier.fromString("minecraft:stone_block_slab4"), ItemStoneSlab4::class.java),
        )
        register(
            ItemType.STONE_BRICK_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:stone_brick_stairs")),
        )
        register(ItemType.STONE_BUTTON, ItemRegistryData(Identifier.fromString("minecraft:stone_button")))
        register(
            ItemType.STONE_HOE,
            ItemRegistryData(Identifier.fromString("minecraft:stone_hoe"), ItemStoneHoe::class.java),
        )
        register(
            ItemType.STONE_PICKAXE,
            ItemRegistryData(Identifier.fromString("minecraft:stone_pickaxe"), ItemStonePickaxe::class.java),
        )
        register(
            ItemType.STONE_PRESSURE_PLATE,
            ItemRegistryData(Identifier.fromString("minecraft:stone_pressure_plate")),
        )
        register(
            ItemType.STONE_SHOVEL,
            ItemRegistryData(Identifier.fromString("minecraft:stone_shovel"), ItemStoneShovel::class.java),
        )
        register(ItemType.STONE_STAIRS, ItemRegistryData(Identifier.fromString("minecraft:stone_stairs")))
        register(
            ItemType.STONE_SWORD,
            ItemRegistryData(Identifier.fromString("minecraft:stone_sword"), ItemStoneSword::class.java),
        )
        register(
            ItemType.STONEBRICK,
            ItemRegistryData(Identifier.fromString("minecraft:stonebrick"), ItemStonebrick::class.java),
        )
        register(ItemType.STONECUTTER, ItemRegistryData(Identifier.fromString("minecraft:stonecutter")))
        register(
            ItemType.STONECUTTER_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:stonecutter_block")),
        )
        register(
            ItemType.STRAY_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:stray_spawn_egg")),
        )
        register(
            ItemType.STRIDER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:strider_spawn_egg")),
        )
        register(ItemType.STRING, ItemRegistryData(Identifier.fromString("minecraft:string")))
        register(
            ItemType.STRIPPED_ACACIA_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_acacia_log")),
        )
        register(
            ItemType.STRIPPED_BIRCH_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_birch_log")),
        )
        register(
            ItemType.STRIPPED_CRIMSON_HYPHAE,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_crimson_hyphae")),
        )
        register(
            ItemType.STRIPPED_CRIMSON_STEM,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_crimson_stem")),
        )
        register(
            ItemType.STRIPPED_DARK_OAK_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_dark_oak_log")),
        )
        register(
            ItemType.STRIPPED_JUNGLE_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_jungle_log")),
        )
        register(
            ItemType.STRIPPED_MANGROVE_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_mangrove_log")),
        )
        register(
            ItemType.STRIPPED_MANGROVE_WOOD,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_mangrove_wood")),
        )
        register(
            ItemType.STRIPPED_OAK_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_oak_log")),
        )
        register(
            ItemType.STRIPPED_SPRUCE_LOG,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_spruce_log")),
        )
        register(
            ItemType.STRIPPED_WARPED_HYPHAE,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_warped_hyphae")),
        )
        register(
            ItemType.STRIPPED_WARPED_STEM,
            ItemRegistryData(Identifier.fromString("minecraft:stripped_warped_stem")),
        )
        register(
            ItemType.STRUCTURE_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:structure_block")),
        )
        register(ItemType.STRUCTURE_VOID, ItemRegistryData(Identifier.fromString("minecraft:structure_void")))
        register(ItemType.STRUCTURE_VOID, ItemRegistryData(Identifier.fromString("minecraft:structure_void")))
        register(ItemType.SUGAR, ItemRegistryData(Identifier.fromString("minecraft:sugar")))
        register(ItemType.SUGAR_CANE, ItemRegistryData(Identifier.fromString("minecraft:sugar_cane")))
        register(
            ItemType.SUSPICIOUS_STEW,
            ItemRegistryData(Identifier.fromString("minecraft:suspicious_stew")),
        )
        register(
            ItemType.SWEET_BERRIES,
            ItemRegistryData(Identifier.fromString("minecraft:sweet_berries"), ItemSweetBerries::class.java),
        )
        register(
            ItemType.SWEET_BERRY_BUSH,
            ItemRegistryData(Identifier.fromString("minecraft:sweet_berry_bush")),
        )
        register(
            ItemType.TADPOLE_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:tadpole_bucket"), ItemBucket::class.java),
        )
        register(
            ItemType.TADPOLE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:tadpole_spawn_egg")),
        )
        register(
            ItemType.TALLGRASS,
            ItemRegistryData(Identifier.fromString("minecraft:tallgrass"), ItemTallGrass::class.java),
        )
        register(ItemType.TARGET, ItemRegistryData(Identifier.fromString("minecraft:target")))
        register(ItemType.TINTED_GLASS, ItemRegistryData(Identifier.fromString("minecraft:tinted_glass")))
        register(ItemType.TNT, ItemRegistryData(Identifier.fromString("minecraft:tnt")))
        register(ItemType.TNT_MINECART, ItemRegistryData(Identifier.fromString("minecraft:tnt_minecart")))
        register(ItemType.TORCH, ItemRegistryData(Identifier.fromString("minecraft:torch")))
        register(
            ItemType.TOTEM_OF_UNDYING,
            ItemRegistryData(Identifier.fromString("minecraft:totem_of_undying")),
        )
        register(
            ItemType.TRADER_LLAMA_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:trader_llama_spawn_egg")),
        )
        register(
            ItemType.OAK_TRAPDOOR,
            ItemRegistryData(Identifier.fromString("minecraft:trapdoor"), ItemWoodenTrapdoor::class.java),
        )
        register(
            ItemType.TRAPPED_CHEST,
            ItemRegistryData(Identifier.fromString("minecraft:trapped_chest"), ItemTrappedChest::class.java),
        )
        register(ItemType.TRIDENT, ItemRegistryData(Identifier.fromString("minecraft:trident")))
        register(ItemType.TRIP_WIRE, ItemRegistryData(Identifier.fromString("minecraft:trip_wire")))
        register(ItemType.TRIPWIRE_HOOK, ItemRegistryData(Identifier.fromString("minecraft:tripwire_hook")))
        register(
            ItemType.TROPICAL_FISH,
            ItemRegistryData(Identifier.fromString("minecraft:tropical_fish"), ItemTropicalFish::class.java),
        )
        register(
            ItemType.TROPICAL_FISH_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:tropical_fish_bucket"), ItemBucket::class.java),
        )
        register(
            ItemType.TROPICAL_FISH_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:tropical_fish_spawn_egg")),
        )
        register(
            ItemType.TORCHFLOWER_SEEDS,
            ItemRegistryData(Identifier.fromString("minecraft:torchflower_seeds")),
        )
        register(ItemType.TUFF, ItemRegistryData(Identifier.fromString("minecraft:tuff")))
        register(ItemType.TURTLE_EGG, ItemRegistryData(Identifier.fromString("minecraft:turtle_egg")))
        register(
            ItemType.TURTLE_HELMET,
            ItemRegistryData(Identifier.fromString("minecraft:turtle_helmet"), ItemTurtleHelmet::class.java),
        )
        register(
            ItemType.TURTLE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:turtle_spawn_egg")),
        )
        register(ItemType.TWISTING_VINES, ItemRegistryData(Identifier.fromString("minecraft:twisting_vines")))
        register(
            ItemType.UNDERWATER_TORCH,
            ItemRegistryData(Identifier.fromString("minecraft:underwater_torch")),
        )
        register(
            ItemType.UNDYED_SHULKER_BOX,
            ItemRegistryData(
                Identifier.fromString("minecraft:undyed_shulker_box"),
                ItemUndyedShulkerBox::class.java,
            ),
        )
        register(ItemType.UNKNOWN, ItemRegistryData(Identifier.fromString("minecraft:unknown")))
        register(
            ItemType.UNLIT_REDSTONE_TORCH,
            ItemRegistryData(Identifier.fromString("minecraft:unlit_redstone_torch")),
        )
        register(
            ItemType.UNPOWERED_COMPARATOR,
            ItemRegistryData(Identifier.fromString("minecraft:unpowered_comparator")),
        )
        register(
            ItemType.UNPOWERED_REPEATER,
            ItemRegistryData(Identifier.fromString("minecraft:unpowered_repeater")),
        )
        register(
            ItemType.VERDANT_FROGLIGHT,
            ItemRegistryData(Identifier.fromString("minecraft:verdant_froglight")),
        )
        register(ItemType.VEX_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:vex_spawn_egg")))
        register(
            ItemType.VILLAGER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:villager_spawn_egg")),
        )
        register(
            ItemType.VINDICATOR_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:vindicator_spawn_egg")),
        )
        register(ItemType.VINE, ItemRegistryData(Identifier.fromString("minecraft:vine")))
        register(ItemType.WALL_BANNER, ItemRegistryData(Identifier.fromString("minecraft:wall_banner")))
        register(ItemType.WALL_SIGN, ItemRegistryData(Identifier.fromString("minecraft:wall_sign")))
        register(
            ItemType.WANDERING_TRADER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:wandering_trader_spawn_egg")),
        )
        register(
            ItemType.WARDEN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:warden_spawn_egg")),
        )
        register(ItemType.WARPED_BUTTON, ItemRegistryData(Identifier.fromString("minecraft:warped_button")))
        register(
            ItemType.WARPED_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:warped_door"), ItemDoor::class.java),
        )
        register(
            ItemType.WARPED_DOUBLE_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:warped_double_slab")),
        )
        register(ItemType.WARPED_FENCE, ItemRegistryData(Identifier.fromString("minecraft:warped_fence")))
        register(
            ItemType.WARPED_FENCE_GATE,
            ItemRegistryData(Identifier.fromString("minecraft:warped_fence_gate")),
        )
        register(ItemType.WARPED_FUNGUS, ItemRegistryData(Identifier.fromString("minecraft:warped_fungus")))
        register(
            ItemType.WARPED_FUNGUS_ON_A_STICK,
            ItemRegistryData(Identifier.fromString("minecraft:warped_fungus_on_a_stick")),
        )
        register(ItemType.WARPED_HYPHAE, ItemRegistryData(Identifier.fromString("minecraft:warped_hyphae")))
        register(ItemType.WARPED_NYLIUM, ItemRegistryData(Identifier.fromString("minecraft:warped_nylium")))
        register(ItemType.WARPED_PLANKS, ItemRegistryData(Identifier.fromString("minecraft:warped_planks")))
        register(
            ItemType.WARPED_PRESSURE_PLATE,
            ItemRegistryData(Identifier.fromString("minecraft:warped_pressure_plate")),
        )
        register(ItemType.WARPED_ROOTS, ItemRegistryData(Identifier.fromString("minecraft:warped_roots")))
        register(
            ItemType.WARPED_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:warped_sign"), ItemWarpedSign::class.java),
        )
        register(ItemType.WARPED_SLAB, ItemRegistryData(Identifier.fromString("minecraft:warped_slab")))
        register(
            ItemType.WARPED_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:warped_stairs"), ItemStairs::class.java),
        )
        register(
            ItemType.WARPED_STANDING_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:warped_standing_sign")),
        )
        register(ItemType.WARPED_STEM, ItemRegistryData(Identifier.fromString("minecraft:warped_stem")))
        register(
            ItemType.WARPED_TRAPDOOR,
            ItemRegistryData(Identifier.fromString("minecraft:warped_trapdoor")),
        )
        register(
            ItemType.WARPED_WALL_SIGN,
            ItemRegistryData(Identifier.fromString("minecraft:warped_wall_sign")),
        )
        register(
            ItemType.WARPED_WART_BLOCK,
            ItemRegistryData(Identifier.fromString("minecraft:warped_wart_block")),
        )
        register(ItemType.WATER, ItemRegistryData(Identifier.fromString("minecraft:water")))
        register(
            ItemType.WATER_BUCKET,
            ItemRegistryData(Identifier.fromString("minecraft:water_bucket"), ItemWaterBucket::class.java),
        )
        register(ItemType.WATERLILY, ItemRegistryData(Identifier.fromString("minecraft:waterlily")))
        register(ItemType.WAXED_COPPER, ItemRegistryData(Identifier.fromString("minecraft:waxed_copper")))
        register(
            ItemType.WAXED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_cut_copper")),
        )
        register(
            ItemType.WAXED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_cut_copper_stairs")),
        )
        register(
            ItemType.WAXED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_double_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_EXPOSED_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_exposed_copper")),
        )
        register(
            ItemType.WAXED_EXPOSED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_exposed_cut_copper")),
        )
        register(
            ItemType.WAXED_EXPOSED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_exposed_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_EXPOSED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_exposed_cut_copper_stairs")),
        )
        register(
            ItemType.WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_exposed_double_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_OXIDIZED_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_oxidized_copper")),
        )
        register(
            ItemType.WAXED_OXIDIZED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_oxidized_cut_copper")),
        )
        register(
            ItemType.WAXED_OXIDIZED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_oxidized_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_oxidized_cut_copper_stairs")),
        )
        register(
            ItemType.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_oxidized_double_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_WEATHERED_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_weathered_copper")),
        )
        register(
            ItemType.WAXED_WEATHERED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_weathered_cut_copper")),
        )
        register(
            ItemType.WAXED_WEATHERED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_weathered_cut_copper_slab")),
        )
        register(
            ItemType.WAXED_WEATHERED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_weathered_cut_copper_stairs")),
        )
        register(
            ItemType.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:waxed_weathered_double_cut_copper_slab")),
        )
        register(
            ItemType.WEATHERED_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:weathered_copper")),
        )
        register(
            ItemType.WEATHERED_CUT_COPPER,
            ItemRegistryData(Identifier.fromString("minecraft:weathered_cut_copper")),
        )
        register(
            ItemType.WEATHERED_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:weathered_cut_copper_slab")),
        )
        register(
            ItemType.WEATHERED_CUT_COPPER_STAIRS,
            ItemRegistryData(Identifier.fromString("minecraft:weathered_cut_copper_stairs")),
        )
        register(
            ItemType.WEATHERED_DOUBLE_CUT_COPPER_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:weathered_double_cut_copper_slab")),
        )
        register(ItemType.WEB, ItemRegistryData(Identifier.fromString("minecraft:web")))
        register(ItemType.WEEPING_VINES, ItemRegistryData(Identifier.fromString("minecraft:weeping_vines")))
        register(ItemType.WHEAT, ItemRegistryData(Identifier.fromString("minecraft:wheat")))
        register(ItemType.WHEAT_SEEDS, ItemRegistryData(Identifier.fromString("minecraft:wheat_seeds")))
        register(
            ItemType.WHITE_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:white_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.WHITE_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:white_candle_cake")),
        )
        register(ItemType.WHITE_DYE, ItemRegistryData(Identifier.fromString("minecraft:white_dye")))
        register(
            ItemType.WHITE_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:white_glazed_terracotta")),
        )
        register(
            ItemType.WITCH_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:witch_spawn_egg")),
        )
        register(ItemType.WITHER_ROSE, ItemRegistryData(Identifier.fromString("minecraft:wither_rose")))
        register(
            ItemType.WITHER_SKELETON_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:wither_skeleton_spawn_egg")),
        )
        register(ItemType.WOLF_SPAWN_EGG, ItemRegistryData(Identifier.fromString("minecraft:wolf_spawn_egg")))
        register(
            ItemType.WOOD,
            ItemRegistryData(Identifier.fromString("minecraft:wood"), ItemWood::class.java),
        )
        register(
            ItemType.WOODEN_AXE,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_axe"), ItemWoodenAxe::class.java),
        )
        register(
            ItemType.WOODEN_BUTTON,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_button"), ItemWoodenButton::class.java),
        )
        register(
            ItemType.WOODEN_DOOR,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_door"), ItemWoodenDoor::class.java),
        )
        register(
            ItemType.WOODEN_HOE,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_hoe"), ItemWoodenHoe::class.java),
        )
        register(
            ItemType.WOODEN_PICKAXE,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_pickaxe"), ItemWoodenPickaxe::class.java),
        )
        register(
            ItemType.WOODEN_PRESSURE_PLATE,
            ItemRegistryData(
                Identifier.fromString("minecraft:wooden_pressure_plate"),
                ItemWoodenPressurePlate::class.java,
            ),
        )
        register(
            ItemType.WOODEN_SHOVEL,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_shovel"), ItemWoodenShovel::class.java),
        )
        register(
            ItemType.WOODEN_SLAB,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_slab"), ItemWoodenSlab::class.java),
        )
        register(
            ItemType.WOODEN_SWORD,
            ItemRegistryData(Identifier.fromString("minecraft:wooden_sword"), ItemWoodenSword::class.java),
        )
        register(
            ItemType.WHITE_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:white_wool"), ItemWhiteWool::class.java),
        )
        register(
            ItemType.ORANGE_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:orange_wool"), ItemOrangeWool::class.java),
        )
        register(
            ItemType.MAGENTA_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:magenta_wool"), ItemMagentaWool::class.java),
        )
        register(
            ItemType.LIGHT_BLUE_WOOL,
            ItemRegistryData(
                Identifier.fromString("minecraft:light_blue_wool"),
                ItemLightBlueWool::class.java,
            ),
        )
        register(
            ItemType.YELLOW_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:yellow_wool"), ItemYellowWool::class.java),
        )
        register(
            ItemType.LIME_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:lime_wool"), ItemLimeWool::class.java),
        )
        register(
            ItemType.PINK_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:pink_wool"), ItemPinkWool::class.java),
        )
        register(
            ItemType.GRAY_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:gray_wool"), ItemGrayWool::class.java),
        )
        register(
            ItemType.SILVER_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:silver_wool"), ItemSilverWool::class.java),
        )
        register(
            ItemType.CYAN_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:cyan_wool"), ItemCyanWool::class.java),
        )
        register(
            ItemType.PURPLE_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:purple_wool"), ItemPurpleWool::class.java),
        )
        register(
            ItemType.BLUE_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:blue_wool"), ItemLightBlueWool::class.java),
        )
        register(
            ItemType.BROWN_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:brown_wool"), ItemBrownWool::class.java),
        )
        register(
            ItemType.GREEN_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:green_wool"), ItemGreenWool::class.java),
        )
        register(
            ItemType.RED_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:red_wool"), ItemRedWool::class.java),
        )
        register(
            ItemType.BLACK_WOOL,
            ItemRegistryData(Identifier.fromString("minecraft:black_wool"), ItemBlackWool::class.java),
        )
        register(ItemType.WRITABLE_BOOK, ItemRegistryData(Identifier.fromString("minecraft:writable_book")))
        register(ItemType.WRITTEN_BOOK, ItemRegistryData(Identifier.fromString("minecraft:written_book")))
        register(
            ItemType.YELLOW_CANDLE,
            ItemRegistryData(Identifier.fromString("minecraft:yellow_candle"), ItemCandle::class.java),
        )
        register(
            ItemType.YELLOW_CANDLE_CAKE,
            ItemRegistryData(Identifier.fromString("minecraft:yellow_candle_cake")),
        )
        register(ItemType.YELLOW_DYE, ItemRegistryData(Identifier.fromString("minecraft:yellow_dye")))
        register(ItemType.YELLOW_FLOWER, ItemRegistryData(Identifier.fromString("minecraft:yellow_flower")))
        register(
            ItemType.YELLOW_GLAZED_TERRACOTTA,
            ItemRegistryData(Identifier.fromString("minecraft:yellow_glazed_terracotta")),
        )
        register(
            ItemType.ZOGLIN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:zoglin_spawn_egg")),
        )
        register(
            ItemType.ZOMBIE_HORSE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:zombie_horse_spawn_egg")),
        )
        register(
            ItemType.ZOMBIE_PIGMAN_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:zombie_pigman_spawn_egg")),
        )
        register(
            ItemType.ZOMBIE_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:zombie_spawn_egg")),
        )
        register(
            ItemType.ZOMBIE_VILLAGER_SPAWN_EGG,
            ItemRegistryData(Identifier.fromString("minecraft:zombie_villager_spawn_egg")),
        )
    }

    fun initItemProperties() {
        Bootstrap::class.java.classLoader.getResourceAsStream("item_properties.json")!!.use { inputStream ->
            val inputStreamReader = InputStreamReader(inputStream)
            val itemEntries =
                jacksonObjectMapper().readValue<Map<String, Map<String, Any>>>(inputStreamReader)
            itemEntries.forEach { (identifier: String, map: Map<String, Any>) ->
                ITEM_PROPERTIES[
                    Identifier.fromString(identifier),
                ] = ItemProperties(map["max_stack_size"] as Int)
            }
        }
    }

    private fun register(itemType: ItemType, registryData: ItemRegistryData) {
        ITEMS[itemType] = registryData
        ITEMTYPE_FROM_IDENTIFIER[registryData.identifier] = itemType
        registryData.itemClass?.let {
            ITEMCLASS_FROM_ITEMTYPE[itemType] = it
        }
        val item: Item = Item.create(itemType)
        itemDefinitionRegistry.register(item.runtimeId, item.definition)
    }

    fun getItemClass(itemType: ItemType): Class<out Item> {
        return ITEMCLASS_FROM_ITEMTYPE.getValue(itemType)
    }

    fun itemClassExists(itemType: ItemType): Boolean {
        return ITEMCLASS_FROM_ITEMTYPE.containsKey(itemType)
    }

    val itemIdentifiers: List<Identifier>
        get() = ITEMS.values.stream().map { registryData: ItemRegistryData -> registryData.identifier }
            .collect(Collectors.toList())

    fun getItemType(identifier: Identifier): ItemType {
        return ITEMTYPE_FROM_IDENTIFIER.getValue(identifier)
    }

    fun hasItemType(identifier: Identifier): Boolean {
        return ITEMTYPE_FROM_IDENTIFIER.containsKey(identifier)
    }

    fun getItemRegistryData(itemType: ItemType): ItemRegistryData {
        return ITEMS.getValue(itemType)
    }

    fun getItemProperties(identifier: Identifier): ItemProperties? {
        return ITEM_PROPERTIES[identifier]
    }

    class ItemRegistryData {
        val identifier: Identifier
        var itemClass: Class<out Item>? = null
            private set

        constructor(identifier: Identifier) {
            this.identifier = identifier
        }

        constructor(identifier: Identifier, itemClass: Class<out Item>?) {
            this.identifier = identifier
            this.itemClass = itemClass
        }
    }
}
