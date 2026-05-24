package com.focusguardpro.ui.child

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.focusguardpro.R
import com.focusguardpro.databinding.ActivityChildDashboardBinding

/**
 * Main activity for the Child role.
 * Hosts a minimal bottom navigation: Dashboard | Schedule | Requests
 */
class ChildDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }
    }
}
