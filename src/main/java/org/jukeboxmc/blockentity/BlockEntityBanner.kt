package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import com.nukkitx.nbt.NbtType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.BlockColor

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityBanner(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType) {
    private var baseColor = 0
    var type = 0
        private set
    private val patterns: MutableMap<String, Int> = LinkedHashMap()
    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        baseColor = compound.getInt("Base", 0)
        type = compound.getInt("Type", 0)
        val patterns = compound.getList("blocks", NbtType.COMPOUND)
        if (patterns != null) {
            for (pattern in patterns) {
                this.patterns[pattern.getString("Pattern", "ss")] = pattern.getInt("Color", 0)
            }
        }
    }

    override fun toCompound(): NbtMapBuilder {
        val compound = super.toCompound()
        compound.putInt("Base", baseColor)
        compound.putInt("Type", type)
        if (patterns.size > 0) {
            val pattern: MutableList<NbtMap> = ArrayList()
            for ((key, value) in patterns) {
                val patternBuilder = NbtMap.builder()
                patternBuilder.putString("Pattern", key)
                patternBuilder.putInt("Color", value)
                pattern.add(patternBuilder.build())
            }
            compound.putList("Patterns", NbtType.COMPOUND, pattern)
        }
        return compound
    }

    fun setColor(blockColor: BlockColor): BlockEntityBanner {
        baseColor = blockColor.ordinal
        return this
    }

    fun getBaseColor(): BlockColor {
        return BlockColor.values()[baseColor]
    }

    fun setType(type: Int): BlockEntityBanner {
        this.type = type
        return this
    }
}
