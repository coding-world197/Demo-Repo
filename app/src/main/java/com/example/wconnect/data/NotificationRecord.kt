package com.example.wconnect.data

data class NotificationRecord(
    val id: Long,
    val app: String,
    val sender: String,
    val message: String,
    val receivedAt: Long,
    val category: String
)
