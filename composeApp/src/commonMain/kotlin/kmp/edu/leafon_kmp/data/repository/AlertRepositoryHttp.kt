package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.Alert
import kmp.edu.leafon_kmp.data.mapper.toDomain
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient

class AlertRepositoryHttp(
    private val apiClient: LeafOnApiClient,
) : AlertRepository {
    override suspend fun listAlerts(): List<Alert> =
        apiClient.getAlerts().map { it.toDomain() }

    override suspend fun listUnreadAlerts(): List<Alert> =
        apiClient.getUnreadAlerts().map { it.toDomain() }

    override suspend fun markAsRead(id: String): Alert =
        apiClient.markAlertAsRead(id).toDomain()
}
