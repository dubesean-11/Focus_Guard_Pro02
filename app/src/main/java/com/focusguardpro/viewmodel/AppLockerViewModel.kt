package com.focusguardpro.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.focusguardpro.model.AppInfo
import com.focusguardpro.utils.AppUtils
import com.focusguardpro.utils.PrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the App Locker screen.
 * Loads installed apps and manages lock state.
 *
 * TODO: Sync lock state to Firebase so it applies to child device in real-time.
 */
class AppLockerViewModel(application: Application) : AndroidViewModel(application) {

    private val _apps = MutableLiveData<List<AppInfo>>(emptyList())
    val apps: LiveData<List<AppInfo>> = _apps

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _filterQuery = MutableLiveData<String>("")

    fun loadApps(context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            val appList = withContext(Dispatchers.IO) {
                AppUtils.getInstalledUserApps(context)
            }
            _apps.value = appList
            _isLoading.value = false
        }
    }

    fun toggleLock(context: Context, app: AppInfo) {
        val updated = _apps.value.orEmpty().map {
            if (it.packageName == app.packageName) it.copy(isLocked = !it.isLocked) else it
        }
        _apps.value = updated

        // Persist to local prefs
        val newApp = updated.first { it.packageName == app.packageName }
        if (newApp.isLocked) {
            PrefsHelper.addLockedApp(context, app.packageName)
        } else {
            PrefsHelper.removeLockedApp(context, app.packageName)
        }

        // TODO: Sync to Firebase
        // FirebaseFirestore.getInstance()
        //     .collection("families").document(familyId)
        //     .collection("lockedApps").document(app.packageName)
        //     .set(mapOf("locked" to newApp.isLocked))
    }

    fun getFilteredApps(query: String, lockedOnly: Boolean): List<AppInfo> {
        val base = _apps.value.orEmpty()
        val filtered = if (lockedOnly) base.filter { it.isLocked } else base
        return if (query.isBlank()) filtered
        else filtered.filter {
            it.appName.contains(query, ignoreCase = true) ||
                    it.packageName.contains(query, ignoreCase = true)
        }
    }
}
