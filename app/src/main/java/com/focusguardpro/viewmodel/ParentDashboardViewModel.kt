package com.focusguardpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focusguardpro.model.AppRequest
import com.focusguardpro.model.RequestStatus

/**
 * ViewModel for the Parent Dashboard screen.
 *
 * TODO: Replace in-memory lists with Firebase Firestore listeners when cloud is configured.
 */
class ParentDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _pendingRequests = MutableLiveData<List<AppRequest>>(emptyList())
    val pendingRequests: LiveData<List<AppRequest>> = _pendingRequests

    private val _connectionStatus = MutableLiveData<Boolean>(false)
    val connectionStatus: LiveData<Boolean> = _connectionStatus

    private val _activeScheduleSummary = MutableLiveData<String?>(null)
    val activeScheduleSummary: LiveData<String?> = _activeScheduleSummary

    // Stub: simulate loading requests
    fun loadData() {
        // TODO: Replace with Firebase real-time listener
        // FirebaseFirestore.getInstance()
        //     .collection("families").document(familyId)
        //     .collection("requests")
        //     .whereEqualTo("status", "PENDING")
        //     .addSnapshotListener { snapshots, error -> ... }

        // For now, use empty list — demo data can be added here
        _pendingRequests.value = emptyList()
        _connectionStatus.value = false
        _activeScheduleSummary.value = null
    }

    fun approveRequest(request: AppRequest) {
        // TODO: Update Firebase record
        val updated = _pendingRequests.value.orEmpty().map {
            if (it.id == request.id) it.copy(status = RequestStatus.APPROVED) else it
        }.filter { it.status == RequestStatus.PENDING }
        _pendingRequests.value = updated
    }

    fun denyRequest(request: AppRequest) {
        // TODO: Update Firebase record
        val updated = _pendingRequests.value.orEmpty().map {
            if (it.id == request.id) it.copy(status = RequestStatus.DENIED) else it
        }.filter { it.status == RequestStatus.PENDING }
        _pendingRequests.value = updated
    }
}
