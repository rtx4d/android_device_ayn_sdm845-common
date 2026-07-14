/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.ayn.led

import org.lineageos.settings.ayn.utils.NodeUtils

/*
 * Describes what the LED hardware on this device can actually do.
 *
 * Newer devices expose per-LED RGB nodes (multi_index / multi_intensity)
 * alongside brightness, so the full color UI applies. Older devices (such
 * as the side strips and sticks here) only expose a brightness node that
 * behaves as a plain on/off switch (0 = off, any 1-255 = on) with no color
 * or real dimming. In that case we present a simple per-LED toggle layout.
 *
 * Detection is done once by probing the sysfs nodes and then cached, since
 * the hardware doesn't change while the system is running.
 */
object LedCapabilities {
    private const val LED_BASE_PATH = "/sys/class/leds"

    /** All LEDs this module knows about, in display order. */
    val ALL_LED_NAMES = listOf("left:strip", "left:stick", "right:strip", "right:stick")

    /** The LED names whose brightness node is actually present on this device. */
    val presentLeds: List<String> by lazy {
        ALL_LED_NAMES.filter { NodeUtils.exists("$LED_BASE_PATH/$it/brightness") }
    }

    /**
     * True when at least one present LED exposes an RGB color node, meaning
     * the full color UI is meaningful. False when the hardware is on/off only.
     */
    val isRgb: Boolean by lazy {
        presentLeds.any { NodeUtils.exists("$LED_BASE_PATH/$it/multi_intensity") }
    }
}
