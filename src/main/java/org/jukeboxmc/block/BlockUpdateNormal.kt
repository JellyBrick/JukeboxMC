package org.jukeboxmc.block

import lombok.AllArgsConstructor
import lombok.Data
import org.jukeboxmc.block.direction.BlockFace

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Data
@AllArgsConstructor
class BlockUpdateNormal {
    private val block: Block? = null
    private val blockFace: BlockFace? = null
}