package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.FlowerType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemRedFlower
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockRedFlower : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemRedFlower>(ItemType.RED_FLOWER).setFlowerType(
            flowerType,
        )
    }

    fun setFlowerType(flowerType: FlowerType): BlockRedFlower {
        return setState("flower_type", flowerType.name.lowercase(Locale.getDefault()))
    }

    val flowerType: FlowerType
        get() = if (stateExists("flower_type")) FlowerType.valueOf(getStringState("flower_type")) else FlowerType.TULIP_RED
}
