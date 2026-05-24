package com.focusguardpro.model

/**
 * Represents an installed app on the device.
 * Used by the AppLocker module to display and manage app locking.
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    var isLocked: Boolean = false
)
