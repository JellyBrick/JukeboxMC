package org.jukeboxmc.plugin.annotation

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Startup(val value: PluginLoadOrder)