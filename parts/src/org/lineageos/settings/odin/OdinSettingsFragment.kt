/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.odin

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import com.android.settingslib.widget.SettingsBasePreferenceFragment
import org.lineageos.settings.odin.light.LightActivity
import org.lineageos.settings.odin.performance.PerformanceActivity

class OdinSettingsFragment : SettingsBasePreferenceFragment(),
    Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.odin_settings, rootKey)

        findPreference<Preference>("performance")?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), PerformanceActivity::class.java)
            startActivity(intent)
            true
        }

        findPreference<Preference>("light")?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), LightActivity::class.java)
            startActivity(intent)
            true
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        return true
    }
}