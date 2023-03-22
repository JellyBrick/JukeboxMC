package org.jukeboxmc.block.palette

interface RuntimeDataDeserializer<V> {
    fun deserialize(id: Int): V
}
