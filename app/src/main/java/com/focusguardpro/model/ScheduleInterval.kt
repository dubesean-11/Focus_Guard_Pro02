package com.focusguardpro.model

/**
 * Represents a single time interval in a schedule (study or break block).
 *
 * @param startHour Start hour (0-23)
 * @param startMinute Start minute (0-59)
 * @param endHour End hour (0-23)
 * @param endMinute End minute (0-59)
 * @param type Whether this is a study or break period
 */
data class ScheduleInterval(
    val id: String = java.util.UUID.randomUUID().toString(),
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 10,
    val endMinute: Int = 0,
    val type: IntervalType = IntervalType.STUDY
) {
    fun startTimeString(): String = String.format("%02d:%02d", startHour, startMinute)
    fun endTimeString(): String = String.format("%02d:%02d", endHour, endMinute)
    fun displayString(): String = "${startTimeString()} – ${endTimeString()}"
}

enum class IntervalType {
    STUDY,
    BREAK
}
