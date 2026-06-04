// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.performance

import android.content.Context
import android.os.SystemProperties
import androidx.preference.PreferenceManager
import org.lineageos.settings.odin.utils.FileUtils

object PerformanceUtils {

    const val MODE_STANDARD = 0
    const val MODE_PERFORMANCE = 1
    const val MODE_HIGH_PERFORMANCE = 2

    const val KEY_PERFORMANCE_MODE = "performance_mode"
    const val KEY_FAN_MODE = "fan_mode"

    const val FAN_OFF = 0
    const val FAN_QUIET = 1
    const val FAN_MEDIUM = 2
    const val FAN_SPORT = 3
    const val FAN_SMART = 4

    private const val PATH_GOVERNOR = "/sys/class/kgsl/kgsl-3d0/devfreq/governor"
    private const val PATH_THERMAL_MODE = "/sys/class/gpio5_pwm2/thermal_mode"
    private const val PATH_FAN = "/sys/class/gpio5_pwm2/state"
    const val PATH_FAN_SPEED = "/sys/class/gpio5_pwm2/duty"

    private const val GOVERNOR_STANDARD = "msm-adreno-tz"
    private const val GOVERNOR_HIGH = "simple_ondemand"

    private const val FAN_SPEED_QUIET = 8000
    private const val FAN_SPEED_MEDIUM = 25000
    private const val FAN_SPEED_SPORT = 50000

    private const val PROP_THERMAL_RESTART = "sys.odinparts.thermal_restart"

    fun setMode(context: Context, mode: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putString(KEY_PERFORMANCE_MODE, mode.toString()).apply()
        applyMode(mode)
        val fanMode = defaultFanForMode(mode, getFanMode(context))
        saveFanMode(context, fanMode)
    }

    fun getMode(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_PERFORMANCE_MODE, MODE_STANDARD.toString())
            ?.toIntOrNull() ?: MODE_STANDARD
    }

    fun minFanMode(performanceMode: Int): Int = when (performanceMode) {
        MODE_PERFORMANCE -> FAN_QUIET
        MODE_HIGH_PERFORMANCE -> FAN_MEDIUM
        else -> FAN_OFF
    }

    fun defaultFanForMode(performanceMode: Int, fanMode: Int): Int {
        if (fanMode == FAN_SMART) return fanMode
        return minFanMode(performanceMode)
    }

    fun applyMode(mode: Int) {
        FileUtils.write(PATH_GOVERNOR, if (mode == MODE_HIGH_PERFORMANCE) GOVERNOR_HIGH else GOVERNOR_STANDARD)
        FileUtils.write(PATH_THERMAL_MODE, mode.toString())
        SystemProperties.set(PROP_THERMAL_RESTART, mode.toString())
    }

    fun setFan(mode: Int) {
        SmartFan.setEnabled(false)

        if (mode == FAN_OFF) {
            FileUtils.write(PATH_FAN, "0")
            return
        }

        if (mode == FAN_SMART) {
            FileUtils.write(PATH_FAN, "1")
            SmartFan.setEnabled(true)
            return
        }

        val speed = when (mode) {
            FAN_QUIET -> FAN_SPEED_QUIET
            FAN_MEDIUM -> FAN_SPEED_MEDIUM
            FAN_SPORT -> FAN_SPEED_SPORT
            else -> return
        }

        FileUtils.write(PATH_FAN, "1")
        FileUtils.write(PATH_FAN_SPEED, speed.toString())
    }

    fun saveFanMode(context: Context, mode: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_FAN_MODE, mode.toString())
            .apply()
        setFan(mode)
    }

    fun getFanMode(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_FAN_MODE, FAN_OFF.toString())
            ?.toIntOrNull() ?: FAN_OFF
    }

    fun applyAll(context: Context) {
        applyMode(getMode(context))
        setFan(getFanMode(context))
    }

    fun nextMode(context: Context): Int {
        val next = (getMode(context) + 1) % 3
        setMode(context, next)
        return next
    }
}
