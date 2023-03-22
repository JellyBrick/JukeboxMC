package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.GrassType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemTallGrass
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockTallGrass : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        val blockDown = world.getBlock(blockPosition)
        if (blockDown.type == BlockType.GRASS || blockDown.type == BlockType.DIRT || blockDown.type == BlockType.PODZOL) {
            world.setBlock(placePosition, this)
            return true
        }
        return false
    }

    override fun toItem(): Item {
        return Item.create<ItemTallGrass>(ItemType.TALLGRASS).setGrassType(
            grassType,
        )
    }

    fun setGrassType(grassType: GrassType): BlockTallGrass {
        return setState("tall_grass_type", grassType.name.lowercase(Locale.getDefault()))
    }

    val grassType: GrassType
        get() = if (stateExists("tall_grass_type")) GrassType.valueOf(getStringState("tall_grass_type")) else GrassType.DEFAULT
}
