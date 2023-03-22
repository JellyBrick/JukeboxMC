package org.jukeboxmc.block

import org.jukeboxmc.math.Vector
import java.util.LinkedList
import java.util.Queue

/**
 * @author geNAZt
 * @author LucGamesYT
 * @version 1.0
 */
class BlockUpdateList {
    private var element: Element? = null

    @Synchronized
    fun addElement(timeValue: Long, blockPosition: Vector) {
        if (element == null) {
            element = Element(
                timeValue,
                null,
                object : LinkedList<Vector>() {
                    init {
                        add(blockPosition)
                    }
                },
            )
        } else {
            var element = element
            var previousElement: Element? = null
            while (element != null && element.timeValue < timeValue) {
                previousElement = element
                element = element.nextElement
            }
            if (element == null) {
                previousElement!!.nextElement = Element(
                    timeValue,
                    null,
                    object : LinkedList<Vector>() {
                        init {
                            add(blockPosition)
                        }
                    },
                )
            } else {
                if (element.timeValue != timeValue) {
                    val nextElement = Element(
                        timeValue,
                        element,
                        object : LinkedList<Vector>() {
                            init {
                                add(blockPosition)
                            }
                        },
                    )
                    if (previousElement != null) {
                        previousElement.nextElement = nextElement
                    } else {
                        this.element = nextElement
                    }
                } else {
                    element.positionQueue.add(blockPosition)
                }
            }
        }
    }

    @get:Synchronized
    val nextTaskTime: Long
        get() = if (element != null) {
            element!!.timeValue
        } else {
            Long.MAX_VALUE
        }

    @get:Synchronized
    val nextElement: Vector?
        get() {
            if (element == null) {
                return null
            }
            while (element != null && element!!.positionQueue.size == 0) {
                element = element!!.nextElement
            }
            if (element == null) {
                return null
            }
            val blockPosition = element!!.positionQueue.poll()
            while (element!!.positionQueue.size == 0) {
                element = element!!.nextElement
                if (element == null) {
                    break
                }
            }
            return blockPosition
        }

    @Synchronized
    fun size(timeValue: Long): Int {
        var element: Element? = element ?: return 0
        do {
            if (element!!.timeValue == timeValue) {
                return element.positionQueue.size
            }
        } while (element!!.nextElement.also { element = it } != null)
        return 0
    }

    @Synchronized
    operator fun contains(blockPosition: Vector): Boolean {
        var element: Element? = element ?: return false
        do {
            if (element!!.positionQueue.contains(blockPosition)) {
                return true
            }
        } while (element!!.nextElement.also { element = it } != null)
        return false
    }

    class Element(var timeValue: Long, var nextElement: Element?, var positionQueue: Queue<Vector>)
}
