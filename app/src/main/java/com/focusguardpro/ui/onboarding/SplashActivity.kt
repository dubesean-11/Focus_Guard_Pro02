package com.focusguardpro.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.focusguardpro.R
import com.focusguardpro.model.UserRole
import com.focusguardpro.ui.child.ChildDashboardActivity
import com.focusguardpro.ui.parent.ParentDashboardActivity
import com.focusguardpro.utils.PrefsHelper

/**
 * Splash screen that acts as the app's entry point.
 *
 * Routing logic:
 * 1. If no role selected → go to RoleSelectionActivity
 * 2. If role selected but onboarding incomplete → go to onboarding
 * 3. If all complete → go to the correct dashboard
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Short delay to show splash, then route
        Handler(Looper.getMainLooper()).postDelayed({
            routeToNextScreen()
        }, SPLASH_DELAY_MS)
    }

    private fun routeToNextScreen() {
        if (!PrefsHelper.isRoleSelected(this)) {
            // First launch: select role
            startActivity(Intent(this, RoleSelectionActivity::class.java))
        } else if (!PrefsHelper.isOnboardingComplete(this)) {
            // Role selected but onboarding not done
            val role = PrefsHelper.getUserRole(this)
            val intent = if (role == UserRole.PARENT) {
                Intent(this, ParentOnboardingActivity::class.java)
            } else {
                Intent(this, ChildOnboardingActivity::class.java)
            }
            startActivity(intent)
        } else {
            // Fully set up — go to dashboard
            val role = PrefsHelper.getUserRole(this)
            val intent = if (role == UserRole.PARENT) {
                Intent(this, ParentDashboardActivity::class.java)
            } else {
                Intent(this, ChildDashboardActivity::class.java)
            }
            startActivity(intent)
        }
        finish()
    }

    companion object {
        private const val SPLASH_DELAY_MS = 1200L
    }
}
