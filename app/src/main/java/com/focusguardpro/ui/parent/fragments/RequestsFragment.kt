package com.focusguardpro.ui.parent.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.focusguardpro.adapter.RequestsAdapter
import com.focusguardpro.databinding.FragmentRequestsBinding
import com.focusguardpro.viewmodel.RequestsViewModel

/**
 * Requests fragment (parent view) — shows all pending app access requests from the child.
 *
 * TODO: Add Firebase real-time listener to push new requests here automatically.
 */
class RequestsFragment : Fragment() {

    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestsViewModel by viewModels()
    private lateinit var requestsAdapter: RequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestsAdapter = RequestsAdapter(
            showActions = true,
            onApprove = { request ->
                viewModel.approveRequest(request)
                com.google.android.material.snackbar.Snackbar
                    .make(binding.root, getString(com.focusguardpro.R.string.request_approved), com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                    .show()
            },
            onDeny = { request ->
                viewModel.denyRequest(request)
                com.google.android.material.snackbar.Snackbar
                    .make(binding.root, getString(com.focusguardpro.R.string.request_denied), com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                    .show()
            }
        )

        binding.rvRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadRequests()
            binding.swipeRefresh.isRefreshing = false
        }

        observeViewModel()
        viewModel.loadRequests()
    }

    private fun observeViewModel() {
        viewModel.requests.observe(viewLifecycleOwner) { requests ->
            requestsAdapter.submitList(requests)
            val hasPending = requests.isNotEmpty()
            binding.tvNoRequests.visibility = if (hasPending) View.GONE else View.VISIBLE
            binding.rvRequests.visibility = if (hasPending) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
