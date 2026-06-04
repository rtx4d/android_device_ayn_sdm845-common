// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.performance

import android.os.Handler
import android.os.Looper
import org.lineageos.settings.odin.utils.FileUtils
import kotlin.math.floor

object SmartFan {

    private const val CHECK_PERIOD_MS = 5000L
    private const val TEMP_THRESHOLD = 45.0
    private const val SMART_SPEED_CAP = 30000
    private const val PATH_CPU_TEMP = "/sys/class/thermal/thermal_zone0/temp"

    private val handler = Handler(Looper.getMainLooper())
    private val action = object : Runnable {
        override fun run() {
            val temp = readCpuTemp()
            if (temp != null) {
                val speed = if (temp >= TEMP_THRESHOLD) {
                    getSmartSpeed(temp).coerceAtMost(SMART_SPEED_CAP)
                } else {
                    0
                }
                FileUtils.write(PerformanceUtils.PATH_FAN_SPEED, speed.toString())
            }
            handler.postDelayed(this, CHECK_PERIOD_MS)
        }
    }

    fun setEnabled(enabled: Boolean) {
        handler.removeCallbacks(action)
        if (enabled) handler.post(action)
    }

    private fun readCpuTemp(): Double? {
        return FileUtils.read(PATH_CPU_TEMP)?.toIntOrNull()?.let { it / 1000.0 }
    }

    private fun getSmartSpeed(temp: Double): Int {
        return floor((0.5 * temp - 12.5) * 1000.0).toInt()
    }
}
