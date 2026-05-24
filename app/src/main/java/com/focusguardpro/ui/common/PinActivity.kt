package com.focusguardpro.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.focusguardpro.R
import com.focusguardpro.databinding.ActivityPinBinding
import com.focusguardpro.utils.PinHelper

/**
 * PIN entry/creation activity.
 *
 * Use EXTRA_MODE to specify the behavior:
 *   MODE_ENTER  — verify existing PIN (returns RESULT_OK on success)
 *   MODE_SET    — create a new PIN (4-digit confirmation flow)
 *
 * TODO: Add biometric authentication option for PIN bypass.
 */
class PinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinBinding

    private val mode get() = intent.getStringExtra(EXTRA_MODE) ?: MODE_ENTER
    private val pinDots get() = listOf(binding.dot1, binding.dot2, binding.dot3, binding.dot4)

    private val enteredPin = StringBuilder()
    private var confirmPin: String? = null
    private var isConfirming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvPinTitle.text = when (mode) {
            MODE_SET -> getString(R.string.pin_create_title)
            else -> getString(R.string.pin_title)
        }

        setupNumberPad()
    }

    private fun setupNumberPad() {
        val buttons = listOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9"
        )

        buttons.forEach { (btn, digit) ->
            btn.setOnClickListener { appendDigit(digit) }
        }

        binding.btnBackspace.setOnClickListener { removeLastDigit() }
    }

    private fun appendDigit(digit: String) {
        if (enteredPin.length >= PIN_LENGTH) return
        enteredPin.append(digit)
        updateDots()

        if (enteredPin.length == PIN_LENGTH) {
            processPin()
        }
    }

    private fun removeLastDigit() {
        if (enteredPin.isNotEmpty()) {
            enteredPin.deleteCharAt(enteredPin.length - 1)
            updateDots()
        }
        binding.tvPinError.visibility = View.GONE
    }

    private fun updateDots() {
        pinDots.forEachIndexed { index, dot ->
            dot.setBackgroundResource(
                if (index < enteredPin.length) R.color.primary
                else android.R.color.darker_gray
            )
        }
    }

    private fun processPin() {
        when (mode) {
            MODE_ENTER -> verifyPin()
            MODE_SET -> handleSetPin()
        }
    }

    private fun verifyPin() {
        if (PinHelper.verifyPin(this, enteredPin.toString())) {
            setResult(RESULT_OK)
            finish()
        } else {
            showError(getString(R.string.pin_incorrect))
        }
    }

    private fun handleSetPin() {
        if (!isConfirming) {
            // First entry — ask to confirm
            confirmPin = enteredPin.toString()
            enteredPin.clear()
            isConfirming = true
            binding.tvPinTitle.text = getString(R.string.pin_confirm_title)
            updateDots()
        } else {
            // Confirmation entry
            if (enteredPin.toString() == confirmPin) {
                PinHelper.setPin(this, enteredPin.toString())
                setResult(RESULT_OK)
                finish()
            } else {
                showError(getString(R.string.pin_mismatch))
                isConfirming = false
                confirmPin = null
                binding.tvPinTitle.text = getString(R.string.pin_create_title)
            }
        }
    }

    private fun showError(message: String) {
        enteredPin.clear()
        updateDots()
        binding.tvPinError.text = message
        binding.tvPinError.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_MODE = "pin_mode"
        const val MODE_ENTER = "enter"
        const val MODE_SET = "set"
        private const val PIN_LENGTH = 4

        fun intentEnter(context: Context) =
            Intent(context, PinActivity::class.java).apply {
                putExtra(EXTRA_MODE, MODE_ENTER)
            }

        fun intentSet(context: Context) =
            Intent(context, PinActivity::class.java).apply {
                putExtra(EXTRA_MODE, MODE_SET)
            }
    }
}
