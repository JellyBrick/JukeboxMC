package org.jukeboxmc.block.palette

import com.nukkitx.nbt.NbtMap

interface PersistentDataDeserializer<V> {
    fun deserialize(nbtMap: NbtMap?): V
}