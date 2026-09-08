# Zero Budget Browser

Local-first Android browser. No backend, no paid API, no analytics.

**Phase 1 only.** Later phases are not in this tree.

## What Phase 1 contains

- Clean WebView UI with system light/dark theme
- Address bar: URL or Google search (search URL built on-device)
- Direct HTTPS loads (phone to website, no proxy)
- Back / Forward / Reload / Home
- Loading bar and failed-load panel with Retry
- Android Back: WebView history first, then leave the activity
- Tab foundation (`TabManager`, single active tab)
- Local history (`filesDir/history.json`)
- Local bookmarks (`filesDir/bookmarks.json`)
- Unit tests for resolver, history, bookmarks, tabs, back policy

## What is intentionally NOT included

- AI / paid APIs / accounts / cloud sync
- Custom search engine product or ad server
- Own browser engine
- Full tab UI / multi-WebView switching
- History/bookmarks screens (storage only)
- Tracking or analytics

## Technologies

- Kotlin, Android SDK 35, minSdk 24
- AndroidX AppCompat + Material 3 (DayNight)
- System WebView (`androidx.webkit`)
- Local JSON files (no Room / no network store)
- JUnit 4 unit tests

## How to build / run

On a machine with Android Studio or the command-line SDK:

GitHub Actions on `main` also builds the debug APK (Actions, latest run, `app-debug` artifact).

```bash
export ANDROID_HOME=/path/to/Android/Sdk
# Gradle 8.9 + JDK 17
gradle :app:assembleDebug :app:testDebugUnitTest
```

Debug APK:

```
app/build/outputs/apk/debug/app-debug.apk
```

Install on a phone (USB debugging or copy the APK):

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

From Android Studio: Open the repo, Run `app`.

## How to test (manual)

1. Launch **Zero Budget Browser**.
2. Home page (Google) should load over HTTPS.
3. Type `example.com` and go. Should open `https://example.com`.
4. Type `open source browser`. Should open Google search results.
5. Use in-page links, then Back / Forward.
6. Tap Reload.
7. Tap Home. Returns to Google.
8. Tap the star. Toast: bookmark saved locally.
9. Toggle system dark mode. Toolbar should follow.
10. Airplane mode, reload. Error panel + Retry, no crash.
11. System Back: page history first, then leave the app.
12. Kill and reopen. History stays in app-private storage only.

## Known limitations

- One visible tab. `TabManager` can hold more tabs; no tab switcher UI yet.
- History and bookmarks have no list screens yet.
- Default search is Google; template lives in `AppSettings` for later configuration.
- Relies on the device System WebView.
- Debug APK is for sideload only.
- JavaScript is enabled so normal sites work. No content blockers in Phase 1.

## Privacy

History and bookmarks stay in app-private files. Nothing is uploaded. No analytics SDK.
