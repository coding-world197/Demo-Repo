package com.example.wconnect.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Small local-only starter store. Replace with Room when the project grows. */
object NotificationStore {
    private const val PREFS = "wconnect_records"
    private const val KEY = "records"

    @Synchronized
    fun add(context: Context, record: NotificationRecord) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val old = runCatching { JSONArray(prefs.getString(KEY, "[]")) }.getOrDefault(JSONArray())
        val next = JSONArray()
        next.put(toJson(record))
        // Keep the newest 500 records so notification history does not grow without limit.
        for (i in 0 until minOf(old.length(), 499)) next.put(old.getJSONObject(i))
        prefs.edit().putString(KEY, next.toString()).apply()
    }

    @Synchronized
    fun all(context: Context): List<NotificationRecord> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]")
        val array = runCatching { JSONArray(raw) }.getOrDefault(JSONArray())
        return buildList {
            for (i in 0 until array.length()) {
                val o = array.optJSONObject(i) ?: continue
                add(NotificationRecord(
                    id = o.optLong("id"), app = o.optString("app"),
                    sender = o.optString("sender"), message = o.optString("message"),
                    receivedAt = o.optLong("receivedAt"), category = o.optString("category")
                ))
            }
        }
    }

    private fun toJson(r: NotificationRecord) = JSONObject().apply {
        put("id", r.id); put("app", r.app); put("sender", r.sender)
        put("message", r.message); put("receivedAt", r.receivedAt); put("category", r.category)
    }
}
