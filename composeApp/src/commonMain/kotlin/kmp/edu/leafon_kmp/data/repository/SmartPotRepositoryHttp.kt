package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.data.mapper.toCreateRequest
import kmp.edu.leafon_kmp.data.mapper.toDomain
import kmp.edu.leafon_kmp.data.mapper.toUpdateRequest
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient

class SmartPotRepositoryHttp(
    private val apiClient: LeafOnApiClient,
) : SmartPotRepository {

    override suspend fun listSmartPots(): List<SmartPot> {
        return apiClient.getSmartPots().map { it.toDomain() }
    }

    override suspend fun getSmartPotById(id: String): SmartPot {
        return apiClient.getSmartPotById(id).toDomain()
    }

    override suspend fun createSmartPot(
        plantName: String,
        humidityMin: Int,
        deviceId: String?,
    ): SmartPot {
        val draft = SmartPot(
            id = "",
            plantName = plantName,
            humidityMin = humidityMin,
            deviceId = deviceId,
        )

        return apiClient.createSmartPot(draft.toCreateRequest()).toDomain()
    }

    override suspend fun updateSmartPot(
        id: String,
        plantName: String,
        humidityMin: Int,
        deviceId: String?,
    ): SmartPot {
        val draft = SmartPot(
            id = id,
            plantName = plantName,
            humidityMin = humidityMin,
            deviceId = deviceId,
        )

        return apiClient.updateSmartPot(id, draft.toUpdateRequest()).toDomain()
    }

    override suspend fun deleteSmartPot(id: String) {
        apiClient.deleteSmartPot(id)
    }
}
