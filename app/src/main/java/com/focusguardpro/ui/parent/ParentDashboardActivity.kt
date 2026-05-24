package com.focusguardpro.ui.parent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.focusguardpro.R
import com.focusguardpro.databinding.ActivityParentDashboardBinding

/**
 * Main activity for the Parent role.
 * Hosts a bottom navigation bar with 5 destinations:
 *   Dashboard | Apps (Locker) | Schedule | Requests | Settings
 */
class ParentDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)

        // Set up Navigation Component with bottom nav
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // Update toolbar title on navigation
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }
    }
}
