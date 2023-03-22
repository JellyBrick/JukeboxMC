package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockWater : BlockLiquid {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun canBeReplaced(block: Block?): Boolean {
        return true
    }

    override val tickRate: Int
        get() = 5

    override fun usesWaterLogging(): Boolean {
        return true
    }

    override fun canPassThrough(): Boolean {
        return true
    }

    override fun getBlock(liquidDepth: Int): BlockLiquid {
        val blockWater: BlockWater = create(BlockType.WATER)
        blockWater.setLiquidDepth(liquidDepth)
        return blockWater
    }
}
