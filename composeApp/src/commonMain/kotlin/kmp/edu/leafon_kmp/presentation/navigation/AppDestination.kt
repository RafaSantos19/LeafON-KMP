package kmp.edu.leafon_kmp.presentation.navigation

sealed interface AppDestination {
    data object Login : AppDestination
    data object Register : AppDestination
    data object Home : AppDestination
    data object Pots : AppDestination
    data object Alerts : AppDestination
    data object CreatePot : AppDestination
    data object Profile : AppDestination

    data class PotDetail(val potId: String) : AppDestination
    data class EditPot(val potId: String) : AppDestination
    data class PotRoutines(val potId: String) : AppDestination
    data class CreateRoutine(val potId: String) : AppDestination
    data class PotAlerts(val potId: String) : AppDestination
}
