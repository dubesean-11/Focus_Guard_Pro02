package com.focusguardpro.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.focusguardpro.R
import com.focusguardpro.databinding.ActivityChildOnboardingBinding
import com.focusguardpro.ui.child.ChildDashboardActivity
import com.focusguardpro.utils.PrefsHelper

/**
 * Three-page onboarding for the Child role.
 */
class ChildOnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildOnboardingBinding

    private val pages = listOf(
        OnboardingPage(R.string.child_onboarding_title_1, R.string.child_onboarding_desc_1),
        OnboardingPage(R.string.child_onboarding_title_2, R.string.child_onboarding_desc_2),
        OnboardingPage(R.string.child_onboarding_title_3, R.string.child_onboarding_desc_3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingPagerAdapter(pages)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { _, _ -> }.attach()

        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < pages.size - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnNext.text = if (position == pages.size - 1)
                    getString(R.string.btn_get_started)
                else
                    getString(R.string.btn_next)
            }
        })
    }

    private fun finishOnboarding() {
        PrefsHelper.setOnboardingComplete(this, true)
        startActivity(Intent(this, ChildDashboardActivity::class.java))
        finish()
    }
}
