package org.jukeboxmc.block

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.LevelEventType
import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import java.util.LinkedList
import java.util.Locale
import java.util.Optional
import java.util.function.Consumer
import lombok.ToString
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
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.potion.ConduitPowerEffect
import org.jukeboxmc.potion.EffectType
import org.jukeboxmc.potion.HasteEffect
import org.jukeboxmc.potion.MiningFatigueEffect
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
@ToString(exclude = ["blockProperties"])
open class Block @JvmOverloads constructor(identifier: Identifier?, blockStates: NbtMap? = null) : Cloneable {
    var runtimeId: Int
        protected set
    var identifier: Identifier?
        protected set
    protected var blockStates: NbtMap?
    var type: BlockType?
        protected set
    protected var location: Location? = null
    protected var layer = 0
    protected var blockProperties: BlockProperties?

    init {
        var blockStates = blockStates
        this.identifier = identifier
        this.blockStates = blockStates
        type = BlockRegistry.getBlockType(identifier)
        blockProperties = BlockRegistry.getBlockProperties(identifier)
        if (!STATES.containsKey(this.identifier)) {
            val toRuntimeId: Object2ObjectMap<NbtMap?, Int> = Object2ObjectLinkedOpenHashMap()
            for (blockMap in BlockPalette.searchBlocks { blockMap: NbtMap ->
                blockMap.getString("name").lowercase(Locale.getDefault()) == this.identifier.getFullName()
            }) {
                toRuntimeId[blockMap!!.getCompound("states")] = BlockPalette.getRuntimeId(blockMap)
            }
            STATES[this.identifier] = toRuntimeId
        }
        if (blockStates == null) {
            val states: List<NbtMap?> = LinkedList(STATES[this.identifier]!!.keys)
            blockStates = if (states.isEmpty()) NbtMap.EMPTY else states[0]
        }
        this.blockStates = blockStates
        runtimeId = STATES[this.identifier]!![this.blockStates]!!
    }

    fun getBlockStates(): NbtMap? {
        return blockStates
    }

    fun setBlockStates(blockStates: NbtMap?) {
        this.blockStates = blockStates
    }

    val world: World?
        get() = location.getWorld()

    fun getLocation(): Location? {
        return location
    }

    fun setLocation(location: Location?) {
        this.location = location
    }

    fun getLayer(): Int {
        return layer
    }

    fun setLayer(layer: Int) {
        this.layer = layer
    }

    fun checkValidity(): Boolean {
        return location != null && location.getWorld() != null && location.getWorld()
            .getBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), layer, location.getDimension())
            .getRuntimeId() == runtimeId
    }

    fun <B : Block?> setState(state: String, value: Any): B {
        if (!blockStates!!.containsKey(state)) {
            throw AssertionError("State " + state + " was not found in block " + identifier)
        }
        if (blockStates!![state]!!.javaClass != value.javaClass) {
            throw AssertionError("State $state type is not the same for value  $value")
        }
        val valid = checkValidity()
        val nbtMapBuilder = blockStates!!.toBuilder()
        nbtMapBuilder[state] = value
        for ((blockMap) in STATES[identifier]!!) {
            if (blockMap == nbtMapBuilder) {
                blockStates = blockMap
            }
        }
        runtimeId = STATES[identifier]!![blockStates]!!
        if (valid) {
            this.sendUpdate()
            location.getChunk().setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), layer, this)
        }
        return this as B
    }

    fun stateExists(value: String): Boolean {
        return blockStates!!.containsKey(value)
    }

    fun getStringState(value: String): String {
        return blockStates!!.getString(value).uppercase(Locale.getDefault())
    }

    fun getByteState(value: String): Byte {
        return blockStates!!.getByte(value)
    }

    fun getBooleanState(value: String): Boolean {
        return blockStates!!.getByte(value).toInt() == 1
    }

    fun getIntState(value: String): Int {
        return blockStates!!.getInt(value)
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
        val x = location.getBlockX() + position.blockX
        val y = location.getBlockY() + position.blockY
        val z = location.getBlockZ() + position.blockZ
        return location.getWorld().getBlock(x, y, z, layer, location.getDimension())
    }

    open fun toItem(): Item {
        return Item.Companion.create<Item>(identifier)
    }

    open val boundingBox: AxisAlignedBB
        get() = AxisAlignedBB(
            location!!.x,
            location!!.y,
            location!!.z,
            location!!.x + 1,
            location!!.y + 1,
            location!!.z + 1
        )

    fun breakBlock(player: Player, item: Item) {
        if (player.gameMode == GameMode.SPECTATOR) {
            this.sendUpdate(player)
            return
        }
        val itemDropList: List<Item>
        itemDropList = if (item.getEnchantment(EnchantmentType.SILK_TOUCH) != null) {
            listOf(toItem())
        } else {
            getDrops(item)
        }
        val blockBreakEvent = BlockBreakEvent(player, this, itemDropList)
        Server.Companion.getInstance().getPluginManager().callEvent(blockBreakEvent)
        if (blockBreakEvent.isCancelled) {
            player.inventory.sendItemInHand()
            this.sendUpdate(player)
            return
        }
        val breakLocation = location
        onBlockBreak(breakLocation!!)
        if (player.gameMode == GameMode.SURVIVAL) {
            if (item is Durability) {
                item.updateItem(player, 1)
            }
            player.exhaust(0.025f)
            val itemDrops: MutableList<EntityItem> = ArrayList()
            for (droppedItem in blockBreakEvent.drops) {
                if (droppedItem.type != ItemType.AIR) {
                    itemDrops.add(player.world.dropItem(droppedItem, breakLocation.clone(), null))
                }
            }
            if (!itemDrops.isEmpty()) {
                itemDrops.forEach(Consumer { obj: EntityItem -> obj.spawn() })
            }
        }
        playBreakSound()
        breakLocation.world.sendLevelEvent(breakLocation, LevelEventType.PARTICLE_DESTROY_BLOCK, runtimeId)
    }

    private fun playBreakSound() {
        location.getWorld().playSound(location, SoundEvent.BREAK, runtimeId)
    }

    open fun onBlockBreak(breakPosition: Vector) {
        location.getWorld().setBlock(breakPosition, create(BlockType.AIR), 0, breakPosition.dimension)
    }

    fun sendUpdate() {
        val updateBlockPacket = UpdateBlockPacket()
        updateBlockPacket.runtimeId = runtimeId
        updateBlockPacket.blockPosition = location!!.toVector3i()
        updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
        updateBlockPacket.dataLayer = layer
        location.getWorld().sendChunkPacket(location.getChunkX(), location.getChunkZ(), updateBlockPacket)
    }

    fun sendUpdate(player: Player) {
        if (location == null) {
            return
        }
        val updateBlockPacket = UpdateBlockPacket()
        updateBlockPacket.runtimeId = runtimeId
        updateBlockPacket.blockPosition = location!!.toVector3i()
        updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
        updateBlockPacket.dataLayer = layer
        player.playerConnection.sendPacket(updateBlockPacket)
    }

    fun getBreakTime(item: Item, player: Player): Double {
        val hardness = hardness
        if (hardness == 0.0) {
            return 0
        }
        val blockType = type
        val correctTool = correctTool0(
            toolType,
            item.toolType
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
            blockType!!,
            itemToolType,
            itemTier,
            efficiencyLoreLevel,
            hasteEffectLevel,
            miningFatigueLevel,
            insideOfWaterWithoutAquaAffinity,
            outOfWaterButNotOnGround
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
        outOfWaterButNotOnGround: Boolean
    ): Double {
        val baseTime: Double
        baseTime = if (canHarvest(item) || canHarvestWithHand) {
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
        return if (itemToolType == ToolType.NONE) 1.0 else when (itemTierType) {
            TierType.WOODEN -> 2.0
            TierType.STONE -> 4.0
            TierType.IRON -> 6.0
            TierType.DIAMOND -> 8.0
            TierType.NETHERITE -> 9.0
            TierType.GOLD -> 12.0
            else -> 1.0
        }
    }

    private fun speedBonusByEfficiencyLore0(efficiencyLoreLevel: Int): Double {
        return if (efficiencyLoreLevel == 0) 0 else (efficiencyLoreLevel * efficiencyLoreLevel + 1).toDouble()
    }

    private fun speedRateByHasteLore0(hasteLoreLevel: Int): Double {
        return 1.0 + 0.2 * hasteLoreLevel
    }

    fun canHarvest(item: Item): Boolean {
        return tierType == TierType.NONE || toolType == ToolType.NONE || correctTool0(
            toolType, item.toolType
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
        blockFace: BlockFace
    ): Boolean {
        world.setBlock(placePosition, this, 0, player.dimension, true)
        return true
    }

    open fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item
    ): Boolean {
        return false
    }

    open val blockEntity: BlockEntity?
        get() = null

    fun canBreakWithHand(): Boolean {
        return blockProperties.isCanBreakWithHand()
    }

    val isSolid: Boolean
        get() = blockProperties.isSolid()
    val isTransparent: Boolean
        get() = blockProperties.isTransparent()
    val hardness: Double
        get() = blockProperties.getHardness()

    open fun canPassThrough(): Boolean {
        return blockProperties.isCanPassThrough()
    }

    val toolType: ToolType
        get() = blockProperties.getToolType()
    val tierType: TierType
        get() = blockProperties.getTierType()

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
        } else emptyList()
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
            block.location = location
            block.layer = layer
            block.blockProperties = blockProperties
            block
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }

    companion object {
        val STATES: Object2ObjectMap<Identifier?, Object2ObjectMap<NbtMap?, Int>> = Object2ObjectLinkedOpenHashMap()
        fun <T : Block?> create(blockType: BlockType?): T {
            return if (BlockRegistry.blockClassExists(blockType)) {
                try {
                    val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                        Identifier::class.java
                    )
                    constructor.newInstance(BlockRegistry.getIdentifier(blockType)) as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            } else Block(BlockRegistry.getIdentifier(blockType)) as T
        }

        fun <T : Block?> create(blockType: BlockType?, blockStates: NbtMap?): T {
            return if (BlockRegistry.blockClassExists(blockType)) {
                try {
                    val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                        Identifier::class.java, NbtMap::class.java
                    )
                    constructor.newInstance(BlockRegistry.getIdentifier(blockType), blockStates) as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            } else Block(BlockRegistry.getIdentifier(blockType), blockStates) as T
        }

        fun <T : Block?> create(identifier: Identifier?): T {
            val blockType = BlockRegistry.getBlockType(identifier)
            return if (BlockRegistry.blockClassExists(blockType)) {
                try {
                    val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                        Identifier::class.java
                    )
                    constructor.newInstance(identifier) as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            } else Block(identifier, null) as T
        }

        fun <T : Block?> create(identifier: Identifier?, blockStates: NbtMap?): T {
            val blockType = BlockRegistry.getBlockType(identifier)
            return if (BlockRegistry.blockClassExists(blockType)) {
                try {
                    val constructor = BlockRegistry.getBlockClass(blockType).getConstructor(
                        Identifier::class.java, NbtMap::class.java
                    )
                    constructor.newInstance(identifier, blockStates) as T
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            } else Block(identifier, blockStates) as T
        }
    }
}