package org.jukeboxmc.world.chunk.util // ktlint-disable filename

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.palette.PersistentDataDeserializer
import org.jukeboxmc.block.palette.PersistentDataSerializer
import org.jukeboxmc.block.palette.RuntimeDataDeserializer
import org.jukeboxmc.block.palette.RuntimeDataSerializer

class SimpleRuntimeDataSerializer<V>(private val func: (V) -> Int) : RuntimeDataSerializer<V> {
    override fun serialize(value: V): Int {
        return func(value)
    }
}

class SimplePersistentDataSerializer<V>(private val func: (V) -> NbtMap?) : PersistentDataSerializer<V> {
    override fun serialize(value: V): NbtMap? {
        return func(value)
    }
}

class SimpleRuntimeDataDeserializer<V>(private val func: (Int) -> V) : RuntimeDataDeserializer<V> {
    override fun deserialize(id: Int): V {
        return func(id)
    }
}

class SimplePersistentDataDeserializer<V>(private val func: (NbtMap) -> V) : PersistentDataDeserializer<V> {
    override fun deserialize(nbtMap: NbtMap): V {
        return func(nbtMap)
    }
}
