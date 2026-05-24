package com.focusguardpro.model

/**
 * Represents a user's role in the app.
 * Stored in SharedPreferences on first launch.
 */
enum class UserRole {
    PARENT,
    CHILD,
    NONE  // Not yet selected
}
