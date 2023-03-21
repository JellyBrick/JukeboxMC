package org.jukeboxmc.block.data

import lombok.AllArgsConstructor
import lombok.Data
import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Data
@AllArgsConstructor
class BlockProperties {
    private val hardness = 0.0
    private val solid = false
    private val transparent = false
    private val toolType: ToolType = null
    private val tierType: TierType = null
    private val canBreakWithHand = false
    private val canPassThrough = false
}