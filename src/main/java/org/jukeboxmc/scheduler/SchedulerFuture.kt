package org.jukeboxmc.scheduler

import java.util.Objects
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.function.BiConsumer

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SchedulerFuture<V> : Runnable, Future<V> {
    private val scheduler: Scheduler
    private val callable: Callable<V>?
    private val consumers: MutableSet<BiConsumer<V, Throwable?>>?
    private val waiting: Object?

    @Volatile
    private var runner: Thread? = null

    var value: V? = null

    @Volatile
    private var throwable: Throwable? = null

    @Volatile
    private var finished: Boolean

    @Volatile
    private var cancelled: Boolean

    constructor(scheduler: Scheduler, value: V) {
        this.scheduler = scheduler
        callable = null
        consumers = null
        runner = Thread.currentThread()
        this.value = value
        waiting = null
        finished = true
        cancelled = false
    }

    constructor(scheduler: Scheduler, callable: Callable<V>?) {
        this.scheduler = scheduler
        this.callable = callable
        consumers = LinkedHashSet()
        finished = false
        waiting = Object()
        cancelled = false
    }

    override fun run() {
        if (runner != null) return
        runner = Thread.currentThread()
        try {
            value = Objects.requireNonNull(callable)?.call()!!
        } catch (throwable: Throwable) {
            this.throwable = throwable
        } finally {
            finished = true
            synchronized(waiting!!) { waiting.notifyAll() }

            scheduler.execute {
                for (consumer in consumers!!) value?.let { consumer.accept(it, throwable) }
                consumers.clear()
            }
        }
    }

    fun forceRun(): SchedulerFuture<V> {
        check(!(callable == null || finished)) { "Cannot force run a finished future" }
        this.run()
        return this
    }

    @Synchronized
    fun onFinish(consumer: BiConsumer<V, Throwable?>): SchedulerFuture<V> {
        scheduler.execute {
            if (finished) {
                value?.let { consumer.accept(it, throwable) }
            } else {
                consumers!!.add(consumer)
            }
        }
        return this
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        if (finished) return false
        return if (runner == null) {
            false
        } else {
            try {
                if (mayInterruptIfRunning) {
                    runner!!.interrupt()
                    return true
                }
                false
            } finally {
                cancelled = true
            }
        }
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun isDone(): Boolean {
        return finished
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    override fun get(): V? {
        if (finished) {
            if (throwable != null) throw ExecutionException(throwable)
            return value
        }
        synchronized(waiting!!) {
            waiting.wait()
            if (throwable != null) throw ExecutionException(throwable)
            return value
        }
    }

    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    override fun get(timeout: Long, timeUnit: TimeUnit): V? {
        if (finished) {
            if (throwable != null) throw ExecutionException(throwable)
            return value
        }
        synchronized(waiting!!) {
            waiting.wait(timeUnit.toMillis(timeout))
            if (!finished) throw TimeoutException()
            if (throwable != null) throw ExecutionException(throwable)
            return value
        }
    }

    companion object {
        fun <V> completed(scheduler: Scheduler, value: V): SchedulerFuture<V> {
            return SchedulerFuture(scheduler, value)
        }

        fun <V> supplyAsync(scheduler: Scheduler, callable: Callable<V>?): SchedulerFuture<V> {
            val future = SchedulerFuture(scheduler, callable)
            scheduler.executeAsync(future)
            return future
        }
    }
}
