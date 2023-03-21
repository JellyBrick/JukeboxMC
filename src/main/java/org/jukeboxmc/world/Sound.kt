package org.jukeboxmc.world

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class Sound(val sound: String) {
    AMBIENT_BASALT_DELTAS_MOOD("ambient.basalt_deltas.mood"), AMBIENT_CAVE("ambient.cave"), AMBIENT_CRIMSON_FOREST_MOOD(
        "ambient.crimson_forest.mood",
    ),
    AMBIENT_NETHER_WASTES_MOOD("ambient.nether_wastes.mood"), AMBIENT_SOULSAND_VALLEY_MOOD("ambient.soulsand_valley.mood"), AMBIENT_WARPED_FOREST_MOOD(
        "ambient.warped_forest.mood",
    ),
    AMBIENT_WEATHER_LIGHTNING_IMPACT("ambient.weather.lightning.impact"), AMBIENT_WEATHER_RAIN("ambient.weather.rain"), AMBIENT_WEATHER_THUNDER(
        "ambient.weather.thunder",
    ),
    ARMOR_EQUIP_CHAIN("armor.equip_chain"), ARMOR_EQUIP_DIAMOND("armor.equip_diamond"), ARMOR_EQUIP_GENERIC("armor.equip_generic"), ARMOR_EQUIP_GOLD(
        "armor.equip_gold",
    ),
    ARMOR_EQUIP_IRON("armor.equip_iron"), ARMOR_EQUIP_LEATHER("armor.equip_leather"), ARMOR_EQUIP_NETHERITE("armor.equip_netherite"), BEACON_ACTIVATE(
        "beacon.activate",
    ),
    BEACON_AMBIENT("beacon.ambient"), BEACON_DEACTIVATE("beacon.deactivate"), BEACON_POWER("beacon.power"), BLOCK_BAMBOO_BREAK(
        "block.bamboo.break",
    ),
    BLOCK_BAMBOO_FALL("block.bamboo.fall"), BLOCK_BAMBOO_HIT("block.bamboo.hit"), BLOCK_BAMBOO_PLACE("block.bamboo.place"), BLOCK_BAMBOO_STEP(
        "block.bamboo.step",
    ),
    BLOCK_BAMBOO_SAPLING_BREAK("block.bamboo_sapling.break"), BLOCK_BAMBOO_SAPLING_PLACE("block.bamboo_sapling.place"), BLOCK_BARREL_CLOSE(
        "block.barrel.close",
    ),
    BLOCK_BARREL_OPEN("block.barrel.open"), BLOCK_BEEHIVE_DRIP("block.beehive.drip"), BLOCK_BEEHIVE_ENTER("block.beehive.enter"), BLOCK_BEEHIVE_EXIT(
        "block.beehive.exit",
    ),
    BLOCK_BEEHIVE_SHEAR("block.beehive.shear"), BLOCK_BEEHIVE_WORK("block.beehive.work"), BLOCK_BELL_HIT("block.bell.hit"), BLOCK_BLASTFURNACE_FIRE_CRACKLE(
        "block.blastfurnace.fire_crackle",
    ),
    BLOCK_CAMPFIRE_CRACKLE("block.campfire.crackle"), BLOCK_CARTOGRAPHY_TABLE_USE("block.cartography_table.use"), BLOCK_CHORUSFLOWER_DEATH(
        "block.chorusflower.death",
    ),
    BLOCK_CHORUSFLOWER_GROW("block.chorusflower.grow"), BLOCK_COMPOSTER_EMPTY("block.composter.empty"), BLOCK_COMPOSTER_FILL(
        "block.composter.fill",
    ),
    BLOCK_COMPOSTER_FILL_SUCCESS("block.composter.fill_success"), BLOCK_COMPOSTER_READY("block.composter.ready"), BLOCK_END_PORTAL_SPAWN(
        "block.end_portal.spawn",
    ),
    BLOCK_END_PORTAL_FRAME_FILL("block.end_portal_frame.fill"), BLOCK_FALSE_PERMISSIONS("block.false_permissions"), BLOCK_FURNACE_LIT(
        "block.furnace.lit",
    ),
    BLOCK_GRINDSTONE_USE("block.grindstone.use"), BLOCK_ITEMFRAME_ADD_ITEM("block.itemframe.add_item"), BLOCK_ITEMFRAME_BREAK(
        "block.itemframe.break",
    ),
    BLOCK_ITEMFRAME_PLACE("block.itemframe.place"), BLOCK_ITEMFRAME_REMOVE_ITEM("block.itemframe.remove_item"), BLOCK_ITEMFRAME_ROTATE_ITEM(
        "block.itemframe.rotate_item",
    ),
    BLOCK_LANTERN_BREAK("block.lantern.break"), BLOCK_LANTERN_FALL("block.lantern.fall"), BLOCK_LANTERN_HIT("block.lantern.hit"), BLOCK_LANTERN_PLACE(
        "block.lantern.place",
    ),
    BLOCK_LANTERN_STEP("block.lantern.step"), BLOCK_LOOM_USE("block.loom.use"), BLOCK_SCAFFOLDING_BREAK("block.scaffolding.break"), BLOCK_SCAFFOLDING_CLIMB(
        "block.scaffolding.climb",
    ),
    BLOCK_SCAFFOLDING_FALL("block.scaffolding.fall"), BLOCK_SCAFFOLDING_HIT("block.scaffolding.hit"), BLOCK_SCAFFOLDING_PLACE(
        "block.scaffolding.place",
    ),
    BLOCK_SCAFFOLDING_STEP("block.scaffolding.step"), BLOCK_SMOKER_SMOKE("block.smoker.smoke"), BLOCK_STONECUTTER_USE("block.stonecutter.use"), BLOCK_SWEET_BERRY_BUSH_BREAK(
        "block.sweet_berry_bush.break",
    ),
    BLOCK_SWEET_BERRY_BUSH_HURT("block.sweet_berry_bush.hurt"), BLOCK_SWEET_BERRY_BUSH_PICK("block.sweet_berry_bush.pick"), BLOCK_SWEET_BERRY_BUSH_PLACE(
        "block.sweet_berry_bush.place",
    ),
    BLOCK_TURTLE_EGG_BREAK("block.turtle_egg.break"), BLOCK_TURTLE_EGG_CRACK("block.turtle_egg.crack"), BLOCK_TURTLE_EGG_DROP(
        "block.turtle_egg.drop",
    ),
    BOTTLE_DRAGONBREATH("bottle.dragonbreath"), BUBBLE_DOWN("bubble.down"), BUBBLE_DOWNINSIDE("bubble.downinside"), BUBBLE_POP(
        "bubble.pop",
    ),
    BUBBLE_UP("bubble.up"), BUBBLE_UPINSIDE("bubble.upinside"), BUCKET_EMPTY_FISH("bucket.empty_fish"), BUCKET_EMPTY_LAVA(
        "bucket.empty_lava",
    ),
    BUCKET_EMPTY_WATER("bucket.empty_water"), BUCKET_FILL_FISH("bucket.fill_fish"), BUCKET_FILL_LAVA("bucket.fill_lava"), BUCKET_FILL_WATER(
        "bucket.fill_water",
    ),
    CAMERA_TAKE_PICTURE("camera.take_picture"), CAULDRON_ADDDYE("cauldron.adddye"), CAULDRON_CLEANARMOR("cauldron.cleanarmor"), CAULDRON_CLEANBANNER(
        "cauldron.cleanbanner",
    ),
    CAULDRON_DYEARMOR("cauldron.dyearmor"), CAULDRON_EXPLODE("cauldron.explode"), CAULDRON_FILLPOTION("cauldron.fillpotion"), CAULDRON_FILLWATER(
        "cauldron.fillwater",
    ),
    CAULDRON_TAKEPOTION("cauldron.takepotion"), CAULDRON_TAKEWATER("cauldron.takewater"), CONDUIT_ACTIVATE("conduit.activate"), CONDUIT_AMBIENT(
        "conduit.ambient",
    ),
    CONDUIT_ATTACK("conduit.attack"), CONDUIT_DEACTIVATE("conduit.deactivate"), CONDUIT_SHORT("conduit.short"), CROSSBOW_LOADING_END(
        "crossbow.loading.end",
    ),
    CROSSBOW_LOADING_MIDDLE("crossbow.loading.middle"), CROSSBOW_LOADING_START("crossbow.loading.start"), CROSSBOW_QUICK_CHARGE_END(
        "crossbow.quick_charge.end",
    ),
    CROSSBOW_QUICK_CHARGE_MIDDLE("crossbow.quick_charge.middle"), CROSSBOW_QUICK_CHARGE_START("crossbow.quick_charge.start"), CROSSBOW_SHOOT(
        "crossbow.shoot",
    ),
    DAMAGE_FALLBIG("damage.fallbig"), DAMAGE_FALLSMALL("damage.fallsmall"), DIG_ANCIENT_DEBRIS("dig.ancient_debris"), DIG_BASALT(
        "dig.basalt",
    ),
    DIG_BONE_BLOCK("dig.bone_block"), DIG_CHAIN("dig.chain"), DIG_CLOTH("dig.cloth"), DIG_CORAL("dig.coral"), DIG_FUNGUS(
        "dig.fungus",
    ),
    DIG_GRASS("dig.grass"), DIG_GRAVEL("dig.gravel"), DIG_HONEY_BLOCK("dig.honey_block"), DIG_LODESTONE("dig.lodestone"), DIG_NETHER_BRICK(
        "dig.nether_brick",
    ),
    DIG_NETHER_GOLD_ORE("dig.nether_gold_ore"), DIG_NETHER_SPROUTS("dig.nether_sprouts"), DIG_NETHER_WART("dig.nether_wart"), DIG_NETHERITE(
        "dig.netherite",
    ),
    DIG_NETHERRACK("dig.netherrack"), DIG_NYLIUM("dig.nylium"), DIG_ROOTS("dig.roots"), DIG_SAND("dig.sand"), DIG_SHROOMLIGHT(
        "dig.shroomlight",
    ),
    DIG_SNOW("dig.snow"), DIG_SOUL_SAND("dig.soul_sand"), DIG_SOUL_SOIL("dig.soul_soil"), DIG_STEM("dig.stem"), DIG_STONE(
        "dig.stone",
    ),
    DIG_VINES("dig.vines"), DIG_WOOD("dig.wood"), ELYTRA_LOOP("elytra.loop"), ENTITY_ZOMBIE_CONVERTED_TO_DROWNED("entity.zombie.converted_to_drowned"), FALL_ANCIENT_DEBRIS(
        "fall.ancient_debris",
    ),
    FALL_BASALT("fall.basalt"), FALL_BONE_BLOCK("fall.bone_block"), FALL_CHAIN("fall.chain"), FALL_CLOTH("fall.cloth"), FALL_CORAL(
        "fall.coral",
    ),
    FALL_EGG("fall.egg"), FALL_GRASS("fall.grass"), FALL_GRAVEL("fall.gravel"), FALL_HONEY_BLOCK("fall.honey_block"), FALL_LADDER(
        "fall.ladder",
    ),
    FALL_NETHER_BRICK("fall.nether_brick"), FALL_NETHER_GOLD_ORE("fall.nether_gold_ore"), FALL_NETHER_SPROUTS("fall.nether_sprouts"), FALL_NETHER_WART(
        "fall.nether_wart",
    ),
    FALL_NETHERITE("fall.netherite"), FALL_NETHERRACK("fall.netherrack"), FALL_NYLIUM("fall.nylium"), FALL_ROOTS("fall.roots"), FALL_SAND(
        "fall.sand",
    ),
    FALL_SHROOMLIGHT("fall.shroomlight"), FALL_SLIME("fall.slime"), FALL_SNOW("fall.snow"), FALL_SOUL_SAND("fall.soul_sand"), FALL_SOUL_SOIL(
        "fall.soul_soil",
    ),
    FALL_STEM("fall.stem"), FALL_STONE("fall.stone"), FALL_VINES("fall.vines"), FALL_WOOD("fall.wood"), FIRE_FIRE("fire.fire"), FIRE_IGNITE(
        "fire.ignite",
    ),
    FIREWORK_BLAST("firework.blast"), FIREWORK_LARGE_BLAST("firework.large_blast"), FIREWORK_LAUNCH("firework.launch"), FIREWORK_SHOOT(
        "firework.shoot",
    ),
    FIREWORK_TWINKLE("firework.twinkle"), GAME_PLAYER_ATTACK_NODAMAGE("game.player.attack.nodamage"), GAME_PLAYER_ATTACK_STRONG(
        "game.player.attack.strong",
    ),
    GAME_PLAYER_DIE("game.player.die"), GAME_PLAYER_HURT("game.player.hurt"), HIT_ANCIENT_DEBRIS("hit.ancient_debris"), HIT_ANVIL(
        "hit.anvil",
    ),
    HIT_BASALT("hit.basalt"), HIT_BONE_BLOCK("hit.bone_block"), HIT_CHAIN("hit.chain"), HIT_CLOTH("hit.cloth"), HIT_CORAL(
        "hit.coral",
    ),
    HIT_GRASS("hit.grass"), HIT_GRAVEL("hit.gravel"), HIT_HONEY_BLOCK("hit.honey_block"), HIT_LADDER("hit.ladder"), HIT_NETHER_BRICK(
        "hit.nether_brick",
    ),
    HIT_NETHER_GOLD_ORE("hit.nether_gold_ore"), HIT_NETHER_SPROUTS("hit.nether_sprouts"), HIT_NETHER_WART("hit.nether_wart"), HIT_NETHERITE(
        "hit.netherite",
    ),
    HIT_NETHERRACK("hit.netherrack"), HIT_NYLIUM("hit.nylium"), HIT_ROOTS("hit.roots"), HIT_SAND("hit.sand"), HIT_SHROOMLIGHT(
        "hit.shroomlight",
    ),
    HIT_SLIME("hit.slime"), HIT_SNOW("hit.snow"), HIT_SOUL_SAND("hit.soul_sand"), HIT_SOUL_SOIL("hit.soul_soil"), HIT_STEM(
        "hit.stem",
    ),
    HIT_STONE("hit.stone"), HIT_VINES("hit.vines"), HIT_WOOD("hit.wood"), ITEM_BOOK_PAGE_TURN("item.book.page_turn"), ITEM_BOOK_PUT(
        "item.book.put",
    ),
    ITEM_SHIELD_BLOCK("item.shield.block"), ITEM_TRIDENT_HIT("item.trident.hit"), ITEM_TRIDENT_HIT_GROUND("item.trident.hit_ground"), ITEM_TRIDENT_RETURN(
        "item.trident.return",
    ),
    ITEM_TRIDENT_RIPTIDE_1("item.trident.riptide_1"), ITEM_TRIDENT_RIPTIDE_2("item.trident.riptide_2"), ITEM_TRIDENT_RIPTIDE_3(
        "item.trident.riptide_3",
    ),
    ITEM_TRIDENT_THROW("item.trident.throw"), ITEM_TRIDENT_THUNDER("item.trident.thunder"), JUMP_ANCIENT_DEBRIS("jump.ancient_debris"), JUMP_BASALT(
        "jump.basalt",
    ),
    JUMP_BONE_BLOCK("jump.bone_block"), JUMP_CHAIN("jump.chain"), JUMP_CLOTH("jump.cloth"), JUMP_CORAL("jump.coral"), JUMP_GRASS(
        "jump.grass",
    ),
    JUMP_GRAVEL("jump.gravel"), JUMP_HONEY_BLOCK("jump.honey_block"), JUMP_NETHER_BRICK("jump.nether_brick"), JUMP_NETHER_GOLD_ORE(
        "jump.nether_gold_ore",
    ),
    JUMP_NETHER_SPROUTS("jump.nether_sprouts"), JUMP_NETHER_WART("jump.nether_wart"), JUMP_NETHERITE("jump.netherite"), JUMP_NETHERRACK(
        "jump.netherrack",
    ),
    JUMP_NYLIUM("jump.nylium"), JUMP_ROOTS("jump.roots"), JUMP_SAND("jump.sand"), JUMP_SHROOMLIGHT("jump.shroomlight"), JUMP_SLIME(
        "jump.slime",
    ),
    JUMP_SNOW("jump.snow"), JUMP_SOUL_SAND("jump.soul_sand"), JUMP_SOUL_SOIL("jump.soul_soil"), JUMP_STEM("jump.stem"), JUMP_STONE(
        "jump.stone",
    ),
    JUMP_VINES("jump.vines"), JUMP_WOOD("jump.wood"), LAND_ANCIENT_DEBRIS("land.ancient_debris"), LAND_BASALT("land.basalt"), LAND_BONE_BLOCK(
        "land.bone_block",
    ),
    LAND_CHAIN("land.chain"), LAND_CLOTH("land.cloth"), LAND_CORAL("land.coral"), LAND_GRASS("land.grass"), LAND_GRAVEL(
        "land.gravel",
    ),
    LAND_HONEY_BLOCK("land.honey_block"), LAND_NETHER_BRICK("land.nether_brick"), LAND_NETHER_GOLD_ORE("land.nether_gold_ore"), LAND_NETHER_SPROUTS(
        "land.nether_sprouts",
    ),
    LAND_NETHER_WART("land.nether_wart"), LAND_NETHERITE("land.netherite"), LAND_NETHERRACK("land.netherrack"), LAND_NYLIUM(
        "land.nylium",
    ),
    LAND_ROOTS("land.roots"), LAND_SAND("land.sand"), LAND_SHROOMLIGHT("land.shroomlight"), LAND_SLIME("land.slime"), LAND_SNOW(
        "land.snow",
    ),
    LAND_SOUL_SAND("land.soul_sand"), LAND_SOUL_SOIL("land.soul_soil"), LAND_STEM("land.stem"), LAND_STONE("land.stone"), LAND_VINES(
        "land.vines",
    ),
    LAND_WOOD("land.wood"), LEASHKNOT_BREAK("leashknot.break"), LEASHKNOT_PLACE("leashknot.place"), LIQUID_LAVA("liquid.lava"), LIQUID_LAVAPOP(
        "liquid.lavapop",
    ),
    LIQUID_WATER("liquid.water"), LODESTONE_COMPASS_LINK_COMPASS_TO_LODESTONE("lodestone_compass.link_compass_to_lodestone"), MINECART_BASE(
        "minecart.base",
    ),
    MINECART_INSIDE("minecart.inside"), MOB_AGENT_SPAWN("mob.agent.spawn"), MOB_ARMOR_STAND_BREAK("mob.armor_stand.break"), MOB_ARMOR_STAND_HIT(
        "mob.armor_stand.hit",
    ),
    MOB_ARMOR_STAND_LAND("mob.armor_stand.land"), MOB_ARMOR_STAND_PLACE("mob.armor_stand.place"), MOB_BAT_DEATH("mob.bat.death"), MOB_BAT_HURT(
        "mob.bat.hurt",
    ),
    MOB_BAT_IDLE("mob.bat.idle"), MOB_BAT_TAKEOFF("mob.bat.takeoff"), MOB_BEE_AGGRESSIVE("mob.bee.aggressive"), MOB_BEE_DEATH(
        "mob.bee.death",
    ),
    MOB_BEE_HURT("mob.bee.hurt"), MOB_BEE_LOOP("mob.bee.loop"), MOB_BEE_POLLINATE("mob.bee.pollinate"), MOB_BEE_STING("mob.bee.sting"), MOB_BLAZE_BREATHE(
        "mob.blaze.breathe",
    ),
    MOB_BLAZE_DEATH("mob.blaze.death"), MOB_BLAZE_HIT("mob.blaze.hit"), MOB_BLAZE_SHOOT("mob.blaze.shoot"), MOB_CAT_BEG(
        "mob.cat.beg",
    ),
    MOB_CAT_EAT("mob.cat.eat"), MOB_CAT_HISS("mob.cat.hiss"), MOB_CAT_HIT("mob.cat.hit"), MOB_CAT_MEOW("mob.cat.meow"), MOB_CAT_PURR(
        "mob.cat.purr",
    ),
    MOB_CAT_PURREOW("mob.cat.purreow"), MOB_CAT_STRAYMEOW("mob.cat.straymeow"), MOB_CHICKEN_HURT("mob.chicken.hurt"), MOB_CHICKEN_PLOP(
        "mob.chicken.plop",
    ),
    MOB_CHICKEN_SAY("mob.chicken.say"), MOB_CHICKEN_STEP("mob.chicken.step"), MOB_COW_HURT("mob.cow.hurt"), MOB_COW_MILK(
        "mob.cow.milk",
    ),
    MOB_COW_SAY("mob.cow.say"), MOB_COW_STEP("mob.cow.step"), MOB_CREEPER_DEATH("mob.creeper.death"), MOB_CREEPER_SAY("mob.creeper.say"), MOB_DOLPHIN_ATTACK(
        "mob.dolphin.attack",
    ),
    MOB_DOLPHIN_BLOWHOLE("mob.dolphin.blowhole"), MOB_DOLPHIN_DEATH("mob.dolphin.death"), MOB_DOLPHIN_EAT("mob.dolphin.eat"), MOB_DOLPHIN_HURT(
        "mob.dolphin.hurt",
    ),
    MOB_DOLPHIN_IDLE("mob.dolphin.idle"), MOB_DOLPHIN_IDLE_WATER("mob.dolphin.idle_water"), MOB_DOLPHIN_JUMP("mob.dolphin.jump"), MOB_DOLPHIN_PLAY(
        "mob.dolphin.play",
    ),
    MOB_DOLPHIN_SPLASH("mob.dolphin.splash"), MOB_DOLPHIN_SWIM("mob.dolphin.swim"), MOB_DROWNED_DEATH("mob.drowned.death"), MOB_DROWNED_DEATH_WATER(
        "mob.drowned.death_water",
    ),
    MOB_DROWNED_HURT("mob.drowned.hurt"), MOB_DROWNED_HURT_WATER("mob.drowned.hurt_water"), MOB_DROWNED_SAY("mob.drowned.say"), MOB_DROWNED_SAY_WATER(
        "mob.drowned.say_water",
    ),
    MOB_DROWNED_SHOOT("mob.drowned.shoot"), MOB_DROWNED_STEP("mob.drowned.step"), MOB_DROWNED_SWIM("mob.drowned.swim"), MOB_ELDERGUARDIAN_CURSE(
        "mob.elderguardian.curse",
    ),
    MOB_ELDERGUARDIAN_DEATH("mob.elderguardian.death"), MOB_ELDERGUARDIAN_HIT("mob.elderguardian.hit"), MOB_ELDERGUARDIAN_IDLE(
        "mob.elderguardian.idle",
    ),
    MOB_ENDERDRAGON_DEATH("mob.enderdragon.death"), MOB_ENDERDRAGON_FLAP("mob.enderdragon.flap"), MOB_ENDERDRAGON_GROWL(
        "mob.enderdragon.growl",
    ),
    MOB_ENDERDRAGON_HIT("mob.enderdragon.hit"), MOB_ENDERMEN_DEATH("mob.endermen.death"), MOB_ENDERMEN_HIT("mob.endermen.hit"), MOB_ENDERMEN_IDLE(
        "mob.endermen.idle",
    ),
    MOB_ENDERMEN_PORTAL("mob.endermen.portal"), MOB_ENDERMEN_SCREAM("mob.endermen.scream"), MOB_ENDERMEN_STARE("mob.endermen.stare"), MOB_ENDERMITE_HIT(
        "mob.endermite.hit",
    ),
    MOB_ENDERMITE_KILL("mob.endermite.kill"), MOB_ENDERMITE_SAY("mob.endermite.say"), MOB_ENDERMITE_STEP("mob.endermite.step"), MOB_EVOCATION_FANGS_ATTACK(
        "mob.evocation_fangs.attack",
    ),
    MOB_EVOCATION_ILLAGER_AMBIENT("mob.evocation_illager.ambient"), MOB_EVOCATION_ILLAGER_CAST_SPELL("mob.evocation_illager.cast_spell"), MOB_EVOCATION_ILLAGER_CELEBRATE(
        "mob.evocation_illager.celebrate",
    ),
    MOB_EVOCATION_ILLAGER_DEATH("mob.evocation_illager.death"), MOB_EVOCATION_ILLAGER_HURT("mob.evocation_illager.hurt"), MOB_EVOCATION_ILLAGER_PREPARE_ATTACK(
        "mob.evocation_illager.prepare_attack",
    ),
    MOB_EVOCATION_ILLAGER_PREPARE_SUMMON("mob.evocation_illager.prepare_summon"), MOB_EVOCATION_ILLAGER_PREPARE_WOLOLO("mob.evocation_illager.prepare_wololo"), MOB_FISH_FLOP(
        "mob.fish.flop",
    ),
    MOB_FISH_HURT("mob.fish.hurt"), MOB_FISH_STEP("mob.fish.step"), MOB_FOX_AGGRO("mob.fox.aggro"), MOB_FOX_AMBIENT("mob.fox.ambient"), MOB_FOX_BITE(
        "mob.fox.bite",
    ),
    MOB_FOX_DEATH("mob.fox.death"), MOB_FOX_EAT("mob.fox.eat"), MOB_FOX_HURT("mob.fox.hurt"), MOB_FOX_SCREECH("mob.fox.screech"), MOB_FOX_SLEEP(
        "mob.fox.sleep",
    ),
    MOB_FOX_SNIFF("mob.fox.sniff"), MOB_FOX_SPIT("mob.fox.spit"), MOB_GHAST_AFFECTIONATE_SCREAM("mob.ghast.affectionate_scream"), MOB_GHAST_CHARGE(
        "mob.ghast.charge",
    ),
    MOB_GHAST_DEATH("mob.ghast.death"), MOB_GHAST_FIREBALL("mob.ghast.fireball"), MOB_GHAST_MOAN("mob.ghast.moan"), MOB_GHAST_SCREAM(
        "mob.ghast.scream",
    ),
    MOB_GUARDIAN_AMBIENT("mob.guardian.ambient"), MOB_GUARDIAN_ATTACK_LOOP("mob.guardian.attack_loop"), MOB_GUARDIAN_DEATH(
        "mob.guardian.death",
    ),
    MOB_GUARDIAN_FLOP("mob.guardian.flop"), MOB_GUARDIAN_HIT("mob.guardian.hit"), MOB_GUARDIAN_LAND_DEATH("mob.guardian.land_death"), MOB_GUARDIAN_LAND_HIT(
        "mob.guardian.land_hit",
    ),
    MOB_GUARDIAN_LAND_IDLE("mob.guardian.land_idle"), MOB_HOGLIN_AMBIENT("mob.hoglin.ambient"), MOB_HOGLIN_ANGRY("mob.hoglin.angry"), MOB_HOGLIN_ATTACK(
        "mob.hoglin.attack",
    ),
    MOB_HOGLIN_DEATH("mob.hoglin.death"), MOB_HOGLIN_HOWL("mob.hoglin.howl"), MOB_HOGLIN_HURT("mob.hoglin.hurt"), MOB_HOGLIN_RETREAT(
        "mob.hoglin.retreat",
    ),
    MOB_HOGLIN_STEP("mob.hoglin.step"), MOB_HORSE_ANGRY("mob.horse.angry"), MOB_HORSE_ARMOR("mob.horse.armor"), MOB_HORSE_BREATHE(
        "mob.horse.breathe",
    ),
    MOB_HORSE_DEATH("mob.horse.death"), MOB_HORSE_DONKEY_ANGRY("mob.horse.donkey.angry"), MOB_HORSE_DONKEY_DEATH("mob.horse.donkey.death"), MOB_HORSE_DONKEY_HIT(
        "mob.horse.donkey.hit",
    ),
    MOB_HORSE_DONKEY_IDLE("mob.horse.donkey.idle"), MOB_HORSE_EAT("mob.horse.eat"), MOB_HORSE_GALLOP("mob.horse.gallop"), MOB_HORSE_HIT(
        "mob.horse.hit",
    ),
    MOB_HORSE_IDLE("mob.horse.idle"), MOB_HORSE_JUMP("mob.horse.jump"), MOB_HORSE_LAND("mob.horse.land"), MOB_HORSE_LEATHER(
        "mob.horse.leather",
    ),
    MOB_HORSE_SKELETON_DEATH("mob.horse.skeleton.death"), MOB_HORSE_SKELETON_HIT("mob.horse.skeleton.hit"), MOB_HORSE_SKELETON_IDLE(
        "mob.horse.skeleton.idle",
    ),
    MOB_HORSE_SOFT("mob.horse.soft"), MOB_HORSE_WOOD("mob.horse.wood"), MOB_HORSE_ZOMBIE_DEATH("mob.horse.zombie.death"), MOB_HORSE_ZOMBIE_HIT(
        "mob.horse.zombie.hit",
    ),
    MOB_HORSE_ZOMBIE_IDLE("mob.horse.zombie.idle"), MOB_HUSK_AMBIENT("mob.husk.ambient"), MOB_HUSK_DEATH("mob.husk.death"), MOB_HUSK_HURT(
        "mob.husk.hurt",
    ),
    MOB_HUSK_STEP("mob.husk.step"), MOB_IRONGOLEM_DEATH("mob.irongolem.death"), MOB_IRONGOLEM_HIT("mob.irongolem.hit"), MOB_IRONGOLEM_THROW(
        "mob.irongolem.throw",
    ),
    MOB_IRONGOLEM_WALK("mob.irongolem.walk"), MOB_LLAMA_ANGRY("mob.llama.angry"), MOB_LLAMA_DEATH("mob.llama.death"), MOB_LLAMA_EAT(
        "mob.llama.eat",
    ),
    MOB_LLAMA_HURT("mob.llama.hurt"), MOB_LLAMA_IDLE("mob.llama.idle"), MOB_LLAMA_SPIT("mob.llama.spit"), MOB_LLAMA_STEP(
        "mob.llama.step",
    ),
    MOB_LLAMA_SWAG("mob.llama.swag"), MOB_MAGMACUBE_BIG("mob.magmacube.big"), MOB_MAGMACUBE_JUMP("mob.magmacube.jump"), MOB_MAGMACUBE_SMALL(
        "mob.magmacube.small",
    ),
    MOB_MOOSHROOM_CONVERT("mob.mooshroom.convert"), MOB_MOOSHROOM_EAT("mob.mooshroom.eat"), MOB_MOOSHROOM_SUSPICIOUS_MILK(
        "mob.mooshroom.suspicious_milk",
    ),
    MOB_OCELOT_DEATH("mob.ocelot.death"), MOB_OCELOT_IDLE("mob.ocelot.idle"), MOB_PANDA_BITE("mob.panda.bite"), MOB_PANDA_CANT_BREED(
        "mob.panda.cant_breed",
    ),
    MOB_PANDA_DEATH("mob.panda.death"), MOB_PANDA_EAT("mob.panda.eat"), MOB_PANDA_HURT("mob.panda.hurt"), MOB_PANDA_IDLE(
        "mob.panda.idle",
    ),
    MOB_PANDA_IDLE_AGGRESSIVE("mob.panda.idle.aggressive"), MOB_PANDA_IDLE_WORRIED("mob.panda.idle.worried"), MOB_PANDA_PRESNEEZE(
        "mob.panda.presneeze",
    ),
    MOB_PANDA_SNEEZE("mob.panda.sneeze"), MOB_PANDA_STEP("mob.panda.step"), MOB_PANDA_BABY_IDLE("mob.panda_baby.idle"), MOB_PARROT_DEATH(
        "mob.parrot.death",
    ),
    MOB_PARROT_EAT("mob.parrot.eat"), MOB_PARROT_FLY("mob.parrot.fly"), MOB_PARROT_HURT("mob.parrot.hurt"), MOB_PARROT_IDLE(
        "mob.parrot.idle",
    ),
    MOB_PARROT_STEP("mob.parrot.step"), MOB_PHANTOM_BITE("mob.phantom.bite"), MOB_PHANTOM_DEATH("mob.phantom.death"), MOB_PHANTOM_FLAP(
        "mob.phantom.flap",
    ),
    MOB_PHANTOM_HURT("mob.phantom.hurt"), MOB_PHANTOM_IDLE("mob.phantom.idle"), MOB_PHANTOM_SWOOP("mob.phantom.swoop"), MOB_PIG_BOOST(
        "mob.pig.boost",
    ),
    MOB_PIG_DEATH("mob.pig.death"), MOB_PIG_SAY("mob.pig.say"), MOB_PIG_STEP("mob.pig.step"), MOB_PIGLIN_ADMIRING_ITEM("mob.piglin.admiring_item"), MOB_PIGLIN_AMBIENT(
        "mob.piglin.ambient",
    ),
    MOB_PIGLIN_ANGRY("mob.piglin.angry"), MOB_PIGLIN_CELEBRATE("mob.piglin.celebrate"), MOB_PIGLIN_CONVERTED_TO_ZOMBIFIED(
        "mob.piglin.converted_to_zombified",
    ),
    MOB_PIGLIN_DEATH("mob.piglin.death"), MOB_PIGLIN_HURT("mob.piglin.hurt"), MOB_PIGLIN_JEALOUS("mob.piglin.jealous"), MOB_PIGLIN_RETREAT(
        "mob.piglin.retreat",
    ),
    MOB_PIGLIN_STEP("mob.piglin.step"), MOB_PIGLIN_BRUTE_AMBIENT("mob.piglin_brute.ambient"), MOB_PIGLIN_BRUTE_ANGRY("mob.piglin_brute.angry"), MOB_PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED(
        "mob.piglin_brute.converted_to_zombified",
    ),
    MOB_PIGLIN_BRUTE_DEATH("mob.piglin_brute.death"), MOB_PIGLIN_BRUTE_HURT("mob.piglin_brute.hurt"), MOB_PIGLIN_BRUTE_STEP(
        "mob.piglin_brute.step",
    ),
    MOB_PILLAGER_CELEBRATE("mob.pillager.celebrate"), MOB_PILLAGER_DEATH("mob.pillager.death"), MOB_PILLAGER_HURT("mob.pillager.hurt"), MOB_PILLAGER_IDLE(
        "mob.pillager.idle",
    ),
    MOB_POLARBEAR_DEATH("mob.polarbear.death"), MOB_POLARBEAR_HURT("mob.polarbear.hurt"), MOB_POLARBEAR_IDLE("mob.polarbear.idle"), MOB_POLARBEAR_STEP(
        "mob.polarbear.step",
    ),
    MOB_POLARBEAR_WARNING("mob.polarbear.warning"), MOB_POLARBEAR_BABY_IDLE("mob.polarbear_baby.idle"), MOB_RABBIT_DEATH(
        "mob.rabbit.death",
    ),
    MOB_RABBIT_HOP("mob.rabbit.hop"), MOB_RABBIT_HURT("mob.rabbit.hurt"), MOB_RABBIT_IDLE("mob.rabbit.idle"), MOB_RAVAGER_AMBIENT(
        "mob.ravager.ambient",
    ),
    MOB_RAVAGER_BITE("mob.ravager.bite"), MOB_RAVAGER_CELEBRATE("mob.ravager.celebrate"), MOB_RAVAGER_DEATH("mob.ravager.death"), MOB_RAVAGER_HURT(
        "mob.ravager.hurt",
    ),
    MOB_RAVAGER_ROAR("mob.ravager.roar"), MOB_RAVAGER_STEP("mob.ravager.step"), MOB_RAVAGER_STUN("mob.ravager.stun"), MOB_SHEEP_SAY(
        "mob.sheep.say",
    ),
    MOB_SHEEP_SHEAR("mob.sheep.shear"), MOB_SHEEP_STEP("mob.sheep.step"), MOB_SHULKER_AMBIENT("mob.shulker.ambient"), MOB_SHULKER_BULLET_HIT(
        "mob.shulker.bullet.hit",
    ),
    MOB_SHULKER_CLOSE("mob.shulker.close"), MOB_SHULKER_CLOSE_HURT("mob.shulker.close.hurt"), MOB_SHULKER_DEATH("mob.shulker.death"), MOB_SHULKER_HURT(
        "mob.shulker.hurt",
    ),
    MOB_SHULKER_OPEN("mob.shulker.open"), MOB_SHULKER_SHOOT("mob.shulker.shoot"), MOB_SHULKER_TELEPORT("mob.shulker.teleport"), MOB_SILVERFISH_HIT(
        "mob.silverfish.hit",
    ),
    MOB_SILVERFISH_KILL("mob.silverfish.kill"), MOB_SILVERFISH_SAY("mob.silverfish.say"), MOB_SILVERFISH_STEP("mob.silverfish.step"), MOB_SKELETON_DEATH(
        "mob.skeleton.death",
    ),
    MOB_SKELETON_HURT("mob.skeleton.hurt"), MOB_SKELETON_SAY("mob.skeleton.say"), MOB_SKELETON_STEP("mob.skeleton.step"), MOB_SLIME_ATTACK(
        "mob.slime.attack",
    ),
    MOB_SLIME_BIG("mob.slime.big"), MOB_SLIME_DEATH("mob.slime.death"), MOB_SLIME_HURT("mob.slime.hurt"), MOB_SLIME_JUMP(
        "mob.slime.jump",
    ),
    MOB_SLIME_SMALL("mob.slime.small"), MOB_SLIME_SQUISH("mob.slime.squish"), MOB_SNOWGOLEM_DEATH("mob.snowgolem.death"), MOB_SNOWGOLEM_HURT(
        "mob.snowgolem.hurt",
    ),
    MOB_SNOWGOLEM_SHOOT("mob.snowgolem.shoot"), MOB_SPIDER_DEATH("mob.spider.death"), MOB_SPIDER_SAY("mob.spider.say"), MOB_SPIDER_STEP(
        "mob.spider.step",
    ),
    MOB_SQUID_AMBIENT("mob.squid.ambient"), MOB_SQUID_DEATH("mob.squid.death"), MOB_SQUID_HURT("mob.squid.hurt"), MOB_STRAY_AMBIENT(
        "mob.stray.ambient",
    ),
    MOB_STRAY_DEATH("mob.stray.death"), MOB_STRAY_HURT("mob.stray.hurt"), MOB_STRAY_STEP("mob.stray.step"), MOB_STRIDER_DEATH(
        "mob.strider.death",
    ),
    MOB_STRIDER_EAT("mob.strider.eat"), MOB_STRIDER_HURT("mob.strider.hurt"), MOB_STRIDER_IDLE("mob.strider.idle"), MOB_STRIDER_PANIC(
        "mob.strider.panic",
    ),
    MOB_STRIDER_STEP("mob.strider.step"), MOB_STRIDER_STEP_LAVA("mob.strider.step_lava"), MOB_STRIDER_TEMPT("mob.strider.tempt"), MOB_TURTLE_AMBIENT(
        "mob.turtle.ambient",
    ),
    MOB_TURTLE_DEATH("mob.turtle.death"), MOB_TURTLE_HURT("mob.turtle.hurt"), MOB_TURTLE_STEP("mob.turtle.step"), MOB_TURTLE_SWIM(
        "mob.turtle.swim",
    ),
    MOB_TURTLE_BABY_BORN("mob.turtle_baby.born"), MOB_TURTLE_BABY_DEATH("mob.turtle_baby.death"), MOB_TURTLE_BABY_HURT("mob.turtle_baby.hurt"), MOB_TURTLE_BABY_STEP(
        "mob.turtle_baby.step",
    ),
    MOB_VEX_AMBIENT("mob.vex.ambient"), MOB_VEX_CHARGE("mob.vex.charge"), MOB_VEX_DEATH("mob.vex.death"), MOB_VEX_HURT("mob.vex.hurt"), MOB_VILLAGER_DEATH(
        "mob.villager.death",
    ),
    MOB_VILLAGER_HAGGLE("mob.villager.haggle"), MOB_VILLAGER_HIT("mob.villager.hit"), MOB_VILLAGER_IDLE("mob.villager.idle"), MOB_VILLAGER_NO(
        "mob.villager.no",
    ),
    MOB_VILLAGER_YES("mob.villager.yes"), MOB_VINDICATOR_CELEBRATE("mob.vindicator.celebrate"), MOB_VINDICATOR_DEATH("mob.vindicator.death"), MOB_VINDICATOR_HURT(
        "mob.vindicator.hurt",
    ),
    MOB_VINDICATOR_IDLE("mob.vindicator.idle"), MOB_WANDERINGTRADER_DEATH("mob.wanderingtrader.death"), MOB_WANDERINGTRADER_DISAPPEARED(
        "mob.wanderingtrader.disappeared",
    ),
    MOB_WANDERINGTRADER_DRINK_MILK("mob.wanderingtrader.drink_milk"), MOB_WANDERINGTRADER_DRINK_POTION("mob.wanderingtrader.drink_potion"), MOB_WANDERINGTRADER_HAGGLE(
        "mob.wanderingtrader.haggle",
    ),
    MOB_WANDERINGTRADER_HURT("mob.wanderingtrader.hurt"), MOB_WANDERINGTRADER_IDLE("mob.wanderingtrader.idle"), MOB_WANDERINGTRADER_NO(
        "mob.wanderingtrader.no",
    ),
    MOB_WANDERINGTRADER_REAPPEARED("mob.wanderingtrader.reappeared"), MOB_WANDERINGTRADER_YES("mob.wanderingtrader.yes"), MOB_WITCH_AMBIENT(
        "mob.witch.ambient",
    ),
    MOB_WITCH_CELEBRATE("mob.witch.celebrate"), MOB_WITCH_DEATH("mob.witch.death"), MOB_WITCH_DRINK("mob.witch.drink"), MOB_WITCH_HURT(
        "mob.witch.hurt",
    ),
    MOB_WITCH_THROW("mob.witch.throw"), MOB_WITHER_AMBIENT("mob.wither.ambient"), MOB_WITHER_BREAK_BLOCK("mob.wither.break_block"), MOB_WITHER_DEATH(
        "mob.wither.death",
    ),
    MOB_WITHER_HURT("mob.wither.hurt"), MOB_WITHER_SHOOT("mob.wither.shoot"), MOB_WITHER_SPAWN("mob.wither.spawn"), MOB_WOLF_BARK(
        "mob.wolf.bark",
    ),
    MOB_WOLF_DEATH("mob.wolf.death"), MOB_WOLF_GROWL("mob.wolf.growl"), MOB_WOLF_HURT("mob.wolf.hurt"), MOB_WOLF_PANTING(
        "mob.wolf.panting",
    ),
    MOB_WOLF_SHAKE("mob.wolf.shake"), MOB_WOLF_STEP("mob.wolf.step"), MOB_WOLF_WHINE("mob.wolf.whine"), MOB_ZOGLIN_ANGRY(
        "mob.zoglin.angry",
    ),
    MOB_ZOGLIN_ATTACK("mob.zoglin.attack"), MOB_ZOGLIN_DEATH("mob.zoglin.death"), MOB_ZOGLIN_HURT("mob.zoglin.hurt"), MOB_ZOGLIN_IDLE(
        "mob.zoglin.idle",
    ),
    MOB_ZOGLIN_STEP("mob.zoglin.step"), MOB_ZOMBIE_DEATH("mob.zombie.death"), MOB_ZOMBIE_HURT("mob.zombie.hurt"), MOB_ZOMBIE_REMEDY(
        "mob.zombie.remedy",
    ),
    MOB_ZOMBIE_SAY("mob.zombie.say"), MOB_ZOMBIE_STEP("mob.zombie.step"), MOB_ZOMBIE_UNFECT("mob.zombie.unfect"), MOB_ZOMBIE_WOOD(
        "mob.zombie.wood",
    ),
    MOB_ZOMBIE_WOODBREAK("mob.zombie.woodbreak"), MOB_ZOMBIE_VILLAGER_DEATH("mob.zombie_villager.death"), MOB_ZOMBIE_VILLAGER_HURT(
        "mob.zombie_villager.hurt",
    ),
    MOB_ZOMBIE_VILLAGER_SAY("mob.zombie_villager.say"), MOB_ZOMBIEPIG_ZPIG("mob.zombiepig.zpig"), MOB_ZOMBIEPIG_ZPIGANGRY(
        "mob.zombiepig.zpigangry",
    ),
    MOB_ZOMBIEPIG_ZPIGDEATH("mob.zombiepig.zpigdeath"), MOB_ZOMBIEPIG_ZPIGHURT("mob.zombiepig.zpighurt"), MUSIC_GAME("music.game"), MUSIC_GAME_CREATIVE(
        "music.game.creative",
    ),
    MUSIC_GAME_CREDITS("music.game.credits"), MUSIC_GAME_CRIMSON_FOREST("music.game.crimson_forest"), MUSIC_GAME_END("music.game.end"), MUSIC_GAME_ENDBOSS(
        "music.game.endboss",
    ),
    MUSIC_GAME_NETHER("music.game.nether"), MUSIC_GAME_NETHER_WASTES("music.game.nether_wastes"), MUSIC_GAME_SOULSAND_VALLEY(
        "music.game.soulsand_valley",
    ),
    MUSIC_GAME_WATER("music.game.water"), MUSIC_MENU("music.menu"), NOTE_BANJO("note.banjo"), NOTE_BASS("note.bass"), NOTE_BASSATTACK(
        "note.bassattack",
    ),
    NOTE_BD("note.bd"), NOTE_BELL("note.bell"), NOTE_BIT("note.bit"), NOTE_CHIME("note.chime"), NOTE_COW_BELL("note.cow_bell"), NOTE_DIDGERIDOO(
        "note.didgeridoo",
    ),
    NOTE_FLUTE("note.flute"), NOTE_GUITAR("note.guitar"), NOTE_HARP("note.harp"), NOTE_HAT("note.hat"), NOTE_IRON_XYLOPHONE(
        "note.iron_xylophone",
    ),
    NOTE_PLING("note.pling"), NOTE_SNARE("note.snare"), NOTE_XYLOPHONE("note.xylophone"), PARTICLE_SOUL_ESCAPE("particle.soul_escape"), PORTAL_PORTAL(
        "portal.portal",
    ),
    PORTAL_TRAVEL("portal.travel"), PORTAL_TRIGGER("portal.trigger"), RAID_HORN("raid.horn"), RANDOM_ANVIL_BREAK("random.anvil_break"), RANDOM_ANVIL_LAND(
        "random.anvil_land",
    ),
    RANDOM_ANVIL_USE("random.anvil_use"), RANDOM_BOW("random.bow"), RANDOM_BOWHIT("random.bowhit"), RANDOM_BREAK("random.break"), RANDOM_BURP(
        "random.burp",
    ),
    RANDOM_CHESTCLOSED("random.chestclosed"), RANDOM_CHESTOPEN("random.chestopen"), RANDOM_CLICK("random.click"), RANDOM_DOOR_CLOSE(
        "random.door_close",
    ),
    RANDOM_DOOR_OPEN("random.door_open"), RANDOM_DRINK("random.drink"), RANDOM_DRINK_HONEY("random.drink_honey"), RANDOM_EAT(
        "random.eat",
    ),
    RANDOM_ENDERCHESTCLOSED("random.enderchestclosed"), RANDOM_ENDERCHESTOPEN("random.enderchestopen"), RANDOM_EXPLODE("random.explode"), RANDOM_FIZZ(
        "random.fizz",
    ),
    RANDOM_FUSE("random.fuse"), RANDOM_GLASS("random.glass"), RANDOM_HURT("random.hurt"), RANDOM_LEVELUP("random.levelup"), RANDOM_ORB(
        "random.orb",
    ),
    RANDOM_POP("random.pop"), RANDOM_POP2("random.pop2"), RANDOM_POTION_BREWED("random.potion.brewed"), RANDOM_SCREENSHOT(
        "random.screenshot",
    ),
    RANDOM_SHULKERBOXCLOSED("random.shulkerboxclosed"), RANDOM_SHULKERBOXOPEN("random.shulkerboxopen"), RANDOM_SPLASH("random.splash"), RANDOM_SWIM(
        "random.swim",
    ),
    RANDOM_TOAST("random.toast"), RANDOM_TOTEM("random.totem"), RECORD_11("record.11"), RECORD_13("record.13"), RECORD_BLOCKS(
        "record.blocks",
    ),
    RECORD_CAT("record.cat"), RECORD_CHIRP("record.chirp"), RECORD_FAR("record.far"), RECORD_MALL("record.mall"), RECORD_MELLOHI(
        "record.mellohi",
    ),
    RECORD_PIGSTEP("record.pigstep"), RECORD_STAL("record.stal"), RECORD_STRAD("record.strad"), RECORD_WAIT("record.wait"), RECORD_WARD(
        "record.ward",
    ),
    RESPAWN_ANCHOR_AMBIENT("respawn_anchor.ambient"), RESPAWN_ANCHOR_CHARGE("respawn_anchor.charge"), RESPAWN_ANCHOR_DEPLETE(
        "respawn_anchor.deplete",
    ),
    RESPAWN_ANCHOR_SET_SPAWN("respawn_anchor.set_spawn"), SMITHING_TABLE_USE("smithing_table.use"), STEP_ANCIENT_DEBRIS(
        "step.ancient_debris",
    ),
    STEP_BASALT("step.basalt"), STEP_BONE_BLOCK("step.bone_block"), STEP_CHAIN("step.chain"), STEP_CLOTH("step.cloth"), STEP_CORAL(
        "step.coral",
    ),
    STEP_GRASS("step.grass"), STEP_GRAVEL("step.gravel"), STEP_HONEY_BLOCK("step.honey_block"), STEP_LADDER("step.ladder"), STEP_NETHER_BRICK(
        "step.nether_brick",
    ),
    STEP_NETHER_GOLD_ORE("step.nether_gold_ore"), STEP_NETHER_SPROUTS("step.nether_sprouts"), STEP_NETHER_WART("step.nether_wart"), STEP_NETHERITE(
        "step.netherite",
    ),
    STEP_NETHERRACK("step.netherrack"), STEP_NYLIUM("step.nylium"), STEP_ROOTS("step.roots"), STEP_SAND("step.sand"), STEP_SHROOMLIGHT(
        "step.shroomlight",
    ),
    STEP_SLIME("step.slime"), STEP_SNOW("step.snow"), STEP_SOUL_SAND("step.soul_sand"), STEP_SOUL_SOIL("step.soul_soil"), STEP_STEM(
        "step.stem",
    ),
    STEP_STONE("step.stone"), STEP_VINES("step.vines"), STEP_WOOD("step.wood"), TILE_PISTON_IN("tile.piston.in"), TILE_PISTON_OUT(
        "tile.piston.out",
    ),
    UI_CARTOGRAPHY_TABLE_TAKE_RESULT("ui.cartography_table.take_result"), UI_LOOM_SELECT_PATTERN("ui.loom.select_pattern"), UI_LOOM_TAKE_RESULT(
        "ui.loom.take_result",
    ),
    UI_STONECUTTER_TAKE_RESULT("ui.stonecutter.take_result"), USE_ANCIENT_DEBRIS("use.ancient_debris"), USE_BASALT("use.basalt"), USE_BONE_BLOCK(
        "use.bone_block",
    ),
    USE_CHAIN("use.chain"), USE_CLOTH("use.cloth"), USE_CORAL("use.coral"), USE_GRASS("use.grass"), USE_GRAVEL("use.gravel"), USE_HONEY_BLOCK(
        "use.honey_block",
    ),
    USE_LADDER("use.ladder"), USE_NETHER_BRICK("use.nether_brick"), USE_NETHER_GOLD_ORE("use.nether_gold_ore"), USE_NETHER_SPROUTS(
        "use.nether_sprouts",
    ),
    USE_NETHER_WART("use.nether_wart"), USE_NETHERITE("use.netherite"), USE_NETHERRACK("use.netherrack"), USE_NYLIUM("use.nylium"), USE_ROOTS(
        "use.roots",
    ),
    USE_SAND("use.sand"), USE_SHROOMLIGHT("use.shroomlight"), USE_SLIME("use.slime"), USE_SNOW("use.snow"), USE_SOUL_SAND(
        "use.soul_sand",
    ),
    USE_SOUL_SOIL("use.soul_soil"), USE_STEM("use.stem"), USE_STONE("use.stone"), USE_VINES("use.vines"), USE_WOOD("use.wood"), VR_STUTTERTURN(
        "vr.stutterturn",
    )
}
