package com.focusguardpro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.focusguardpro.R
import com.focusguardpro.utils.PrefsHelper

/**
 * AppLock foreground service.
 *
 * Runs in the background on the child device to enforce app locks defined by the parent.
 *
 * CURRENT STATE: Stub implementation — starts and shows a foreground notification.
 *
 * TODO: Implement full app monitoring using UsageStatsManager:
 *   1. Obtain PACKAGE_USAGE_STATS permission via Settings.
 *   2. Poll UsageStatsManager periodically to detect foreground app.
 *   3. If foreground app is in the locked list, launch the block screen.
 *   4. Listen to Firebase for real-time lock list updates.
 *
 * TODO: Add AccessibilityService approach as an alternative/complement to UsageStats.
 */
class AppLockService : Service() {

    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            startForeground(NOTIFICATION_ID, buildNotification())
            Log.d(TAG, "AppLock service started")
            // TODO: Begin monitoring loop here
            startMonitoring()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d(TAG, "AppLock service stopped")
    }

    /**
     * Start app monitoring logic.
     * TODO: Implement with UsageStatsManager.
     */
    private fun startMonitoring() {
        // TODO: Start coroutine / handler to poll foreground app
        // val usageStats = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        // val lockedApps = PrefsHelper.getLockedApps(this)
        // while (isRunning) {
        //     val foregroundApp = getForegroundApp(usageStats)
        //     if (foregroundApp != null && lockedApps.contains(foregroundApp)) {
        //         launchBlockScreen(foregroundApp)
        //     }
        //     Thread.sleep(1000L)
        // }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Focus Guard Pro")
            .setContentText("Active — protecting your focus")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Lock Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps Focus Guard Pro active in the background"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "AppLockService"
        private const val CHANNEL_ID = "focus_guard_lock_service"
        private const val NOTIFICATION_ID = 1001

        fun start(context: Context) {
            val intent = Intent(context, AppLockService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AppLockService::class.java))
        }
    }
}
