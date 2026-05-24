package com.focusguardpro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.focusguardpro.databinding.ItemAppBinding
import com.focusguardpro.model.AppInfo
import com.focusguardpro.utils.AppUtils

/**
 * RecyclerView adapter for displaying installed apps in the App Locker screen.
 */
class AppListAdapter(
    private val onToggleLock: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppListAdapter.AppViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AppViewHolder(private val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppInfo) {
            binding.tvAppName.text = app.appName
            binding.tvPackageName.text = app.packageName

            // Load app icon
            val icon = AppUtils.getAppIcon(binding.root.context, app.packageName)
            if (icon != null) {
                Glide.with(binding.root.context)
                    .load(icon)
                    .into(binding.ivAppIcon)
            }

            // Set switch state without triggering listener
            binding.switchLocked.setOnCheckedChangeListener(null)
            binding.switchLocked.isChecked = app.isLocked
            binding.switchLocked.setOnCheckedChangeListener { _, _ ->
                onToggleLock(app)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppInfo>() {
            override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean =
                oldItem.packageName == newItem.packageName

            override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean =
                oldItem == newItem
        }
    }
}
