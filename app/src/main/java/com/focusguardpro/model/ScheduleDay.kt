package com.focusguardpro.model

/**
 * Represents the schedule for a single day of the week.
 *
 * @param dayOfWeek Calendar.MONDAY through Calendar.SUNDAY
 * @param intervals Ordered list of time intervals for this day
 */
data class ScheduleDay(
    val dayOfWeek: Int,
    val intervals: MutableList<ScheduleInterval> = mutableListOf()
) {
    val dayName: String
        get() = when (dayOfWeek) {
            java.util.Calendar.MONDAY -> "Monday"
            java.util.Calendar.TUESDAY -> "Tuesday"
            java.util.Calendar.WEDNESDAY -> "Wednesday"
            java.util.Calendar.THURSDAY -> "Thursday"
            java.util.Calendar.FRIDAY -> "Friday"
            java.util.Calendar.SATURDAY -> "Saturday"
            java.util.Calendar.SUNDAY -> "Sunday"
            else -> "Unknown"
        }
}
