package org.jukeboxmc.blockentity

import com.google.common.base.Joiner
import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.event.block.BlockSignChangeEvent
import org.jukeboxmc.player.Player
import java.util.Arrays
import java.util.Collections

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntitySign(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType) {
    val lines: MutableList<String>

    init {
        lines = ArrayList(4)
    }

    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        val text = compound.getString("Text", "")
        lines.addAll(
            Arrays.asList(
                *text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray(),
            ),
        )
    }

    override fun toCompound(): NbtMapBuilder {
        val compound = super.toCompound()
        compound.putString("Text", Joiner.on("\n").skipNulls().join(lines))
        return compound
    }

    fun updateBlockEntitySign(nbt: NbtMap, player: Player) {
        val text = nbt.getString("Text", "")
        val splitLine = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val lineList = ArrayList<String>()
        Collections.addAll(lineList, *splitLine)
        val blockSignChangeEvent = BlockSignChangeEvent(block, player, lineList)
        Server.instance.pluginManager.callEvent(blockSignChangeEvent)
        if (blockSignChangeEvent.isCancelled) {
            return
        }
        lines.clear()
        lines.addAll(blockSignChangeEvent.getLines())
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.blockPosition = block.getLocation().toVector3i()
        blockEntityDataPacket.data = toCompound().build()
        Server.instance.broadcastPacket(blockEntityDataPacket)
    }

    fun updateBlockEntitySign() {
        val blockEntityDataPacket = BlockEntityDataPacket()
        blockEntityDataPacket.blockPosition = block.getLocation().toVector3i()
        blockEntityDataPacket.data = toCompound().build()
        Server.instance.broadcastPacket(blockEntityDataPacket)
    }
}
