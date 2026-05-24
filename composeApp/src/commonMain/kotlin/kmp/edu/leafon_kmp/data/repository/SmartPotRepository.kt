package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.SmartPot

interface SmartPotRepository {
    suspend fun listSmartPots(): List<SmartPot>

    suspend fun getSmartPotById(id: String): SmartPot

    suspend fun createSmartPot(
        plantName: String,
        humidityMin: Int,
        deviceId: String?,
    ): SmartPot

    suspend fun updateSmartPot(
        id: String,
        plantName: String,
        humidityMin: Int,
        deviceId: String?,
    ): SmartPot

    suspend fun deleteSmartPot(id: String)
}
