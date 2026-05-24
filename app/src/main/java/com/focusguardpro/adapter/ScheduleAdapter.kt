package com.focusguardpro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focusguardpro.R
import com.focusguardpro.databinding.ItemScheduleIntervalBinding
import com.focusguardpro.model.IntervalType
import com.focusguardpro.model.ScheduleInterval

/**
 * RecyclerView adapter for displaying schedule intervals.
 */
class ScheduleAdapter(
    private val onDelete: ((ScheduleInterval) -> Unit)? = null,
    private val editable: Boolean = true
) : ListAdapter<ScheduleInterval, ScheduleAdapter.IntervalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervalViewHolder {
        val binding = ItemScheduleIntervalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return IntervalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IntervalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IntervalViewHolder(private val binding: ItemScheduleIntervalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(interval: ScheduleInterval) {
            val context = binding.root.context
            binding.tvIntervalType.text = if (interval.type == IntervalType.STUDY) "Study" else "Break"
            binding.tvIntervalTime.text = interval.displayString()

            val colorRes = if (interval.type == IntervalType.STUDY)
                R.color.schedule_block_color else R.color.break_block_color
            binding.viewTypeIndicator.setBackgroundColor(ContextCompat.getColor(context, colorRes))

            if (editable) {
                binding.btnDeleteInterval.setOnClickListener { onDelete?.invoke(interval) }
            } else {
                binding.btnDeleteInterval.visibility = android.view.View.GONE
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScheduleInterval>() {
            override fun areItemsTheSame(oldItem: ScheduleInterval, newItem: ScheduleInterval): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ScheduleInterval, newItem: ScheduleInterval): Boolean =
                oldItem == newItem
        }
    }
}
