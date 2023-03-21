package org.jukeboxmc.block.palette

interface RuntimeDataSerializer<V> {
    fun serialize(value: V): Int
}