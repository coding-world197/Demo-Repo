package com.example.wconnect.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.wconnect.data.NotificationRecord
import com.example.wconnect.data.NotificationStore

class WhatsAppNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Only capture WhatsApp and WhatsApp Business notifications.
        val packageName = sbn.packageName
        if (packageName != WHATSAPP && packageName != WHATSAPP_BUSINESS) return

        val extras = sbn.notification.extras ?: return
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
        val text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()
        if (title.isBlank() && text.isBlank()) return

        // Notification text is not guaranteed to identify View Once or expose media bytes.
        // We deliberately store only the visible notification text and metadata.
        val record = NotificationRecord(
            id = sbn.postTime,
            app = if (packageName == WHATSAPP) "WhatsApp" else "WhatsApp Business",
            sender = title,
            message = text,
            receivedAt = sbn.postTime,
            category = classify(sbn.notification)
        )
        NotificationStore.add(applicationContext, record)
    }

    private fun classify(notification: Notification): String {
        val text = buildString {
            append(notification.extras?.getCharSequence(Notification.EXTRA_TITLE).orEmpty())
            append(' ')
            append(notification.extras?.getCharSequence(Notification.EXTRA_TEXT).orEmpty())
        }.lowercase()
        return when {
            "view once" in text || "once" in text -> "Possible View Once notification"
            "voice" in text || "audio" in text -> "Possible audio notification"
            "photo" in text || "image" in text -> "Possible photo notification"
            else -> "Message notification"
        }
    }

    companion object {
        private const val WHATSAPP = "com.whatsapp"
        private const val WHATSAPP_BUSINESS = "com.whatsapp.w4b"
    }
}
