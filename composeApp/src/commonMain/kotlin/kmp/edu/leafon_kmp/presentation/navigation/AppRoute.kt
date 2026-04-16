package kmp.edu.leafon_kmp.presentation.navigation

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object Register : AppRoute("register")
    data object Home : AppRoute("home")
    data object Alerts : AppRoute("alerts")
    data object Pots : AppRoute("pots")
    data object CreatePot : AppRoute("pots/create")
    data object PotDetail : AppRoute("pots/detail/{potId}") {
        const val ARG_POT_ID = "potId"

        fun createRoute(potId: String) = "pots/detail/$potId"
    }
    data object PotRoutines : AppRoute("pots/{potId}/routines") {
        const val ARG_POT_ID = "potId"

        fun createRoute(potId: String) = "pots/$potId/routines"
    }
    data object CreateRoutine : AppRoute("pots/{potId}/routines/create") {
        const val ARG_POT_ID = "potId"

        fun createRoute(potId: String) = "pots/$potId/routines/create"
    }
    data object PotAlerts : AppRoute("pots/{potId}/alerts") {
        const val ARG_POT_ID = "potId"

        fun createRoute(potId: String) = "pots/$potId/alerts"
    }
    data object EditPot : AppRoute("pots/edit/{potId}") {
        const val ARG_POT_ID = "potId"

        fun createRoute(potId: String) = "pots/edit/$potId"
    }
    data object Profile : AppRoute("profile")
}
