package org.jukeboxmc.command

import com.google.common.collect.ImmutableMap
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType

/**
 * @author Cloudburst
 * @version 1.0
 */
class CommandParameter {
    var name: String
    var type: CommandParamType
    var optional: Boolean
    var options: Byte = 0
    var enumData: CommandEnum? = null
    var postFix: String? = null

    constructor(name: String, type: CommandParamType, optional: Boolean) {
        this.name = name
        this.type = type
        this.optional = optional
    }

    @JvmOverloads
    constructor(name: String, enumType: String, optional: Boolean = false) {
        this.name = name
        enumData = CommandEnum(enumType, ArrayList())
        type = CommandParamType.TEXT
        this.optional = optional
    }

    @JvmOverloads
    constructor(name: String, enumValues: MutableList<String>, optional: Boolean = false) {
        this.name = name
        type = CommandParamType.TEXT
        enumData = CommandEnum(name + "Enums", enumValues)
        this.optional = optional
    }

    fun toNetwork(): CommandParamData {
        return CommandParamData().apply {
            this.name = name
            this.isOptional = optional
            this.enumData =
                if (enumData != null) {
                    CommandEnumData(
                        name,
                        enumData.values,
                        false,
                    )
                } else {
                    null
                }
            this.type = PARAM_MAPPINGS[type.paramType]
            this.postfix = postFix
//            this.options = emptySet<CommandParamOption>()
        }
    }

    companion object {
        private val PARAM_MAPPINGS: ImmutableMap<CommandParamType, CommandParam> =
            ImmutableMap.builder<CommandParamType, CommandParam>()
                .put(CommandParamType.INT, CommandParam.INT)
                .put(CommandParamType.FLOAT, CommandParam.FLOAT)
                .put(CommandParamType.VALUE, CommandParam.VALUE)
                .put(CommandParamType.WILDCARD_INT, CommandParam.WILDCARD_INT)
                .put(CommandParamType.OPERATOR, CommandParam.OPERATOR)
                .put(CommandParamType.TARGET, CommandParam.TARGET)
                .put(CommandParamType.WILDCARD_TARGET, CommandParam.WILDCARD_TARGET)
                .put(CommandParamType.FILE_PATH, CommandParam.FILE_PATH)
                .put(CommandParamType.INT_RANGE, CommandParam.INT_RANGE)
                .put(CommandParamType.STRING, CommandParam.STRING)
                .put(CommandParamType.POSITION, CommandParam.POSITION)
                .put(CommandParamType.BLOCK_POSITION, CommandParam.BLOCK_POSITION)
                .put(CommandParamType.MESSAGE, CommandParam.MESSAGE)
                .put(CommandParamType.TEXT, CommandParam.TEXT)
                .put(CommandParamType.JSON, CommandParam.JSON)
                .put(CommandParamType.COMMAND, CommandParam.COMMAND)
                .build()
    }
}
