package com.focusguardpro.ui.child.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.focusguardpro.adapter.RequestsAdapter
import com.focusguardpro.databinding.FragmentChildRequestsBinding
import com.focusguardpro.viewmodel.ChildDashboardViewModel

/**
 * Child requests fragment — shows the child's own requests and their status.
 */
class ChildRequestsFragment : Fragment() {

    private var _binding: FragmentChildRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChildDashboardViewModel by viewModels({ requireActivity() })
    private lateinit var requestsAdapter: RequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Child view — no approve/deny actions, just status
        requestsAdapter = RequestsAdapter(showActions = false)
        binding.rvChildRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestsAdapter
        }

        binding.fabNewRequest.setOnClickListener {
            // Navigate back to Home to use the request dialog
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.myRequests.observe(viewLifecycleOwner) { requests ->
            requestsAdapter.submitList(requests)
            binding.tvNoRequests.visibility = if (requests.isEmpty()) View.VISIBLE else View.GONE
            binding.rvChildRequests.visibility = if (requests.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
