// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.lineageos.settings.odin.light.LightUtils
import org.lineageos.settings.odin.performance.PerformanceUtils

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        LightUtils.applyAll(context)
        PerformanceUtils.applyAll(context)
    }
}
