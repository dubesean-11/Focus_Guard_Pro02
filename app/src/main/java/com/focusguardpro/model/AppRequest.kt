package com.focusguardpro.model

/**
 * Represents a child's request for temporary access to a locked app.
 *
 * TODO: When Firebase is integrated, this will be synced to Firestore in real-time.
 *       See: /families/{familyId}/requests/{requestId}
 */
data class AppRequest(
    val id: String = java.util.UUID.randomUUID().toString(),
    val packageName: String = "",
    val appName: String = "",
    val message: String = "",
    val timestampMillis: Long = System.currentTimeMillis(),
    val status: RequestStatus = RequestStatus.PENDING
) {
    fun timeAgoString(): String {
        val diff = System.currentTimeMillis() - timestampMillis
        return when {
            diff < 60_000 -> "Just now"
            diff < 3_600_000 -> "${diff / 60_000}m ago"
            diff < 86_400_000 -> "${diff / 3_600_000}h ago"
            else -> "${diff / 86_400_000}d ago"
        }
    }
}

enum class RequestStatus {
    PENDING,
    APPROVED,
    DENIED
}
