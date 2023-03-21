package org.jukeboxmc.world

import com.google.common.collect.ImmutableSet
import com.nukkitx.nbt.NBTInputStream
import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtUtils
import com.nukkitx.nbt.util.stream.LittleEndianDataInputStream
import com.nukkitx.nbt.util.stream.LittleEndianDataOutputStream
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.LevelEventType
import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.Locale
import java.util.Objects
import java.util.Queue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.item.EntityItem
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.ChunkManager
import org.jukeboxmc.world.gamerule.GameRule
import org.jukeboxmc.world.generator.Generator
import org.jukeboxmc.world.generator.NormalGenerator
import org.jukeboxmc.world.leveldb.LevelDB

/**
 * @author LucGamesYT
 * @version 1.0
 */
class World(var name: String, val server: Server, generatorMap: Map<Dimension?, String?>) {
    private val BLOCK_AIR: Block = Block.Companion.create<Block>(BlockType.AIR)
    private val STORAGE_VERSION = 9
    private val gameRules: GameRules
    private val blockUpdateList: BlockUpdateList
    val worldFolder: File
    private val worldFile: File
    private val levelDB: LevelDB
    private val chunkManagers: MutableMap<Dimension?, ChunkManager>
    private val generators: MutableMap<Dimension?, ThreadLocal<Generator?>>
    var difficulty: Difficulty?
        private set
    private var spawnLocation: Location
    var seed: Long = 0
    private var worldTime = 0
    private var nextTimeSendTick: Long = 0
    private val entities: MutableMap<Long, Entity?>
    private val blockUpdateNormals: Queue<BlockUpdateNormal>

    init {
        gameRules = GameRules()
        blockUpdateList = BlockUpdateList()
        worldFolder = File("./worlds/$name")
        worldFile = File(worldFolder, "level.dat")
        if (!worldFolder.exists()) {
            worldFolder.mkdirs()
        }
        levelDB = LevelDB(this)
        chunkManagers = EnumMap<Dimension, ChunkManager>(Dimension::class.java)
        for (dimension in Dimension.values()) {
            chunkManagers[dimension] = ChunkManager(this, dimension)
        }
        val sendWarning = AtomicBoolean(false)
        generators = EnumMap<Dimension, ThreadLocal<Generator>>(
            Dimension::class.java
        )
        for (dimension in Dimension.values()) {
            val generatorName = generatorMap[dimension]
            generators[dimension] = ThreadLocal.withInitial {
                val generator = server.createGenerator(generatorName!!, this, dimension)
                if (generator != null && generator.javaClass == NormalGenerator::class.java && !sendWarning.get()) {
                    Server.Companion.getInstance().getLogger()
                        .warn("§cYou are currently using the Normal Generator, it may cause strong peformance problems!")
                    sendWarning.set(true)
                }
                generator
            }
        }
        val generator = getGenerator(Dimension.OVERWORLD)
        difficulty = server.difficulty
        spawnLocation = Location(this, generator.getSpawnLocation())
        entities = ConcurrentHashMap<Long, Entity>()
        blockUpdateNormals = ConcurrentLinkedQueue<BlockUpdateNormal>()
        loadLevelFile()
    }

    private fun loadLevelFile() {
        if (worldFile.exists()) {
            try {
                LittleEndianDataInputStream(Files.newInputStream(worldFile.toPath())).use { inputStream ->
                    NBTInputStream(inputStream).use { stream ->
                        val version = inputStream.readInt()
                        val size = inputStream.readInt()
                        val nbtTag = stream.readTag() as NbtMap
                        nbtTag.listenForString("LevelName") { name: String -> this.name = name }
                        nbtTag.listenForInt("Difficulty") { value: Int -> difficulty = Difficulty.values()[value] }
                        if (nbtTag.containsKey("SpawnX") && nbtTag.containsKey("SpawnY") && nbtTag.containsKey("SpawnZ")) {
                            spawnLocation = Location(
                                this,
                                nbtTag.getInt("SpawnX"),
                                nbtTag.getInt("SpawnY"),
                                nbtTag.getInt("SpawnZ")
                            )
                        }
                        nbtTag.listenForLong("RandomSeed") { seed: Long -> this.seed = seed }
                        nbtTag.listenForLong("Time") { value: Long -> worldTime = value.toInt() }
                        for (value in GameRule.values()) {
                            val identifer = value.identifier.lowercase(Locale.getDefault())
                            if (nbtTag.containsKey(identifer)) {
                                gameRules.set(value, nbtTag[identifer])
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else {
            seed = ThreadLocalRandom.current().nextInt().toLong()
            try {
                if (worldFile.createNewFile()) {
                    saveLevelFile()
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun saveLevelFile() {
        val nbtTag = NbtMap.builder()
        nbtTag.putInt("StorageVersion", STORAGE_VERSION)
        nbtTag.putString("LevelName", name)
        nbtTag.putInt("Difficulty", difficulty!!.ordinal)
        nbtTag["SpawnX"] = spawnLocation.blockX
        nbtTag["SpawnY"] = spawnLocation.blockY
        nbtTag["SpawnZ"] = spawnLocation.blockZ
        nbtTag.putLong("RandomSeed", seed)
        nbtTag.putLong("Time", worldTime.toLong())
        for (gameRule in gameRules.getGameRules()) {
            if (gameRule.value is Boolean) {
                nbtTag.putBoolean(gameRule.name.uppercase(Locale.getDefault()), gameRule.value as Boolean)
            } else if (gameRule.value is Int) {
                nbtTag.putInt(gameRule.name.uppercase(Locale.getDefault()), gameRule.value as Int)
            } else if (gameRule.value is Float) {
                nbtTag.putFloat(gameRule.name.uppercase(Locale.getDefault()), gameRule.value as Float)
            }
        }
        var tagBytes: ByteArray
        try {
            ByteArrayOutputStream().use { stream ->
                NbtUtils.createWriterLE(stream).use { nbtOutputStream ->
                    nbtOutputStream.writeTag(nbtTag.build())
                    tagBytes = stream.toByteArray()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        try {
            LittleEndianDataOutputStream(Files.newOutputStream(worldFile.toPath())).use { stream ->
                stream.writeInt(STORAGE_VERSION)
                stream.writeInt(tagBytes.size)
                stream.write(tagBytes)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun update(currentTick: Long) {
        if (currentTick % 100 == 0L) {
            for (chunkManager in chunkManagers.values) {
                chunkManager.tick()
            }
        }
        if (getGameRule(GameRule.DO_DAYLIGHT_CYCLE)) {
            worldTime += 1
        }
        if (currentTick >= nextTimeSendTick) {
            setWorldTime(worldTime)
            nextTimeSendTick = currentTick + 12 * 20 //Client send the time every 12 seconds
        }
        if (!entities.isEmpty()) {
            for (entity in entities.values) {
                entity!!.update(currentTick)
            }
        }
        for (dimension in Dimension.values()) {
            val blockEntities = getBlockEntities(dimension)
            if (blockEntities.size > 0) {
                for (blockEntity in blockEntities) {
                    blockEntity?.update(currentTick)
                }
            }
        }
        while (!blockUpdateNormals.isEmpty()) {
            val updateNormal: BlockUpdateNormal = blockUpdateNormals.poll()
            updateNormal.getBlock().onUpdate(UpdateReason.NORMAL)
        }
        while (blockUpdateList.getNextTaskTime() < currentTick) {
            val blockPosition: Vector = blockUpdateList.getNextElement() ?: break
            val block = this.getBlock(blockPosition)
            if (block != null) {
                val nextTime = block.onUpdate(UpdateReason.SCHEDULED)
                if (nextTime > currentTick) {
                    blockUpdateList.addElement(nextTime, blockPosition)
                }
            }
        }
    }

    fun getGameRules(): GameRules {
        return gameRules
    }

    fun <V> getGameRule(gameRule: GameRule): V {
        return gameRules.get<V>(gameRule)
    }

    fun setGameRule(gameRule: GameRule?, value: Any?) {
        gameRules.set(gameRule, value)
        for (player in players) {
            player.getPlayerConnection().sendPacket(gameRules.updatePacket())
        }
    }

    @Synchronized
    fun getGenerator(dimension: Dimension?): Generator? {
        return generators[dimension]!!.get()
    }

    fun setDifficulty(difficulty: Difficulty) {
        this.difficulty = difficulty
        val setDifficultyPacket = SetDifficultyPacket()
        setDifficultyPacket.setDifficulty(difficulty.ordinal)
        sendWorldPacket(setDifficultyPacket)
    }

    fun getSpawnLocation(): Location {
        return spawnLocation
    }

    fun setSpawnLocation(spawnLocation: Location) {
        this.spawnLocation = spawnLocation
        val setSpawnPositionPacket = SetSpawnPositionPacket()
        setSpawnPositionPacket.setSpawnType(SetSpawnPositionPacket.Type.WORLD_SPAWN)
        setSpawnPositionPacket.setBlockPosition(spawnLocation.toVector3i())
        setSpawnPositionPacket.setDimensionId(spawnLocation.dimension.ordinal)
        server.broadcastPacket(setSpawnPositionPacket)
    }

    fun getWorldTime(): Int {
        return worldTime
    }

    fun setWorldTime(worldTime: Int) {
        this.worldTime = worldTime
        val setTimePacket = SetTimePacket()
        setTimePacket.setTime(worldTime)
        Server.Companion.getInstance().broadcastPacket(setTimePacket)
    }

    fun addEntity(entity: Entity) {
        entities[entity.entityId] = entity
    }

    fun removeEntity(entity: Entity) {
        entities.remove(entity.entityId)
    }

    val entitys: Collection<Entity?>
        get() = entities.values
    val players: Collection<Player?>
        get() = entities.values.stream().filter { entity: Entity? -> entity is Player }
            .map { entity: Entity? -> entity as Player? }.collect(Collectors.toSet())

    fun getBlock(vector: Vector, layer: Int, dimension: Dimension?): Block {
        val chunk = this.getLoadedChunk(vector.chunkX, vector.chunkZ, dimension) ?: return BLOCK_AIR
        return chunk.getBlock(vector.blockX, vector.blockY, vector.blockZ, layer)
    }

    fun getBlock(vector: Vector, layer: Int): Block {
        return this.getBlock(vector, layer, Dimension.OVERWORLD)
    }

    fun getBlock(vector: Vector): Block {
        return this.getBlock(vector, 0, vector.dimension)
    }

    fun getBlock(vector: Vector, dimension: Dimension?): Block {
        return this.getBlock(vector, 0, dimension)
    }

    fun getBlock(x: Int, y: Int, z: Int, layer: Int, dimension: Dimension?): Block {
        return this.getBlock(Vector(x, y, z), layer, dimension)
    }

    fun getBlock(x: Int, y: Int, z: Int, layer: Int): Block {
        return this.getBlock(x, y, z, layer, Dimension.OVERWORLD)
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        return this.getBlock(x, y, z, 0)
    }

    fun setBlock(vector: Vector, block: Block, layer: Int, dimension: Dimension?, updateBlock: Boolean) {
        val chunk = this.getLoadedChunk(vector.chunkX, vector.chunkZ, dimension) ?: return
        chunk.setBlock(vector.blockX, vector.blockY, vector.blockZ, layer, block)
        chunk.isDirty = true
        val location = Location(this, vector, dimension)
        block.location = location
        block.layer = layer
        val updateBlockPacket = UpdateBlockPacket()
        updateBlockPacket.blockPosition = vector.toVector3i()
        updateBlockPacket.runtimeId = block.runtimeId
        updateBlockPacket.dataLayer = layer
        updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
        sendChunkPacket(vector.chunkX, vector.chunkZ, updateBlockPacket)
        if (block.blockEntity != null) {
            this.removeBlockEntity(vector, dimension)
        }
        if (updateBlock) {
            block.onUpdate(UpdateReason.NORMAL)
            this.getBlock(vector, if (layer == 0) 1 else 0).onUpdate(UpdateReason.NORMAL)
            updateBlockAround(vector)
            var next: Long
            for (blockFace in BlockFace.values()) {
                val blockSide = block.getSide(blockFace)
                if (blockSide!!.onUpdate(UpdateReason.NEIGHBORS).also { next = it } > server.currentTick) {
                    scheduleBlockUpdate(blockSide.location, next)
                }
            }
        }
    }

    fun setBlock(vector: Vector, block: Block, layer: Int, dimension: Dimension?) {
        this.setBlock(vector, block, layer, dimension, true)
    }

    fun setBlock(vector: Vector, block: Block, layer: Int) {
        this.setBlock(vector, block, layer, vector.dimension)
    }

    fun setBlock(vector: Vector, block: Block) {
        this.setBlock(vector, block, 0, vector.dimension)
    }

    fun setBlock(x: Int, y: Int, z: Int, layer: Int, block: Block, dimension: Dimension?) {
        this.setBlock(Vector(x, y, z), block, layer, dimension)
    }

    fun setBlock(x: Int, y: Int, z: Int, layer: Int, block: Block) {
        this.setBlock(x, y, z, layer, block, Dimension.OVERWORLD)
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        this.setBlock(x, y, z, 0, block)
    }

    @Synchronized
    fun getBlockEntity(vector: Vector, dimension: Dimension?): BlockEntity? {
        val chunk = this.getLoadedChunk(vector.chunkX, vector.chunkZ, dimension) ?: return null
        return chunk.getBlockEntity(vector.blockX, vector.blockY, vector.blockZ)
    }

    @Synchronized
    fun getBlockEntity(x: Int, y: Int, z: Int, dimension: Dimension?): BlockEntity? {
        val chunk = this.getLoadedChunk(x shr 4, z shr 4, dimension) ?: return null
        return chunk.getBlockEntity(x, y, z)
    }

    @Synchronized
    fun setBlockEntity(x: Int, y: Int, z: Int, blockEntity: BlockEntity, dimension: Dimension?) {
        val chunk = this.getLoadedChunk(x shr 4, z shr 4, dimension) ?: return
        chunk.setBlockEntity(x, y, z, blockEntity)
    }

    @Synchronized
    fun setBlockEntity(vector: Vector, blockEntity: BlockEntity, dimension: Dimension?) {
        val chunk = this.getLoadedChunk(vector.chunkX, vector.chunkZ, dimension) ?: return
        chunk.setBlockEntity(vector.blockX, vector.blockY, vector.blockZ, blockEntity)
    }

    @Synchronized
    fun removeBlockEntity(vector: Vector, dimension: Dimension?) {
        val chunk = this.getLoadedChunk(vector.chunkX, vector.chunkZ, dimension) ?: return
        chunk.removeBlockEntity(vector.blockX, vector.blockY, vector.blockZ)
    }

    @Synchronized
    fun removeBlockEntity(x: Int, y: Int, z: Int, dimension: Dimension?) {
        val chunk = this.getLoadedChunk(x shr 4, z shr 4, dimension) ?: return
        chunk.removeBlockEntity(x, y, z)
    }

    @Synchronized
    fun getBlockEntities(dimension: Dimension?): Collection<BlockEntity?> {
        val blockEntities: MutableSet<BlockEntity?> = LinkedHashSet()
        for (loadedChunk in chunkManagers[dimension].getLoadedChunks()) {
            blockEntities.addAll(loadedChunk!!.blockEntities)
        }
        return blockEntities
    }

    @Synchronized
    fun getBiome(vector: Vector, dimension: Dimension?): Biome? {
        val chunk = Objects.requireNonNull(getChunk(vector.chunkX, vector.chunkZ, dimension))
        return chunk!!.getBiome(vector.blockX, vector.blockY, vector.blockZ)
    }

    @Synchronized
    fun setBiome(vector: Vector, dimension: Dimension?, biome: Biome?) {
        val chunk = Objects.requireNonNull(getChunk(vector.chunkX, vector.chunkZ, dimension))
        chunk!!.setBiome(vector.blockX, vector.blockY, vector.blockZ, biome)
    }

    @Synchronized
    fun setBiome(x: Int, y: Int, z: Int, dimension: Dimension?, biome: Biome?) {
        this.setBiome(Vector(x, y, z), dimension, biome)
    }

    fun setBiome(vector: Vector, biome: Biome?) {
        this.setBiome(vector, Dimension.OVERWORLD, biome)
    }

    fun setBiome(x: Int, y: Int, z: Int, biome: Biome?) {
        this.setBiome(Vector(x, y, z), biome)
    }

    @Synchronized
    fun isChunkLoaded(chunkX: Int, chunkZ: Int, dimension: Dimension?): Boolean {
        return chunkManagers[dimension]!!.isChunkLoaded(chunkX, chunkZ)
    }

    @Synchronized
    fun getChunk(chunkX: Int, chunkZ: Int, dimension: Dimension?): Chunk? {
        return chunkManagers[dimension]!!.getChunk(chunkX, chunkZ)
    }

    @Synchronized
    fun getLoadedChunk(chunkX: Int, chunkZ: Int, dimension: Dimension?): Chunk? {
        return chunkManagers[dimension]!!.getLoadedChunk(chunkX, chunkZ)
    }

    @Synchronized
    fun getLoadedChunk(vector: Vector, dimension: Dimension?): Chunk? {
        return this.getLoadedChunk(vector.chunkX, vector.chunkZ, dimension)
    }

    @Synchronized
    fun getLoadedChunk(hash: Long, dimension: Dimension?): Chunk? {
        return chunkManagers[dimension]!!.getLoadedChunk(hash)
    }

    @Synchronized
    fun getLoadedChunks(dimension: Dimension?): Set<Chunk?> {
        return chunkManagers[dimension].getLoadedChunks()
    }

    fun getChunkFuture(chunkX: Int, chunkZ: Int, dimension: Dimension?): CompletableFuture<Chunk?>? {
        return chunkManagers[dimension]!!.getChunkFuture(chunkX, chunkZ)
    }

    fun getChunks(dimension: Dimension?): Set<Chunk?> {
        return chunkManagers[dimension].getLoadedChunks()
    }

    fun saveChunk(chunk: Chunk): CompletableFuture<Void?> {
        return levelDB.saveChunk(chunk)
    }

    fun saveChunks(dimension: Dimension?): CompletableFuture<Void?> {
        return chunkManagers[dimension]!!.saveChunks()
    }

    fun readChunk(chunk: Chunk): CompletableFuture<Chunk?> {
        return levelDB.readChunk(chunk)
    }

    fun getChunkPlayers(chunkX: Int, chunkZ: Int, dimension: Dimension?): Set<Player> {
        val chunk = this.getLoadedChunk(chunkX, chunkZ, dimension)
        return if (chunk == null) ImmutableSet.of() else HashSet(chunk.players)
    }

    fun sendChunkPacket(chunkX: Int, chunkZ: Int, packet: BedrockPacket?) {
        for (entity in entities.values) {
            if (entity is Player) {
                if (entity.isChunkLoaded(chunkX, chunkZ)) {
                    entity.playerConnection.sendPacket(packet)
                }
            }
        }
    }

    fun playSound(location: Location, soundEvent: SoundEvent?) {
        this.playSound(null, location, soundEvent, -1, ":", false, false)
    }

    fun playSound(position: Vector, soundEvent: SoundEvent?) {
        this.playSound(null, position, soundEvent, -1, ":", false, false)
    }

    fun playSound(player: Player, soundEvent: SoundEvent?) {
        this.playSound(player, player.location, soundEvent, -1, ":", false, false)
    }

    fun playSound(position: Vector, soundEvent: SoundEvent?, data: Int) {
        this.playSound(null, position, soundEvent, data, ":", false, false)
    }

    fun playSound(position: Vector, soundEvent: SoundEvent?, data: Int, entityIdentifier: String?) {
        this.playSound(null, position, soundEvent, data, entityIdentifier, false, false)
    }

    fun playSound(
        position: Vector,
        soundEvent: SoundEvent?,
        data: Int,
        entityIdentifier: String?,
        isBaby: Boolean,
        isGlobal: Boolean
    ) {
        this.playSound(null, position, soundEvent, data, entityIdentifier, isBaby, isGlobal)
    }

    fun playSound(
        player: Player?,
        position: Vector,
        soundEvent: SoundEvent?,
        data: Int,
        entityIdentifier: String?,
        isBaby: Boolean,
        isGlobal: Boolean
    ) {
        val levelSoundEventPacket = LevelSoundEventPacket()
        levelSoundEventPacket.setSound(soundEvent)
        levelSoundEventPacket.setPosition(position.toVector3f())
        levelSoundEventPacket.setExtraData(data)
        levelSoundEventPacket.setIdentifier(entityIdentifier)
        levelSoundEventPacket.setBabySound(isBaby)
        levelSoundEventPacket.setRelativeVolumeDisabled(isGlobal)
        if (player == null) {
            sendChunkPacket(position.chunkX, position.chunkZ, levelSoundEventPacket)
        } else {
            player.playerConnection.sendPacket(levelSoundEventPacket)
        }
    }

    fun spawnParticle(particle: Particle, position: Vector) {
        this.spawnParticle(null, particle, position, 0)
    }

    fun spawnParticle(particle: Particle, position: Vector, data: Int) {
        this.spawnParticle(null, particle, position, data)
    }

    fun spawnParticle(player: Player?, particle: Particle, position: Vector) {
        this.spawnParticle(player, particle, position, 0)
    }

    fun spawnParticle(player: Player?, particle: Particle, position: Vector, data: Int) {
        val levelEventPacket = LevelEventPacket()
        levelEventPacket.setType(particle.toLevelEvent())
        levelEventPacket.setData(data)
        levelEventPacket.setPosition(position.toVector3f())
        if (player != null) {
            player.playerConnection.sendPacket(levelEventPacket)
        } else {
            sendChunkPacket(position.chunkX, position.chunkZ, levelEventPacket)
        }
    }

    fun sendLevelEvent(position: Vector, levelEventType: LevelEventType?) {
        this.sendLevelEvent(null, position, levelEventType, 0)
    }

    fun sendLevelEvent(player: Player?, position: Vector, levelEventType: LevelEventType?) {
        this.sendLevelEvent(player, position, levelEventType, 0)
    }

    fun sendLevelEvent(position: Vector, levelEventType: LevelEventType?, data: Int) {
        this.sendLevelEvent(null, position, levelEventType, data)
    }

    fun sendLevelEvent(player: Player?, position: Vector, levelEventType: LevelEventType?, data: Int) {
        val levelEventPacket = LevelEventPacket()
        levelEventPacket.setPosition(position.toVector3f())
        levelEventPacket.setType(levelEventType)
        levelEventPacket.setData(data)
        if (player != null) {
            player.playerConnection.sendPacket(levelEventPacket)
        } else {
            sendChunkPacket(position.chunkX, position.chunkZ, levelEventPacket)
        }
    }

    private fun getRelative(blockPosition: Vector, position: Vector): Vector {
        val x = blockPosition.x + position.x
        val y = blockPosition.y + position.y
        val z = blockPosition.z + position.z
        return Vector(x, y, z, blockPosition.dimension)
    }

    fun getSidePosition(blockPosition: Vector, blockFace: BlockFace): Vector {
        return when (blockFace) {
            BlockFace.DOWN -> getRelative(blockPosition, Vector(0, -1, 0))
            BlockFace.UP -> getRelative(blockPosition, Vector(0, 1, 0))
            BlockFace.NORTH -> getRelative(blockPosition, Vector(0, 0, -1))
            BlockFace.SOUTH -> getRelative(blockPosition, Vector(0, 0, 1))
            BlockFace.WEST -> getRelative(blockPosition, Vector(-1, 0, 0))
            BlockFace.EAST -> getRelative(blockPosition, Vector(1, 0, 0))
        }
    }

    fun close() {
        levelDB.close()
        saveLevelFile()
    }

    fun save() {
        for (dimension in Dimension.values()) {
            saveChunks(dimension).whenComplete { unused: Void?, throwable: Throwable? -> }
        }
        saveLevelFile()
    }

    fun getNearbyEntities(boundingBox: AxisAlignedBB, dimension: Dimension?, entity: Entity?): Collection<Entity> {
        val targetEntity: MutableSet<Entity> = HashSet()
        val minX = FastMath.floor(((boundingBox.minX - 2) / 16).toDouble()).toInt()
        val maxX = FastMath.ceil(((boundingBox.maxX + 2) / 16).toDouble()).toInt()
        val minZ = FastMath.floor(((boundingBox.minZ - 2) / 16).toDouble()).toInt()
        val maxZ = FastMath.ceil(((boundingBox.maxZ + 2) / 16).toDouble()).toInt()
        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                val chunk = this.getLoadedChunk(x, z, dimension)
                if (chunk != null) {
                    for (iterateEntities in chunk.entities) {
                        if (iterateEntities == null) {
                            continue
                        }
                        if (iterateEntities != entity) {
                            val bb = iterateEntities.boundingBox
                            if (bb.intersectsWith(boundingBox)) {
                                if (entity != null) {
                                    if (entity.canCollideWith(iterateEntities)) {
                                        targetEntity.add(iterateEntities)
                                    }
                                } else {
                                    targetEntity.add(iterateEntities)
                                }
                            }
                        }
                    }
                }
            }
        }
        return targetEntity
    }

    fun getCollisionCubes(entity: Entity, boundingBox: AxisAlignedBB, includeEntities: Boolean): List<AxisAlignedBB?> {
        val minX = FastMath.floor(boundingBox.minX.toDouble()).toInt()
        val minY = FastMath.floor(boundingBox.minY.toDouble()).toInt()
        val minZ = FastMath.floor(boundingBox.minZ.toDouble()).toInt()
        val maxX = FastMath.ceil(boundingBox.maxX.toDouble()).toInt()
        val maxY = FastMath.ceil(boundingBox.maxY.toDouble()).toInt()
        val maxZ = FastMath.ceil(boundingBox.maxZ.toDouble()).toInt()
        val collides: MutableList<AxisAlignedBB?> = ArrayList()
        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val block = this.getBlock(Vector(x, y, z), 0)
                    if (!block.canPassThrough() && block.boundingBox.intersectsWith(boundingBox)) {
                        collides.add(block.boundingBox)
                    }
                }
            }
        }
        if (includeEntities) {
            for (nearbyEntity in getNearbyEntities(boundingBox.grow(0.25f, 0.25f, 0.25f), entity.dimension, entity)) {
                if (!nearbyEntity.canPassThrough()) {
                    collides.add(nearbyEntity.boundingBox.clone())
                }
            }
        }
        return collides
    }

    fun scheduleBlockUpdate(location: Location?, delay: Long) {
        blockUpdateList.addElement(server.currentTick + delay, location)
    }

    fun updateBlockAround(location: Vector) {
        val block = this.getBlock(location)
        for (blockFace in BlockFace.values()) {
            val blockLayer0 = block.getSide(blockFace, 0)
            val blockLayer1 = block.getSide(blockFace, 1)
            blockUpdateNormals.add(BlockUpdateNormal(blockLayer0, blockFace))
            blockUpdateNormals.add(BlockUpdateNormal(blockLayer1, blockFace))
        }
    }

    fun dropItem(item: Item?, location: Vector, velocity: Vector?): EntityItem {
        var velocity = velocity
        if (velocity == null) {
            velocity = Vector(
                (ThreadLocalRandom.current().nextDouble() * 0.2f - 0.1f).toFloat(),
                0.2f, (ThreadLocalRandom.current().nextDouble() * 0.2f - 0.1f).toFloat()
            )
        }
        val entityItem = Objects.requireNonNull<EntityItem>(Entity.Companion.create<EntityItem>(EntityType.ITEM))
        entityItem.item = item
        entityItem.setVelocity(velocity, false)
        entityItem.location = Location(this, location)
        entityItem.setPickupDelay(1, TimeUnit.SECONDS)
        return entityItem
    }

    fun sendWorldPacket(packet: BedrockPacket?) {
        for (player in players) {
            player.getPlayerConnection().sendPacket(packet)
        }
    }

    fun sendDimensionPacket(packet: BedrockPacket?, dimension: Dimension?) {
        for (player in players) {
            if (player.getDimension() == dimension) {
                player.getPlayerConnection().sendPacket(packet)
            }
        }
    }

    fun getEntity(entityId: Long): Entity? {
        return entities[entityId]
    }
}