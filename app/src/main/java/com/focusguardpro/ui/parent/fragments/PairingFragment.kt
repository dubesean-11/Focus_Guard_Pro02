package com.focusguardpro.ui.parent.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.focusguardpro.R
import com.focusguardpro.databinding.FragmentPairingBinding
import com.focusguardpro.utils.PrefsHelper

/**
 * Pairing fragment — shows pairing code and QR placeholder.
 * Parent can enter a child device's code to link devices.
 *
 * TODO: Implement full Firebase-based pairing:
 *   1. Generate a short code stored in Firebase under /pairing/{code}
 *   2. Child enters code → look up in Firebase → link devices under /families/{id}
 *   3. Generate QR code from the pairing code using ZXing library
 */
class PairingFragment : Fragment() {

    private var _binding: FragmentPairingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPairingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show device ID as a pairing stub
        val deviceId = PrefsHelper.getDeviceId(requireContext())
        // Show only first 6 chars as a simple code
        val shortCode = deviceId.filter { it.isLetterOrDigit() }.take(6).uppercase()
        binding.tvPairingCode.text = shortCode

        binding.btnConnect.setOnClickListener {
            val code = binding.etPairingCode.text?.toString()?.trim()
            if (code.isNullOrBlank() || code.length < 6) {
                binding.etPairingCode.error = "Please enter a valid 6-digit code"
                return@setOnClickListener
            }
            // TODO: Look up pairing code in Firebase and link devices
            Snackbar.make(
                binding.root,
                getString(R.string.coming_soon),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
