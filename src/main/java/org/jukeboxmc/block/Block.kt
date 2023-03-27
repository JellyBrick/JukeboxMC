package org.jukeboxmc.block

import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.protocol.bedrock.data.LevelEvent
import org.cloudburstmc.protocol.bedrock.data.SoundEvent
import org.cloudburstmc.protocol.bedrock.data.defintions.BlockDefinition
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.behavior.Waterlogable
import org.jukeboxmc.block.data.BlockProperties
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.entity.item.EntityItem
import org.jukeboxmc.event.block.BlockBreakEvent
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType
import org.jukeboxmc.item.enchantment.Enchantment
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.network.registry.SimpleDefinitionRegistry
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.potion.ConduitPowerEffect
import org.jukeboxmc.potion.EffectType
import org.jukeboxmc.potion.HasteEffect
import org.jukeboxmc.potion.MiningFatigueEffect
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.LinkedList
import java.util.Locale
import java.util.Optional

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class Block @JvmOverloads constructor(identifier: Identifier, blockStates: NbtMap? = null) : Cloneable {
    var runtimeId: Int
        protected set
    var identifier: Identifier
        protected set
    var blockStates: NbtMap
    var type: BlockType
        protected set
    lateinit var location: Location
    var layer = 0
    protected var blockProperties: BlockProperties

    var definition: BlockDefinition
        internal set

    init {
        this.identifier = identifier
        type = BlockRegistry.getBlockType(identifier)
        blockProperties = BlockRegistry.getBlockProperties(identifier)
        val state = BlockRegistry.getBlockStates(identifier)
        val variableBlockStates: NbtMap = if (blockStates == null) {
            val states: List<NbtMap> = LinkedList(state.keys)
            if (states.isEmpty()) NbtMap.EMPTY else states[0]
        } else {
            blockStates
        }
        this.blockStates = variableBlockStates
        runtimeId = BlockRegistry.getRuntimeId(identifier, variableBlockStates)
        definition = registry.getDefinition(runtimeId)
    }

    val world: World
        get() = location.world

    fun checkValidity(): Boolean = if (this::location.isInitialized) {
        location.world.getBlock(location.blockX, location.blockY, location.blockZ, layer, location.dimension)
            .runtimeId == runtimeId
    } else {
        false
    }

    fun setStateBlock(state: String, value: Any): Block {
        if (!blockStates.containsKey(state)) {
            throw AssertionError("State $state was not found in block $identifier")
        }
        if (blockStates.getValue(state)::class.java != value::class.java) {
            if (!(value::class.java == java.lang.Byte::class.java && blockStates.getValue(state)::class.java == java.lang.Integer::class.java)) { // :(
                throw AssertionError("State $state type is not the same for value $value")
            }
        }
        val valid = checkValidity()
        val nbtMapBuilder = blockStates.toBuilder()
        nbtMapBuilder[state] = value
        val states = BlockRegistry.getBlockStates(identifier)
        states.forEach { (blockMap) ->
            if (blockMap == nbtMapBuilder) {
                blockStates = blockMap
            }
        }
        runtimeId = states.getValue(blockStates)
        if (valid) {
            this.sendUpdate()
            location.chunk?.setBlock(location.blockX, location.blockY, location.blockZ, layer, this)
        }
        return this
    }

    inline fun <reified T : Block> setState(state: String, value: Any): T = setStateBlock(state, value) as T

    fun stateExists(value: String): Boolean {
        return blockStates.containsKey(value)
    }

    fun getStringState(value: String): String {
        return blockStates.getString(value).uppercase(Locale.getDefault())
    }

    fun getByteState(value: String): Byte {
        return blockStates.getByte(value)
    }

    fun getBooleanState(value: String): Boolean {
        return blockStates.getBoolean(value)
    }

    fun getIntState(value: String): Int {
        return blockStates.getInt(value)
    }

    fun getSide(direction: Direction): Block {
        return this.getSide(direction, 0)
    }

    fun getSide(blockFace: BlockFace): Block {
        return this.getSide(blockFace, 0)
    }

    fun getSide(direction: Direction, layer: Int): Block {
        return when (direction) {
            Direction.SOUTH -> getRelative(Vector(0, 0, 1), layer)!!
            Direction.NORTH -> getRelative(Vector(0, 0, -1), layer)!!
            Direction.EAST -> getRelative(Vector(1, 0, 0), layer)!!
            Direction.WEST -> getRelative(Vector(-1, 0, 0), layer)!!
        }
    }

    fun getSide(blockFace: BlockFace, layer: Int): Block {
        return when (blockFace) {
            BlockFace.DOWN -> getRelative(Vector(0, -1, 0), layer)!!
            BlockFace.UP -> getRelative(Vector(0, 1, 0), layer)!!
            BlockFace.SOUTH -> getRelative(Vector(0, 0, 1), layer)!!
            BlockFace.NORTH -> getRelative(Vector(0, 0, -1), layer)!!
            BlockFace.EAST -> getRelative(Vector(1, 0, 0), layer)!!
            BlockFace.WEST -> getRelative(Vector(-1, 0, 0), layer)!!
        }
    }

    fun getRelative(position: Vector, layer: Int): Block? {
        val x = location.blockX + position.blockX
        val y = location.blockY + position.blockY
        val z = location.blockZ + position.blockZ
        return location.world.getBlock(x, y, z, layer, location.dimension)
    }

    open fun toItem(): Item {
        return Item.create(identifier)
    }

    open val boundingBox: AxisAlignedBB
        get() = AxisAlignedBB(
            location.x,
            location.y,
            location.z,
            location.x + 1,
            location.y + 1,
            location.z + 1,
        )

    fun breakBlock(player: Player, item: Item) {
        if (player.gameMode == GameMode.SPECTATOR) {
            this.sendUpdate(player)
            return
        }
        val itemDropList = if (item.getEnchantment(EnchantmentType.SILK_TOUCH) != null) {
            listOf(toItem())
        } else {
            getDrops(item)
        }
        val blockBreakEvent = BlockBreakEvent(player, this, itemDropList)
        Server.instance.pluginManager.callEvent(blockBreakEvent)
        if (blockBreakEvent.isCancelled) {
            player.inventory.sendItemInHand()
            this.sendUpdate(player)
            return
        }
        val breakLocation = location
        onBlockBreak(breakLocation)
        if (player.gameMode == GameMode.SURVIVAL) {
            if (item is Durability) {
                item.updateItem(player, 1)
            }
            player.exhaust(0.025f)
            val itemDrops = mutableListOf<EntityItem>()
            for (droppedItem in blockBreakEvent.drops) {
                if (droppedItem.type != ItemType.AIR) {
                    itemDrops.add(player.world.dropItem(droppedItem, breakLocation.clone(), null))
                }
            }
            if (itemDrops.isNotEmpty()) {
                itemDrops.forEach { it.spawn() }
            }
        }
        playBreakSound()
        breakLocation.world.sendLevelEvent(breakLocation, LevelEvent.PARTICLE_DESTROY_BLOCK, runtimeId)
    }

    private fun playBreakSound() {
        location.world.playSound(location, SoundEvent.BREAK, runtimeId)
    }

    open fun onBlockBreak(breakPosition: Vector) {
        location.world.setBlock(breakPosition, create(BlockType.AIR), 0, breakPosition.dimension)
    }

    fun sendUpdate() {
        val updateBlockPacket = UpdateBlockPacket()
        updateBlockPacket.definition = definition
        updateBlockPacket.blockPosition = location.toVector3i()
        updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
        updateBlockPacket.dataLayer = layer
        location.world.sendChunkPacket(location.chunkX, location.chunkZ, updateBlockPacket)
    }

    fun sendUpdate(player: Player) {
        val updateBlockPacket = UpdateBlockPacket()
        updateBlockPacket.definition = definition
        updateBlockPacket.blockPosition = location.toVector3i()
        updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
        updateBlockPacket.dataLayer = layer
        player.playerConnection.sendPacket(updateBlockPacket)
    }

    fun getBreakTime(item: Item, player: Player): Double {
        val hardness = hardness
        if (hardness == 0.0) {
            return 0.0
        }
        val blockType = type
        val correctTool = correctTool0(
            toolType,
            item.toolType,
        ) ||
            item.toolType == ToolType.SHEARS &&
            (blockType == BlockType.WEB || blockType == BlockType.LEAVES || blockType == BlockType.LEAVES2)
        val canBreakWithHand = canBreakWithHand()
        val itemToolType = item.toolType
        val itemTier = item.tierType
        val efficiencyLoreLevel =
            Optional.ofNullable(item.getEnchantment(EnchantmentType.EFFICIENCY)).map { obj: Enchantment -> obj.level }
                .orElse(0.toShort()).toInt()
        val miningFatigueLevel = Optional.ofNullable(player.getEffect<MiningFatigueEffect>(EffectType.MINING_FATIGUE))
            .map { obj: MiningFatigueEffect -> obj.amplifier }
            .orElse(0)
        var hasteEffectLevel = Optional.ofNullable(player.getEffect<HasteEffect>(EffectType.HASTE))
            .map { obj: HasteEffect -> obj.amplifier }
            .orElse(0)
        val conduitPowerLevel = Optional.ofNullable(player.getEffect<ConduitPowerEffect>(EffectType.CONDUIT_POWER))
            .map { obj: ConduitPowerEffect -> obj.amplifier }
            .orElse(0)
        hasteEffectLevel += conduitPowerLevel
        val insideOfWaterWithoutAquaAffinity = player.isInWater && conduitPowerLevel <= 0 &&
            Optional.ofNullable(player.armorInventory.helmet.getEnchantment(EnchantmentType.AQUA_AFFINITY))
                .map { obj: Enchantment -> obj.level }
                .map { l: Short -> l >= 1 }.orElse(false)
        val outOfWaterButNotOnGround = !player.isInWater && !player.isOnGround
        return breakTime0(
            item,
            hardness,
            correctTool,
            canBreakWithHand,
            blockType,
            itemToolType,
            itemTier,
            efficiencyLoreLevel,
            hasteEffectLevel,
            miningFatigueLevel,
            insideOfWaterWithoutAquaAffinity,
            outOfWaterButNotOnGround,
        )
    }

    private fun breakTime0(
        item: Item,
        blockHardness: Double,
        correctTool: Boolean,
        canHarvestWithHand: Boolean,
        blockType: BlockType,
        itemToolType: ToolType,
        itemTierType: TierType,
        efficiencyLoreLevel: Int,
        hasteEffectLevel: Int,
        miningFatigueLevel: Int,
        insideOfWaterWithoutAquaAffinity: Boolean,
        outOfWaterButNotOnGround: Boolean,
    ): Double {
        val baseTime = if (canHarvest(item) || canHarvestWithHand) {
            1.5 * blockHardness
        } else {
            5.0 * blockHardness
        }
        var speed = 1.0 / baseTime
        if (correctTool) {
            speed *= toolBreakTimeBonus0(itemToolType, itemTierType, blockType)
        }
        speed += speedBonusByEfficiencyLore0(efficiencyLoreLevel)
        speed *= speedRateByHasteLore0(hasteEffectLevel)
        if (insideOfWaterWithoutAquaAffinity) speed *= 0.2
        if (outOfWaterButNotOnGround) speed *= 0.2
        return 1.0 / speed
    }

    private fun toolBreakTimeBonus0(itemToolType: ToolType, itemTierType: TierType, blockType: BlockType): Double {
        if (itemToolType == ToolType.SWORD) return if (blockType == BlockType.WEB) 15.0 else 1.0
        if (itemToolType == ToolType.SHEARS) {
            if (blockType.isWool(blockType) || blockType == BlockType.LEAVES || blockType == BlockType.LEAVES2) {
                return 5.0
            } else if (blockType == BlockType.WEB) {
                return 15.0
            }
            return 1.0
        }
        return if (itemToolType == ToolType.NONE) {
            1.0
        } else {
            when (itemTierType) {
                TierType.WOODEN -> 2.0
                TierType.STONE -> 4.0
                TierType.IRON -> 6.0
                TierType.DIAMOND -> 8.0
                TierType.NETHERITE -> 9.0
                TierType.GOLD -> 12.0
                else -> 1.0
            }
        }
    }

    private fun speedBonusByEfficiencyLore0(efficiencyLoreLevel: Int): Double {
        return if (efficiencyLoreLevel == 0) 0.0 else (efficiencyLoreLevel * efficiencyLoreLevel + 1).toDouble()
    }

    private fun speedRateByHasteLore0(hasteLoreLevel: Int): Double {
        return 1.0 + 0.2 * hasteLoreLevel
    }

    fun canHarvest(item: Item): Boolean {
        return tierType == TierType.NONE || toolType == ToolType.NONE || correctTool0(
            toolType,
            item.toolType,
        ) && item.tierType.ordinal >= tierType.ordinal
    }

    private fun correctTool0(blockItemToolType: ToolType, itemToolType: ToolType): Boolean {
        return blockItemToolType == ToolType.SWORD && itemToolType == ToolType.SWORD || blockItemToolType == ToolType.SHOVEL && itemToolType == ToolType.SHOVEL || blockItemToolType == ToolType.PICKAXE && itemToolType == ToolType.PICKAXE || blockItemToolType == ToolType.AXE && itemToolType == ToolType.AXE || blockItemToolType == ToolType.HOE && itemToolType == ToolType.HOE || blockItemToolType == ToolType.SHEARS && itemToolType == ToolType.SHEARS || blockItemToolType == ToolType.NONE
    }

    open fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        world.setBlock(placePosition, this, 0, player.dimension, true)
        return true
    }

    open fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item,
    ): Boolean {
        return false
    }

    open val blockEntity: BlockEntity?
        get() = null

    fun canBreakWithHand(): Boolean {
        return blockProperties.canBreakWithHand
    }

    val isSolid: Boolean
        get() = blockProperties.solid
    val isTransparent: Boolean
        get() = blockProperties.transparent
    val hardness: Double
        get() = blockProperties.hardness

    open fun canPassThrough(): Boolean {
        return blockProperties.canPassThrough
    }

    val toolType: ToolType
        get() = blockProperties.toolType
    val tierType: TierType
        get() = blockProperties.tierType

    open fun canBeReplaced(block: Block?): Boolean {
        return false
    }

    open fun onUpdate(updateReason: UpdateReason): Long {
        return -1
    }

    open fun enterBlock(player: Player) {}
    open fun leaveBlock(player: Player?) {}
    open fun canBeFlowedInto(): Boolean {
        return false
    }

    fun canWaterloggingFlowInto(): Boolean {
        return canBeFlowedInto() || this is Waterlogable && (this as Waterlogable).waterLoggingLevel > 1
    }

    open fun getDrops(item: Item?): List<Item> {
        return if (item == null || isCorrectToolType(item) && isCorrectTierType(item)) {
            listOf(toItem())
        } else {
            emptyList()
        }
    }

    fun isCorrectToolType(item: Item): Boolean {
        return item.toolType.ordinal >= toolType.ordinal
    }

    fun isCorrectTierType(item: Item): Boolean {
        return item.tierType.ordinal >= tierType.ordinal
    }

    val isWater: Boolean
        get() = type == BlockType.WATER || type == BlockType.FLOWING_WATER
    val isLava: Boolean
        get() = type == BlockType.LAVA || type == BlockType.FLOWING_LAVA

    public override fun clone(): Block {
        return try {
            val block = super.clone() as Block
            block.runtimeId = runtimeId
            block.identifier = identifier
            block.blockStates = blockStates
            block.type = type
            if (this::location.isInitialized) {
                block.location = location
            }
            block.layer = layer
            block.blockProperties = blockProperties
            block
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }

    companion object {
        fun createBlock(blockType: BlockType): Block {
            return if (BlockRegistry.blockClassExists(blockType)) {
                val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                    Identifier::class.java,
                )
                constructor.newInstance(BlockRegistry.getIdentifier(blockType))
            } else {
                Block(BlockRegistry.getIdentifier(blockType))
            }
        }

        inline fun <reified T : Block> create(blockType: BlockType): T = createBlock(blockType) as T

        fun createBlock(blockType: BlockType, blockStates: NbtMap): Block {
            return if (BlockRegistry.blockClassExists(blockType)) {
                val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                    Identifier::class.java,
                    NbtMap::class.java,
                )
                constructor.newInstance(BlockRegistry.getIdentifier(blockType), blockStates)
            } else {
                Block(BlockRegistry.getIdentifier(blockType), blockStates)
            }
        }

        inline fun <reified T : Block> create(blockType: BlockType, blockStates: NbtMap): T =
            createBlock(blockType, blockStates) as T

        fun createBlock(identifier: Identifier): Block {
            val blockType = BlockRegistry.getBlockType(identifier)
            return if (BlockRegistry.blockClassExists(blockType)) {
                val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                    Identifier::class.java,
                )
                constructor.newInstance(identifier)
            } else {
                Block(identifier, null)
            }
        }

        inline fun <reified T : Block> create(identifier: Identifier): T =
            createBlock(identifier) as T

        fun createBlock(identifier: Identifier, blockStates: NbtMap): Block {
            val blockType = BlockRegistry.getBlockType(identifier)
            return if (BlockRegistry.blockClassExists(blockType)) {
                val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                    Identifier::class.java,
                    NbtMap::class.java,
                )
                constructor.newInstance(identifier, blockStates)
            } else {
                Block(identifier, blockStates)
            }
        }

        inline fun <reified T : Block> create(identifier: Identifier, blockStates: NbtMap): T =
            createBlock(identifier, blockStates) as T

        private val registry: SimpleDefinitionRegistry<BlockDefinition> = SimpleDefinitionRegistry.getRegistry()
    }
}
