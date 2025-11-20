/*
 * Copyright (C) 2026 euclidOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.deviceinfo.firmwareversion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemProperties
import androidx.preference.Preference
import com.android.settings.R
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.preference.PreferenceBinding

class EuclidVersionPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    companion object {
        private const val VERSION_PROPERTY = "ro.euclid.version"
        private const val BUILD_TYPE_PROPERTY = "ro.euclid.buildtype"
        private const val CODENAME_PROPERTY = "ro.euclid.codename"
        private const val DEVICE_PROPERTY = "ro.product.device"
    }

    override val key: String
        get() = "euclid_version"

    override val title: Int
        get() = R.string.euclid_firmware_version

    override fun intent(context: Context): Intent? =
        Intent(Intent.ACTION_VIEW).setData(
            Uri.parse(context.getString(R.string.euclid_website_uri))
        )

    override fun isAvailable(context: Context): Boolean = context.hasRequiredProps()

    override fun getSummary(context: Context): String = context.buildSummary()

    private fun Context.hasRequiredProps(): Boolean {
        val version = SystemProperties.get(VERSION_PROPERTY, "")
        val buildType = SystemProperties.get(BUILD_TYPE_PROPERTY, "")
        val codename = SystemProperties.get(CODENAME_PROPERTY, "")
        return version.isNotEmpty() && buildType.isNotEmpty() && codename.isNotEmpty()
    }

    private fun Context.buildSummary(): String {
        val version = SystemProperties.get(VERSION_PROPERTY, "")
        val buildType = SystemProperties.get(BUILD_TYPE_PROPERTY, "")
        val codename = SystemProperties.get(CODENAME_PROPERTY, "Unknown")
        val device = SystemProperties.get(DEVICE_PROPERTY, "Unknown")

        return if (version.isNotEmpty() && buildType.isNotEmpty()) {
            "$version | $codename | $device | $buildType"
        } else {
            getString(R.string.device_info_default)
        }
    }

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        preference.isCopyingEnabled = true
    }
}
