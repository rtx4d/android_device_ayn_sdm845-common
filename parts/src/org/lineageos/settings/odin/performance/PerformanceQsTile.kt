// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.performance

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import org.lineageos.settings.odin.R

class PerformanceQsTile : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile(PerformanceUtils.getMode(this))
    }

    override fun onClick() {
        super.onClick()
        updateTile(PerformanceUtils.nextMode(this))
    }

    private fun updateTile(mode: Int) {
        val tile = qsTile ?: return
        tile.state = if (mode == PerformanceUtils.MODE_STANDARD) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE
        tile.subtitle = when (mode) {
            PerformanceUtils.MODE_PERFORMANCE -> getString(R.string.performance_mode_performance)
            PerformanceUtils.MODE_HIGH_PERFORMANCE -> getString(R.string.performance_mode_high)
            else -> getString(R.string.performance_mode_standard)
        }
        tile.updateTile()
    }
}
