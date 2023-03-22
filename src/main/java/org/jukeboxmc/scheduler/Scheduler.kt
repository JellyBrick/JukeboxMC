package org.jukeboxmc.scheduler

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.jukeboxmc.Server
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * @author WaterdogPE
 * @version 1.0
 */
class Scheduler(server: Server) {
    private val server: Server

    private val threadedExecutor: ExecutorService

    val chunkExecutor: ExecutorService
    private val taskHandlerMap: MutableMap<Int, TaskHandler> = ConcurrentHashMap<Int, TaskHandler>()
    private val assignedTasks: MutableMap<Long, LinkedList<TaskHandler>> =
        ConcurrentHashMap<Long, LinkedList<TaskHandler>>()
    private val pendingTasks = LinkedList<TaskHandler>()
    private val currentId = AtomicInteger()

    init {
        instance = this
        this.server = server
        val builder = ThreadFactoryBuilder()
        builder.setNameFormat("JukeboxMC Scheduler Executor")
        threadedExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), builder.build())
        chunkExecutor =
            Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("JukeboxMC Chunk Executor").build())
    }

    fun onTick(currentTick: Long) {
        var task: TaskHandler
        while (pendingTasks.poll().also { task = it } != null) {
            val tick = max(currentTick, task.nextRunTick)
            assignedTasks.computeIfAbsent(tick) { integer: Long? -> LinkedList() }
                .add(task)
        }
        val queued = assignedTasks.remove(currentTick) ?: return
        for (taskHandler in queued) {
            runTask(taskHandler, currentTick)
        }
    }

    private fun runTask(taskHandler: TaskHandler, currentTick: Long) {
        if (taskHandler.isCancelled) {
            taskHandlerMap.remove(taskHandler.taskId)
            return
        }
        if (taskHandler.isAsync) {
            threadedExecutor.execute { taskHandler.onRun(currentTick) }
        } else {
            taskHandler.onRun(currentTick)
        }
        if (taskHandler.calculateNextTick(currentTick)) {
            pendingTasks.add(taskHandler)
            return
        }
        val removed = taskHandlerMap.remove(taskHandler.taskId)
        removed?.cancel()
    }

    fun executeAsync(task: Runnable): TaskHandler {
        return this.execute(task, true)
    }

    fun execute(task: Runnable): TaskHandler {
        return addTask(task, 0, 0, false)
    }

    fun execute(task: Runnable, async: Boolean): TaskHandler {
        return addTask(task, 0, 0, async)
    }

    fun scheduleDelayed(task: Runnable, delay: Int): TaskHandler {
        return this.scheduleDelayed(task, delay, false)
    }

    fun scheduleDelayed(task: Runnable, delay: Int, async: Boolean): TaskHandler {
        return addTask(task, delay, 0, async)
    }

    fun scheduleRepeating(task: Runnable, period: Int): TaskHandler {
        return this.scheduleRepeating(task, period, false)
    }

    fun scheduleRepeating(task: Runnable, period: Int, async: Boolean): TaskHandler {
        return addTask(task, 0, period, async)
    }

    fun scheduleDelayedRepeating(task: Runnable, delay: Int, period: Int): TaskHandler {
        return this.scheduleDelayedRepeating(task, delay, period, false)
    }

    fun scheduleDelayedRepeating(task: Runnable, delay: Int, period: Int, async: Boolean): TaskHandler {
        return addTask(task, delay, period, async)
    }

    private fun addTask(runnable: Runnable, delay: Int, period: Int, async: Boolean): TaskHandler {
        if (delay < 0 || period < 0) {
            throw RuntimeException("Attempted to register a task with negative delay or period!")
        }
        val currentTick = currentTick
        val taskId = currentId.getAndIncrement()
        val handler = TaskHandler(runnable, taskId, async)
        handler.delay = delay
        handler.period = period
        handler.nextRunTick = if (handler.isDelayed) currentTick + delay else currentTick
        (runnable as? Task)?.setHandler(handler)
        pendingTasks.add(handler)
        taskHandlerMap[taskId] = handler
        return handler
    }

    fun shutdown() {
        server.logger.debug("Scheduler shutdown initialized!")
        threadedExecutor.shutdown()
        var count = 25
        while (!threadedExecutor.isTerminated && count-- > 0) {
            try {
                threadedExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)
            } catch (ignore: InterruptedException) {
            }
        }
    }

    val currentTick: Long
        get() = server.currentTick

    companion object {
        lateinit var instance: Scheduler
            internal set
    }
}
