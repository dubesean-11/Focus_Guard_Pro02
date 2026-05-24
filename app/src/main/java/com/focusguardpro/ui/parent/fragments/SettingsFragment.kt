package com.focusguardpro.ui.parent.fragments

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.focusguardpro.R
import com.focusguardpro.databinding.FragmentSettingsBinding
import com.focusguardpro.model.UserRole
import com.focusguardpro.receiver.FocusDeviceAdminReceiver
import com.focusguardpro.ui.common.PinActivity
import com.focusguardpro.ui.onboarding.RoleSelectionActivity
import com.focusguardpro.utils.PinHelper
import com.focusguardpro.utils.PrefsHelper

/**
 * Settings fragment — PIN management, security, Firebase setup, and role switching.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var deviceAdminComponent: ComponentName

    private val deviceAdminLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        updateDeviceAdminSwitch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        devicePolicyManager =
            requireContext().getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceAdminComponent = ComponentName(requireContext(), FocusDeviceAdminReceiver::class.java)

        // Current role
        binding.tvCurrentRole.text = if (PrefsHelper.getUserRole(requireContext()) == UserRole.PARENT)
            "Parent" else "Child"

        // PIN status
        updatePinStatus()

        // Set PIN
        binding.rowSetPin.setOnClickListener {
            startActivity(Intent(requireContext(), PinActivity::class.java).apply {
                putExtra(PinActivity.EXTRA_MODE, PinActivity.MODE_SET)
            })
        }

        // Device Admin toggle
        updateDeviceAdminSwitch()
        binding.switchDeviceAdmin.setOnCheckedChangeListener { _, checked ->
            if (checked && !isDeviceAdminEnabled()) {
                requestDeviceAdmin()
            } else if (!checked && isDeviceAdminEnabled()) {
                devicePolicyManager.removeActiveAdmin(deviceAdminComponent)
            }
        }

        // Accessibility settings
        binding.btnAccessibility.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        // Firebase setup (stub)
        binding.btnSetupFirebase.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Firebase Setup")
                .setMessage("To set up Firebase:\n\n1. Create a project at console.firebase.google.com\n2. Add an Android app (package: com.focusguardpro)\n3. Download google-services.json to app/\n4. Uncomment Firebase lines in build.gradle\n\nSee README.md for full instructions.")
                .setPositiveButton(R.string.ok, null)
                .show()
        }

        // Switch role
        binding.rowSwitchRole.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.switch_role)
                .setMessage(R.string.switch_role_confirm)
                .setPositiveButton(R.string.yes) { _, _ ->
                    PrefsHelper.clearAll(requireContext())
                    startActivity(
                        Intent(requireContext(), RoleSelectionActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        updatePinStatus()
        updateDeviceAdminSwitch()
    }

    private fun updatePinStatus() {
        val isSet = PinHelper.isPinSet(requireContext())
        binding.tvPinStatus.text = if (isSet) "Set ✓" else "Not Set"
        binding.tvPinStatus.setTextColor(
            requireContext().getColor(if (isSet) R.color.status_active else R.color.status_locked)
        )
    }

    private fun isDeviceAdminEnabled(): Boolean =
        devicePolicyManager.isAdminActive(deviceAdminComponent)

    private fun updateDeviceAdminSwitch() {
        binding.switchDeviceAdmin.setOnCheckedChangeListener(null)
        binding.switchDeviceAdmin.isChecked = isDeviceAdminEnabled()
        binding.switchDeviceAdmin.setOnCheckedChangeListener { _, checked ->
            if (checked && !isDeviceAdminEnabled()) {
                requestDeviceAdmin()
            } else if (!checked && isDeviceAdminEnabled()) {
                devicePolicyManager.removeActiveAdmin(deviceAdminComponent)
            }
        }
    }

    private fun requestDeviceAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponent)
            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Enable to prevent Focus Guard Pro from being uninstalled without the parent PIN."
            )
        }
        deviceAdminLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
