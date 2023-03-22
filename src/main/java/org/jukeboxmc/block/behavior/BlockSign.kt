package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.SignDirection
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntitySign
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BlockSign : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override val blockEntity: BlockEntity?
        get() = location.world?.getBlockEntity(location, location.dimension) as BlockEntitySign?
    val lines: List<String>?
        get() {
            val blockEntity: BlockEntitySign? = blockEntity as BlockEntitySign?
            return if (blockEntity != null) ArrayList(blockEntity.lines) else null
        }

    fun setLine(line: Int, value: String?) {
        if (line > 4 || line < 0) {
            return
        }
        val blockEntity: BlockEntitySign? = blockEntity as BlockEntitySign?
        if (blockEntity != null) {
            blockEntity.lines[line] = value ?: ""
            blockEntity.updateBlockEntitySign()
        }
    }

    var signDirection: SignDirection
        get() = if (stateExists("ground_sign_direction")) SignDirection.values()[getIntState("ground_sign_direction")] else SignDirection.SOUTH
        set(signDirection) {
            setState<Block>("ground_sign_direction", signDirection.ordinal)
        }
}
