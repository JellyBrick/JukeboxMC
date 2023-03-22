package org.jukeboxmc.command

import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission

/**
 * @author Kaooot, LucGamesYT
 * @version 1.0
 */
abstract class Command(var commandData: CommandData) {
    init {
        val clazz: Class<*> = this.javaClass
        if (clazz.isAnnotationPresent(Name::class.java)) {
            commandData.name =
                clazz.getAnnotation(Name::class.java).value
        }
        if (clazz.isAnnotationPresent(Description::class.java)) {
            commandData.description =
                clazz.getAnnotation(Description::class.java).value
        }
        if (clazz.isAnnotationPresent(Permission::class.java)) {
            commandData.setPermission(clazz.getAnnotation(Permission::class.java).value)
        }
        commandData = commandData.rebuild()
    }

    /**
     * This method is called when the command is executed.
     *
     * @param commandSender The sender of the command.
     * @param command       The command that was executed.
     * @param args          The arguments of the command.
     */
    abstract fun execute(commandSender: CommandSender, command: String, args: Array<String>)
}
