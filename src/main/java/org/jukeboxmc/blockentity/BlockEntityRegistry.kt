package org.jukeboxmc.blockentity

/**
 * @author LucGamesYT
 * @version 1.0
 */
object BlockEntityRegistry {
    private val BLOCKENTITYCLASS_FROM_BLOCKENTITYTYPE: MutableMap<BlockEntityType?, Class<out BlockEntity>> =
        LinkedHashMap()
    private val BLOCKENTITYID: MutableMap<BlockEntityType, String> = LinkedHashMap()
    fun init() {
        register(BlockEntityType.BED, BlockEntityBed::class.java, "Bed")
        register(BlockEntityType.FURNACE, BlockEntityFurnace::class.java, "Furnace")
        register(BlockEntityType.SIGN, BlockEntitySign::class.java, "Sign")
        register(BlockEntityType.BEEHIVE, BlockEntityBeehive::class.java, "Beehive")
        register(BlockEntityType.BANNER, BlockEntityBanner::class.java, "Banner")
        register(BlockEntityType.SKULL, BlockEntitySkull::class.java, "Skull")
        register(BlockEntityType.BLAST_FURNACE, BlockEntityBlastFurnace::class.java, "BlastFurnace")
        register(BlockEntityType.SMOKER, BlockEntitySmoker::class.java, "Smoker")
        register(BlockEntityType.LECTERN, BlockEntityLectern::class.java, "Lectern")
        register(BlockEntityType.ENCHANTMENT_TABLE, BlockEntityEnchantmentTable::class.java, "EnchantTable")
        register(BlockEntityType.CHEST, BlockEntityChest::class.java, "Chest")
        register(BlockEntityType.ENDER_CHEST, BlockEntityEnderChest::class.java, "EnderChest")
        register(BlockEntityType.SHULKER_BOX, BlockEntityShulkerBox::class.java, "ShulkerBox")
        register(BlockEntityType.DROPPER, BlockEntityDropper::class.java, "Dropper")
        register(BlockEntityType.HOPPER, BlockEntityHopper::class.java, "Hopper")
        register(BlockEntityType.DISPENSER, BlockEntityDispenser::class.java, "Dispenser")
        register(BlockEntityType.LOOM, BlockEntityLoom::class.java, "Loom")
        register(BlockEntityType.BARREL, BlockEntityBarrel::class.java, "Barrel")
        register(BlockEntityType.BREWING_STAND, BlockEntityBrewingStand::class.java, "BrewingStand")
        register(BlockEntityType.MOB_SPAWNER, BlockEntityMobSpawner::class.java, "Bell")
        register(BlockEntityType.BELL, BlockEntityBell::class.java, "MobSpawner")
    }

    private fun register(
        blockEntityType: BlockEntityType,
        blockEntityClass: Class<out BlockEntity>,
        blockEntityId: String,
    ) {
        BLOCKENTITYCLASS_FROM_BLOCKENTITYTYPE[blockEntityType] = blockEntityClass
        BLOCKENTITYID[blockEntityType] = blockEntityId
    }

    fun getBlockEntityClass(blockEntityType: BlockEntityType?): Class<out BlockEntity> {
        return BLOCKENTITYCLASS_FROM_BLOCKENTITYTYPE[blockEntityType]!!
    }

    fun getBlockEntityType(blockEntityType: BlockEntityType): String? {
        return BLOCKENTITYID[blockEntityType]
    }

    fun getBlockEntityTypeById(blockEntityId: String?): BlockEntityType? {
        for ((key, value) in BLOCKENTITYID) {
            if (value.equals(blockEntityId, ignoreCase = true)) {
                return key
            }
        }
        return null
    }
}
