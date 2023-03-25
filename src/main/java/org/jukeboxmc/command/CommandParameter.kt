package org.jukeboxmc.command

import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamOption
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType

/**
 * @author Cloudburst
 * @version 1.0
 */
class CommandParameter {
    var name: String
    var type: CommandParamType
    var optional: Boolean
    var options: MutableSet<CommandParamOption> = hashSetOf()
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
        enumData = CommandEnum(enumType, mutableListOf())
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
        return CommandParamData().also {
            it.name = name
            it.isOptional = optional
            it.enumData = enumData?.let { commandEnum ->
                CommandEnumData(
                    name,
                    commandEnum.toNetwork().values,
                    false,
                )
            }
            it.type = PARAM_MAPPINGS[type]
            it.postfix = postFix
            it.options.addAll(options)
        }
    }

    companion object {
        private val PARAM_MAPPINGS =
            mapOf(
                CommandParamType.INT to CommandParam.INT,
                CommandParamType.FLOAT to CommandParam.FLOAT,
                CommandParamType.VALUE to CommandParam.VALUE,
                CommandParamType.WILDCARD_INT to CommandParam.WILDCARD_INT,
                CommandParamType.OPERATOR to CommandParam.OPERATOR,
                CommandParamType.COMPARE_OPERATOR to CommandParam.COMPARE_OPERATOR,
                CommandParamType.TARGET to CommandParam.TARGET,
                CommandParamType.WILDCARD_TARGET to CommandParam.WILDCARD_TARGET,
                CommandParamType.FILE_PATH to CommandParam.FILE_PATH,
                CommandParamType.INT_RANGE to CommandParam.INT_RANGE,
                CommandParamType.EQUIPMENT_SLOTS to CommandParam.EQUIPMENT_SLOTS,
                CommandParamType.STRING to CommandParam.STRING,
                CommandParamType.BLOCK_POSITION to CommandParam.BLOCK_POSITION,
                CommandParamType.POSITION to CommandParam.POSITION,
                CommandParamType.MESSAGE to CommandParam.MESSAGE,
                CommandParamType.TEXT to CommandParam.TEXT,
                CommandParamType.JSON to CommandParam.JSON,
                CommandParamType.BLOCK_STATES to CommandParam.BLOCK_STATES,
                CommandParamType.COMMAND to CommandParam.COMMAND,
            )
    }
}
