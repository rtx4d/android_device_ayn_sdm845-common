/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.ayn

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.android.settingslib.widget.SettingsBasePreferenceFragment

class AynSettingsFragment :
    SettingsBasePreferenceFragment(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.ayn_panel, rootKey)
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        return true
    }

    /*
     * The activity uses Theme.SubSettingsBase.Expressive which doesn't
     * supply the attributes that androidx.preference (and even
     * androidx.appcompat.AlertDialog) need to resolve their dialog
     * layouts, so the default ListPreference click path crashes inside
     * AlertDialog.Builder.create() with Resources$NotFoundException 0x0.
     *
     * Build the dialog ourselves using the platform AlertDialog with an
     * explicit DeviceDefault theme — that resource is always present in
     * the framework and doesn't depend on the activity theme having any
     * androidx attributes set. Pick the light or dark variant from the
     * current night-mode configuration so the dialog follows the system
     * theme instead of always rendering dark.
     */
    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is ListPreference) {
            showListPreferenceDialog(preference)
            return
        }
        super.onDisplayPreferenceDialog(preference)
    }

    private fun showListPreferenceDialog(preference: ListPreference) {
        val entries = preference.entries
        val values = preference.entryValues
        if (entries == null || values == null) {
            super.onDisplayPreferenceDialog(preference)
            return
        }

        val initialIndex = preference.findIndexOfValue(preference.value)
            .takeIf { it >= 0 } ?: 0
        var picked = initialIndex

        AlertDialog.Builder(requireContext(), resolveDialogTheme())
            .setTitle(preference.dialogTitle ?: preference.title)
            .setSingleChoiceItems(entries, initialIndex) { _, which ->
                picked = which
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (picked < 0 || picked >= values.size) return@setPositiveButton
                val newValue = values[picked].toString()
                if (preference.callChangeListener(newValue)) {
                    preference.value = newValue
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun resolveDialogTheme(): Int {
        val isNight = (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        return if (isNight) {
            android.R.style.Theme_DeviceDefault_Dialog_Alert
        } else {
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        }
    }
}
