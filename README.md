# WConnect (starter Android project)

A local-only Android starter that records visible WhatsApp and WhatsApp Business notification text using Android's user-authorized NotificationListenerService.

## Open
1. Install Android Studio (recent stable version) and Android SDK 35.
2. Open this `WConnect` folder in Android Studio.
3. Let Gradle sync complete.
4. Run on your Android phone (USB debugging enabled) or emulator.
5. Open WConnect and tap **Enable Notification Access**.
6. In Android Settings, enable WConnect notification access. Return to the app and receive a WhatsApp notification.

## Current limitations
- This is a starter project, not a complete WhatsApp client.
- It stores only text/metadata exposed in Android notifications. Android/WhatsApp may hide message previews or media content.
- It does not read WhatsApp's private database, log into WhatsApp, download protected View Once media, or bypass screenshot/privacy protections.
- Notification text is not reliable proof that a message is View Once. The app labels it as a *possible* View Once notification only when visible text contains a matching hint.
- Records are stored locally in app-private SharedPreferences, with a 500-record cap. Add encryption, deletion controls, and a Room database before production use.
- The project includes WhatsApp and WhatsApp Business package filters. Other app package names are ignored.

## Next development milestones
1. Improve message grouping and contact-wise history.
2. Add user-controlled retention and delete/export controls.
3. Add media backup only for files the user can legitimately access through Android APIs and user-granted permissions.
4. Replace the simple local store with Room and add tests.
