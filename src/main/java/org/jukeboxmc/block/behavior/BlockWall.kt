package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.block.data.WallConnectionType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.*

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BlockWall : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        updateWall()
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    override fun onUpdate(updateReason: UpdateReason): Long {
        if (updateReason == UpdateReason.NEIGHBORS) {
            updateWall()
        }
        return super.onUpdate(updateReason)
    }

    override val boundingBox: AxisAlignedBB
        get() {
            val north = canConnect(this.getSide(BlockFace.NORTH))
            val south = canConnect(this.getSide(BlockFace.SOUTH))
            val west = canConnect(this.getSide(BlockFace.WEST))
            val east = canConnect(this.getSide(BlockFace.EAST))
            var n: Float = if (north) 0f else 0.25f
            var s: Float = if (south) 1f else 0.75f
            var w: Float = if (west) 0f else 0.25f
            var e: Float = if (east) 1f else 0.75f
            if (north && south && !west && !east) {
                w = 0.3125f
                e = 0.6875f
            } else if (!north && !south && west && east) {
                n = 0.3125f
                s = 0.6875f
            }
            return AxisAlignedBB(
                location.x + w,
                location.y,
                location.z + n,
                location.x + e,
                location.y + 1.5f,
                location.z + s,
            )
        }

    protected fun updateWall() {
        for (value in Direction.values()) {
            val block = this.getSide(value)
            if (canConnect(block)) {
                connect(value, WallConnectionType.SHORT)
            } else {
                connect(value, WallConnectionType.NONE)
            }
        }
        if (getWallConnectionType(Direction.NORTH) == WallConnectionType.SHORT &&
            getWallConnectionType(Direction.SOUTH) == WallConnectionType.SHORT
        ) {
            isWallPost = getWallConnectionType(Direction.WEST) != WallConnectionType.NONE || getWallConnectionType(
                Direction.EAST,
            ) != WallConnectionType.NONE
        } else if (getWallConnectionType(Direction.WEST) == WallConnectionType.SHORT && getWallConnectionType(Direction.EAST) == WallConnectionType.SHORT) {
            isWallPost = getWallConnectionType(Direction.SOUTH) != WallConnectionType.NONE ||
                getWallConnectionType(Direction.NORTH) != WallConnectionType.NONE
        }
        if (this.getSide(BlockFace.UP).isSolid) {
            isWallPost = true
        }
        this.sendUpdate()
        location.chunk?.setBlock(location.blockX, location.blockY, location.blockZ, 0, this)
    }

    fun canConnect(block: Block): Boolean {
        return when (block.type) {
            BlockType.COBBLESTONE_WALL, BlockType.BLACKSTONE_WALL, BlockType.POLISHED_BLACKSTONE_WALL, BlockType.POLISHED_BLACKSTONE_BRICK_WALL, BlockType.POLISHED_DEEPSLATE_WALL, BlockType.DEEPSLATE_BRICK_WALL, BlockType.DEEPSLATE_TILE_WALL, BlockType.MUD_BRICK_WALL, BlockType.COBBLED_DEEPSLATE_WALL, BlockType.GLASS_PANE, BlockType.STAINED_GLASS_PANE, BlockType.IRON_BARS -> true
            else -> block.isSolid && !block.isTransparent
        }
    }

    fun connect(direction: Direction, wallConnectionType: WallConnectionType) {
        when (direction) {
            Direction.SOUTH -> wallConnectionTypeSouth = wallConnectionType
            Direction.EAST -> wallConnectionTypeEast = wallConnectionType
            Direction.WEST -> wallConnectionTypeWest = wallConnectionType
            Direction.NORTH -> wallConnectionTypeNorth = wallConnectionType
        }
    }

    fun getWallConnectionType(direction: Direction): WallConnectionType {
        return when (direction) {
            Direction.SOUTH -> wallConnectionTypeSouth
            Direction.EAST -> wallConnectionTypeEast
            Direction.WEST -> wallConnectionTypeWest
            Direction.NORTH -> wallConnectionTypeNorth
        }
    }

    var isWallPost: Boolean
        get() = stateExists("wall_post_bit") && getByteState("wall_post_bit").toInt() == 1
        set(value) {
            setState<Block>("wall_post_bit", if (value) 1.toByte() else 0.toByte())
        }
    var wallConnectionTypeEast: WallConnectionType
        get() = if (stateExists("wall_connection_type_east")) WallConnectionType.valueOf(getStringState("wall_connection_type_east")) else WallConnectionType.NONE
        set(wallConnectionTypeEast) {
            setState<Block>("wall_connection_type_east", wallConnectionTypeEast.name.lowercase(Locale.getDefault()))
        }
    var wallConnectionTypeSouth: WallConnectionType
        get() = if (stateExists("wall_connection_type_south")) WallConnectionType.valueOf(getStringState("wall_connection_type_south")) else WallConnectionType.NONE
        set(wallConnectionTypeEast) {
            setState<Block>("wall_connection_type_south", wallConnectionTypeEast.name.lowercase(Locale.getDefault()))
        }
    var wallConnectionTypeWest: WallConnectionType
        get() = if (stateExists("wall_connection_type_west")) WallConnectionType.valueOf(getStringState("wall_connection_type_west")) else WallConnectionType.NONE
        set(wallConnectionTypeEast) {
            setState<Block>("wall_connection_type_west", wallConnectionTypeEast.name.lowercase(Locale.getDefault()))
        }
    var wallConnectionTypeNorth: WallConnectionType
        get() = if (stateExists("wall_connection_type_north")) WallConnectionType.valueOf(getStringState("wall_connection_type_north")) else WallConnectionType.NONE
        set(wallConnectionTypeEast) {
            setState<Block>("wall_connection_type_north", wallConnectionTypeEast.name.lowercase(Locale.getDefault()))
        }
}
