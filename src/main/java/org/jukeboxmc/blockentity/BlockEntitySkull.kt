package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import org.jukeboxmc.block.Block

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntitySkull(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType) {
    private var skullMeta: Byte = 0
    var rotation: Byte = 0
        private set

    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        skullMeta = compound.getByte("SkullType")
        rotation = compound.getByte("Rot")
    }

    override fun toCompound(): NbtMapBuilder {
        val nbtMapBuilder = super.toCompound()
        nbtMapBuilder!!.putByte("SkullType", skullMeta)
        nbtMapBuilder.putByte("Rot", rotation)
        return nbtMapBuilder
    }

    fun setSkullMeta(skullMeta: Byte): BlockEntitySkull {
        this.skullMeta = skullMeta
        return this
    }

    fun setRotation(rotation: Byte): BlockEntitySkull {
        this.rotation = rotation
        return this
    }
}