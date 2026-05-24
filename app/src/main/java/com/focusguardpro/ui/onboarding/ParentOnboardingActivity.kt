package com.focusguardpro.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.focusguardpro.R
import com.focusguardpro.databinding.ActivityParentOnboardingBinding
import com.focusguardpro.ui.parent.ParentDashboardActivity
import com.focusguardpro.utils.PrefsHelper

/**
 * Three-page onboarding for the Parent role.
 * Uses ViewPager2 + TabLayoutMediator for paged navigation.
 */
class ParentOnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParentOnboardingBinding

    private val pages = listOf(
        OnboardingPage(R.string.parent_onboarding_title_1, R.string.parent_onboarding_desc_1),
        OnboardingPage(R.string.parent_onboarding_title_2, R.string.parent_onboarding_desc_2),
        OnboardingPage(R.string.parent_onboarding_title_3, R.string.parent_onboarding_desc_3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParentOnboardingBinding.inflate(layoutInflater)
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
        startActivity(Intent(this, ParentDashboardActivity::class.java))
        finish()
    }
}

/** Simple data class for onboarding page content */
data class OnboardingPage(val titleRes: Int, val descRes: Int)

/** Adapter for ViewPager2 onboarding pages */
class OnboardingPagerAdapter(private val pages: List<OnboardingPage>) :
    RecyclerView.Adapter<OnboardingPagerAdapter.PageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(page: OnboardingPage) {
            itemView.findViewById<TextView>(R.id.tvOnboardingTitle).setText(page.titleRes)
            itemView.findViewById<TextView>(R.id.tvOnboardingDesc).setText(page.descRes)
        }
    }
}
