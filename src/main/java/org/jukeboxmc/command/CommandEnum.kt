package org.jukeboxmc.command

import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumConstraint
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData

/**
 * @author Cloudburst
 * @version 1.0
 */
class CommandEnum(val name: String, val values: MutableList<String>) {

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun toNetwork(): CommandEnumData {
        // TODO: validate this
        val hashMap = LinkedHashMap<String, Set<CommandEnumConstraint>>()
        val aliases: LinkedHashMap<String, Set<CommandEnumConstraint>> = if (values.size > 0) {
            val aliasList: MutableList<String> = ArrayList(values)
            aliasList.add(name)
            for (alias in aliasList) {
                hashMap[alias] = emptySet()
            }
            hashMap
        } else {
            hashMap
        }
        return CommandEnumData(name + "Aliases", aliases, false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandEnum

        if (name != other.name) return false
        if (values != other.values) return false

        return true
    }
}
