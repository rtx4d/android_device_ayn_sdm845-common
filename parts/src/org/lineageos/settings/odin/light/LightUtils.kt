// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.light

import android.content.Context
import androidx.preference.PreferenceManager
import org.lineageos.settings.odin.utils.FileUtils

object LightUtils {

    const val KEY_LIGHT_MASTER = "light_master"
    const val KEY_LIGHT_JOYSTICK = "light_joystick"
    const val KEY_LIGHT_HANDLE = "light_handle"

    private const val PATH_LEFT_HANDLE = "/sys/class/gpio/gpio129/value"
    private const val PATH_LEFT_JOYSTICK = "/sys/class/gpio/gpio25/value"
    private const val PATH_RIGHT_HANDLE = "/sys/class/gpio/gpio87/value"
    private const val PATH_RIGHT_JOYSTICK = "/sys/class/gpio/gpio7/value"

    private fun writeNode(path: String, enabled: Boolean) {
        FileUtils.write(path, if (enabled) "1" else "0")
    }

    fun setHandleLight(enabled: Boolean) {
        writeNode(PATH_LEFT_HANDLE, enabled)
        writeNode(PATH_RIGHT_HANDLE, enabled)
    }

    fun setJoystickLight(enabled: Boolean) {
        writeNode(PATH_LEFT_JOYSTICK, enabled)
        writeNode(PATH_RIGHT_JOYSTICK, enabled)
    }

    fun applyAll(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val master = prefs.getBoolean(KEY_LIGHT_MASTER, false)
        val joystick = master && prefs.getBoolean(KEY_LIGHT_JOYSTICK, false)
        val handle = master && prefs.getBoolean(KEY_LIGHT_HANDLE, false)
        setJoystickLight(joystick)
        setHandleLight(handle)
    }

    fun isMasterEnabled(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_LIGHT_MASTER, false)
    }

    fun setMasterEnabled(context: Context, enabled: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(KEY_LIGHT_MASTER, enabled)
            .apply()
        applyAll(context)
    }
}
