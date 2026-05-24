package com.focusguardpro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focusguardpro.databinding.ItemRequestBinding
import com.focusguardpro.model.AppRequest
import com.focusguardpro.model.RequestStatus

/**
 * RecyclerView adapter for displaying app requests.
 * Works for both parent view (with approve/deny buttons) and child view (status only).
 */
class RequestsAdapter(
    private val showActions: Boolean = true,
    private val onApprove: ((AppRequest) -> Unit)? = null,
    private val onDeny: ((AppRequest) -> Unit)? = null
) : ListAdapter<AppRequest, RequestsAdapter.RequestViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RequestViewHolder(private val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(request: AppRequest) {
            binding.tvRequestApp.text = request.appName
            binding.tvRequestMessage.text = request.message.ifBlank { "No message" }
            binding.tvRequestTime.text = request.timeAgoString()

            // Status chip color
            binding.chipRequestStatus.text = when (request.status) {
                RequestStatus.PENDING -> "Pending"
                RequestStatus.APPROVED -> "Approved"
                RequestStatus.DENIED -> "Denied"
            }

            // Show action buttons only for parent and only for pending requests
            val showButtons = showActions && request.status == RequestStatus.PENDING
            binding.layoutActions.visibility = if (showButtons) View.VISIBLE else View.GONE

            binding.btnApprove.setOnClickListener { onApprove?.invoke(request) }
            binding.btnDeny.setOnClickListener { onDeny?.invoke(request) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppRequest>() {
            override fun areItemsTheSame(oldItem: AppRequest, newItem: AppRequest): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AppRequest, newItem: AppRequest): Boolean =
                oldItem == newItem
        }
    }
}
