// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.performance

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.android.settingslib.widget.SettingsBasePreferenceFragment
import org.lineageos.settings.odin.R
import org.lineageos.settings.odin.utils.requirePreference

class PerformanceFragment : SettingsBasePreferenceFragment(),
    Preference.OnPreferenceChangeListener {

    private lateinit var performancePref: ListPreference
    private lateinit var fanPref: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.performance, rootKey)

        performancePref = requirePreference(PerformanceUtils.KEY_PERFORMANCE_MODE)
        fanPref = requirePreference(PerformanceUtils.KEY_FAN_MODE)

        performancePref.value = PerformanceUtils.getMode(requireContext()).toString()
        refreshFanEntries(PerformanceUtils.getMode(requireContext()))

        performancePref.onPreferenceChangeListener = this
        fanPref.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val value = (newValue as String).toInt()
        when (preference.key) {
            PerformanceUtils.KEY_PERFORMANCE_MODE -> {
                PerformanceUtils.setMode(requireContext(), value)
                refreshFanEntries(value)
            }
            PerformanceUtils.KEY_FAN_MODE -> {
                PerformanceUtils.saveFanMode(requireContext(), value)
            }
        }
        return true
    }

    private fun refreshFanEntries(performanceMode: Int) {
        val allEntries = resources.getStringArray(R.array.fan_mode_entries)
        val allValues = resources.getStringArray(R.array.fan_mode_values)
        val minFan = PerformanceUtils.minFanMode(performanceMode)

        val (entries, values) = allValues
            .zip(allEntries)
            .filter { (value, _) ->
                val intValue = value.toInt()
                intValue == PerformanceUtils.FAN_SMART || intValue >= minFan
            }
            .map { (value, entry) -> entry to value }
            .unzip()

        fanPref.entries = entries.toTypedArray()
        fanPref.entryValues = values.toTypedArray()
        fanPref.value = PerformanceUtils.getFanMode(requireContext()).toString()
    }
}
