package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Dimension

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLava : BlockLiquid {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun canBeReplaced(block: Block?): Boolean {
        return true
    }

    override val flowDecayPerBlock: Int
        get() = if (location.dimension == Dimension.NETHER) {
            1
        } else {
            2
        }
    override val tickRate: Int
        get() = if (location.dimension == Dimension.NETHER) {
            10
        } else {
            30
        }

    override fun checkForHarden() {
        var colliding: Block? = null
        for (value in BlockFace.values()) {
            if (value == BlockFace.DOWN) continue
            val blockSide = this.getSide(value)
            if (blockSide is BlockWater) {
                colliding = blockSide
                break
            }
        }
        if (colliding != null) {
            if (this.liquidDepth == 0) {
                liquidCollide(create<Block>(BlockType.OBSIDIAN))
            } else if (this.liquidDepth <= 4) {
                liquidCollide(create<Block>(BlockType.COBBLESTONE))
            }
        }
    }

    override fun flowIntoBlock(block: Block, newFlowDecay: Int) {
        if (block is BlockWater) {
            (block as BlockLiquid).liquidCollide(create<Block>(BlockType.STONE))
        } else {
            super.flowIntoBlock(block, newFlowDecay)
        }
    }

    override fun getBlock(liquidDepth: Int): BlockLiquid {
        val blockLava: BlockLava = create<BlockLava>(BlockType.LAVA)
        blockLava.setLiquidDepth(liquidDepth)
        return blockLava
    }
}
