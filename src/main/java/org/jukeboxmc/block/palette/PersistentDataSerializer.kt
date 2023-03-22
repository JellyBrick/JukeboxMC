package org.jukeboxmc.block.palette

import com.nukkitx.nbt.NbtMap

interface PersistentDataSerializer<V> {
    fun serialize(value: V): NbtMap?
}
