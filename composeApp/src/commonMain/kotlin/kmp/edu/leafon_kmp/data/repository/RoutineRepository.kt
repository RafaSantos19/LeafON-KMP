package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.Routine
import kmp.edu.leafon_kmp.core.model.RoutineType

interface RoutineRepository {
    suspend fun listRoutines(): List<Routine>

    suspend fun getRoutineById(id: String): Routine

    suspend fun createRoutine(
        smartPotId: String,
        type: RoutineType,
        name: String,
        scheduledTime: String,
        daysOfWeek: String,
        durationSec: Int,
        active: Boolean,
    ): Routine

    suspend fun updateRoutine(
        id: String,
        smartPotId: String,
        type: RoutineType,
        name: String,
        scheduledTime: String,
        daysOfWeek: String,
        durationSec: Int,
        active: Boolean,
    ): Routine

    suspend fun activateRoutine(id: String): Routine

    suspend fun deactivateRoutine(id: String): Routine

    suspend fun simulateExecution(id: String): Routine

    suspend fun deleteRoutine(id: String)
}
