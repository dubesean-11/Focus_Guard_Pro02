package com.focusguardpro.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Device Admin Receiver for Focus Guard Pro.
 *
 * Handles Device Admin lifecycle events (enabled, disabled).
 * When enabled, the app cannot be uninstalled without first disabling device admin.
 *
 * TODO: Extend with additional policies (e.g., prevent factory reset) as needed.
 *
 * Setup:
 *   1. User must manually enable via Settings > Security > Device Admins, or
 *   2. Prompt from the app: startActivityForResult(Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN))
 */
class FocusDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d(TAG, "Device Admin ENABLED — uninstall protection active")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.d(TAG, "Device Admin DISABLED — uninstall protection removed")
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return "Disabling protection will allow Focus Guard Pro to be uninstalled. Are you sure?"
    }

    companion object {
        private const val TAG = "FocusDeviceAdmin"
    }
}
