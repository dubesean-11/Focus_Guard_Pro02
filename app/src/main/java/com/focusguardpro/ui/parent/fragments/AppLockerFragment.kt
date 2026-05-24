package com.focusguardpro.ui.parent.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.focusguardpro.adapter.AppListAdapter
import com.focusguardpro.databinding.FragmentAppLockerBinding
import com.focusguardpro.viewmodel.AppLockerViewModel

/**
 * App Locker fragment — lists installed apps with lock toggles.
 * Parent can search and lock/unlock individual apps.
 */
class AppLockerFragment : Fragment() {

    private var _binding: FragmentAppLockerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AppLockerViewModel by viewModels()
    private lateinit var appAdapter: AppListAdapter

    private var currentQuery = ""
    private var showLockedOnly = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appAdapter = AppListAdapter { app ->
            viewModel.toggleLock(requireContext(), app)
        }

        binding.rvApps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appAdapter
        }

        // Search
        binding.etSearchApps.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currentQuery = s?.toString() ?: ""
                refreshList()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Tab filter
        binding.tabsApps.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showLockedOnly = tab?.position == 1
                refreshList()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Pull to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadApps(requireContext())
        }

        observeViewModel()
        viewModel.loadApps(requireContext())
    }

    private fun refreshList() {
        val filtered = viewModel.getFilteredApps(currentQuery, showLockedOnly)
        appAdapter.submitList(filtered)
        binding.tvNoApps.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        viewModel.apps.observe(viewLifecycleOwner) {
            refreshList()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.swipeRefresh.isRefreshing = loading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
