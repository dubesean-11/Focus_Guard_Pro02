package com.focusguardpro.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Helper for secure PIN management.
 * Uses EncryptedSharedPreferences to store the PIN hash securely.
 *
 * TODO: For production, replace PIN hash with a proper key derivation function (e.g., PBKDF2).
 */
object PinHelper {

    private const val SECURE_PREFS_NAME = "focus_guard_secure_prefs"
    private const val KEY_PIN_HASH = "pin_hash"
    private const val KEY_PIN_SET = "pin_set"

    private fun securePrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            SECURE_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Returns true if a PIN has been set.
     */
    fun isPinSet(context: Context): Boolean =
        securePrefs(context).getBoolean(KEY_PIN_SET, false)

    /**
     * Sets a new PIN (stores a simple hash).
     * TODO: Replace with PBKDF2 or bcrypt for production.
     */
    fun setPin(context: Context, pin: String) {
        val hash = hashPin(pin)
        securePrefs(context).edit()
            .putString(KEY_PIN_HASH, hash)
            .putBoolean(KEY_PIN_SET, true)
            .apply()
    }

    /**
     * Verifies the entered PIN against the stored hash.
     */
    fun verifyPin(context: Context, enteredPin: String): Boolean {
        if (!isPinSet(context)) return false
        val storedHash = securePrefs(context).getString(KEY_PIN_HASH, null) ?: return false
        return storedHash == hashPin(enteredPin)
    }

    /**
     * Clears the stored PIN.
     */
    fun clearPin(context: Context) {
        securePrefs(context).edit()
            .remove(KEY_PIN_HASH)
            .putBoolean(KEY_PIN_SET, false)
            .apply()
    }

    /**
     * Simple hash function for PIN.
     * TODO: Replace with PBKDF2 + salt for production security.
     */
    private fun hashPin(pin: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(pin.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
