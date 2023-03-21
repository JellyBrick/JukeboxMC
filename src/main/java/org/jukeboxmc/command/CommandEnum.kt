package org.jukeboxmc.command

import lombok.ToString

/**
 * @author Cloudburst
 * @version 1.0
 */
@ToString
class CommandEnum(val name: String, val values: MutableList<String?>?) {

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun toNetwork(): CommandEnumData {
        val aliases: Array<String?>
        aliases = if (values!!.size > 0) {
            val aliasList: MutableList<String?> = ArrayList(values)
            aliasList.add(name)
            aliasList.toTypedArray()
        } else {
            arrayOf(name)
        }
        return CommandEnumData(name + "Aliases", aliases, false)
    }
}