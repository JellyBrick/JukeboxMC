package org.jukeboxmc.world.chunk

import it.unimi.dsi.fastutil.longs.LongComparator
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Utils

/**
 * @author LucGamesYT
 * @version 1.0
 */
@JvmRecord
data class ChunkComparator(val player: Player) : LongComparator {
    override fun compare(o1: Long, o2: Long): Int {
        val x1 = Utils.fromHashX(o1)
        val z1 = Utils.fromHashZ(o1)
        val x2 = Utils.fromHashX(o2)
        val z2 = Utils.fromHashZ(o2)
        val spawnX = player.blockX shr 4
        val spawnZ = player.blockZ shr 4
        return Integer.compare(distance(spawnX, spawnZ, x1, z1), distance(spawnX, spawnZ, x2, z2))
    }

    companion object {
        fun distance(centerX: Int, centerZ: Int, x: Int, z: Int): Int {
            val dx = centerX - x
            val dz = centerZ - z
            return dx * dx + dz * dz
        }
    }
}