package org.jukeboxmc.block.data

import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType

/**
 * @author LucGamesYT
 * @version 1.0
 */
data class BlockProperties(
    val hardness: Double,
    val solid: Boolean,
    val transparent: Boolean,
    val toolType: ToolType,
    val tierType: TierType,
    val canBreakWithHand: Boolean,
    val canPassThrough: Boolean,
)
