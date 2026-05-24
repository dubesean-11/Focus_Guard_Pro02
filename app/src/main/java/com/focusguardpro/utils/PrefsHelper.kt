package com.focusguardpro.utils

import android.content.Context
import android.content.SharedPreferences
import com.focusguardpro.model.UserRole

/**
 * Centralized helper for SharedPreferences.
 * Uses standard SharedPreferences for non-sensitive data.
 *
 * TODO: For PIN storage, use EncryptedSharedPreferences (see PinHelper).
 */
object PrefsHelper {

    private const val PREFS_NAME = "focus_guard_prefs"

    // Keys
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    private const val KEY_FAMILY_ID = "family_id"
    private const val KEY_DEVICE_ID = "device_id"
    private const val KEY_LOCKED_APPS = "locked_apps"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // --- User Role ---

    fun setUserRole(context: Context, role: UserRole) {
        prefs(context).edit().putString(KEY_USER_ROLE, role.name).apply()
    }

    fun getUserRole(context: Context): UserRole {
        val roleName = prefs(context).getString(KEY_USER_ROLE, UserRole.NONE.name)
        return try {
            UserRole.valueOf(roleName ?: UserRole.NONE.name)
        } catch (e: IllegalArgumentException) {
            UserRole.NONE
        }
    }

    fun isRoleSelected(context: Context): Boolean =
        getUserRole(context) != UserRole.NONE

    // --- Onboarding ---

    fun setOnboardingComplete(context: Context, complete: Boolean) {
        prefs(context).edit().putBoolean(KEY_ONBOARDING_COMPLETE, complete).apply()
    }

    fun isOnboardingComplete(context: Context): Boolean =
        prefs(context).getBoolean(KEY_ONBOARDING_COMPLETE, false)

    // --- Device / Family pairing ---

    fun setFamilyId(context: Context, familyId: String) {
        prefs(context).edit().putString(KEY_FAMILY_ID, familyId).apply()
    }

    fun getFamilyId(context: Context): String? =
        prefs(context).getString(KEY_FAMILY_ID, null)

    fun setDeviceId(context: Context, deviceId: String) {
        prefs(context).edit().putString(KEY_DEVICE_ID, deviceId).apply()
    }

    fun getDeviceId(context: Context): String {
        val existing = prefs(context).getString(KEY_DEVICE_ID, null)
        if (existing != null) return existing
        val newId = java.util.UUID.randomUUID().toString()
        setDeviceId(context, newId)
        return newId
    }

    // --- Locked apps ---

    fun setLockedApps(context: Context, packageNames: Set<String>) {
        prefs(context).edit().putStringSet(KEY_LOCKED_APPS, packageNames).apply()
    }

    fun getLockedApps(context: Context): Set<String> =
        prefs(context).getStringSet(KEY_LOCKED_APPS, emptySet()) ?: emptySet()

    fun addLockedApp(context: Context, packageName: String) {
        val current = getLockedApps(context).toMutableSet()
        current.add(packageName)
        setLockedApps(context, current)
    }

    fun removeLockedApp(context: Context, packageName: String) {
        val current = getLockedApps(context).toMutableSet()
        current.remove(packageName)
        setLockedApps(context, current)
    }

    // --- Clear all (for role switch / reset) ---

    fun clearAll(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
