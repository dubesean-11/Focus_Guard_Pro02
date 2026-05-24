package com.focusguardpro.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.focusguardpro.model.AppInfo

/**
 * Utility functions for discovering installed apps.
 * Used by the AppLocker module.
 */
object AppUtils {

    /**
     * Returns a list of user-installed apps (excludes system apps).
     * Filters out the Focus Guard Pro app itself.
     */
    fun getInstalledUserApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val lockedApps = PrefsHelper.getLockedApps(context)

        return installedApps
            .filter { appInfo ->
                // Keep only user-installed apps (not system)
                (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 &&
                        appInfo.packageName != context.packageName
            }
            .map { appInfo ->
                AppInfo(
                    packageName = appInfo.packageName,
                    appName = pm.getApplicationLabel(appInfo).toString(),
                    isLocked = lockedApps.contains(appInfo.packageName)
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    /**
     * Returns the drawable icon for an installed app, or null if unavailable.
     */
    fun getAppIcon(context: Context, packageName: String): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    /**
     * Checks if a package is currently installed.
     */
    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
