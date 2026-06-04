// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.light

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class LightQsTile : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        val enabled = !LightUtils.isMasterEnabled(this)
        LightUtils.setMasterEnabled(this, enabled)
        updateTile()
    }

    private fun updateTile() {
        val tile = qsTile ?: return
        val enabled = LightUtils.isMasterEnabled(this)
        tile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }
}
