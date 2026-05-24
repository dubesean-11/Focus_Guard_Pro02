package com.focusguardpro.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.focusguardpro.service.AppLockService

/**
 * Receives BOOT_COMPLETED broadcast to restart the AppLock service after device reboot.
 *
 * TODO: Only start service on the child device (check role in prefs before starting).
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Boot completed — restarting AppLock service")
            // TODO: Check if role is CHILD before starting
            // val role = PrefsHelper.getUserRole(context)
            // if (role == UserRole.CHILD) { ... }
            AppLockService.start(context)
        }
    }

    companion object {
        private const val TAG = "BootReceiver"
    }
}
