package com.focusguardpro.ui.child.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.focusguardpro.R
import com.focusguardpro.adapter.ScheduleAdapter
import com.focusguardpro.databinding.FragmentChildHomeBinding
import com.focusguardpro.viewmodel.ChildDashboardViewModel

/**
 * Child home fragment — shows current status, today's schedule, and a quick request button.
 */
class ChildHomeFragment : Fragment() {

    private var _binding: FragmentChildHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChildDashboardViewModel by viewModels()
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleAdapter = ScheduleAdapter(editable = false)
        binding.rvTodaySchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }

        binding.btnSendRequest.setOnClickListener {
            showSendRequestDialog()
        }

        observeViewModel()
        viewModel.loadData()
    }

    private fun observeViewModel() {
        viewModel.currentStatus.observe(viewLifecycleOwner) { status ->
            binding.tvCurrentStatus.text = status
            binding.tvCurrentStatus.setTextColor(
                requireContext().getColor(
                    when (status) {
                        getString(R.string.study_time) -> R.color.status_locked
                        getString(R.string.break_time) -> R.color.status_pending
                        else -> R.color.status_active
                    }
                )
            )
        }

        viewModel.todayIntervals.observe(viewLifecycleOwner) { intervals ->
            scheduleAdapter.submitList(intervals)
        }
    }

    private fun showSendRequestDialog() {
        val appNames = arrayOf("YouTube", "TikTok", "Instagram", "Games", "Other")
        var selectedApp = appNames[0]

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.send_request))
            .setSingleChoiceItems(appNames, 0) { _, which ->
                selectedApp = appNames[which]
            }
            .setPositiveButton("Send") { _, _ ->
                viewModel.sendRequest(
                    appName = selectedApp,
                    packageName = "",
                    message = "Please allow me to use $selectedApp"
                )
                com.google.android.material.snackbar.Snackbar
                    .make(binding.root, "Request sent to parent!", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
