package org.jukeboxmc.command

import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType
import java.util.Locale

/**
 * @author Cloudburst
 * @version 1.0
 */
class CommandData(
    var name: String,
    var description: String,
    var usage: String,
    private var permission: String,
    private var permissionMessage: String,
    private val aliases: CommandEnum,
    private val overloads: MutableList<Array<CommandParameter>>,
) {
    fun getPermission(): String {
        return permission
    }

    fun setPermission(permission: String) {
        this.permission = permission
    }

    fun getAliases(): MutableList<String> {
        return aliases.values
    }

    fun removeAlias(alias: String?) {
        aliases.values.remove(alias)
    }

    fun getOverloads(): List<Array<CommandParameter>> {
        return overloads
    }

    fun getPermissionMessage(): String {
        return permissionMessage
    }

    fun setPermissionMessage(permissionMessage: String) {
        this.permissionMessage = permissionMessage
    }

    fun rebuild(): CommandData {
        if (overloads.size == 0) {
            overloads.add(arrayOf(CommandParameter("args", CommandParamType.TEXT, true)))
        }
        return CommandData(
            name,
            description,
            usage,
            permission,
            permissionMessage,
            CommandEnum(
                name,
                getAliases(),
            ),
            overloads,
        )
    }

    fun toNetwork(): org.cloudburstmc.protocol.bedrock.data.command.CommandData {
        val description = description
        val overloadData: Array<Array<CommandParamData?>?> = arrayOfNulls(
            overloads.size,
        )
        for (i in overloadData.indices) {
            val parameters = overloads[i]
            val params: Array<CommandParamData?> = arrayOfNulls(parameters.size)
            for (i2 in parameters.indices) {
                params[i2] = parameters[i2].toNetwork()
            }
            overloadData[i] = params
        }
        return org.cloudburstmc.protocol.bedrock.data.command.CommandData(
            name,
            description,
            emptySet(),
            0.toByte(),
            aliases.toNetwork(),
            overloadData,
        )
    }

    class Builder {
        private var name = ""
        private var description = ""
        private var usage = ""
        private var permission = ""
        private var permissionMessage = ""
        private var aliases: MutableList<String> = ArrayList()
        private var overloads: MutableList<Array<CommandParameter>> = ArrayList()

        constructor()
        constructor(name: String) {
            this.name = name.lowercase(Locale.getDefault())
        }

        fun build(): CommandData {
            if (overloads.size == 0) {
                overloads.add(arrayOf(CommandParameter("args", CommandParamType.TEXT, true)))
            }
            return CommandData(
                name,
                description,
                usage,
                permission,
                permissionMessage,
                CommandEnum(
                    name,
                    aliases,
                ),
                overloads,
            )
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setUsageMessage(usage: String): Builder {
            this.usage = usage
            return this
        }

        fun setPermissionMessage(message: String): Builder {
            permissionMessage = message
            return this
        }

        fun setPermissions(permissions: String): Builder {
            permission = permissions
            return this
        }

        fun setAliases(vararg aliases: String): Builder {
            this.aliases = mutableListOf(*aliases)
            return this
        }

        fun addAlias(alias: String): Builder {
            aliases.add(alias)
            return this
        }

        fun addAliases(vararg aliases: String): Builder {
            this.aliases.addAll(listOf(*aliases))
            return this
        }

        fun setParameters(vararg paramSet: Array<CommandParameter>): Builder {
            overloads = mutableListOf(*paramSet)
            return this
        }

        fun setParameters(parameters: MutableList<Array<CommandParameter>>): Builder {
            overloads = parameters
            return this
        }

        fun addParameters(vararg paramSet: Array<CommandParameter>): Builder {
            overloads.addAll(listOf(*paramSet))
            return this
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }

        fun builder(commandName: String): Builder {
            return Builder(commandName)
        }
    }
}
