package com.focusguardpro.ui.parent.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.focusguardpro.adapter.ScheduleAdapter
import com.focusguardpro.databinding.FragmentScheduleBinding
import com.focusguardpro.model.IntervalType
import com.focusguardpro.model.ScheduleInterval
import com.focusguardpro.viewmodel.ScheduleViewModel

/**
 * Schedule fragment — allows parent to add/remove study and break intervals per day.
 */
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var scheduleAdapter: ScheduleAdapter
    private var selectedDayIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleAdapter = ScheduleAdapter(
            onDelete = { interval ->
                viewModel.removeInterval(selectedDayIndex, interval.id)
            }
        )

        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }

        // Day tabs
        binding.tabsDays.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedDayIndex = tab?.position ?: 0
                viewModel.selectDay(selectedDayIndex)
                refreshIntervals()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // FAB — add interval
        binding.fabAddInterval.setOnClickListener {
            showAddIntervalDialog()
        }

        observeViewModel()
    }

    private fun refreshIntervals() {
        val intervals = viewModel.getIntervalsForDay(selectedDayIndex)
        scheduleAdapter.submitList(intervals.toList())
        binding.rvSchedule.visibility = if (intervals.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tvNoIntervals.visibility = if (intervals.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        viewModel.scheduleDays.observe(viewLifecycleOwner) {
            refreshIntervals()
        }
    }

    private fun showAddIntervalDialog() {
        val types = arrayOf("Study", "Break")
        var selectedType = IntervalType.STUDY
        var startHour = 9; var startMinute = 0
        var endHour = 10; var endMinute = 0

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Interval")
            .setSingleChoiceItems(types, 0) { _, which ->
                selectedType = if (which == 0) IntervalType.STUDY else IntervalType.BREAK
            }
            .setPositiveButton("Pick Start Time") { _, _ ->
                TimePickerDialog(requireContext(), { _, sH, sM ->
                    startHour = sH; startMinute = sM
                    TimePickerDialog(requireContext(), { _, eH, eM ->
                        endHour = eH; endMinute = eM
                        val interval = ScheduleInterval(
                            startHour = startHour,
                            startMinute = startMinute,
                            endHour = endHour,
                            endMinute = endMinute,
                            type = selectedType
                        )
                        viewModel.addInterval(selectedDayIndex, interval)
                    }, endHour, endMinute, true).show()
                }, startHour, startMinute, true).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
