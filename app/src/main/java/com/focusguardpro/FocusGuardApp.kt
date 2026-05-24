package com.focusguardpro

import android.app.Application
import android.util.Log

/**
 * Custom Application class for Focus Guard Pro.
 *
 * TODO: Initialize Firebase here when google-services.json is added:
 *   FirebaseApp.initializeApp(this)
 */
class FocusGuardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Focus Guard Pro starting…")

        // TODO: Uncomment when Firebase is configured
        // FirebaseApp.initializeApp(this)
    }

    companion object {
        private const val TAG = "FocusGuardApp"
    }
}
