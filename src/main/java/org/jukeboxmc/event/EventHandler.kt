package org.jukeboxmc.event

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class EventHandler(val priority: EventPriority = EventPriority.NORMAL)