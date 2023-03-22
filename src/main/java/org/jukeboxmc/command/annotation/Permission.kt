package org.jukeboxmc.command.annotation

/**
 * @author Kaooot
 * @version 1.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(
    /**
     * Represents the permission of a [org.jukeboxmc.command.Command]
     *
     * @return a fresh [String]
     */
    val value: String = "",
    val showNoPermissionMessage: Boolean = true,
)
