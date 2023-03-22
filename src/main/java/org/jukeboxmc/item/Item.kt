package org.jukeboxmc.item

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtType
import com.nukkitx.nbt.NbtUtils
import com.nukkitx.protocol.bedrock.data.inventory.ItemData
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.enchantment.Enchantment
import org.jukeboxmc.item.enchantment.EnchantmentRegistry
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.util.ItemPalette
import org.jukeboxmc.util.Utils
import org.jukeboxmc.world.Sound
import java.util.*

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class Item : Cloneable {
    var type: ItemType
        protected set
    var identifier: Identifier
        protected set
    var runtimeId: Int
        protected set
    var blockRuntimeId = 0
        protected set
    var stackNetworkId = 0
        protected set
    var amount: Int
        protected set
    var meta: Int
        protected set
    var durability: Int
        protected set
    var displayName: String
        protected set
    var lore: MutableList<String>
        protected set
    var nbt: NbtMap?
        protected set
    var canPlace: List<String>
        protected set
    var canBreak: List<String>
        protected set
    protected var enchantments: MutableMap<EnchantmentType, Enchantment>
    var isEmptyEnchanted = false
        protected set
    var isUnbreakable = false
        protected set
    private var itemProperties: ItemProperties?
    var itemLockType: ItemLockType
        protected set

    @JvmOverloads
    constructor(identifier: Identifier, withNetworkId: Boolean = true) {
        type = ItemRegistry.getItemType(identifier)
        val itemRegistryData = ItemRegistry.getItemRegistryData(type)
        this.identifier = itemRegistryData.identifier
        runtimeId = ItemPalette.getRuntimeId(this.identifier)
        blockRuntimeId = 0
        if (withNetworkId) {
            stackNetworkId = stackNetworkCount++
        }
        amount = 1
        meta = 0
        durability = 0
        displayName = ""
        lore = LinkedList()
        nbt = null
        canPlace = LinkedList()
        canBreak = LinkedList()
        enchantments = EnumMap(EnchantmentType::class.java)
        itemProperties = ItemRegistry.getItemProperties(identifier)
        itemLockType = ItemLockType.NONE
    }

    @JvmOverloads
    constructor(itemType: ItemType, withNetworkId: Boolean = true) {
        type = itemType
        val itemRegistryData = ItemRegistry.getItemRegistryData(itemType)
        identifier = itemRegistryData.identifier
        runtimeId = ItemPalette.getRuntimeId(identifier)
        if (withNetworkId) {
            stackNetworkId = stackNetworkCount++
        }
        blockRuntimeId = try {
            Block.create<Block>(BlockType.valueOf(itemType.name)).runtimeId
        } catch (ignored: Exception) {
            0
        }
        amount = 1
        meta = 0
        durability = 0
        displayName = ""
        lore = LinkedList()
        nbt = null
        canPlace = LinkedList()
        canBreak = LinkedList()
        enchantments = EnumMap(EnchantmentType::class.java)
        itemProperties = ItemRegistry.getItemProperties(identifier)
        itemLockType = ItemLockType.NONE
    }

    @JvmOverloads
    constructor(itemData: ItemData, withNetworkId: Boolean = true) {
        type = ItemRegistry.getItemType(ItemPalette.getIdentifier(itemData.id.toShort()))
        val itemRegistryData = ItemRegistry.getItemRegistryData(type)
        identifier = itemRegistryData.identifier
        runtimeId = ItemPalette.getRuntimeId(identifier)
        blockRuntimeId = itemData.blockRuntimeId
        if (withNetworkId) {
            stackNetworkId = stackNetworkCount++
        }
        amount = itemData.count
        meta = itemData.damage
        durability = 0
        displayName = ""
        lore = LinkedList()
        nbt = itemData.tag
        canPlace = LinkedList()
        canBreak = LinkedList()
        enchantments = EnumMap(EnchantmentType::class.java)
        itemProperties = ItemRegistry.getItemProperties(identifier)
        itemLockType = ItemLockType.NONE
        if (nbt != null) {
            fromNbt(nbt!!)
        }
    }

    fun setRuntimeId(runtimeId: Int): Item {
        this.runtimeId = runtimeId
        return this
    }

    open fun setBlockRuntimeId(blockRuntimeId: Int): Item {
        this.blockRuntimeId = blockRuntimeId
        return this
    }

    fun setStackNetworkId(stackNetworkId: Int): Item {
        this.stackNetworkId = stackNetworkId
        return this
    }

    fun setAmount(amount: Int): Item {
        if (amount > maxStackSize) {
            this.amount = maxStackSize
            return this
        }
        this.amount = amount.coerceAtLeast(0)
        return this
    }

    fun setMeta(meta: Int): Item {
        this.meta = meta
        return this
    }

    fun setDurability(durability: Int): Item {
        this.durability = durability
        return this
    }

    fun setDisplayname(displayname: String): Item {
        this.displayName = displayname
        return this
    }

    fun setLore(lore: MutableList<String>): Item {
        this.lore = lore
        return this
    }

    fun setNbt(nbt: NbtMap?): Item {
        this.nbt = nbt
        if (this.nbt != null) {
            fromNbt(this.nbt!!)
        }
        return this
    }

    fun setCanPlace(canPlace: List<String>): Item {
        this.canPlace = canPlace
        return this
    }

    fun setCanBreak(canBreak: List<String>): Item {
        this.canBreak = canBreak
        return this
    }

    val maxStackSize: Int
        get() = itemProperties?.maxStackSize ?: 1

    fun decreaseAmount(): Item {
        return setAmount(amount - 1)
    }

    fun addEnchantment(enchantmentType: EnchantmentType, level: Int): Item {
        val enchantment: Enchantment = Enchantment.create(enchantmentType)
        enchantment.level = level.coerceAtMost(enchantment.maxLevel).toShort()
        enchantments[enchantmentType] = enchantment
        return this
    }

    fun getEnchantment(enchantmentType: EnchantmentType?): Enchantment? {
        return enchantments[enchantmentType]
    }

    val entchantments: Collection<Enchantment?>
        get() = enchantments.values

    fun setEmptyEnchanted(emptyEnchanted: Boolean): Item {
        isEmptyEnchanted = emptyEnchanted
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): Item {
        isUnbreakable = unbreakable
        return this
    }

    fun setItemLockType(locktype: ItemLockType): Item {
        itemLockType = locktype
        return this
    }

    private fun setItemLockType(locktype: Int): Item {
        itemLockType = ItemLockType.values()[locktype]
        return this
    }

    open fun toBlock(): Block {
        return if (blockRuntimeId == 0) {
            Block.create<Block>(BlockType.AIR)
                .clone()
        } else {
            BlockPalette.getBlockByNBT(BlockPalette.getBlockNbt(blockRuntimeId)).clone()
        }
    }

    fun toNbt(): NbtMap? {
        val nbtBuilder = if (nbt == null) NbtMap.builder() else nbt!!.toBuilder()
        if (durability > 0) {
            nbtBuilder.putInt("Damage", durability)
        }
        val displayBuilder = NbtMap.builder()
        if (displayName.isNotEmpty()) {
            displayBuilder["Name"] = displayName
        }
        if (lore.isNotEmpty()) {
            displayBuilder.putList("Lore", NbtType.STRING, lore)
        }
        if (!displayBuilder.isEmpty()) {
            nbtBuilder.putCompound("display", displayBuilder.build())
        }
        if (enchantments.isNotEmpty()) {
            val enchantmentNBT: MutableList<NbtMap> = ArrayList()
            for (enchantment in enchantments.values) {
                enchantmentNBT.add(
                    NbtMap.builder()
                        .putShort("id", enchantment.id)
                        .putShort("lvl", enchantment.level)
                        .build(),
                )
            }
            nbtBuilder.putList("ench", NbtType.COMPOUND, enchantmentNBT)
        } else {
            if (isEmptyEnchanted) {
                nbtBuilder.putList("ench", NbtType.COMPOUND, emptyList())
            }
        }
        if (itemLockType != ItemLockType.NONE) {
            nbtBuilder.putByte("minecraft:item_lock", itemLockType.ordinal.toByte())
        }
        return if (nbtBuilder.isEmpty()) null else nbtBuilder.build()
    }

    protected fun fromNbt(nbtMap: NbtMap) {
        nbtMap.listenForByte("Count") { amount: Byte ->
            setAmount(
                amount.toInt(),
            )
        }
        nbtMap.listenForInt("Damage") { durability: Int -> setDurability(durability) }
        nbtMap.listenForCompound("display") { displayTag: NbtMap ->
            displayTag.listenForString("Name") { displayname: String -> setDisplayname(displayname) }
            displayTag.listenForList("Lore", NbtType.STRING) { c: List<String>? ->
                lore.addAll(
                    c!!,
                )
            }
        }
        nbtMap.listenForList("ench", NbtType.COMPOUND) { compound: List<NbtMap> ->
            for (map in compound) {
                val id = map.getShort("id")
                val level = map.getShort("lvl")
                addEnchantment(EnchantmentRegistry.getEnchantmentType(id)!!, level.toInt())
            }
        }
        if (nbtMap.containsKey("minecraft:item_lock", NbtType.BYTE)) {
            nbtMap.listenForByte("minecraft:item_lock") { locktype: Byte -> this.setItemLockType(locktype.toInt()) }
        }
    }

    open fun toItemData(): ItemData {
        return ItemData.builder()
            .netId(stackNetworkId)
            .usingNetId(stackNetworkId > 0)
            .id(runtimeId)
            .blockRuntimeId(blockRuntimeId)
            .damage(meta)
            .count(amount)
            .tag(toNbt())
            .canPlace(canPlace.toTypedArray())
            .canBreak(canBreak.toTypedArray())
            .build()
    }

    @JvmOverloads
    fun updateItem(player: Player, amount: Int, playSound: Boolean = true) {
        if (player.gameMode == GameMode.SURVIVAL) {
            if (calculateDurability(amount)) {
                player.inventory.itemInHand = create(ItemType.AIR)
                if (playSound) {
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                }
            } else {
                player.inventory.itemInHand = this
            }
        }
    }

    fun calculateDurability(durability: Int): Boolean {
        if (this is Durability) {
            if (isUnbreakable) {
                return false
            }
            val enchantment = getEnchantment(EnchantmentType.UNBREAKING)
            if (enchantment != null) {
                val chance = Random().nextInt(100)
                val percent = 100 / (enchantment.level + 1)
                if (!(enchantment.level > 0 && percent <= chance)) {
                    this.durability += durability
                }
            } else {
                this.durability += durability
            }
            return durabilityAndCheckAmount(this.durability)
        }
        return false
    }

    private fun durabilityAndCheckAmount(durability: Int): Boolean {
        if (this is Durability) {
            val value = this as Durability
            val maxDurability: Int = value.maxDurability
            var intdurability = durability
            if (intdurability >= maxDurability) {
                if (--amount <= 0) {
                    return true
                }
                intdurability = 0
            }
            this.durability = intdurability
        }
        return false
    }

    open fun interact(player: Player, blockFace: BlockFace?, clickedPosition: Vector?, clickedBlock: Block): Boolean {
        return false
    }

    open fun useOnBlock(player: Player, block: Block, placeLocation: Location): Boolean {
        return false
    }

    open fun useInAir(player: Player, clickVector: Vector): Boolean {
        return false
    }

    open fun onUse(player: Player): Boolean {
        return false
    }

    open fun addToHand(player: Player) {}
    open fun removeFromHand(player: Player) {}
    open val toolType: ToolType
        get() = ToolType.NONE
    open val tierType: TierType
        get() = TierType.NONE

    public override fun clone(): Item {
        return try {
            val clone = super.clone() as Item
            clone.type = type
            clone.identifier = identifier
            clone.runtimeId = runtimeId
            clone.blockRuntimeId = blockRuntimeId
            clone.stackNetworkId = stackNetworkId
            clone.amount = amount
            clone.meta = meta
            clone.durability = durability
            clone.displayName = displayName
            clone.lore = lore
            clone.nbt = nbt
            clone.canPlace = canPlace
            clone.canBreak = canBreak
            clone.enchantments = enchantments
            clone.itemProperties = itemProperties
            clone
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }

    fun equalsExact(item: Item): Boolean {
        return this == item && amount == item.amount
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Item) {
            other.runtimeId == runtimeId && other.meta == meta && (other.blockRuntimeId == blockRuntimeId || other.blockRuntimeId == 0 || blockRuntimeId == 0)
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = runtimeId
        result = 31 * result + blockRuntimeId
        result = 31 * result + meta
        return result
    }

    companion object {
        var stackNetworkCount = 0
        fun createItem(itemData: ItemData): Item {
            if (itemData.id == 0) {
                return create(ItemType.AIR)
            }
            val item = create<Item>(ItemPalette.getIdentifier(itemData.id.toShort()))
            item.setBlockRuntimeId(itemData.blockRuntimeId)
            item.setMeta(itemData.damage)
            item.setAmount(itemData.count)
            item.setNbt(itemData.tag)
            if (item.nbt != null) {
                item.fromNbt(item.nbt!!)
            }
            return item
        }

        inline fun <reified T : Item> create(itemData: ItemData): T =
            createItem(itemData) as T

        fun createItem(identifier: Identifier): Item {
            val itemType = ItemRegistry.getItemType(identifier)
            return if (ItemRegistry.itemClassExists(itemType)) {
                val constructor = ItemRegistry.getItemClass(itemType)
                    .getConstructor(
                        ItemType::class.java,
                    )
                constructor.isAccessible = true
                constructor.newInstance(itemType)
            } else {
                Item(itemType)
            }
        }

        inline fun <reified T : Item> create(identifier: Identifier): T =
            createItem(identifier) as T

        fun createItem(itemType: ItemType): Item {
            return if (ItemRegistry.itemClassExists(itemType)) {
                val constructor = ItemRegistry.getItemClass(itemType)
                    .getConstructor(
                        ItemType::class.java,
                    )
                constructor.isAccessible = true
                constructor.newInstance(itemType)
            } else {
                Item(itemType)
            }
        }

        inline fun <reified T : Item> create(itemType: ItemType): T =
            createItem(itemType) as T

        fun createItem(itemType: ItemType, amount: Int): Item {
            return if (ItemRegistry.itemClassExists(itemType)) {
                val constructor = ItemRegistry.getItemClass(itemType)
                    .getConstructor(
                        ItemType::class.java,
                    )
                constructor.isAccessible = true
                constructor.newInstance(itemType).setAmount(amount)
            } else {
                Item(itemType, true).setAmount(amount)
            }
        }

        inline fun <reified T : Item> create(itemType: ItemType, amount: Int): T =
            createItem(itemType, amount) as T

        fun createItem(itemType: ItemType, amount: Int, meta: Int): Item {
            return if (ItemRegistry.itemClassExists(itemType)) {
                val constructor = ItemRegistry.getItemClass(itemType)
                    .getConstructor(
                        ItemType::class.java,
                    )
                constructor.isAccessible = true
                constructor.newInstance(itemType).setAmount(amount).setMeta(meta)
            } else {
                Item(itemType, true).setAmount(amount).setMeta(meta)
            }
        }

        inline fun <reified T : Item> create(itemType: ItemType, amount: Int, meta: Int): T =
            createItem(itemType, amount, meta) as T

        fun toBase64(item: Item): String {
            val itemNbt = NbtMap.builder()
                .putString("Name", item.identifier.fullName)
                .putInt("Count", item.amount)
                .putInt("Meta", item.meta)
                .putBoolean("Unbreakable", item.isUnbreakable)
                .putCompound("BlockState", item.toBlock().blockStates)
                .putCompound("tag", if (item.toNbt() != null) item.toNbt() else NbtMap.EMPTY)
                .build()
            val buffer = Unpooled.buffer()
            NbtUtils.createWriterLE(ByteBufOutputStream(buffer))
                .use { networkWriter -> networkWriter.writeTag(itemNbt) }
            return Base64.getMimeEncoder().encodeToString(Utils.array(buffer))
        }

        fun fromBase64AsItem(json: String): Item {
            val decode = Base64.getMimeDecoder().decode(json)
            NbtUtils.createReaderLE(ByteBufInputStream(Unpooled.wrappedBuffer(decode))).use { reader ->
                val compound = reader.readTag() as NbtMap
                val identifier: Identifier = Identifier.fromString(compound.getString("Name"))
                val amount = compound.getInt("Count")
                val meta = compound.getInt("Meta")
                val unbreakable = compound.getBoolean("Unbreakable")
                val blockStates = compound.getCompound("BlockState")
                val itemTag = compound.getCompound("tag")
                var blockRuntimeId = 0
                for (blockNbt in BlockPalette.searchBlocks { nbtMap: NbtMap ->
                    nbtMap.getString("name").equals(identifier.fullName, ignoreCase = true)
                }) {
                    if (blockNbt.getCompound("states") == blockStates) {
                        blockRuntimeId = BlockPalette.getRuntimeId(blockNbt)
                    }
                }
                val item = create<Item>(identifier).setAmount(amount).setMeta(meta).setUnbreakable(unbreakable)
                    .setBlockRuntimeId(blockRuntimeId)
                if (!itemTag.isEmpty()) {
                    item.setNbt(itemTag)
                }
                return item
            }
        }

        inline fun <reified T : Item> fromBase64(json: String): T = fromBase64AsItem(json) as T
    }
}
