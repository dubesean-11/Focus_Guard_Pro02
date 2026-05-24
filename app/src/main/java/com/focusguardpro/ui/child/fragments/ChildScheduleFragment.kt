package com.focusguardpro.ui.child.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.focusguardpro.adapter.ScheduleAdapter
import com.focusguardpro.databinding.FragmentChildScheduleBinding
import com.focusguardpro.viewmodel.ChildDashboardViewModel
import java.util.Calendar

/**
 * Child schedule fragment — read-only view of today's schedule.
 *
 * TODO: Subscribe to Firebase to get real-time schedule updates from parent.
 */
class ChildScheduleFragment : Fragment() {

    private var _binding: FragmentChildScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChildDashboardViewModel by viewModels({ requireActivity() })
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleAdapter = ScheduleAdapter(editable = false)
        binding.rvChildSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.todayIntervals.observe(viewLifecycleOwner) { intervals ->
            scheduleAdapter.submitList(intervals)
            binding.tvNoSchedule.visibility = if (intervals.isEmpty()) View.VISIBLE else View.GONE
            binding.rvChildSchedule.visibility = if (intervals.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
