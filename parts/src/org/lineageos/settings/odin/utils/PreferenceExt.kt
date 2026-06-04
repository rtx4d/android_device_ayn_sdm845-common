// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.utils

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

inline fun <reified T : Preference> PreferenceFragmentCompat.requirePreference(key: String): T {
    return findPreference(key) ?: error("Preference '$key' not found")
}
