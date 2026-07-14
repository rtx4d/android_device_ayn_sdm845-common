/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.ayn.led

data class RgbColor(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    fun clamped() = RgbColor(
        red = red.coerceIn(0, 255),
        green = green.coerceIn(0, 255),
        blue = blue.coerceIn(0, 255),
    )
}

data class LedState(
    val target: LedTarget = LedTarget.ALL,
    val color: RgbColor = RgbColor(0, 255, 255),
    val brightness: Int = 255,
    val lastNonZeroBrightness: Int = 255,
    val enabled: Boolean = true,
    val stripLightsEnabled: Boolean = true,
    // Per-LED on/off switches used by the simple (non-RGB) hardware layout.
    val leftStrip: Boolean = true,
    val leftStick: Boolean = true,
    val rightStrip: Boolean = true,
    val rightStick: Boolean = true,
) {
    fun clamped() = copy(
        color = color.clamped(),
        brightness = brightness.coerceIn(0, 255),
        lastNonZeroBrightness = lastNonZeroBrightness.coerceIn(1, 255),
    )

    /** Whether a given LED name should be on under the simple on/off layout. */
    fun isLedOn(ledName: String) = when (ledName) {
        "left:strip" -> leftStrip
        "left:stick" -> leftStick
        "right:strip" -> rightStrip
        "right:stick" -> rightStick
        else -> false
    }
}
