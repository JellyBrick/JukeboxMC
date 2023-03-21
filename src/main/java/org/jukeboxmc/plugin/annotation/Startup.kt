package org.jukeboxmc.plugin.annotation

import org.jukeboxmc.plugin.PluginLoadOrder

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Startup(val value: PluginLoadOrder)
