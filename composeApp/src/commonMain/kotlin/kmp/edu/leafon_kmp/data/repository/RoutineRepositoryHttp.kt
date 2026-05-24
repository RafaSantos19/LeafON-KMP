package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.Routine
import kmp.edu.leafon_kmp.core.model.RoutineType
import kmp.edu.leafon_kmp.data.mapper.toCreateRequest
import kmp.edu.leafon_kmp.data.mapper.toDomain
import kmp.edu.leafon_kmp.data.mapper.toUpdateRequest
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient

class RoutineRepositoryHttp(
    private val apiClient: LeafOnApiClient,
) : RoutineRepository {
    override suspend fun listRoutines(): List<Routine> =
        apiClient.getRoutines().map { it.toDomain() }

    override suspend fun getRoutineById(id: String): Routine =
        apiClient.getRoutineById(id).toDomain()

    override suspend fun createRoutine(
        smartPotId: String,
        type: RoutineType,
        name: String,
        scheduledTime: String,
        daysOfWeek: String,
        durationSec: Int,
        active: Boolean,
    ): Routine {
        return apiClient.createRoutine(
            Routine(
                id = "",
                smartPotId = smartPotId,
                type = type,
                name = name,
                scheduledTime = scheduledTime,
                daysOfWeek = daysOfWeek,
                durationSec = durationSec,
                active = active,
            ).toCreateRequest()
        ).toDomain()
    }

    override suspend fun updateRoutine(
        id: String,
        smartPotId: String,
        type: RoutineType,
        name: String,
        scheduledTime: String,
        daysOfWeek: String,
        durationSec: Int,
        active: Boolean,
    ): Routine {
        return apiClient.updateRoutine(
            id = id,
            request = Routine(
                id = id,
                smartPotId = smartPotId,
                type = type,
                name = name,
                scheduledTime = scheduledTime,
                daysOfWeek = daysOfWeek,
                durationSec = durationSec,
                active = active,
            ).toUpdateRequest(),
        ).toDomain()
    }

    override suspend fun activateRoutine(id: String): Routine =
        apiClient.activateRoutine(id).toDomain()

    override suspend fun deactivateRoutine(id: String): Routine =
        apiClient.deactivateRoutine(id).toDomain()

    override suspend fun simulateExecution(id: String): Routine =
        apiClient.simulateRoutineExecution(id).toDomain()

    override suspend fun deleteRoutine(id: String) {
        apiClient.deleteRoutine(id)
    }
}
