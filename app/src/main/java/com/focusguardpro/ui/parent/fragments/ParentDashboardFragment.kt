package com.focusguardpro.ui.parent.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.focusguardpro.R
import com.focusguardpro.adapter.RequestsAdapter
import com.focusguardpro.databinding.FragmentParentDashboardBinding
import com.focusguardpro.viewmodel.ParentDashboardViewModel

/**
 * Parent Dashboard fragment — overview of connection status, active schedule, and pending requests.
 */
class ParentDashboardFragment : Fragment() {

    private var _binding: FragmentParentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ParentDashboardViewModel by viewModels()
    private lateinit var requestsAdapter: RequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestsAdapter = RequestsAdapter(
            showActions = true,
            onApprove = { request -> viewModel.approveRequest(request) },
            onDeny = { request -> viewModel.denyRequest(request) }
        )

        binding.rvRecentRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestsAdapter
        }

        // Navigate to pairing screen
        binding.btnPairDevice.setOnClickListener {
            findNavController().navigate(R.id.nav_pairing)
        }

        // Quick actions (stub)
        binding.btnLockAll.setOnClickListener {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, getString(R.string.coming_soon), com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .show()
        }
        binding.btnUnlockAll.setOnClickListener {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, getString(R.string.coming_soon), com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .show()
        }

        observeViewModel()
        viewModel.loadData()
    }

    private fun observeViewModel() {
        viewModel.connectionStatus.observe(viewLifecycleOwner) { connected ->
            binding.tvConnectionStatus.text = if (connected)
                getString(R.string.connected) else getString(R.string.not_connected)
            binding.tvConnectionStatus.setTextColor(
                requireContext().getColor(
                    if (connected) R.color.status_active else R.color.status_locked
                )
            )
        }

        viewModel.pendingRequests.observe(viewLifecycleOwner) { requests ->
            requestsAdapter.submitList(requests)
            val hasPending = requests.isNotEmpty()
            binding.tvNoRequests.visibility = if (hasPending) View.GONE else View.VISIBLE
            binding.rvRecentRequests.visibility = if (hasPending) View.VISIBLE else View.GONE
            if (hasPending) {
                binding.chipRequestCount.text = requests.size.toString()
                binding.chipRequestCount.visibility = View.VISIBLE
            } else {
                binding.chipRequestCount.visibility = View.GONE
            }
        }

        viewModel.activeScheduleSummary.observe(viewLifecycleOwner) { summary ->
            binding.tvActiveSchedule.text = summary ?: getString(R.string.no_schedule)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
