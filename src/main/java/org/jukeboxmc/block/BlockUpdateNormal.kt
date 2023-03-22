package org.jukeboxmc.block

import org.jukeboxmc.block.direction.BlockFace

/**
 * @author LucGamesYT
 * @version 1.0
 */
data class BlockUpdateNormal(
    val block: Block,
    val blockFace: BlockFace,
)
