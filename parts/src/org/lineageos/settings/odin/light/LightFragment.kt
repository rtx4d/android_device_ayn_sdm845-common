// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.settings.odin.light

import android.os.Bundle
import android.widget.CompoundButton
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.android.settingslib.widget.MainSwitchPreference
import com.android.settingslib.widget.SettingsBasePreferenceFragment
import org.lineageos.settings.odin.R
import org.lineageos.settings.odin.utils.requirePreference

class LightFragment : SettingsBasePreferenceFragment(),
    Preference.OnPreferenceChangeListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var masterSwitch: MainSwitchPreference
    private lateinit var joystickSwitch: SwitchPreferenceCompat
    private lateinit var handleSwitch: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.light, rootKey)

        masterSwitch = requirePreference(LightUtils.KEY_LIGHT_MASTER)
        joystickSwitch = requirePreference(LightUtils.KEY_LIGHT_JOYSTICK)
        handleSwitch = requirePreference(LightUtils.KEY_LIGHT_HANDLE)

        masterSwitch.addOnSwitchChangeListener(this)
        joystickSwitch.onPreferenceChangeListener = this
        handleSwitch.onPreferenceChangeListener = this

        updateDependents(masterSwitch.isChecked)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        LightUtils.setMasterEnabled(requireContext(), isChecked)
        updateDependents(isChecked)
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val enabled = newValue as Boolean
        when (preference.key) {
            LightUtils.KEY_LIGHT_JOYSTICK -> LightUtils.setJoystickLight(
                masterSwitch.isChecked && enabled
            )
            LightUtils.KEY_LIGHT_HANDLE -> LightUtils.setHandleLight(
                masterSwitch.isChecked && enabled
            )
        }
        return true
    }

    private fun updateDependents(masterEnabled: Boolean) {
        joystickSwitch.isEnabled = masterEnabled
        handleSwitch.isEnabled = masterEnabled
        if (!masterEnabled) {
            LightUtils.setJoystickLight(false)
            LightUtils.setHandleLight(false)
        } else {
            LightUtils.setJoystickLight(joystickSwitch.isChecked)
            LightUtils.setHandleLight(handleSwitch.isChecked)
        }
    }
}
