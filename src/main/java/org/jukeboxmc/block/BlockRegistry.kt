package org.jukeboxmc.block

import com.google.gson.Gson
import org.jukeboxmc.Bootstrap
import org.jukeboxmc.block.behavior.BlockAcaciaStandingSign
import org.jukeboxmc.block.behavior.BlockAcaciaWallSign
import org.jukeboxmc.block.behavior.BlockAir
import org.jukeboxmc.block.behavior.BlockAnvil
import org.jukeboxmc.block.behavior.BlockBamboo
import org.jukeboxmc.block.behavior.BlockBarrel
import org.jukeboxmc.block.behavior.BlockBasalt
import org.jukeboxmc.block.behavior.BlockBed
import org.jukeboxmc.block.behavior.BlockBell
import org.jukeboxmc.block.behavior.BlockBirchWallSign
import org.jukeboxmc.block.behavior.BlockBlackStoneSlab
import org.jukeboxmc.block.behavior.BlockBlackWool
import org.jukeboxmc.block.behavior.BlockBlastFurnace
import org.jukeboxmc.block.behavior.BlockBlueWool
import org.jukeboxmc.block.behavior.BlockBrewingStand
import org.jukeboxmc.block.behavior.BlockBrownWool
import org.jukeboxmc.block.behavior.BlockButton
import org.jukeboxmc.block.behavior.BlockCampfire
import org.jukeboxmc.block.behavior.BlockCandle
import org.jukeboxmc.block.behavior.BlockCarpet
import org.jukeboxmc.block.behavior.BlockCartographyTable
import org.jukeboxmc.block.behavior.BlockCauldron
import org.jukeboxmc.block.behavior.BlockChain
import org.jukeboxmc.block.behavior.BlockChest
import org.jukeboxmc.block.behavior.BlockCoalOre
import org.jukeboxmc.block.behavior.BlockCobbledDeepslateSlab
import org.jukeboxmc.block.behavior.BlockCobblestoneWall
import org.jukeboxmc.block.behavior.BlockCompartor
import org.jukeboxmc.block.behavior.BlockComposter
import org.jukeboxmc.block.behavior.BlockConcrete
import org.jukeboxmc.block.behavior.BlockConcretePowder
import org.jukeboxmc.block.behavior.BlockCopperOre
import org.jukeboxmc.block.behavior.BlockCoral
import org.jukeboxmc.block.behavior.BlockCoralBlock
import org.jukeboxmc.block.behavior.BlockCoralFan
import org.jukeboxmc.block.behavior.BlockCoralFanDead
import org.jukeboxmc.block.behavior.BlockCraftingTable
import org.jukeboxmc.block.behavior.BlockCrimsonHyphae
import org.jukeboxmc.block.behavior.BlockCrimsonSlab
import org.jukeboxmc.block.behavior.BlockCrimsonStandingSign
import org.jukeboxmc.block.behavior.BlockCrimsonStem
import org.jukeboxmc.block.behavior.BlockCrimsonWallSign
import org.jukeboxmc.block.behavior.BlockCutCopperSlab
import org.jukeboxmc.block.behavior.BlockCyanWool
import org.jukeboxmc.block.behavior.BlockDarkOakStandingSign
import org.jukeboxmc.block.behavior.BlockDarkOakWallSign
import org.jukeboxmc.block.behavior.BlockDaylightDetector
import org.jukeboxmc.block.behavior.BlockDaylightDetectorInverted
import org.jukeboxmc.block.behavior.BlockDeepslateBrickSlab
import org.jukeboxmc.block.behavior.BlockDeepslateTileSlab
import org.jukeboxmc.block.behavior.BlockDiamondOre
import org.jukeboxmc.block.behavior.BlockDirt
import org.jukeboxmc.block.behavior.BlockDispenser
import org.jukeboxmc.block.behavior.BlockDoor
import org.jukeboxmc.block.behavior.BlockDoubleBlackStoneSlab
import org.jukeboxmc.block.behavior.BlockDoubleCobbledDeepslateSlab
import org.jukeboxmc.block.behavior.BlockDoubleCrimsonSlab
import org.jukeboxmc.block.behavior.BlockDoubleCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleDeepslateBrickSlab
import org.jukeboxmc.block.behavior.BlockDoubleDeepslateTileSlab
import org.jukeboxmc.block.behavior.BlockDoubleExposedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleMangroveSlab
import org.jukeboxmc.block.behavior.BlockDoubleMudBrickSlab
import org.jukeboxmc.block.behavior.BlockDoubleOxidizedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoublePlant
import org.jukeboxmc.block.behavior.BlockDoublePolishedBlackstoneBrickSlab
import org.jukeboxmc.block.behavior.BlockDoublePolishedBlackstoneSlab
import org.jukeboxmc.block.behavior.BlockDoublePolishedDeepslateSlab
import org.jukeboxmc.block.behavior.BlockDoubleStoneSlab
import org.jukeboxmc.block.behavior.BlockDoubleStoneSlab2
import org.jukeboxmc.block.behavior.BlockDoubleStoneSlab3
import org.jukeboxmc.block.behavior.BlockDoubleStoneSlab4
import org.jukeboxmc.block.behavior.BlockDoubleWarpedSlab
import org.jukeboxmc.block.behavior.BlockDoubleWaxedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleWaxedExposedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleWaxedOxidizedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleWaxedWeatheredCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleWeatheredCutCopperSlab
import org.jukeboxmc.block.behavior.BlockDoubleWoodenSlab
import org.jukeboxmc.block.behavior.BlockDropper
import org.jukeboxmc.block.behavior.BlockEmeraldOre
import org.jukeboxmc.block.behavior.BlockEnchantingTable
import org.jukeboxmc.block.behavior.BlockEndPortalFrame
import org.jukeboxmc.block.behavior.BlockEndRod
import org.jukeboxmc.block.behavior.BlockEnderChest
import org.jukeboxmc.block.behavior.BlockExposedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockFence
import org.jukeboxmc.block.behavior.BlockFenceGate
import org.jukeboxmc.block.behavior.BlockFurnace
import org.jukeboxmc.block.behavior.BlockGoldOre
import org.jukeboxmc.block.behavior.BlockGrass
import org.jukeboxmc.block.behavior.BlockGravel
import org.jukeboxmc.block.behavior.BlockGrayWool
import org.jukeboxmc.block.behavior.BlockGreenWool
import org.jukeboxmc.block.behavior.BlockGrindstone
import org.jukeboxmc.block.behavior.BlockHangingRoots
import org.jukeboxmc.block.behavior.BlockHopper
import org.jukeboxmc.block.behavior.BlockIronOre
import org.jukeboxmc.block.behavior.BlockJungleStandingSign
import org.jukeboxmc.block.behavior.BlockJungleWallSign
import org.jukeboxmc.block.behavior.BlockKelp
import org.jukeboxmc.block.behavior.BlockLadder
import org.jukeboxmc.block.behavior.BlockLantern
import org.jukeboxmc.block.behavior.BlockLapisOre
import org.jukeboxmc.block.behavior.BlockLava
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLeaves2
import org.jukeboxmc.block.behavior.BlockLectern
import org.jukeboxmc.block.behavior.BlockLever
import org.jukeboxmc.block.behavior.BlockLightBlueWool
import org.jukeboxmc.block.behavior.BlockLightningRod
import org.jukeboxmc.block.behavior.BlockLimeWool
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.behavior.BlockLog2
import org.jukeboxmc.block.behavior.BlockMagentaWool
import org.jukeboxmc.block.behavior.BlockMangroveLog
import org.jukeboxmc.block.behavior.BlockMangroveSlab
import org.jukeboxmc.block.behavior.BlockMangroveStandingSign
import org.jukeboxmc.block.behavior.BlockMangroveWallSign
import org.jukeboxmc.block.behavior.BlockMangroveWood
import org.jukeboxmc.block.behavior.BlockMudBrickSlab
import org.jukeboxmc.block.behavior.BlockMuddyMangroveRoots
import org.jukeboxmc.block.behavior.BlockNetherGoldOre
import org.jukeboxmc.block.behavior.BlockNetherWart
import org.jukeboxmc.block.behavior.BlockOakStandingSign
import org.jukeboxmc.block.behavior.BlockOakWallSign
import org.jukeboxmc.block.behavior.BlockOrangeWool
import org.jukeboxmc.block.behavior.BlockOxidizedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockPinkWool
import org.jukeboxmc.block.behavior.BlockPiston
import org.jukeboxmc.block.behavior.BlockPlanks
import org.jukeboxmc.block.behavior.BlockPointedDripstone
import org.jukeboxmc.block.behavior.BlockPolishedBasalt
import org.jukeboxmc.block.behavior.BlockPolishedBlackstoneBrickSlab
import org.jukeboxmc.block.behavior.BlockPolishedBlackstoneSlab
import org.jukeboxmc.block.behavior.BlockPolishedDeepslateSlab
import org.jukeboxmc.block.behavior.BlockPressurePlate
import org.jukeboxmc.block.behavior.BlockPrismarine
import org.jukeboxmc.block.behavior.BlockPurpleWool
import org.jukeboxmc.block.behavior.BlockPurpurBlock
import org.jukeboxmc.block.behavior.BlockQuartzBlock
import org.jukeboxmc.block.behavior.BlockQuartzOre
import org.jukeboxmc.block.behavior.BlockRail
import org.jukeboxmc.block.behavior.BlockRedFlower
import org.jukeboxmc.block.behavior.BlockRedSandstone
import org.jukeboxmc.block.behavior.BlockRedWool
import org.jukeboxmc.block.behavior.BlockRedstoneOre
import org.jukeboxmc.block.behavior.BlockRepeater
import org.jukeboxmc.block.behavior.BlockSand
import org.jukeboxmc.block.behavior.BlockSandstone
import org.jukeboxmc.block.behavior.BlockSapling
import org.jukeboxmc.block.behavior.BlockSeaPickle
import org.jukeboxmc.block.behavior.BlockSeagrass
import org.jukeboxmc.block.behavior.BlockShulkerBox
import org.jukeboxmc.block.behavior.BlockSilverWool
import org.jukeboxmc.block.behavior.BlockSkull
import org.jukeboxmc.block.behavior.BlockSmithingTable
import org.jukeboxmc.block.behavior.BlockSmoker
import org.jukeboxmc.block.behavior.BlockSnowLayer
import org.jukeboxmc.block.behavior.BlockSponge
import org.jukeboxmc.block.behavior.BlockSpruceStandingSign
import org.jukeboxmc.block.behavior.BlockSpruceWallSign
import org.jukeboxmc.block.behavior.BlockStainedGlass
import org.jukeboxmc.block.behavior.BlockStainedGlassPane
import org.jukeboxmc.block.behavior.BlockStainedHardenedClay
import org.jukeboxmc.block.behavior.BlockStairs
import org.jukeboxmc.block.behavior.BlockStandingBanner
import org.jukeboxmc.block.behavior.BlockStickyPiston
import org.jukeboxmc.block.behavior.BlockStippedLog
import org.jukeboxmc.block.behavior.BlockStone
import org.jukeboxmc.block.behavior.BlockStoneSlab
import org.jukeboxmc.block.behavior.BlockStoneSlab2
import org.jukeboxmc.block.behavior.BlockStoneSlab3
import org.jukeboxmc.block.behavior.BlockStoneSlab4
import org.jukeboxmc.block.behavior.BlockStonebrick
import org.jukeboxmc.block.behavior.BlockStonecutterBlock
import org.jukeboxmc.block.behavior.BlockStrippedCrimsonHyphae
import org.jukeboxmc.block.behavior.BlockStrippedMangroveWood
import org.jukeboxmc.block.behavior.BlockStrippedWarpedHyphae
import org.jukeboxmc.block.behavior.BlockSugarCane
import org.jukeboxmc.block.behavior.BlockTallGrass
import org.jukeboxmc.block.behavior.BlockTorch
import org.jukeboxmc.block.behavior.BlockTrapdoor
import org.jukeboxmc.block.behavior.BlockTrappedChest
import org.jukeboxmc.block.behavior.BlockTripwireHook
import org.jukeboxmc.block.behavior.BlockUndyedShulkerBox
import org.jukeboxmc.block.behavior.BlockVine
import org.jukeboxmc.block.behavior.BlockWall
import org.jukeboxmc.block.behavior.BlockWallBanner
import org.jukeboxmc.block.behavior.BlockWarpedHyphae
import org.jukeboxmc.block.behavior.BlockWarpedSlab
import org.jukeboxmc.block.behavior.BlockWarpedStandingSign
import org.jukeboxmc.block.behavior.BlockWarpedStem
import org.jukeboxmc.block.behavior.BlockWarpedWallSign
import org.jukeboxmc.block.behavior.BlockWater
import org.jukeboxmc.block.behavior.BlockWaterlily
import org.jukeboxmc.block.behavior.BlockWaxedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockWaxedExposedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockWaxedOxidizedCutCopperSlab
import org.jukeboxmc.block.behavior.BlockWaxedWeatheredCutCopperSlab
import org.jukeboxmc.block.behavior.BlockWeatheredCutCopperSlab
import org.jukeboxmc.block.behavior.BlockWhiteWool
import org.jukeboxmc.block.behavior.BlockWood
import org.jukeboxmc.block.behavior.BlockWoodenFence
import org.jukeboxmc.block.behavior.BlockWoodenSlab
import org.jukeboxmc.block.behavior.BlockYellowWool
import org.jukeboxmc.block.data.BlockProperties
import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType
import org.jukeboxmc.util.Identifier
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Objects

/**
 * @author LucGamesYT
 * @version 1.0
 */
object BlockRegistry {
    private val IDENTIFIER_FROM_BLOCKTYPE: MutableMap<BlockType?, Identifier> = LinkedHashMap()
    private val BLOCKTYPE_FROM_IDENTIFIER: MutableMap<Identifier?, BlockType> = LinkedHashMap()
    private val BLOCK_PROPERTIES: MutableMap<Identifier?, BlockProperties> = LinkedHashMap()
    private val BLOCKCLASS_FROM_BLOCKTYPE: MutableMap<BlockType?, Class<out Block>> = LinkedHashMap()
    fun init() {
        register(
            BlockType.ACACIA_BUTTON,
            Identifier.fromString("minecraft:acacia_button"),
            BlockButton::class.java,
        )
        register(BlockType.ACACIA_DOOR, Identifier.fromString("minecraft:acacia_door"), BlockDoor::class.java)
        register(
            BlockType.ACACIA_FENCE_GATE,
            Identifier.fromString("minecraft:acacia_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(
            BlockType.ACACIA_PRESSURE_PLATE,
            Identifier.fromString("minecraft:acacia_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.ACACIA_STAIRS,
            Identifier.fromString("minecraft:acacia_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.ACACIA_STANDING_SIGN,
            Identifier.fromString("minecraft:acacia_standing_sign"),
            BlockAcaciaStandingSign::class.java,
        )
        register(
            BlockType.ACACIA_TRAPDOOR,
            Identifier.fromString("minecraft:acacia_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.ACACIA_WALL_SIGN,
            Identifier.fromString("minecraft:acacia_wall_sign"),
            BlockAcaciaWallSign::class.java,
        )
        register(
            BlockType.ACTIVATOR_RAIL,
            Identifier.fromString("minecraft:activator_rail"),
            BlockRail::class.java,
        )
        register(BlockType.AIR, Identifier.fromString("minecraft:air"), BlockAir::class.java)
        register(BlockType.ALLOW, Identifier.fromString("minecraft:allow"))
        register(BlockType.AMETHYST_BLOCK, Identifier.fromString("minecraft:amethyst_block"))
        register(BlockType.AMETHYST_CLUSTER, Identifier.fromString("minecraft:amethyst_cluster"))
        register(BlockType.ANCIENT_DEBRIS, Identifier.fromString("minecraft:ancient_debris"))
        register(
            BlockType.ANDESITE_STAIRS,
            Identifier.fromString("minecraft:andesite_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.ANVIL, Identifier.fromString("minecraft:anvil"), BlockAnvil::class.java)
        register(BlockType.AZALEA, Identifier.fromString("minecraft:azalea"))
        register(BlockType.AZALEA_LEAVES, Identifier.fromString("minecraft:azalea_leaves"))
        register(BlockType.AZALEA_LEAVES_FLOWERED, Identifier.fromString("minecraft:azalea_leaves_flowered"))
        register(BlockType.BAMBOO, Identifier.fromString("minecraft:bamboo"), BlockBamboo::class.java)
        register(BlockType.BAMBOO_SAPLING, Identifier.fromString("minecraft:bamboo_sapling"))
        register(BlockType.BARREL, Identifier.fromString("minecraft:barrel"), BlockBarrel::class.java)
        register(BlockType.BARRIER, Identifier.fromString("minecraft:barrier"))
        register(BlockType.BASALT, Identifier.fromString("minecraft:basalt"), BlockBasalt::class.java)
        register(BlockType.BEACON, Identifier.fromString("minecraft:beacon"))
        register(BlockType.BED, Identifier.fromString("minecraft:bed"), BlockBed::class.java)
        register(BlockType.BEDROCK, Identifier.fromString("minecraft:bedrock"))
        register(BlockType.BEEHIVE, Identifier.fromString("minecraft:beehive"))
        register(BlockType.BEETROOT, Identifier.fromString("minecraft:beetroot"))
        register(BlockType.BEE_NEST, Identifier.fromString("minecraft:bee_nest"))
        register(BlockType.BELL, Identifier.fromString("minecraft:bell"), BlockBell::class.java)
        register(BlockType.BIG_DRIPLEAF, Identifier.fromString("minecraft:big_dripleaf"))
        register(
            BlockType.BIRCH_BUTTON,
            Identifier.fromString("minecraft:birch_button"),
            BlockButton::class.java,
        )
        register(BlockType.BIRCH_DOOR, Identifier.fromString("minecraft:birch_door"), BlockDoor::class.java)
        register(
            BlockType.BIRCH_FENCE_GATE,
            Identifier.fromString("minecraft:birch_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(
            BlockType.BIRCH_PRESSURE_PLATE,
            Identifier.fromString("minecraft:birch_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.BIRCH_STAIRS,
            Identifier.fromString("minecraft:birch_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.BIRCH_STANDING_SIGN,
            Identifier.fromString("minecraft:birch_standing_sign"),
            BlockAcaciaStandingSign::class.java,
        )
        register(
            BlockType.BIRCH_TRAPDOOR,
            Identifier.fromString("minecraft:birch_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.BIRCH_WALL_SIGN,
            Identifier.fromString("minecraft:birch_wall_sign"),
            BlockBirchWallSign::class.java,
        )
        register(BlockType.BLACKSTONE, Identifier.fromString("minecraft:blackstone"))
        register(
            BlockType.BLACKSTONE_DOUBLE_SLAB,
            Identifier.fromString("minecraft:blackstone_double_slab"),
            BlockDoubleBlackStoneSlab::class.java,
        )
        register(
            BlockType.BLACKSTONE_SLAB,
            Identifier.fromString("minecraft:blackstone_slab"),
            BlockBlackStoneSlab::class.java,
        )
        register(
            BlockType.BLACKSTONE_STAIRS,
            Identifier.fromString("minecraft:blackstone_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.BLACKSTONE_WALL,
            Identifier.fromString("minecraft:blackstone_wall"),
            BlockWall::class.java,
        )
        register(
            BlockType.BLACK_CANDLE,
            Identifier.fromString("minecraft:black_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.BLACK_CANDLE_CAKE, Identifier.fromString("minecraft:black_candle_cake"))
        register(
            BlockType.BLACK_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:black_glazed_terracotta"),
        )
        register(
            BlockType.BLAST_FURNACE,
            Identifier.fromString("minecraft:blast_furnace"),
            BlockBlastFurnace::class.java,
        )
        register(
            BlockType.BLUE_CANDLE,
            Identifier.fromString("minecraft:blue_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.BLUE_CANDLE_CAKE, Identifier.fromString("minecraft:blue_candle_cake"))
        register(BlockType.BLUE_GLAZED_TERRACOTTA, Identifier.fromString("minecraft:blue_glazed_terracotta"))
        register(BlockType.BLUE_ICE, Identifier.fromString("minecraft:blue_ice"))
        register(BlockType.BONE_BLOCK, Identifier.fromString("minecraft:bone_block"))
        register(BlockType.BOOKSHELF, Identifier.fromString("minecraft:bookshelf"))
        register(BlockType.BORDER_BLOCK, Identifier.fromString("minecraft:border_block"))
        register(
            BlockType.BREWING_STAND,
            Identifier.fromString("minecraft:brewing_stand"),
            BlockBrewingStand::class.java,
        )
        register(BlockType.BRICK_BLOCK, Identifier.fromString("minecraft:brick_block"))
        register(
            BlockType.BRICK_STAIRS,
            Identifier.fromString("minecraft:brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.BROWN_CANDLE,
            Identifier.fromString("minecraft:brown_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.BROWN_CANDLE_CAKE, Identifier.fromString("minecraft:brown_candle_cake"))
        register(
            BlockType.BROWN_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:brown_glazed_terracotta"),
        )
        register(BlockType.BROWN_MUSHROOM, Identifier.fromString("minecraft:brown_mushroom"))
        register(BlockType.BROWN_MUSHROOM_BLOCK, Identifier.fromString("minecraft:brown_mushroom_block"))
        register(BlockType.BUBBLE_COLUMN, Identifier.fromString("minecraft:bubble_column"))
        register(BlockType.BUDDING_AMETHYST, Identifier.fromString("minecraft:budding_amethyst"))
        register(BlockType.CACTUS, Identifier.fromString("minecraft:cactus"))
        register(BlockType.CAKE, Identifier.fromString("minecraft:cake"))
        register(BlockType.CALCITE, Identifier.fromString("minecraft:calcite"))
        register(BlockType.CAMERA, Identifier.fromString("minecraft:camera"))
        register(BlockType.CAMPFIRE, Identifier.fromString("minecraft:campfire"), BlockCampfire::class.java)
        register(BlockType.CANDLE, Identifier.fromString("minecraft:candle"), BlockCandle::class.java)
        register(BlockType.CANDLE_CAKE, Identifier.fromString("minecraft:candle_cake"))
        register(BlockType.CARPET, Identifier.fromString("minecraft:carpet"), BlockCarpet::class.java)
        register(BlockType.CARROTS, Identifier.fromString("minecraft:carrots"))
        register(
            BlockType.CARTOGRAPHY_TABLE,
            Identifier.fromString("minecraft:cartography_table"),
            BlockCartographyTable::class.java,
        )
        register(BlockType.CARVED_PUMPKIN, Identifier.fromString("minecraft:carved_pumpkin"))
        register(BlockType.CAULDRON, Identifier.fromString("minecraft:cauldron"), BlockCauldron::class.java)
        register(BlockType.CAVE_VINES, Identifier.fromString("minecraft:cave_vines"))
        register(
            BlockType.CAVE_VINES_BODY_WITH_BERRIES,
            Identifier.fromString("minecraft:cave_vines_body_with_berries"),
        )
        register(
            BlockType.CAVE_VINES_HEAD_WITH_BERRIES,
            Identifier.fromString("minecraft:cave_vines_head_with_berries"),
        )
        register(BlockType.CHAIN, Identifier.fromString("minecraft:chain"), BlockChain::class.java)
        register(BlockType.CHAIN_COMMAND_BLOCK, Identifier.fromString("minecraft:chain_command_block"))
        register(BlockType.CHEMICAL_HEAT, Identifier.fromString("minecraft:chemical_heat"))
        register(BlockType.CHEMISTRY_TABLE, Identifier.fromString("minecraft:chemistry_table"))
        register(BlockType.CHEST, Identifier.fromString("minecraft:chest"), BlockChest::class.java)
        register(BlockType.CHISELED_DEEPSLATE, Identifier.fromString("minecraft:chiseled_deepslate"))
        register(BlockType.CHISELED_NETHER_BRICKS, Identifier.fromString("minecraft:chiseled_nether_bricks"))
        register(
            BlockType.CHISELED_POLISHED_BLACKSTONE,
            Identifier.fromString("minecraft:chiseled_polished_blackstone"),
        )
        register(BlockType.CHORUS_FLOWER, Identifier.fromString("minecraft:chorus_flower"))
        register(BlockType.CHORUS_PLANT, Identifier.fromString("minecraft:chorus_plant"))
        register(BlockType.CLAY, Identifier.fromString("minecraft:clay"))
        register(
            BlockType.CLIENT_REQUEST_PLACEHOLDER_BLOCK,
            Identifier.fromString("minecraft:client_request_placeholder_block"),
        )
        register(BlockType.COAL_BLOCK, Identifier.fromString("minecraft:coal_block"))
        register(BlockType.COAL_ORE, Identifier.fromString("minecraft:coal_ore"), BlockCoalOre::class.java)
        register(BlockType.COBBLED_DEEPSLATE, Identifier.fromString("minecraft:cobbled_deepslate"))
        register(
            BlockType.COBBLED_DEEPSLATE_DOUBLE_SLAB,
            Identifier.fromString("minecraft:cobbled_deepslate_double_slab"),
            BlockDoubleCobbledDeepslateSlab::class.java,
        )
        register(
            BlockType.COBBLED_DEEPSLATE_SLAB,
            Identifier.fromString("minecraft:cobbled_deepslate_slab"),
            BlockCobbledDeepslateSlab::class.java,
        )
        register(
            BlockType.COBBLED_DEEPSLATE_STAIRS,
            Identifier.fromString("minecraft:cobbled_deepslate_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.COBBLED_DEEPSLATE_WALL,
            Identifier.fromString(" minecraft:cobbled_deepslate_wall"),
            BlockWall::class.java,
        )
        register(BlockType.COBBLESTONE, Identifier.fromString("minecraft:cobblestone"))
        register(
            BlockType.COBBLESTONE_WALL,
            Identifier.fromString("minecraft:cobblestone_wall"),
            BlockCobblestoneWall::class.java,
        )
        register(BlockType.COCOA, Identifier.fromString("minecraft:cocoa"))
        register(BlockType.COLORED_TORCH_BP, Identifier.fromString("minecraft:colored_torch_bp"))
        register(BlockType.COLORED_TORCH_RG, Identifier.fromString("minecraft:colored_torch_rg"))
        register(BlockType.COMMAND_BLOCK, Identifier.fromString("minecraft:command_block"))
        register(
            BlockType.COMPOSTER,
            Identifier.fromString("minecraft:composter"),
            BlockComposter::class.java,
        )
        register(BlockType.CONCRETE, Identifier.fromString("minecraft:concrete"), BlockConcrete::class.java)
        register(
            BlockType.CONCRETE_POWDER,
            Identifier.fromString("minecraft:concrete_powder"),
            BlockConcretePowder::class.java,
        )
        register(BlockType.CONDUIT, Identifier.fromString("minecraft:conduit"))
        register(BlockType.COPPER_BLOCK, Identifier.fromString("minecraft:copper_block"))
        register(
            BlockType.COPPER_ORE,
            Identifier.fromString("minecraft:copper_ore"),
            BlockCopperOre::class.java,
        )
        register(BlockType.CORAL, Identifier.fromString("minecraft:coral"), BlockCoral::class.java)
        register(
            BlockType.CORAL_BLOCK,
            Identifier.fromString("minecraft:coral_block"),
            BlockCoralBlock::class.java,
        )
        register(BlockType.CORAL_FAN, Identifier.fromString("minecraft:coral_fan"), BlockCoralFan::class.java)
        register(
            BlockType.CORAL_FAN_DEAD,
            Identifier.fromString("minecraft:coral_fan_dead"),
            BlockCoralFanDead::class.java,
        )
        register(BlockType.CORAL_FAN_HANG, Identifier.fromString("minecraft:coral_fan_hang"))
        register(BlockType.CORAL_FAN_HANG2, Identifier.fromString("minecraft:coral_fan_hang2"))
        register(BlockType.CORAL_FAN_HANG3, Identifier.fromString("minecraft:coral_fan_hang3"))
        register(
            BlockType.CRACKED_DEEPSLATE_BRICKS,
            Identifier.fromString("minecraft:cracked_deepslate_bricks"),
        )
        register(
            BlockType.CRACKED_DEEPSLATE_TILES,
            Identifier.fromString("minecraft:cracked_deepslate_tiles"),
        )
        register(BlockType.CRACKED_NETHER_BRICKS, Identifier.fromString("minecraft:cracked_nether_bricks"))
        register(
            BlockType.CRACKED_POLISHED_BLACKSTONE_BRICKS,
            Identifier.fromString("minecraft:cracked_polished_blackstone_bricks"),
        )
        register(
            BlockType.CRAFTING_TABLE,
            Identifier.fromString("minecraft:crafting_table"),
            BlockCraftingTable::class.java,
        )
        register(
            BlockType.CRIMSON_BUTTON,
            Identifier.fromString("minecraft:crimson_button"),
            BlockButton::class.java,
        )
        register(
            BlockType.CRIMSON_DOOR,
            Identifier.fromString("minecraft:crimson_door"),
            BlockDoor::class.java,
        )
        register(
            BlockType.CRIMSON_DOUBLE_SLAB,
            Identifier.fromString("minecraft:crimson_double_slab"),
            BlockDoubleCrimsonSlab::class.java,
        )
        register(
            BlockType.CRIMSON_FENCE,
            Identifier.fromString("minecraft:crimson_fence"),
            BlockFence::class.java,
        )
        register(
            BlockType.CRIMSON_FENCE_GATE,
            Identifier.fromString("minecraft:crimson_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(BlockType.CRIMSON_FUNGUS, Identifier.fromString("minecraft:crimson_fungus"))
        register(
            BlockType.CRIMSON_HYPHAE,
            Identifier.fromString("minecraft:crimson_hyphae"),
            BlockCrimsonHyphae::class.java,
        )
        register(BlockType.CRIMSON_NYLIUM, Identifier.fromString("minecraft:crimson_nylium"))
        register(BlockType.CRIMSON_PLANKS, Identifier.fromString("minecraft:crimson_planks"))
        register(
            BlockType.CRIMSON_PRESSURE_PLATE,
            Identifier.fromString("minecraft:crimson_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(BlockType.CRIMSON_ROOTS, Identifier.fromString("minecraft:crimson_roots"))
        register(
            BlockType.CRIMSON_SLAB,
            Identifier.fromString("minecraft:crimson_slab"),
            BlockCrimsonSlab::class.java,
        )
        register(
            BlockType.CRIMSON_STAIRS,
            Identifier.fromString("minecraft:crimson_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.CRIMSON_STANDING_SIGN,
            Identifier.fromString("minecraft:crimson_standing_sign"),
            BlockCrimsonStandingSign::class.java,
        )
        register(
            BlockType.CRIMSON_STEM,
            Identifier.fromString("minecraft:crimson_stem"),
            BlockCrimsonStem::class.java,
        )
        register(
            BlockType.CRIMSON_TRAPDOOR,
            Identifier.fromString("minecraft:crimson_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.CRIMSON_WALL_SIGN,
            Identifier.fromString("minecraft:crimson_wall_sign"),
            BlockCrimsonWallSign::class.java,
        )
        register(BlockType.CRYING_OBSIDIAN, Identifier.fromString("minecraft:crying_obsidian"))
        register(BlockType.CUT_COPPER, Identifier.fromString("minecraft:cut_copper"))
        register(
            BlockType.CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:cut_copper_slab"),
            BlockCutCopperSlab::class.java,
        )
        register(
            BlockType.CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.CYAN_CANDLE,
            Identifier.fromString("minecraft:cyan_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.CYAN_CANDLE_CAKE, Identifier.fromString("minecraft:cyan_candle_cake"))
        register(BlockType.CYAN_GLAZED_TERRACOTTA, Identifier.fromString("minecraft:cyan_glazed_terracotta"))
        register(
            BlockType.DARKOAK_STANDING_SIGN,
            Identifier.fromString("minecraft:darkoak_standing_sign"),
            BlockDarkOakStandingSign::class.java,
        )
        register(
            BlockType.DARKOAK_WALL_SIGN,
            Identifier.fromString("minecraft:darkoak_wall_sign"),
            BlockDarkOakWallSign::class.java,
        )
        register(
            BlockType.DARK_OAK_BUTTON,
            Identifier.fromString("minecraft:dark_oak_button"),
            BlockButton::class.java,
        )
        register(
            BlockType.DARK_OAK_DOOR,
            Identifier.fromString("minecraft:dark_oak_door"),
            BlockDoor::class.java,
        )
        register(
            BlockType.DARK_OAK_FENCE_GATE,
            Identifier.fromString("minecraft:dark_oak_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(
            BlockType.DARK_OAK_PRESSURE_PLATE,
            Identifier.fromString("minecraft:dark_oak_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.DARK_OAK_STAIRS,
            Identifier.fromString("minecraft:dark_oak_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.DARK_OAK_TRAPDOOR,
            Identifier.fromString("minecraft:dark_oak_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.DARK_PRISMARINE_STAIRS,
            Identifier.fromString("minecraft:dark_prismarine_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.DAYLIGHT_DETECTOR,
            Identifier.fromString("minecraft:daylight_detector"),
            BlockDaylightDetector::class.java,
        )
        register(
            BlockType.DAYLIGHT_DETECTOR_INVERTED,
            Identifier.fromString("minecraft:daylight_detector_inverted"),
            BlockDaylightDetectorInverted::class.java,
        )
        register(BlockType.DEADBUSH, Identifier.fromString("minecraft:deadbush"))
        register(BlockType.DEEPSLATE, Identifier.fromString("minecraft:deepslate"))
        register(BlockType.DEEPSLATE_BRICKS, Identifier.fromString("minecraft:deepslate_bricks"))
        register(
            BlockType.DEEPSLATE_BRICK_DOUBLE_SLAB,
            Identifier.fromString("minecraft:deepslate_brick_double_slab"),
            BlockDoubleDeepslateBrickSlab::class.java,
        )
        register(
            BlockType.DEEPSLATE_BRICK_SLAB,
            Identifier.fromString("minecraft:deepslate_brick_slab"),
            BlockDeepslateBrickSlab::class.java,
        )
        register(
            BlockType.DEEPSLATE_BRICK_STAIRS,
            Identifier.fromString("minecraft:deepslate_brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.DEEPSLATE_BRICK_WALL,
            Identifier.fromString("minecraft:deepslate_brick_wall"),
            BlockWall::class.java,
        )
        register(
            BlockType.DEEPSLATE_COAL_ORE,
            Identifier.fromString("minecraft:deepslate_coal_ore"),
            BlockCoalOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_COPPER_ORE,
            Identifier.fromString("minecraft:deepslate_copper_ore"),
            BlockCopperOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_DIAMOND_ORE,
            Identifier.fromString("minecraft:deepslate_diamond_ore"),
            BlockDiamondOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_EMERALD_ORE,
            Identifier.fromString("minecraft:deepslate_emerald_ore"),
            BlockEmeraldOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_GOLD_ORE,
            Identifier.fromString("minecraft:deepslate_gold_ore"),
            BlockGoldOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_IRON_ORE,
            Identifier.fromString("minecraft:deepslate_iron_ore"),
            BlockIronOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_LAPIS_ORE,
            Identifier.fromString("minecraft:deepslate_lapis_ore"),
            BlockLapisOre::class.java,
        )
        register(
            BlockType.DEEPSLATE_REDSTONE_ORE,
            Identifier.fromString("minecraft:deepslate_redstone_ore"),
            BlockRedstoneOre::class.java,
        )
        register(BlockType.DEEPSLATE_TILES, Identifier.fromString("minecraft:deepslate_tiles"))
        register(
            BlockType.DEEPSLATE_TILE_DOUBLE_SLAB,
            Identifier.fromString("minecraft:deepslate_tile_double_slab"),
            BlockDoubleDeepslateTileSlab::class.java,
        )
        register(
            BlockType.DEEPSLATE_TILE_SLAB,
            Identifier.fromString("minecraft:deepslate_tile_slab"),
            BlockDeepslateTileSlab::class.java,
        )
        register(
            BlockType.DEEPSLATE_TILE_STAIRS,
            Identifier.fromString("minecraft:deepslate_tile_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.DEEPSLATE_TILE_WALL,
            Identifier.fromString("minecraft:deepslate_tile_wall"),
            BlockWall::class.java,
        )
        register(BlockType.DENY, Identifier.fromString("minecraft:deny"))
        register(
            BlockType.DETECTOR_RAIL,
            Identifier.fromString("minecraft:detector_rail"),
            BlockRail::class.java,
        )
        register(BlockType.DIAMOND_BLOCK, Identifier.fromString("minecraft:diamond_block"))
        register(
            BlockType.DIAMOND_ORE,
            Identifier.fromString("minecraft:diamond_ore"),
            BlockDiamondOre::class.java,
        )
        register(
            BlockType.DIORITE_STAIRS,
            Identifier.fromString("minecraft:diorite_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.DIRT, Identifier.fromString("minecraft:dirt"), BlockDirt::class.java)
        register(BlockType.DIRT_WITH_ROOTS, Identifier.fromString("minecraft:dirt_with_roots"))
        register(
            BlockType.DISPENSER,
            Identifier.fromString("minecraft:dispenser"),
            BlockDispenser::class.java,
        )
        register(
            BlockType.DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:double_cut_copper_slab"),
            BlockDoubleCutCopperSlab::class.java,
        )
        register(
            BlockType.DOUBLE_PLANT,
            Identifier.fromString("minecraft:double_plant"),
            BlockDoublePlant::class.java,
        )
        register(
            BlockType.DOUBLE_STONE_BLOCK_SLAB,
            Identifier.fromString("minecraft:double_stone_block_slab"),
            BlockDoubleStoneSlab::class.java,
        )
        register(
            BlockType.DOUBLE_STONE_BLOCK_SLAB2,
            Identifier.fromString("minecraft:double_stone_block_slab2"),
            BlockDoubleStoneSlab2::class.java,
        )
        register(
            BlockType.DOUBLE_STONE_BLOCK_SLAB3,
            Identifier.fromString("minecraft:double_stone_block_slab3"),
            BlockDoubleStoneSlab3::class.java,
        )
        register(
            BlockType.DOUBLE_STONE_BLOCK_SLAB4,
            Identifier.fromString("minecraft:double_stone_block_slab4"),
            BlockDoubleStoneSlab4::class.java,
        )
        register(
            BlockType.DOUBLE_WOODEN_SLAB,
            Identifier.fromString("minecraft:double_wooden_slab"),
            BlockDoubleWoodenSlab::class.java,
        )
        register(BlockType.DRAGON_EGG, Identifier.fromString("minecraft:dragon_egg"))
        register(BlockType.DRIED_KELP_BLOCK, Identifier.fromString("minecraft:dried_kelp_block"))
        register(BlockType.DRIPSTONE_BLOCK, Identifier.fromString("minecraft:dripstone_block"))
        register(BlockType.DROPPER, Identifier.fromString("minecraft:dropper"), BlockDropper::class.java)
        register(BlockType.ELEMENT_0, Identifier.fromString("minecraft:element_0"))
        register(BlockType.ELEMENT_1, Identifier.fromString("minecraft:element_1"))
        register(BlockType.ELEMENT_10, Identifier.fromString("minecraft:element_10"))
        register(BlockType.ELEMENT_100, Identifier.fromString("minecraft:element_100"))
        register(BlockType.ELEMENT_101, Identifier.fromString("minecraft:element_101"))
        register(BlockType.ELEMENT_102, Identifier.fromString("minecraft:element_102"))
        register(BlockType.ELEMENT_103, Identifier.fromString("minecraft:element_103"))
        register(BlockType.ELEMENT_104, Identifier.fromString("minecraft:element_104"))
        register(BlockType.ELEMENT_105, Identifier.fromString("minecraft:element_105"))
        register(BlockType.ELEMENT_106, Identifier.fromString("minecraft:element_106"))
        register(BlockType.ELEMENT_107, Identifier.fromString("minecraft:element_107"))
        register(BlockType.ELEMENT_108, Identifier.fromString("minecraft:element_108"))
        register(BlockType.ELEMENT_109, Identifier.fromString("minecraft:element_109"))
        register(BlockType.ELEMENT_11, Identifier.fromString("minecraft:element_11"))
        register(BlockType.ELEMENT_110, Identifier.fromString("minecraft:element_110"))
        register(BlockType.ELEMENT_111, Identifier.fromString("minecraft:element_111"))
        register(BlockType.ELEMENT_112, Identifier.fromString("minecraft:element_112"))
        register(BlockType.ELEMENT_113, Identifier.fromString("minecraft:element_113"))
        register(BlockType.ELEMENT_114, Identifier.fromString("minecraft:element_114"))
        register(BlockType.ELEMENT_115, Identifier.fromString("minecraft:element_115"))
        register(BlockType.ELEMENT_116, Identifier.fromString("minecraft:element_116"))
        register(BlockType.ELEMENT_117, Identifier.fromString("minecraft:element_117"))
        register(BlockType.ELEMENT_118, Identifier.fromString("minecraft:element_118"))
        register(BlockType.ELEMENT_12, Identifier.fromString("minecraft:element_12"))
        register(BlockType.ELEMENT_13, Identifier.fromString("minecraft:element_13"))
        register(BlockType.ELEMENT_14, Identifier.fromString("minecraft:element_14"))
        register(BlockType.ELEMENT_15, Identifier.fromString("minecraft:element_15"))
        register(BlockType.ELEMENT_16, Identifier.fromString("minecraft:element_16"))
        register(BlockType.ELEMENT_17, Identifier.fromString("minecraft:element_17"))
        register(BlockType.ELEMENT_18, Identifier.fromString("minecraft:element_18"))
        register(BlockType.ELEMENT_19, Identifier.fromString("minecraft:element_19"))
        register(BlockType.ELEMENT_2, Identifier.fromString("minecraft:element_2"))
        register(BlockType.ELEMENT_20, Identifier.fromString("minecraft:element_20"))
        register(BlockType.ELEMENT_21, Identifier.fromString("minecraft:element_21"))
        register(BlockType.ELEMENT_22, Identifier.fromString("minecraft:element_22"))
        register(BlockType.ELEMENT_23, Identifier.fromString("minecraft:element_23"))
        register(BlockType.ELEMENT_24, Identifier.fromString("minecraft:element_24"))
        register(BlockType.ELEMENT_25, Identifier.fromString("minecraft:element_25"))
        register(BlockType.ELEMENT_26, Identifier.fromString("minecraft:element_26"))
        register(BlockType.ELEMENT_27, Identifier.fromString("minecraft:element_27"))
        register(BlockType.ELEMENT_28, Identifier.fromString("minecraft:element_28"))
        register(BlockType.ELEMENT_29, Identifier.fromString("minecraft:element_29"))
        register(BlockType.ELEMENT_3, Identifier.fromString("minecraft:element_3"))
        register(BlockType.ELEMENT_30, Identifier.fromString("minecraft:element_30"))
        register(BlockType.ELEMENT_31, Identifier.fromString("minecraft:element_31"))
        register(BlockType.ELEMENT_32, Identifier.fromString("minecraft:element_32"))
        register(BlockType.ELEMENT_33, Identifier.fromString("minecraft:element_33"))
        register(BlockType.ELEMENT_34, Identifier.fromString("minecraft:element_34"))
        register(BlockType.ELEMENT_35, Identifier.fromString("minecraft:element_35"))
        register(BlockType.ELEMENT_36, Identifier.fromString("minecraft:element_36"))
        register(BlockType.ELEMENT_37, Identifier.fromString("minecraft:element_37"))
        register(BlockType.ELEMENT_38, Identifier.fromString("minecraft:element_38"))
        register(BlockType.ELEMENT_39, Identifier.fromString("minecraft:element_39"))
        register(BlockType.ELEMENT_4, Identifier.fromString("minecraft:element_4"))
        register(BlockType.ELEMENT_40, Identifier.fromString("minecraft:element_40"))
        register(BlockType.ELEMENT_41, Identifier.fromString("minecraft:element_41"))
        register(BlockType.ELEMENT_42, Identifier.fromString("minecraft:element_42"))
        register(BlockType.ELEMENT_43, Identifier.fromString("minecraft:element_43"))
        register(BlockType.ELEMENT_44, Identifier.fromString("minecraft:element_44"))
        register(BlockType.ELEMENT_45, Identifier.fromString("minecraft:element_45"))
        register(BlockType.ELEMENT_46, Identifier.fromString("minecraft:element_46"))
        register(BlockType.ELEMENT_47, Identifier.fromString("minecraft:element_47"))
        register(BlockType.ELEMENT_48, Identifier.fromString("minecraft:element_48"))
        register(BlockType.ELEMENT_49, Identifier.fromString("minecraft:element_49"))
        register(BlockType.ELEMENT_5, Identifier.fromString("minecraft:element_5"))
        register(BlockType.ELEMENT_50, Identifier.fromString("minecraft:element_50"))
        register(BlockType.ELEMENT_51, Identifier.fromString("minecraft:element_51"))
        register(BlockType.ELEMENT_52, Identifier.fromString("minecraft:element_52"))
        register(BlockType.ELEMENT_53, Identifier.fromString("minecraft:element_53"))
        register(BlockType.ELEMENT_54, Identifier.fromString("minecraft:element_54"))
        register(BlockType.ELEMENT_55, Identifier.fromString("minecraft:element_55"))
        register(BlockType.ELEMENT_56, Identifier.fromString("minecraft:element_56"))
        register(BlockType.ELEMENT_57, Identifier.fromString("minecraft:element_57"))
        register(BlockType.ELEMENT_58, Identifier.fromString("minecraft:element_58"))
        register(BlockType.ELEMENT_59, Identifier.fromString("minecraft:element_59"))
        register(BlockType.ELEMENT_6, Identifier.fromString("minecraft:element_6"))
        register(BlockType.ELEMENT_60, Identifier.fromString("minecraft:element_60"))
        register(BlockType.ELEMENT_61, Identifier.fromString("minecraft:element_61"))
        register(BlockType.ELEMENT_62, Identifier.fromString("minecraft:element_62"))
        register(BlockType.ELEMENT_63, Identifier.fromString("minecraft:element_63"))
        register(BlockType.ELEMENT_64, Identifier.fromString("minecraft:element_64"))
        register(BlockType.ELEMENT_65, Identifier.fromString("minecraft:element_65"))
        register(BlockType.ELEMENT_66, Identifier.fromString("minecraft:element_66"))
        register(BlockType.ELEMENT_67, Identifier.fromString("minecraft:element_67"))
        register(BlockType.ELEMENT_68, Identifier.fromString("minecraft:element_68"))
        register(BlockType.ELEMENT_69, Identifier.fromString("minecraft:element_69"))
        register(BlockType.ELEMENT_7, Identifier.fromString("minecraft:element_7"))
        register(BlockType.ELEMENT_70, Identifier.fromString("minecraft:element_70"))
        register(BlockType.ELEMENT_71, Identifier.fromString("minecraft:element_71"))
        register(BlockType.ELEMENT_72, Identifier.fromString("minecraft:element_72"))
        register(BlockType.ELEMENT_73, Identifier.fromString("minecraft:element_73"))
        register(BlockType.ELEMENT_74, Identifier.fromString("minecraft:element_74"))
        register(BlockType.ELEMENT_75, Identifier.fromString("minecraft:element_75"))
        register(BlockType.ELEMENT_76, Identifier.fromString("minecraft:element_76"))
        register(BlockType.ELEMENT_77, Identifier.fromString("minecraft:element_77"))
        register(BlockType.ELEMENT_78, Identifier.fromString("minecraft:element_78"))
        register(BlockType.ELEMENT_79, Identifier.fromString("minecraft:element_79"))
        register(BlockType.ELEMENT_8, Identifier.fromString("minecraft:element_8"))
        register(BlockType.ELEMENT_80, Identifier.fromString("minecraft:element_80"))
        register(BlockType.ELEMENT_81, Identifier.fromString("minecraft:element_81"))
        register(BlockType.ELEMENT_82, Identifier.fromString("minecraft:element_82"))
        register(BlockType.ELEMENT_83, Identifier.fromString("minecraft:element_83"))
        register(BlockType.ELEMENT_84, Identifier.fromString("minecraft:element_84"))
        register(BlockType.ELEMENT_85, Identifier.fromString("minecraft:element_85"))
        register(BlockType.ELEMENT_86, Identifier.fromString("minecraft:element_86"))
        register(BlockType.ELEMENT_87, Identifier.fromString("minecraft:element_87"))
        register(BlockType.ELEMENT_88, Identifier.fromString("minecraft:element_88"))
        register(BlockType.ELEMENT_89, Identifier.fromString("minecraft:element_89"))
        register(BlockType.ELEMENT_9, Identifier.fromString("minecraft:element_9"))
        register(BlockType.ELEMENT_90, Identifier.fromString("minecraft:element_90"))
        register(BlockType.ELEMENT_91, Identifier.fromString("minecraft:element_91"))
        register(BlockType.ELEMENT_92, Identifier.fromString("minecraft:element_92"))
        register(BlockType.ELEMENT_93, Identifier.fromString("minecraft:element_93"))
        register(BlockType.ELEMENT_94, Identifier.fromString("minecraft:element_94"))
        register(BlockType.ELEMENT_95, Identifier.fromString("minecraft:element_95"))
        register(BlockType.ELEMENT_96, Identifier.fromString("minecraft:element_96"))
        register(BlockType.ELEMENT_97, Identifier.fromString("minecraft:element_97"))
        register(BlockType.ELEMENT_98, Identifier.fromString("minecraft:element_98"))
        register(BlockType.ELEMENT_99, Identifier.fromString("minecraft:element_99"))
        register(BlockType.EMERALD_BLOCK, Identifier.fromString("minecraft:emerald_block"))
        register(
            BlockType.EMERALD_ORE,
            Identifier.fromString("minecraft:emerald_ore"),
            BlockEmeraldOre::class.java,
        )
        register(
            BlockType.ENCHANTING_TABLE,
            Identifier.fromString("minecraft:enchanting_table"),
            BlockEnchantingTable::class.java,
        )
        register(
            BlockType.ENDER_CHEST,
            Identifier.fromString("minecraft:ender_chest"),
            BlockEnderChest::class.java,
        )
        register(BlockType.END_BRICKS, Identifier.fromString("minecraft:end_bricks"))
        register(
            BlockType.END_BRICK_STAIRS,
            Identifier.fromString("minecraft:end_brick_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.END_GATEWAY, Identifier.fromString("minecraft:end_gateway"))
        register(BlockType.END_PORTAL, Identifier.fromString("minecraft:end_portal"))
        register(
            BlockType.END_PORTAL_FRAME,
            Identifier.fromString("minecraft:end_portal_frame"),
            BlockEndPortalFrame::class.java,
        )
        register(BlockType.END_ROD, Identifier.fromString("minecraft:end_rod"), BlockEndRod::class.java)
        register(BlockType.END_STONE, Identifier.fromString("minecraft:end_stone"))
        register(BlockType.EXPOSED_COPPER, Identifier.fromString("minecraft:exposed_copper"))
        register(BlockType.EXPOSED_CUT_COPPER, Identifier.fromString("minecraft:exposed_cut_copper"))
        register(
            BlockType.EXPOSED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:exposed_cut_copper_slab"),
            BlockExposedCutCopperSlab::class.java,
        )
        register(
            BlockType.EXPOSED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:exposed_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.EXPOSED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:exposed_double_cut_copper_slab"),
            BlockDoubleExposedCutCopperSlab::class.java,
        )
        register(BlockType.FARMLAND, Identifier.fromString("minecraft:farmland"))
        register(BlockType.FENCE, Identifier.fromString("minecraft:fence"), BlockWoodenFence::class.java)
        register(
            BlockType.FENCE_GATE,
            Identifier.fromString("minecraft:fence_gate"),
            BlockFenceGate::class.java,
        )
        register(BlockType.FIRE, Identifier.fromString("minecraft:fire"))
        register(BlockType.FLETCHING_TABLE, Identifier.fromString("minecraft:fletching_table"))
        register(BlockType.FLOWERING_AZALEA, Identifier.fromString("minecraft:flowering_azalea"))
        register(BlockType.FLOWER_POT, Identifier.fromString("minecraft:flower_pot"))
        register(
            BlockType.FLOWING_LAVA,
            Identifier.fromString("minecraft:flowing_lava"),
            BlockLava::class.java,
        )
        register(
            BlockType.FLOWING_WATER,
            Identifier.fromString("minecraft:flowing_water"),
            BlockWater::class.java,
        )
        register(BlockType.FRAME, Identifier.fromString("minecraft:frame"))
        register(BlockType.FROG_SPAWN, Identifier.fromString("minecraft:frog_spawn"))
        register(BlockType.FROSTED_ICE, Identifier.fromString("minecraft:frosted_ice"))
        register(BlockType.FURNACE, Identifier.fromString("minecraft:furnace"), BlockFurnace::class.java)
        register(BlockType.GILDED_BLACKSTONE, Identifier.fromString("minecraft:gilded_blackstone"))
        register(BlockType.GLASS, Identifier.fromString("minecraft:glass"))
        register(BlockType.GLASS_PANE, Identifier.fromString("minecraft:glass_pane"))
        register(BlockType.GLOWINGOBSIDIAN, Identifier.fromString("minecraft:glowingobsidian"))
        register(BlockType.GLOWSTONE, Identifier.fromString("minecraft:glowstone"))
        register(BlockType.GLOW_FRAME, Identifier.fromString("minecraft:glow_frame"))
        register(BlockType.GLOW_LICHEN, Identifier.fromString("minecraft:glow_lichen"))
        register(BlockType.GOLDEN_RAIL, Identifier.fromString("minecraft:golden_rail"), BlockRail::class.java)
        register(BlockType.GOLD_BLOCK, Identifier.fromString("minecraft:gold_block"))
        register(BlockType.GOLD_ORE, Identifier.fromString("minecraft:gold_ore"), BlockGoldOre::class.java)
        register(
            BlockType.GRANITE_STAIRS,
            Identifier.fromString("minecraft:granite_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.GRASS, Identifier.fromString("minecraft:grass"), BlockGrass::class.java)
        register(BlockType.GRASS_PATH, Identifier.fromString("minecraft:grass_path"))
        register(BlockType.GRAVEL, Identifier.fromString("minecraft:gravel"), BlockGravel::class.java)
        register(
            BlockType.GRAY_CANDLE,
            Identifier.fromString("minecraft:gray_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.GRAY_CANDLE_CAKE, Identifier.fromString("minecraft:gray_candle_cake"))
        register(BlockType.GRAY_GLAZED_TERRACOTTA, Identifier.fromString("minecraft:gray_glazed_terracotta"))
        register(
            BlockType.GREEN_CANDLE,
            Identifier.fromString("minecraft:green_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.GREEN_CANDLE_CAKE, Identifier.fromString("minecraft:green_candle_cake"))
        register(
            BlockType.GREEN_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:green_glazed_terracotta"),
        )
        register(
            BlockType.GRINDSTONE,
            Identifier.fromString("minecraft:grindstone"),
            BlockGrindstone::class.java,
        )
        register(
            BlockType.HANGING_ROOTS,
            Identifier.fromString("minecraft:hanging_roots"),
            BlockHangingRoots::class.java,
        )
        register(BlockType.HARDENED_CLAY, Identifier.fromString("minecraft:hardened_clay"))
        register(BlockType.HARD_GLASS, Identifier.fromString("minecraft:hard_glass"))
        register(BlockType.HARD_GLASS_PANE, Identifier.fromString("minecraft:hard_glass_pane"))
        register(BlockType.HARD_STAINED_GLASS, Identifier.fromString("minecraft:hard_stained_glass"))
        register(
            BlockType.HARD_STAINED_GLASS_PANE,
            Identifier.fromString("minecraft:hard_stained_glass_pane"),
        )
        register(BlockType.HAY_BLOCK, Identifier.fromString("minecraft:hay_block"))
        register(
            BlockType.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Identifier.fromString("minecraft:heavy_weighted_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(BlockType.HONEYCOMB_BLOCK, Identifier.fromString("minecraft:honeycomb_block"))
        register(BlockType.HONEY_BLOCK, Identifier.fromString("minecraft:honey_block"))
        register(BlockType.HOPPER, Identifier.fromString("minecraft:hopper"), BlockHopper::class.java)
        register(BlockType.ICE, Identifier.fromString("minecraft:ice"))
        register(BlockType.INFESTED_DEEPSLATE, Identifier.fromString("minecraft:infested_deepslate"))
        register(BlockType.INFO_UPDATE, Identifier.fromString("minecraft:info_update"))
        register(BlockType.INFO_UPDATE2, Identifier.fromString("minecraft:info_update2"))
        register(BlockType.INVISIBLE_BEDROCK, Identifier.fromString("minecraft:invisible_bedrock"))
        register(BlockType.IRON_BARS, Identifier.fromString("minecraft:iron_bars"))
        register(BlockType.IRON_BLOCK, Identifier.fromString("minecraft:iron_block"))
        register(BlockType.IRON_DOOR, Identifier.fromString("minecraft:iron_door"), BlockDoor::class.java)
        register(BlockType.IRON_ORE, Identifier.fromString("minecraft:iron_ore"), BlockIronOre::class.java)
        register(
            BlockType.IRON_TRAPDOOR,
            Identifier.fromString("minecraft:iron_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(BlockType.JIGSAW, Identifier.fromString("minecraft:jigsaw"))
        register(BlockType.JUKEBOX, Identifier.fromString("minecraft:jukebox"))
        register(
            BlockType.JUNGLE_BUTTON,
            Identifier.fromString("minecraft:jungle_button"),
            BlockButton::class.java,
        )
        register(BlockType.JUNGLE_DOOR, Identifier.fromString("minecraft:jungle_door"), BlockDoor::class.java)
        register(
            BlockType.JUNGLE_FENCE_GATE,
            Identifier.fromString("minecraft:jungle_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(
            BlockType.JUNGLE_PRESSURE_PLATE,
            Identifier.fromString("minecraft:jungle_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.JUNGLE_STAIRS,
            Identifier.fromString("minecraft:jungle_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.JUNGLE_STANDING_SIGN,
            Identifier.fromString("minecraft:jungle_standing_sign"),
            BlockJungleStandingSign::class.java,
        )
        register(
            BlockType.JUNGLE_TRAPDOOR,
            Identifier.fromString("minecraft:jungle_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.JUNGLE_WALL_SIGN,
            Identifier.fromString("minecraft:jungle_wall_sign"),
            BlockJungleWallSign::class.java,
        )
        register(BlockType.KELP, Identifier.fromString("minecraft:kelp"), BlockKelp::class.java)
        register(BlockType.LADDER, Identifier.fromString("minecraft:ladder"), BlockLadder::class.java)
        register(BlockType.LANTERN, Identifier.fromString("minecraft:lantern"), BlockLantern::class.java)
        register(BlockType.LAPIS_BLOCK, Identifier.fromString("minecraft:lapis_block"))
        register(BlockType.LAPIS_ORE, Identifier.fromString("minecraft:lapis_ore"), BlockLapisOre::class.java)
        register(BlockType.LARGE_AMETHYST_BUD, Identifier.fromString("minecraft:large_amethyst_bud"))
        register(BlockType.LAVA, Identifier.fromString("minecraft:lava"), BlockLava::class.java)
        register(BlockType.LAVA_CAULDRON, Identifier.fromString("minecraft:lava_cauldron"))
        register(BlockType.LEAVES, Identifier.fromString("minecraft:leaves"), BlockLeaves::class.java)
        register(BlockType.LEAVES2, Identifier.fromString("minecraft:leaves2"), BlockLeaves2::class.java)
        register(BlockType.LECTERN, Identifier.fromString("minecraft:lectern"), BlockLectern::class.java)
        register(BlockType.LEVER, Identifier.fromString("minecraft:lever"), BlockLever::class.java)
        register(
            BlockType.LIGHTNING_ROD,
            Identifier.fromString("minecraft:lightning_rod"),
            BlockLightningRod::class.java,
        )
        register(BlockType.LIGHT_BLOCK, Identifier.fromString("minecraft:light_block"))
        register(
            BlockType.LIGHT_BLUE_CANDLE,
            Identifier.fromString("minecraft:light_blue_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.LIGHT_BLUE_CANDLE_CAKE, Identifier.fromString("minecraft:light_blue_candle_cake"))
        register(
            BlockType.LIGHT_BLUE_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:light_blue_glazed_terracotta"),
        )
        register(
            BlockType.LIGHT_GRAY_CANDLE,
            Identifier.fromString("minecraft:light_gray_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.LIGHT_GRAY_CANDLE_CAKE, Identifier.fromString("minecraft:light_gray_candle_cake"))
        register(
            BlockType.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Identifier.fromString("minecraft:light_weighted_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.LIME_CANDLE,
            Identifier.fromString("minecraft:lime_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.LIME_CANDLE_CAKE, Identifier.fromString("minecraft:lime_candle_cake"))
        register(BlockType.LIME_GLAZED_TERRACOTTA, Identifier.fromString("minecraft:lime_glazed_terracotta"))
        register(
            BlockType.LIT_BLAST_FURNACE,
            Identifier.fromString("minecraft:lit_blast_furnace"),
            BlockBlastFurnace::class.java,
        )
        register(
            BlockType.LIT_DEEPSLATE_REDSTONE_ORE,
            Identifier.fromString("minecraft:lit_deepslate_redstone_ore"),
            BlockRedstoneOre::class.java,
        )
        register(
            BlockType.LIT_FURNACE,
            Identifier.fromString("minecraft:lit_furnace"),
            BlockFurnace::class.java,
        )
        register(BlockType.LIT_PUMPKIN, Identifier.fromString("minecraft:lit_pumpkin"))
        register(BlockType.LIT_REDSTONE_LAMP, Identifier.fromString("minecraft:lit_redstone_lamp"))
        register(
            BlockType.LIT_REDSTONE_ORE,
            Identifier.fromString("minecraft:lit_redstone_ore"),
            BlockRedstoneOre::class.java,
        )
        register(BlockType.LIT_SMOKER, Identifier.fromString("minecraft:lit_smoker"))
        register(BlockType.LODESTONE, Identifier.fromString("minecraft:lodestone"))
        register(BlockType.LOG, Identifier.fromString("minecraft:log"), BlockLog::class.java)
        register(BlockType.LOG2, Identifier.fromString("minecraft:log2"), BlockLog2::class.java)
        register(BlockType.LOOM, Identifier.fromString("minecraft:loom"))
        register(
            BlockType.MAGENTA_CANDLE,
            Identifier.fromString("minecraft:magenta_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.MAGENTA_CANDLE_CAKE, Identifier.fromString("minecraft:magenta_candle_cake"))
        register(
            BlockType.MAGENTA_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:magenta_glazed_terracotta"),
        )
        register(BlockType.MAGMA, Identifier.fromString("minecraft:magma"))
        register(
            BlockType.MANGROVE_BUTTON,
            Identifier.fromString("minecraft:mangrove_button"),
            BlockButton::class.java,
        )
        register(
            BlockType.MANGROVE_DOOR,
            Identifier.fromString("minecraft:mangrove_door"),
            BlockDoor::class.java,
        )
        register(
            BlockType.MANGROVE_DOUBLE_SLAB,
            Identifier.fromString("minecraft:mangrove_double_slab"),
            BlockDoubleMangroveSlab::class.java,
        )
        register(
            BlockType.MANGROVE_FENCE,
            Identifier.fromString("minecraft:mangrove_fence"),
            BlockFence::class.java,
        )
        register(
            BlockType.MANGROVE_FENCE_GATE,
            Identifier.fromString("minecraft:mangrove_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(BlockType.MANGROVE_LEAVES, Identifier.fromString("minecraft:mangrove_leaves"))
        register(
            BlockType.MANGROVE_LOG,
            Identifier.fromString("minecraft:mangrove_log"),
            BlockMangroveLog::class.java,
        )
        register(BlockType.MANGROVE_PLANKS, Identifier.fromString("minecraft:mangrove_planks"))
        register(
            BlockType.MANGROVE_PRESSURE_PLATE,
            Identifier.fromString("minecraft:mangrove_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(BlockType.MANGROVE_PROPAGULE, Identifier.fromString("minecraft:mangrove_propagule"))
        register(BlockType.MANGROVE_ROOTS, Identifier.fromString("minecraft:mangrove_roots"))
        register(
            BlockType.MANGROVE_SLAB,
            Identifier.fromString("minecraft:mangrove_slab"),
            BlockMangroveSlab::class.java,
        )
        register(
            BlockType.MANGROVE_STAIRS,
            Identifier.fromString("minecraft:mangrove_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.MANGROVE_STANDING_SIGN,
            Identifier.fromString("minecraft:mangrove_standing_sign"),
            BlockMangroveStandingSign::class.java,
        )
        register(
            BlockType.MANGROVE_TRAPDOOR,
            Identifier.fromString("minecraft:mangrove_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.MANGROVE_WALL_SIGN,
            Identifier.fromString("minecraft:mangrove_wall_sign"),
            BlockMangroveWallSign::class.java,
        )
        register(
            BlockType.MANGROVE_WOOD,
            Identifier.fromString("minecraft:mangrove_wood"),
            BlockMangroveWood::class.java,
        )
        register(BlockType.MEDIUM_AMETHYST_BUD, Identifier.fromString("minecraft:medium_amethyst_bud"))
        register(BlockType.MELON_BLOCK, Identifier.fromString("minecraft:melon_block"))
        register(BlockType.MELON_STEM, Identifier.fromString("minecraft:melon_stem"))
        register(BlockType.MOB_SPAWNER, Identifier.fromString("minecraft:mob_spawner"))
        register(BlockType.INFESTED_STONE, Identifier.fromString("minecraft:monster_egg"))
        register(BlockType.MOSSY_COBBLESTONE, Identifier.fromString("minecraft:mossy_cobblestone"))
        register(
            BlockType.MOSSY_COBBLESTONE_STAIRS,
            Identifier.fromString("minecraft:mossy_cobblestone_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.MOSSY_STONE_BRICK_STAIRS,
            Identifier.fromString("minecraft:mossy_stone_brick_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.MOSS_BLOCK, Identifier.fromString("minecraft:moss_block"))
        register(BlockType.MOSS_CARPET, Identifier.fromString("minecraft:moss_carpet"))
        register(BlockType.MOVING_BLOCK, Identifier.fromString("minecraft:moving_block"))
        register(BlockType.MUD, Identifier.fromString("minecraft:mud"))
        register(
            BlockType.MUDDY_MANGROVE_ROOTS,
            Identifier.fromString("minecraft:muddy_mangrove_roots"),
            BlockMuddyMangroveRoots::class.java,
        )
        register(BlockType.MUD_BRICKS, Identifier.fromString("minecraft:mud_bricks"))
        register(
            BlockType.MUD_BRICK_DOUBLE_SLAB,
            Identifier.fromString("minecraft:mud_brick_double_slab"),
            BlockDoubleMudBrickSlab::class.java,
        )
        register(
            BlockType.MUD_BRICK_SLAB,
            Identifier.fromString("minecraft:mud_brick_slab"),
            BlockMudBrickSlab::class.java,
        )
        register(
            BlockType.MUD_BRICK_STAIRS,
            Identifier.fromString("minecraft:mud_brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.MUD_BRICK_WALL,
            Identifier.fromString("minecraft:mud_brick_wall"),
            BlockWall::class.java,
        )
        register(BlockType.MYCELIUM, Identifier.fromString("minecraft:mycelium"))
        register(BlockType.NETHERITE_BLOCK, Identifier.fromString("minecraft:netherite_block"))
        register(BlockType.NETHERRACK, Identifier.fromString("minecraft:netherrack"))
        register(BlockType.NETHERREACTOR, Identifier.fromString("minecraft:netherreactor"))
        register(BlockType.NETHER_BRICK, Identifier.fromString("minecraft:nether_brick"))
        register(
            BlockType.NETHER_BRICK_FENCE,
            Identifier.fromString("minecraft:nether_brick_fence"),
            BlockFence::class.java,
        )
        register(
            BlockType.NETHER_BRICK_STAIRS,
            Identifier.fromString("minecraft:nether_brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.NETHER_GOLD_ORE,
            Identifier.fromString("minecraft:nether_gold_ore"),
            BlockNetherGoldOre::class.java,
        )
        register(BlockType.NETHER_SPROUTS, Identifier.fromString("minecraft:nether_sprouts"))
        register(
            BlockType.NETHER_WART,
            Identifier.fromString("minecraft:nether_wart"),
            BlockNetherWart::class.java,
        )
        register(BlockType.NETHER_WART_BLOCK, Identifier.fromString("minecraft:nether_wart_block"))
        register(
            BlockType.NORMAL_STONE_STAIRS,
            Identifier.fromString("minecraft:normal_stone_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.NOTEBLOCK, Identifier.fromString("minecraft:noteblock"))
        register(BlockType.OAK_STAIRS, Identifier.fromString("minecraft:oak_stairs"), BlockStairs::class.java)
        register(BlockType.OBSERVER, Identifier.fromString("minecraft:observer"))
        register(BlockType.OBSIDIAN, Identifier.fromString("minecraft:obsidian"))
        register(BlockType.OCHRE_FROGLIGHT, Identifier.fromString("minecraft:ochre_froglight"))
        register(
            BlockType.ORANGE_CANDLE,
            Identifier.fromString("minecraft:orange_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.ORANGE_CANDLE_CAKE, Identifier.fromString("minecraft:orange_candle_cake"))
        register(
            BlockType.ORANGE_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:orange_glazed_terracotta"),
        )
        register(BlockType.OXIDIZED_COPPER, Identifier.fromString("minecraft:oxidized_copper"))
        register(BlockType.OXIDIZED_CUT_COPPER, Identifier.fromString("minecraft:oxidized_cut_copper"))
        register(
            BlockType.OXIDIZED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:oxidized_cut_copper_slab"),
            BlockOxidizedCutCopperSlab::class.java,
        )
        register(
            BlockType.OXIDIZED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:oxidized_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.OXIDIZED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:oxidized_double_cut_copper_slab"),
            BlockDoubleOxidizedCutCopperSlab::class.java,
        )
        register(BlockType.PACKED_ICE, Identifier.fromString("minecraft:packed_ice"))
        register(BlockType.PACKED_MUD, Identifier.fromString("minecraft:packed_mud"))
        register(BlockType.PEARLESCENT_FROGLIGHT, Identifier.fromString("minecraft:pearlescent_froglight"))
        register(
            BlockType.PINK_CANDLE,
            Identifier.fromString("minecraft:pink_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.PINK_CANDLE_CAKE, Identifier.fromString("minecraft:pink_candle_cake"))
        register(BlockType.PINK_GLAZED_TERRACOTTA, Identifier.fromString("minecraft:pink_glazed_terracotta"))
        register(BlockType.PISTON, Identifier.fromString("minecraft:piston"), BlockPiston::class.java)
        register(BlockType.PISTON_ARM_COLLISION, Identifier.fromString("minecraft:piston_arm_collision"))
        register(BlockType.PLANKS, Identifier.fromString("minecraft:planks"), BlockPlanks::class.java)
        register(BlockType.PODZOL, Identifier.fromString("minecraft:podzol"))
        register(
            BlockType.POINTED_DRIPSTONE,
            Identifier.fromString("minecraft:pointed_dripstone"),
            BlockPointedDripstone::class.java,
        )
        register(
            BlockType.POLISHED_ANDESITE_STAIRS,
            Identifier.fromString("minecraft:polished_andesite_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.POLISHED_BASALT,
            Identifier.fromString("minecraft:polished_basalt"),
            BlockPolishedBasalt::class.java,
        )
        register(BlockType.POLISHED_BLACKSTONE, Identifier.fromString("minecraft:polished_blackstone"))
        register(
            BlockType.POLISHED_BLACKSTONE_BRICKS,
            Identifier.fromString("minecraft:polished_blackstone_bricks"),
        )
        register(
            BlockType.POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB,
            Identifier.fromString("minecraft:polished_blackstone_brick_double_slab"),
            BlockDoublePolishedBlackstoneBrickSlab::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_BRICK_SLAB,
            Identifier.fromString("minecraft:polished_blackstone_brick_slab"),
            BlockPolishedBlackstoneBrickSlab::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_BRICK_STAIRS,
            Identifier.fromString("minecraft:polished_blackstone_brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_BRICK_WALL,
            Identifier.fromString("minecraft:polished_blackstone_brick_wall"),
            BlockWall::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_BUTTON,
            Identifier.fromString("minecraft:polished_blackstone_button"),
            BlockButton::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_DOUBLE_SLAB,
            Identifier.fromString("minecraft:polished_blackstone_double_slab"),
            BlockDoublePolishedBlackstoneSlab::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Identifier.fromString("minecraft:polished_blackstone_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_SLAB,
            Identifier.fromString("minecraft:polished_blackstone_slab"),
            BlockPolishedBlackstoneSlab::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_STAIRS,
            Identifier.fromString("minecraft:polished_blackstone_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.POLISHED_BLACKSTONE_WALL,
            Identifier.fromString("minecraft:polished_blackstone_wall"),
            BlockWall::class.java,
        )
        register(BlockType.POLISHED_DEEPSLATE, Identifier.fromString("minecraft:polished_deepslate"))
        register(
            BlockType.POLISHED_DEEPSLATE_DOUBLE_SLAB,
            Identifier.fromString("minecraft:polished_deepslate_double_slab"),
            BlockDoublePolishedDeepslateSlab::class.java,
        )
        register(
            BlockType.POLISHED_DEEPSLATE_SLAB,
            Identifier.fromString("minecraft:polished_deepslate_slab"),
            BlockPolishedDeepslateSlab::class.java,
        )
        register(
            BlockType.POLISHED_DEEPSLATE_STAIRS,
            Identifier.fromString("minecraft:polished_deepslate_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.POLISHED_DEEPSLATE_WALL,
            Identifier.fromString("minecraft:polished_deepslate_wall"),
            BlockWall::class.java,
        )
        register(
            BlockType.POLISHED_DIORITE_STAIRS,
            Identifier.fromString("minecraft:polished_diorite_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.POLISHED_GRANITE_STAIRS,
            Identifier.fromString("minecraft:polished_granite_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.PORTAL, Identifier.fromString("minecraft:portal"))
        register(BlockType.POTATOES, Identifier.fromString("minecraft:potatoes"))
        register(BlockType.POWDER_SNOW, Identifier.fromString("minecraft:powder_snow"))
        register(BlockType.POWERED_COMPARATOR, Identifier.fromString("minecraft:powered_comparator"))
        register(BlockType.POWERED_REPEATER, Identifier.fromString("minecraft:powered_repeater"))
        register(
            BlockType.PRISMARINE,
            Identifier.fromString("minecraft:prismarine"),
            BlockPrismarine::class.java,
        )
        register(
            BlockType.PRISMARINE_BRICKS_STAIRS,
            Identifier.fromString("minecraft:prismarine_bricks_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.PRISMARINE_STAIRS,
            Identifier.fromString("minecraft:prismarine_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.PUMPKIN, Identifier.fromString("minecraft:pumpkin"))
        register(BlockType.PUMPKIN_STEM, Identifier.fromString("minecraft:pumpkin_stem"))
        register(
            BlockType.PURPLE_CANDLE,
            Identifier.fromString("minecraft:purple_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.PURPLE_CANDLE_CAKE, Identifier.fromString("minecraft:purple_candle_cake"))
        register(
            BlockType.PURPLE_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:purple_glazed_terracotta"),
        )
        register(
            BlockType.PURPUR_BLOCK,
            Identifier.fromString("minecraft:purpur_block"),
            BlockPurpurBlock::class.java,
        )
        register(
            BlockType.PURPUR_STAIRS,
            Identifier.fromString("minecraft:purpur_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.QUARTZ_BLOCK,
            Identifier.fromString("minecraft:quartz_block"),
            BlockQuartzBlock::class.java,
        )
        register(BlockType.QUARTZ_BRICKS, Identifier.fromString("minecraft:quartz_bricks"))
        register(
            BlockType.QUARTZ_ORE,
            Identifier.fromString("minecraft:quartz_ore"),
            BlockQuartzOre::class.java,
        )
        register(
            BlockType.QUARTZ_STAIRS,
            Identifier.fromString("minecraft:quartz_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.RAIL, Identifier.fromString("minecraft:rail"), BlockRail::class.java)
        register(BlockType.RAW_COPPER_BLOCK, Identifier.fromString("minecraft:raw_copper_block"))
        register(BlockType.RAW_GOLD_BLOCK, Identifier.fromString("minecraft:raw_gold_block"))
        register(BlockType.RAW_IRON_BLOCK, Identifier.fromString("minecraft:raw_iron_block"))
        register(BlockType.REDSTONE_BLOCK, Identifier.fromString("minecraft:redstone_block"))
        register(BlockType.REDSTONE_LAMP, Identifier.fromString("minecraft:redstone_lamp"))
        register(
            BlockType.REDSTONE_ORE,
            Identifier.fromString("minecraft:redstone_ore"),
            BlockRedstoneOre::class.java,
        )
        register(
            BlockType.REDSTONE_TORCH,
            Identifier.fromString("minecraft:redstone_torch"),
            BlockTorch::class.java,
        )
        register(BlockType.REDSTONE_WIRE, Identifier.fromString("minecraft:redstone_wire"))
        register(BlockType.RED_CANDLE, Identifier.fromString("minecraft:red_candle"), BlockCandle::class.java)
        register(BlockType.RED_CANDLE_CAKE, Identifier.fromString("minecraft:red_candle_cake"))
        register(
            BlockType.RED_FLOWER,
            Identifier.fromString("minecraft:red_flower"),
            BlockRedFlower::class.java,
        )
        register(BlockType.RED_GLAZED_TERRACOTTA, Identifier.fromString("minecraft:red_glazed_terracotta"))
        register(BlockType.RED_MUSHROOM, Identifier.fromString("minecraft:red_mushroom"))
        register(BlockType.RED_MUSHROOM_BLOCK, Identifier.fromString("minecraft:red_mushroom_block"))
        register(BlockType.RED_NETHER_BRICK, Identifier.fromString("minecraft:red_nether_brick"))
        register(
            BlockType.RED_NETHER_BRICK_STAIRS,
            Identifier.fromString("minecraft:red_nether_brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.RED_SANDSTONE,
            Identifier.fromString("minecraft:red_sandstone"),
            BlockRedSandstone::class.java,
        )
        register(
            BlockType.RED_SANDSTONE_STAIRS,
            Identifier.fromString("minecraft:red_sandstone_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.SUGAR_CANE, Identifier.fromString("minecraft:reeds"), BlockSugarCane::class.java)
        register(BlockType.REINFORCED_DEEPSLATE, Identifier.fromString("minecraft:reinforced_deepslate"))
        register(
            BlockType.REPEATING_COMMAND_BLOCK,
            Identifier.fromString("minecraft:repeating_command_block"),
        )
        register(BlockType.RESERVED6, Identifier.fromString("minecraft:reserved6"))
        register(BlockType.RESPAWN_ANCHOR, Identifier.fromString("minecraft:respawn_anchor"))
        register(BlockType.SAND, Identifier.fromString("minecraft:sand"), BlockSand::class.java)
        register(
            BlockType.SANDSTONE,
            Identifier.fromString("minecraft:sandstone"),
            BlockSandstone::class.java,
        )
        register(
            BlockType.SANDSTONE_STAIRS,
            Identifier.fromString("minecraft:sandstone_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.SAPLING, Identifier.fromString("minecraft:sapling"), BlockSapling::class.java)
        register(BlockType.SCAFFOLDING, Identifier.fromString("minecraft:scaffolding"))
        register(BlockType.SCULK, Identifier.fromString("minecraft:sculk"))
        register(BlockType.SCULK_CATALYST, Identifier.fromString("minecraft:sculk_catalyst"))
        register(BlockType.SCULK_SENSOR, Identifier.fromString("minecraft:sculk_sensor"))
        register(BlockType.SCULK_SHRIEKER, Identifier.fromString("minecraft:sculk_shrieker"))
        register(BlockType.SCULK_VEIN, Identifier.fromString("minecraft:sculk_vein"))
        register(BlockType.SEAGRASS, Identifier.fromString("minecraft:seagrass"), BlockSeagrass::class.java)
        register(BlockType.SEA_LANTERN, Identifier.fromString("minecraft:sea_lantern"))
        register(
            BlockType.SEA_PICKLE,
            Identifier.fromString("minecraft:sea_pickle"),
            BlockSeaPickle::class.java,
        )
        register(BlockType.SHROOMLIGHT, Identifier.fromString("minecraft:shroomlight"))
        register(
            BlockType.SHULKER_BOX,
            Identifier.fromString("minecraft:shulker_box"),
            BlockShulkerBox::class.java,
        )
        register(
            BlockType.SILVER_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:silver_glazed_terracotta"),
        )
        register(BlockType.SKULL, Identifier.fromString("minecraft:skull"), BlockSkull::class.java)
        register(BlockType.SLIME, Identifier.fromString("minecraft:slime"))
        register(BlockType.SMALL_AMETHYST_BUD, Identifier.fromString("minecraft:small_amethyst_bud"))
        register(BlockType.SMALL_DRIPLEAF_BLOCK, Identifier.fromString("minecraft:small_dripleaf_block"))
        register(
            BlockType.SMITHING_TABLE,
            Identifier.fromString("minecraft:smithing_table"),
            BlockSmithingTable::class.java,
        )
        register(BlockType.SMOKER, Identifier.fromString("minecraft:smoker"), BlockSmoker::class.java)
        register(BlockType.SMOOTH_BASALT, Identifier.fromString("minecraft:smooth_basalt"))
        register(
            BlockType.SMOOTH_QUARTZ_STAIRS,
            Identifier.fromString("minecraft:smooth_quartz_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.SMOOTH_RED_SANDSTONE_STAIRS,
            Identifier.fromString("minecraft:smooth_red_sandstone_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.SMOOTH_SANDSTONE_STAIRS,
            Identifier.fromString("minecraft:smooth_sandstone_stairs"),
            BlockStairs::class.java,
        )
        register(BlockType.SMOOTH_STONE, Identifier.fromString("minecraft:smooth_stone"))
        register(BlockType.SNOW, Identifier.fromString("minecraft:snow"))
        register(
            BlockType.SNOW_LAYER,
            Identifier.fromString("minecraft:snow_layer"),
            BlockSnowLayer::class.java,
        )
        register(
            BlockType.SOUL_CAMPFIRE,
            Identifier.fromString("minecraft:soul_campfire"),
            BlockCampfire::class.java,
        )
        register(BlockType.SOUL_FIRE, Identifier.fromString("minecraft:soul_fire"))
        register(
            BlockType.SOUL_LANTERN,
            Identifier.fromString("minecraft:soul_lantern"),
            BlockLantern::class.java,
        )
        register(BlockType.SOUL_SAND, Identifier.fromString("minecraft:soul_sand"))
        register(BlockType.SOUL_SOIL, Identifier.fromString("minecraft:soul_soil"))
        register(BlockType.SOUL_TORCH, Identifier.fromString("minecraft:soul_torch"), BlockTorch::class.java)
        register(BlockType.SPONGE, Identifier.fromString("minecraft:sponge"), BlockSponge::class.java)
        register(BlockType.SPORE_BLOSSOM, Identifier.fromString("minecraft:spore_blossom"))
        register(
            BlockType.SPRUCE_BUTTON,
            Identifier.fromString("minecraft:spruce_button"),
            BlockButton::class.java,
        )
        register(BlockType.SPRUCE_DOOR, Identifier.fromString("minecraft:spruce_door"), BlockDoor::class.java)
        register(
            BlockType.SPRUCE_FENCE_GATE,
            Identifier.fromString("minecraft:spruce_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(
            BlockType.SPRUCE_PRESSURE_PLATE,
            Identifier.fromString("minecraft:spruce_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.SPRUCE_STAIRS,
            Identifier.fromString("minecraft:spruce_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.SPRUCE_STANDING_SIGN,
            Identifier.fromString("minecraft:spruce_standing_sign"),
            BlockSpruceStandingSign::class.java,
        )
        register(
            BlockType.SPRUCE_TRAPDOOR,
            Identifier.fromString("minecraft:spruce_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.SPRUCE_WALL_SIGN,
            Identifier.fromString("minecraft:spruce_wall_sign"),
            BlockSpruceWallSign::class.java,
        )
        register(
            BlockType.STAINED_GLASS,
            Identifier.fromString("minecraft:stained_glass"),
            BlockStainedGlass::class.java,
        )
        register(
            BlockType.STAINED_GLASS_PANE,
            Identifier.fromString("minecraft:stained_glass_pane"),
            BlockStainedGlassPane::class.java,
        )
        register(
            BlockType.STAINED_HARDENED_CLAY,
            Identifier.fromString("minecraft:stained_hardened_clay"),
            BlockStainedHardenedClay::class.java,
        )
        register(
            BlockType.STANDING_BANNER,
            Identifier.fromString("minecraft:standing_banner"),
            BlockStandingBanner::class.java,
        )
        register(
            BlockType.OAK_STANDING_SIGN,
            Identifier.fromString("minecraft:standing_sign"),
            BlockOakStandingSign::class.java,
        )
        register(
            BlockType.STICKY_PISTON,
            Identifier.fromString("minecraft:sticky_piston"),
            BlockStickyPiston::class.java,
        )
        register(
            BlockType.STICKY_PISTON_ARM_COLLISION,
            Identifier.fromString("minecraft:sticky_piston_arm_collision"),
        )
        register(BlockType.STONE, Identifier.fromString("minecraft:stone"), BlockStone::class.java)
        register(
            BlockType.STONEBRICK,
            Identifier.fromString("minecraft:stonebrick"),
            BlockStonebrick::class.java,
        )
        register(BlockType.STONECUTTER, Identifier.fromString("minecraft:stonecutter"))
        register(
            BlockType.STONECUTTER_BLOCK,
            Identifier.fromString("minecraft:stonecutter_block"),
            BlockStonecutterBlock::class.java,
        )
        register(
            BlockType.STONE_BLOCK_SLAB,
            Identifier.fromString("minecraft:stone_block_slab"),
            BlockStoneSlab::class.java,
        )
        register(
            BlockType.STONE_BLOCK_SLAB2,
            Identifier.fromString("minecraft:stone_block_slab2"),
            BlockStoneSlab2::class.java,
        )
        register(
            BlockType.STONE_BLOCK_SLAB3,
            Identifier.fromString("minecraft:stone_block_slab3"),
            BlockStoneSlab3::class.java,
        )
        register(
            BlockType.STONE_BLOCK_SLAB4,
            Identifier.fromString("minecraft:stone_block_slab4"),
            BlockStoneSlab4::class.java,
        )
        register(
            BlockType.STONE_BRICK_STAIRS,
            Identifier.fromString("minecraft:stone_brick_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.STONE_BUTTON,
            Identifier.fromString("minecraft:stone_button"),
            BlockButton::class.java,
        )
        register(
            BlockType.STONE_PRESSURE_PLATE,
            Identifier.fromString("minecraft:stone_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.STONE_STAIRS,
            Identifier.fromString("minecraft:stone_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.STRIPPED_ACACIA_LOG,
            Identifier.fromString("minecraft:stripped_acacia_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_BIRCH_LOG,
            Identifier.fromString("minecraft:stripped_birch_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_CRIMSON_HYPHAE,
            Identifier.fromString("minecraft:stripped_crimson_hyphae"),
            BlockStrippedCrimsonHyphae::class.java,
        )
        register(
            BlockType.STRIPPED_CRIMSON_STEM,
            Identifier.fromString("minecraft:stripped_crimson_stem"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_DARK_OAK_LOG,
            Identifier.fromString("minecraft:stripped_dark_oak_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_JUNGLE_LOG,
            Identifier.fromString("minecraft:stripped_jungle_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_MANGROVE_LOG,
            Identifier.fromString("minecraft:stripped_mangrove_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_MANGROVE_WOOD,
            Identifier.fromString("minecraft:stripped_mangrove_wood"),
            BlockStrippedMangroveWood::class.java,
        )
        register(
            BlockType.STRIPPED_OAK_LOG,
            Identifier.fromString("minecraft:stripped_oak_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_SPRUCE_LOG,
            Identifier.fromString("minecraft:stripped_spruce_log"),
            BlockStippedLog::class.java,
        )
        register(
            BlockType.STRIPPED_WARPED_HYPHAE,
            Identifier.fromString("minecraft:stripped_warped_hyphae"),
            BlockStrippedWarpedHyphae::class.java,
        )
        register(
            BlockType.STRIPPED_WARPED_STEM,
            Identifier.fromString("minecraft:stripped_warped_stem"),
            BlockStippedLog::class.java,
        )
        register(BlockType.STRUCTURE_BLOCK, Identifier.fromString("minecraft:structure_block"))
        register(BlockType.STRUCTURE_VOID, Identifier.fromString("minecraft:structure_void"))
        register(BlockType.SWEET_BERRY_BUSH, Identifier.fromString("minecraft:sweet_berry_bush"))
        register(
            BlockType.TALLGRASS,
            Identifier.fromString("minecraft:tallgrass"),
            BlockTallGrass::class.java,
        )
        register(BlockType.TARGET, Identifier.fromString("minecraft:target"))
        register(BlockType.TINTED_GLASS, Identifier.fromString("minecraft:tinted_glass"))
        register(BlockType.TNT, Identifier.fromString("minecraft:tnt"))
        register(BlockType.TORCH, Identifier.fromString("minecraft:torch"), BlockTorch::class.java)
        register(BlockType.TRAPDOOR, Identifier.fromString("minecraft:trapdoor"), BlockTrapdoor::class.java)
        register(
            BlockType.TRAPPED_CHEST,
            Identifier.fromString("minecraft:trapped_chest"),
            BlockTrappedChest::class.java,
        )
        register(
            BlockType.TRIPWIRE_HOOK,
            Identifier.fromString("minecraft:tripwire_hook"),
            BlockTripwireHook::class.java,
        )
        register(BlockType.TRIP_WIRE, Identifier.fromString("minecraft:trip_wire"))
        register(BlockType.TUFF, Identifier.fromString("minecraft:tuff"))
        register(BlockType.TURTLE_EGG, Identifier.fromString("minecraft:turtle_egg"))
        register(BlockType.TWISTING_VINES, Identifier.fromString("minecraft:twisting_vines"))
        register(
            BlockType.UNDERWATER_TORCH,
            Identifier.fromString("minecraft:underwater_torch"),
            BlockTorch::class.java,
        )
        register(
            BlockType.UNDYED_SHULKER_BOX,
            Identifier.fromString("minecraft:undyed_shulker_box"),
            BlockUndyedShulkerBox::class.java,
        )
        register(BlockType.UNKNOWN, Identifier.fromString("minecraft:unknown"))
        register(
            BlockType.UNLIT_REDSTONE_TORCH,
            Identifier.fromString("minecraft:unlit_redstone_torch"),
            BlockTorch::class.java,
        )
        register(
            BlockType.UNPOWERED_COMPARATOR,
            Identifier.fromString("minecraft:unpowered_comparator"),
            BlockCompartor::class.java,
        )
        register(
            BlockType.UNPOWERED_REPEATER,
            Identifier.fromString("minecraft:unpowered_repeater"),
            BlockRepeater::class.java,
        )
        register(BlockType.VERDANT_FROGLIGHT, Identifier.fromString("minecraft:verdant_froglight"))
        register(BlockType.VINE, Identifier.fromString("minecraft:vine"), BlockVine::class.java)
        register(
            BlockType.WALL_BANNER,
            Identifier.fromString("minecraft:wall_banner"),
            BlockWallBanner::class.java,
        )
        register(
            BlockType.OAK_WALL_SIGN,
            Identifier.fromString("minecraft:wall_sign"),
            BlockOakWallSign::class.java,
        )
        register(
            BlockType.WARPED_BUTTON,
            Identifier.fromString("minecraft:warped_button"),
            BlockButton::class.java,
        )
        register(BlockType.WARPED_DOOR, Identifier.fromString("minecraft:warped_door"), BlockDoor::class.java)
        register(
            BlockType.WARPED_DOUBLE_SLAB,
            Identifier.fromString("minecraft:warped_double_slab"),
            BlockDoubleWarpedSlab::class.java,
        )
        register(
            BlockType.WARPED_FENCE,
            Identifier.fromString("minecraft:warped_fence"),
            BlockFence::class.java,
        )
        register(
            BlockType.WARPED_FENCE_GATE,
            Identifier.fromString("minecraft:warped_fence_gate"),
            BlockFenceGate::class.java,
        )
        register(BlockType.WARPED_FUNGUS, Identifier.fromString("minecraft:warped_fungus"))
        register(
            BlockType.WARPED_HYPHAE,
            Identifier.fromString("minecraft:warped_hyphae"),
            BlockWarpedHyphae::class.java,
        )
        register(BlockType.WARPED_NYLIUM, Identifier.fromString("minecraft:warped_nylium"))
        register(BlockType.WARPED_PLANKS, Identifier.fromString("minecraft:warped_planks"))
        register(
            BlockType.WARPED_PRESSURE_PLATE,
            Identifier.fromString("minecraft:warped_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(BlockType.WARPED_ROOTS, Identifier.fromString("minecraft:warped_roots"))
        register(
            BlockType.WARPED_SLAB,
            Identifier.fromString("minecraft:warped_slab"),
            BlockWarpedSlab::class.java,
        )
        register(
            BlockType.WARPED_STAIRS,
            Identifier.fromString("minecraft:warped_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.WARPED_STANDING_SIGN,
            Identifier.fromString("minecraft:warped_standing_sign"),
            BlockWarpedStandingSign::class.java,
        )
        register(
            BlockType.WARPED_STEM,
            Identifier.fromString("minecraft:warped_stem"),
            BlockWarpedStem::class.java,
        )
        register(
            BlockType.WARPED_TRAPDOOR,
            Identifier.fromString("minecraft:warped_trapdoor"),
            BlockTrapdoor::class.java,
        )
        register(
            BlockType.WARPED_WALL_SIGN,
            Identifier.fromString("minecraft:warped_wall_sign"),
            BlockWarpedWallSign::class.java,
        )
        register(BlockType.WARPED_WART_BLOCK, Identifier.fromString("minecraft:warped_wart_block"))
        register(BlockType.WATER, Identifier.fromString("minecraft:water"), BlockWater::class.java)
        register(
            BlockType.WATERLILY,
            Identifier.fromString("minecraft:waterlily"),
            BlockWaterlily::class.java,
        )
        register(BlockType.WAXED_COPPER, Identifier.fromString("minecraft:waxed_copper"))
        register(BlockType.WAXED_CUT_COPPER, Identifier.fromString("minecraft:waxed_cut_copper"))
        register(
            BlockType.WAXED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_cut_copper_slab"),
            BlockWaxedCutCopperSlab::class.java,
        )
        register(
            BlockType.WAXED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:waxed_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.WAXED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_double_cut_copper_slab"),
            BlockDoubleWaxedCutCopperSlab::class.java,
        )
        register(BlockType.WAXED_EXPOSED_COPPER, Identifier.fromString("minecraft:waxed_exposed_copper"))
        register(
            BlockType.WAXED_EXPOSED_CUT_COPPER,
            Identifier.fromString("minecraft:waxed_exposed_cut_copper"),
        )
        register(
            BlockType.WAXED_EXPOSED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_exposed_cut_copper_slab"),
            BlockWaxedExposedCutCopperSlab::class.java,
        )
        register(
            BlockType.WAXED_EXPOSED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:waxed_exposed_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_exposed_double_cut_copper_slab"),
            BlockDoubleWaxedExposedCutCopperSlab::class.java,
        )
        register(BlockType.WAXED_OXIDIZED_COPPER, Identifier.fromString("minecraft:waxed_oxidized_copper"))
        register(
            BlockType.WAXED_OXIDIZED_CUT_COPPER,
            Identifier.fromString("minecraft:waxed_oxidized_cut_copper"),
        )
        register(
            BlockType.WAXED_OXIDIZED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_oxidized_cut_copper_slab"),
            BlockWaxedOxidizedCutCopperSlab::class.java,
        )
        register(
            BlockType.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:waxed_oxidized_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_oxidized_double_cut_copper_slab"),
            BlockDoubleWaxedOxidizedCutCopperSlab::class.java,
        )
        register(BlockType.WAXED_WEATHERED_COPPER, Identifier.fromString("minecraft:waxed_weathered_copper"))
        register(
            BlockType.WAXED_WEATHERED_CUT_COPPER,
            Identifier.fromString("minecraft:waxed_weathered_cut_copper"),
        )
        register(
            BlockType.WAXED_WEATHERED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_weathered_cut_copper_slab"),
            BlockWaxedWeatheredCutCopperSlab::class.java,
        )
        register(
            BlockType.WAXED_WEATHERED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:waxed_weathered_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:waxed_weathered_double_cut_copper_slab"),
            BlockDoubleWaxedWeatheredCutCopperSlab::class.java,
        )
        register(BlockType.WEATHERED_COPPER, Identifier.fromString("minecraft:weathered_copper"))
        register(BlockType.WEATHERED_CUT_COPPER, Identifier.fromString("minecraft:weathered_cut_copper"))
        register(
            BlockType.WEATHERED_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:weathered_cut_copper_slab"),
            BlockWeatheredCutCopperSlab::class.java,
        )
        register(
            BlockType.WEATHERED_CUT_COPPER_STAIRS,
            Identifier.fromString("minecraft:weathered_cut_copper_stairs"),
            BlockStairs::class.java,
        )
        register(
            BlockType.WEATHERED_DOUBLE_CUT_COPPER_SLAB,
            Identifier.fromString("minecraft:weathered_double_cut_copper_slab"),
            BlockDoubleWeatheredCutCopperSlab::class.java,
        )
        register(BlockType.WEB, Identifier.fromString("minecraft:web"))
        register(BlockType.WEEPING_VINES, Identifier.fromString("minecraft:weeping_vines"))
        register(BlockType.WHEAT, Identifier.fromString("minecraft:wheat"))
        register(
            BlockType.WHITE_CANDLE,
            Identifier.fromString("minecraft:white_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.WHITE_CANDLE_CAKE, Identifier.fromString("minecraft:white_candle_cake"))
        register(
            BlockType.WHITE_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:white_glazed_terracotta"),
        )
        register(BlockType.WITHER_ROSE, Identifier.fromString("minecraft:wither_rose"))
        register(BlockType.WOOD, Identifier.fromString("minecraft:wood"), BlockWood::class.java)
        register(
            BlockType.OAK_BUTTON,
            Identifier.fromString("minecraft:wooden_button"),
            BlockButton::class.java,
        )
        register(BlockType.WOODEN_DOOR, Identifier.fromString("minecraft:wooden_door"), BlockDoor::class.java)
        register(
            BlockType.WOODEN_PRESSURE_PLATE,
            Identifier.fromString("minecraft:wooden_pressure_plate"),
            BlockPressurePlate::class.java,
        )
        register(
            BlockType.WOODEN_SLAB,
            Identifier.fromString("minecraft:wooden_slab"),
            BlockWoodenSlab::class.java,
        )
        register(
            BlockType.WHITE_WOOL,
            Identifier.fromString("minecraft:white_wool"),
            BlockWhiteWool::class.java,
        )
        register(
            BlockType.ORANGE_WOOL,
            Identifier.fromString("minecraft:orange_wool"),
            BlockOrangeWool::class.java,
        )
        register(
            BlockType.MAGENTA_WOOL,
            Identifier.fromString("minecraft:magenta_wool"),
            BlockMagentaWool::class.java,
        )
        register(
            BlockType.LIGHT_BLUE_WOOL,
            Identifier.fromString("minecraft:light_blue_wool"),
            BlockLightBlueWool::class.java,
        )
        register(
            BlockType.YELLOW_WOOL,
            Identifier.fromString("minecraft:yellow_wool"),
            BlockYellowWool::class.java,
        )
        register(BlockType.LIME_WOOL, Identifier.fromString("minecraft:lime_wool"), BlockLimeWool::class.java)
        register(BlockType.PINK_WOOL, Identifier.fromString("minecraft:pink_wool"), BlockPinkWool::class.java)
        register(BlockType.GRAY_WOOL, Identifier.fromString("minecraft:gray_wool"), BlockGrayWool::class.java)
        register(
            BlockType.SILVER_WOOL,
            Identifier.fromString("minecraft:silver_wool"),
            BlockSilverWool::class.java,
        )
        register(BlockType.CYAN_WOOL, Identifier.fromString("minecraft:cyan_wool"), BlockCyanWool::class.java)
        register(
            BlockType.PURPLE_WOOL,
            Identifier.fromString("minecraft:purple_wool"),
            BlockPurpleWool::class.java,
        )
        register(BlockType.BLUE_WOOL, Identifier.fromString("minecraft:blue_wool"), BlockBlueWool::class.java)
        register(
            BlockType.BROWN_WOOL,
            Identifier.fromString("minecraft:brown_wool"),
            BlockBrownWool::class.java,
        )
        register(
            BlockType.GREEN_WOOL,
            Identifier.fromString("minecraft:green_wool"),
            BlockGreenWool::class.java,
        )
        register(BlockType.RED_WOOL, Identifier.fromString("minecraft:red_wool"), BlockRedWool::class.java)
        register(
            BlockType.BLACK_WOOL,
            Identifier.fromString("minecraft:black_wool"),
            BlockBlackWool::class.java,
        )
        register(
            BlockType.YELLOW_CANDLE,
            Identifier.fromString("minecraft:yellow_candle"),
            BlockCandle::class.java,
        )
        register(BlockType.YELLOW_CANDLE_CAKE, Identifier.fromString("minecraft:yellow_candle_cake"))
        register(BlockType.YELLOW_FLOWER, Identifier.fromString("minecraft:yellow_flower"))
        register(
            BlockType.YELLOW_GLAZED_TERRACOTTA,
            Identifier.fromString("minecraft:yellow_glazed_terracotta"),
        )
    }

    fun initBlockProperties() {
        val GSON = Gson()
        try {
            Objects.requireNonNull(
                Bootstrap::class.java.classLoader.getResourceAsStream("block_properties.json"),
            ).use { inputStream ->
                val inputStreamReader = InputStreamReader(inputStream)
                val itemEntries =
                    GSON.fromJson<Map<String, Map<String, Any>>>(inputStreamReader, MutableMap::class.java)
                itemEntries.forEach { (identifier: String, map: Map<String, Any>) ->
                    BLOCK_PROPERTIES[Identifier.fromString(identifier)] = BlockProperties(
                        map["hardness"] as Double,
                        map["solid"] as Boolean,
                        map["transparent"] as Boolean,
                        ToolType.valueOf((map["tool_type"] as String?)!!),
                        TierType.valueOf((map["tier_type"] as String?)!!),
                        map["can_break_with_hand"] as Boolean,
                        map["can_pass_through"] as Boolean,
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun register(blockType: BlockType, identifier: Identifier, blockClass: Class<out Block>? = null) {
        IDENTIFIER_FROM_BLOCKTYPE[blockType] = identifier
        BLOCKTYPE_FROM_IDENTIFIER[identifier] = blockType
        if (blockClass != null) {
            BLOCKCLASS_FROM_BLOCKTYPE[blockType] = blockClass
        }
    }

    fun getIdentifier(blockType: BlockType?): Identifier? {
        return IDENTIFIER_FROM_BLOCKTYPE[blockType]
    }

    fun getBlockType(identifier: Identifier?): BlockType? {
        return BLOCKTYPE_FROM_IDENTIFIER[identifier]
    }

    fun getBlockClass(blockType: BlockType?): Class<out Block> {
        return BLOCKCLASS_FROM_BLOCKTYPE[blockType]!!
    }

    fun blockClassExists(blockType: BlockType?): Boolean {
        return BLOCKCLASS_FROM_BLOCKTYPE.containsKey(blockType)
    }

    fun getBlockProperties(identifier: Identifier?): BlockProperties? {
        return BLOCK_PROPERTIES[identifier]
    }
}
