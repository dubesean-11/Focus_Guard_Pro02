package com.focusguardpro.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.focusguardpro.databinding.ActivityRoleSelectionBinding
import com.focusguardpro.model.UserRole
import com.focusguardpro.utils.PrefsHelper

/**
 * Role selection screen shown on first launch.
 * The user picks "Parent" or "Child" — this is stored in SharedPreferences.
 */
class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardParent.setOnClickListener {
            selectRole(UserRole.PARENT)
        }

        binding.cardChild.setOnClickListener {
            selectRole(UserRole.CHILD)
        }
    }

    private fun selectRole(role: UserRole) {
        PrefsHelper.setUserRole(this, role)

        val intent = if (role == UserRole.PARENT) {
            Intent(this, ParentOnboardingActivity::class.java)
        } else {
            Intent(this, ChildOnboardingActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
