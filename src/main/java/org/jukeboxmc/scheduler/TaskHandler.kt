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
class TaskHandler(val task: Runnable, val taskId: Int, val isAsync: Boolean) {
    var delay = 0
    var period = 0
    var lastRunTick: Long = 0
        private set
    var nextRunTick: Long = 0
    var isCancelled = false
        private set
    private val runOnce = false
    fun onRun(currentTick: Long) {
        lastRunTick = currentTick
        task.run()
    }

    fun cancel() {
        if (isCancelled) {
            return
        }
        if (task is Task) {
            task.onCancel()
        }
        isCancelled = true
    }

    fun calculateNextTick(currentTick: Long): Boolean {
        if (isCancelled || !isRepeating) {
            return false
        }
        nextRunTick = currentTick + period
        return true
    }

    val isDelayed: Boolean
        get() = delay > 0
    val isRepeating: Boolean
        get() = period > 0
}
