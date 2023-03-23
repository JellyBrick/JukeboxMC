package org.jukeboxmc.block.palette

import org.cloudburstmc.nbt.NbtMap

interface PersistentDataSerializer<V> {
    fun serialize(value: V): NbtMap?
}
