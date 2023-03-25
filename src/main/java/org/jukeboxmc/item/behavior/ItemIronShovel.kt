package org.jukeboxmc.item.behavior

import org.cloudburstmc.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.TierType
import org.jukeboxmc.item.ToolType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemIronShovel : Item, Durability {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun interact(player: Player, blockFace: BlockFace?, clickedVector: Vector?, clickedBlock: Block): Boolean {
        if (clickedBlock.type == BlockType.GRASS) {
            player.world.setBlock(clickedBlock.location, Block.create(BlockType.GRASS_PATH))
            player.world.playSound(clickedBlock.location, SoundEvent.ITEM_USE_ON, 12259)
            this.updateItem(player, 1)
            return true
        }
        return false
    }

    override fun addToHand(player: Player) {
        val attribute = player.getAttribute(AttributeType.ATTACK_DAMAGE)
        attribute.setCurrentValue(3f)
    }

    override fun removeFromHand(player: Player) {
        val attribute = player.getAttribute(AttributeType.ATTACK_DAMAGE)
        attribute.setCurrentValue(attribute.minValue)
    }

    override val maxDurability: Int
        get() = 250
    override val toolType: ToolType
        get() = ToolType.SHOVEL
    override val tierType: TierType
        get() = TierType.IRON
}
