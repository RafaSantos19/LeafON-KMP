package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.Alert

interface AlertRepository {
    suspend fun listAlerts(): List<Alert>

    suspend fun listUnreadAlerts(): List<Alert>

    suspend fun markAsRead(id: String): Alert
}
