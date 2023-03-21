/*
 * Copyright 2021 WaterdogTEAM
 * Licensed under the GNU General Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jukeboxmc.scheduler

/**
 * @author WaterdogPE
 * @version 1.0
 */
abstract class Task : Runnable {
    private var handler: TaskHandler? = null
    abstract fun onRun(currentTick: Long)
    abstract fun onCancel()
    override fun run() {
        onRun(if (handler == null) -1 else handler!!.lastRunTick)
    }

    val taskId: Int
        get() = if (handler == null) -1 else handler!!.taskId

    fun cancel() {
        if (handler != null) {
            handler!!.cancel()
        }
    }

    fun getHandler(): TaskHandler? {
        return handler
    }

    fun setHandler(handler: TaskHandler?) {
        if (this.handler != null) {
            throw SecurityException("Can not change task handler!")
        }
        this.handler = handler
    }
}
