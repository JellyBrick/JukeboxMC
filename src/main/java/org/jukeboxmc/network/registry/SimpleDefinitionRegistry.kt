package org.jukeboxmc.network.registry

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.cloudburstmc.protocol.common.Definition
import org.cloudburstmc.protocol.common.DefinitionRegistry

class SimpleDefinitionRegistry<V : Definition> : DefinitionRegistry<V> {

    private val definitions: Int2ObjectMap<V> = Int2ObjectOpenHashMap()

    override fun getDefinition(runtimeId: Int): V = definitions.get(runtimeId)

    override fun isRegistered(definition: V): Boolean = definitions.containsValue(definition)

    fun register(runtimeId: Int, definition: V) {
        definitions.put(runtimeId, definition)
    }

    companion object {
        private val registries = mutableMapOf<Class<out Definition>, SimpleDefinitionRegistry<out Definition>>()

        fun getRegistry(clazz: Class<out Definition>): DefinitionRegistry<out Definition> {
            return registries.getOrPut(clazz) { SimpleDefinitionRegistry() }
        }

        inline fun <reified D : Definition, reified V : DefinitionRegistry<D>> getRegistry(): V {
            return getRegistry(D::class.java) as V
        }
    }
}
