# Focus Guard Pro 🛡️

**A modern Android parental control app** — helping families build healthy screen habits with flexible scheduling, app locking, and real-time parent-child sync.

---

## 📱 App Overview

Focus Guard Pro allows parents to:
- Lock specific apps on a child's device during study/break schedules
- Approve or deny app access requests from the child
- Pair devices and sync settings via Firebase (cloud-ready)
- Protect settings with a PIN and Device Admin

Children get:
- A minimal dashboard showing their current status (free/study/break)
- The ability to send app access requests to the parent
- A read-only view of today's schedule

---

## 🏗️ Project Structure (MVVM)

```
app/src/main/java/com/focusguardpro/
│
├── FocusGuardApp.kt               # Application class (Firebase init TODO)
│
├── ui/
│   ├── onboarding/
│   │   ├── SplashActivity.kt      # Entry point + routing
│   │   ├── RoleSelectionActivity.kt  # Parent/Child role pick (first launch)
│   │   ├── ParentOnboardingActivity.kt
│   │   └── ChildOnboardingActivity.kt
│   │
│   ├── parent/
│   │   ├── ParentDashboardActivity.kt   # Bottom nav host
│   │   └── fragments/
│   │       ├── ParentDashboardFragment.kt
│   │       ├── AppLockerFragment.kt
│   │       ├── ScheduleFragment.kt
│   │       ├── RequestsFragment.kt
│   │       ├── SettingsFragment.kt
│   │       └── PairingFragment.kt
│   │
│   ├── child/
│   │   ├── ChildDashboardActivity.kt    # Bottom nav host
│   │   └── fragments/
│   │       ├── ChildHomeFragment.kt
│   │       ├── ChildScheduleFragment.kt
│   │       └── ChildRequestsFragment.kt
│   │
│   └── common/
│       └── PinActivity.kt         # 4-digit PIN entry/creation
│
├── viewmodel/
│   ├── ParentDashboardViewModel.kt
│   ├── AppLockerViewModel.kt
│   ├── ScheduleViewModel.kt
│   ├── RequestsViewModel.kt
│   └── ChildDashboardViewModel.kt
│
├── model/
│   ├── UserRole.kt                # PARENT / CHILD enum
│   ├── AppInfo.kt                 # Installed app data
│   ├── ScheduleInterval.kt        # Study/Break time block
│   ├── ScheduleDay.kt             # Day's collection of intervals
│   └── AppRequest.kt             # Child's unlock request
│
├── adapter/
│   ├── AppListAdapter.kt          # RecyclerView for app list
│   ├── RequestsAdapter.kt         # RecyclerView for requests
│   └── ScheduleAdapter.kt        # RecyclerView for schedule intervals
│
├── utils/
│   ├── PrefsHelper.kt             # SharedPreferences helper
│   ├── AppUtils.kt                # Installed app discovery
│   └── PinHelper.kt               # Encrypted PIN storage
│
├── service/
│   └── AppLockService.kt          # Foreground service (UsageStats TODO)
│
└── receiver/
    ├── FocusDeviceAdminReceiver.kt  # Uninstall protection stub
    └── BootReceiver.kt             # Restart service on reboot
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 8+
- Android SDK 34

### Clone and Open
```bash
git clone https://github.com/dubesean-11/Focus_Guard_Pro02.git
```
1. Open **Android Studio** → `File > Open` → select the cloned folder
2. Wait for Gradle sync to complete
3. Run on an emulator or device (API 23+)

### First Run
- The app opens to a **Role Selection** screen (Parent or Child)
- Complete the 3-step onboarding
- You'll land on the appropriate dashboard

---

## 🔥 Adding Firebase (Cloud Sync)

The app is fully wired up for Firebase — all cloud code is commented with `// TODO` markers.

### Steps to enable Firebase:

1. **Create a Firebase project** at [console.firebase.google.com](https://console.firebase.google.com)

2. **Add an Android app** with package name: `com.focusguardpro`

3. **Download `google-services.json`** and place it in the `app/` folder:
   ```
   app/google-services.json   ← put it here
   ```

4. **Uncomment the Firebase plugin** in `build.gradle` (project level):
   ```groovy
   id 'com.google.gms.google-services' version '4.4.0' apply false
   ```

5. **Uncomment Firebase plugin** in `app/build.gradle`:
   ```groovy
   id 'com.google.gms.google-services'
   ```

6. **Uncomment Firebase dependencies** in `app/build.gradle`:
   ```groovy
   implementation platform('com.google.firebase:firebase-bom:32.7.0')
   implementation 'com.google.firebase:firebase-auth-ktx'
   implementation 'com.google.firebase:firebase-firestore-ktx'
   implementation 'com.google.firebase:firebase-database-ktx'
   ```

7. **Set up Firestore rules** (suggested structure):
   ```
   /families/{familyId}/
     lockedApps/{packageName}    → { locked: boolean }
     requests/{requestId}        → { appName, message, status, timestamp }
     schedule/{dayOfWeek}        → { intervals: [...] }
   /pairing/{shortCode}          → { deviceId, role, timestamp }
   ```

8. **Search for `// TODO` comments** in the codebase — each marks a Firebase integration point.

---

## 🔒 Security Notes

### PIN Protection
- Parent PIN is stored using **EncryptedSharedPreferences** (AES-256-GCM)
- See `PinHelper.kt` — currently uses SHA-256 hash
- **TODO for production**: Replace with PBKDF2 + salt or bcrypt

### Device Admin (Uninstall Protection)
- `FocusDeviceAdminReceiver.kt` handles Device Admin events
- When enabled, the app cannot be uninstalled without first removing admin access
- Enabled via Settings → Security → Device Admins (or prompt from Settings screen)

### App Locking
- `AppLockService.kt` runs as a foreground service
- **TODO**: Implement `UsageStatsManager` polling to detect and block foreground apps
- Requires user to grant **Usage Access** permission

---

## 📋 Feature Status

| Feature | Status | Notes |
|---------|--------|-------|
| Role selection | ✅ Working | Stored in SharedPreferences |
| Parent onboarding | ✅ Working | 3-page ViewPager2 |
| Child onboarding | ✅ Working | 3-page ViewPager2 |
| Parent dashboard | ✅ Working | Connection status, requests, schedule summary |
| App Locker UI | ✅ Working | Lists installed apps, lock toggles |
| Schedule editor | ✅ Working | Add/remove intervals per day |
| Requests (parent) | ✅ Working | Approve/deny (local state) |
| Child dashboard | ✅ Working | Status, schedule, send request |
| PIN entry/set | ✅ Working | 4-digit with EncryptedSharedPreferences |
| Device Admin | ✅ Stub | Flow wired, policies in xml |
| Device Pairing | 🔧 Stub | UI ready, needs Firebase |
| Firebase sync | 🔧 TODO | All integration points marked |
| App blocking | 🔧 TODO | Service stub, needs UsageStats |
| QR pairing | 🔧 TODO | UI placeholder, needs ZXing |
| Push notifications | 🔧 TODO | For request responses |
| Biometric unlock | 🔧 TODO | For PIN bypass |

---

## 🎨 UI/Design

- **Material 3** (Material You) design system
- **Light and Dark theme** automatically applied based on system setting
- Themes defined in `res/values/themes.xml` and `res/values-night/themes.xml`
- Primary color: Blue (`#1A73E8`) — parent accent
- Secondary color: Green (`#34A853`) — child accent
- Locked app indicator: Red (`#EA4335`)

---

## 🛠️ Future Work (TODO List)

1. **Firebase integration** — real-time sync of schedule, locks, requests
2. **App blocking** — implement `UsageStatsManager` polling in `AppLockService`
3. **QR code pairing** — add `zxing-android-embedded` library and implement
4. **Push notifications** — Firebase Cloud Messaging for request approvals
5. **Accessibility Service** — alternative/complement to UsageStats for app blocking
6. **Biometric authentication** — allow PIN bypass with fingerprint
7. **Usage reports** — show parents how much time child spent on each app
8. **PBKDF2 PIN hashing** — replace SHA-256 with proper key derivation
9. **Multiple children** — support more than one child device per family
10. **App categories** — group apps (games, social, education) for bulk locking

---

## 📄 License

MIT License — see `LICENSE` for details.

---

*Built with ❤️ using Kotlin, Material 3, MVVM architecture, and Android Jetpack.*