package com.example.wconnect

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wconnect.data.NotificationRecord
import com.example.wconnect.data.NotificationStore
import java.text.DateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val records = NotificationStore.all(this)
                Scaffold { padding ->
                    Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
                        Text("WConnect", style = MaterialTheme.typography.headlineLarge)
                        Text("WhatsApp notification history", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }) {
                            Text("Enable Notification Access")
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Recent records (${records.size})", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        if (records.isEmpty()) {
                            Text("No records yet. Enable notification access, then receive a WhatsApp notification.")
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(records) { record -> RecordCard(record) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordCard(record: NotificationRecord) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(record.sender.ifBlank { record.app }, style = MaterialTheme.typography.titleMedium)
                Text(record.app, style = MaterialTheme.typography.labelSmall)
            }
            Text(record.message.ifBlank { "Notification received" }, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text(record.category + " · " + DateFormat.getDateTimeInstance().format(Date(record.receivedAt)), style = MaterialTheme.typography.labelSmall)
        }
    }
}
