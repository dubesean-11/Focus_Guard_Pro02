package com.focusguardpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focusguardpro.model.ScheduleDay
import com.focusguardpro.model.ScheduleInterval
import java.util.Calendar

/**
 * ViewModel for the Schedule screen.
 * Manages the weekly schedule data.
 *
 * TODO: Persist schedule to Firebase Firestore for real-time sync to child device.
 *       Path: /families/{familyId}/schedule/{dayOfWeek}
 */
class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val _scheduleDays = MutableLiveData<List<ScheduleDay>>(createEmptyWeek())
    val scheduleDays: LiveData<List<ScheduleDay>> = _scheduleDays

    private val _selectedDayIndex = MutableLiveData<Int>(0)
    val selectedDayIndex: LiveData<Int> = _selectedDayIndex

    fun selectDay(index: Int) {
        _selectedDayIndex.value = index
    }

    fun getIntervalsForDay(dayIndex: Int): List<ScheduleInterval> {
        return _scheduleDays.value?.getOrNull(dayIndex)?.intervals ?: emptyList()
    }

    fun addInterval(dayIndex: Int, interval: ScheduleInterval) {
        val days = _scheduleDays.value?.toMutableList() ?: return
        val day = days[dayIndex]
        day.intervals.add(interval)
        // Sort by start time
        day.intervals.sortWith(compareBy({ it.startHour }, { it.startMinute }))
        _scheduleDays.value = days

        // TODO: Save to Firebase
        // syncDayToFirebase(day)
    }

    fun removeInterval(dayIndex: Int, intervalId: String) {
        val days = _scheduleDays.value?.toMutableList() ?: return
        val day = days[dayIndex]
        day.intervals.removeIf { it.id == intervalId }
        _scheduleDays.value = days

        // TODO: Delete from Firebase
        // deleteIntervalFromFirebase(dayIndex, intervalId)
    }

    private fun createEmptyWeek(): List<ScheduleDay> {
        return listOf(
            Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
            Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY
        ).map { dayOfWeek -> ScheduleDay(dayOfWeek) }
    }
}
