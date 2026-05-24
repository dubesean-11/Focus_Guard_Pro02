package com.focusguardpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focusguardpro.model.AppRequest
import com.focusguardpro.model.RequestStatus
import com.focusguardpro.model.ScheduleInterval

/**
 * ViewModel for the Child Dashboard and related child screens.
 *
 * TODO: Connect to Firebase Firestore to receive real-time schedule and lock updates.
 */
class ChildDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentStatus = MutableLiveData<String>("Free Time")
    val currentStatus: LiveData<String> = _currentStatus

    private val _todayIntervals = MutableLiveData<List<ScheduleInterval>>(emptyList())
    val todayIntervals: LiveData<List<ScheduleInterval>> = _todayIntervals

    private val _myRequests = MutableLiveData<List<AppRequest>>(emptyList())
    val myRequests: LiveData<List<AppRequest>> = _myRequests

    fun loadData() {
        // TODO: Listen to Firebase for schedule and request updates
        // FirebaseFirestore.getInstance()
        //     .collection("families").document(familyId)
        //     .collection("schedule")
        //     .addSnapshotListener { ... }

        _currentStatus.value = "Free Time"
        _todayIntervals.value = emptyList()
        _myRequests.value = emptyList()
    }

    fun sendRequest(appName: String, packageName: String, message: String) {
        val request = AppRequest(
            appName = appName,
            packageName = packageName,
            message = message,
            status = RequestStatus.PENDING
        )

        val current = _myRequests.value.orEmpty().toMutableList()
        current.add(0, request)
        _myRequests.value = current

        // TODO: Write request to Firebase
        // FirebaseFirestore.getInstance()
        //     .collection("families").document(familyId)
        //     .collection("requests").document(request.id)
        //     .set(request)
    }
}
