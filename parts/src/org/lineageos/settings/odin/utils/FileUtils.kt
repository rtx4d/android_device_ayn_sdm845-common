//
// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0
//

package org.lineageos.settings.odin.utils

import android.util.Log
import java.io.File
import java.io.IOException

object FileUtils {
    private const val TAG = "FileUtils"

    fun read(fileName: String): String? {
        return try {
            File(fileName).readText().trim()
        } catch (e: IOException) {
            Log.e(TAG, "Could not read from file $fileName", e)
            null
        } catch (e: SecurityException) {
            Log.w(TAG, "No permission to read file $fileName", e)
            null
        }
    }

    fun write(fileName: String, value: String): Boolean {
        return try {
            File(fileName).writeText(value)
            true
        } catch (e: IOException) {
            Log.e(TAG, "Could not write to file $fileName", e)
            false
        } catch (e: SecurityException) {
            Log.w(TAG, "No permission to write file $fileName", e)
            false
        }
    }
}
