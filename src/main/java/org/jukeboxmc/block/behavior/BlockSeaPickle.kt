package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSeaPickle : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    fun clusterCount(value: Int) { // 0-3
        setState<Block>("cluster_count", value)
    }

    val clusterCount: Int
        get() = if (stateExists("cluster_count")) getIntState("cluster_count") else 0
    var isDead: Boolean
        get() = stateExists("dead_bit") && getByteState("dead_bit").toInt() == 1
        set(value) {
            setState<Block>("dead_bit", if (value) 1.toByte() else 0.toByte())
        }
}
