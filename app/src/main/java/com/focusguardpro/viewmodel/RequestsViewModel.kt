package com.focusguardpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focusguardpro.model.AppRequest
import com.focusguardpro.model.RequestStatus

/**
 * ViewModel for the Requests screen (parent side).
 *
 * TODO: Connect to Firebase Firestore for real-time request notifications.
 *       Path: /families/{familyId}/requests
 */
class RequestsViewModel(application: Application) : AndroidViewModel(application) {

    private val _requests = MutableLiveData<List<AppRequest>>(emptyList())
    val requests: LiveData<List<AppRequest>> = _requests

    fun loadRequests() {
        // TODO: Set up Firebase snapshot listener
        // FirebaseFirestore.getInstance()
        //     .collection("families").document(familyId)
        //     .collection("requests")
        //     .addSnapshotListener { snapshots, error ->
        //         if (error != null) return@addSnapshotListener
        //         _requests.value = snapshots?.documents?.mapNotNull { it.toObject(AppRequest::class.java) }
        //     }

        _requests.value = emptyList()
    }

    fun approveRequest(request: AppRequest) {
        updateRequestStatus(request, RequestStatus.APPROVED)
        // TODO: Update Firebase and send push notification to child device
    }

    fun denyRequest(request: AppRequest) {
        updateRequestStatus(request, RequestStatus.DENIED)
        // TODO: Update Firebase and send push notification to child device
    }

    private fun updateRequestStatus(request: AppRequest, status: RequestStatus) {
        val updated = _requests.value.orEmpty().map {
            if (it.id == request.id) it.copy(status = status) else it
        }
        _requests.value = updated
    }
}
