package kmp.edu.leafon_kmp.presentation.navigation

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object Register : AppRoute("register")
    data object Home : AppRoute("home")
    data object Pots : AppRoute("pots")
    data object CreatePot : AppRoute("pots/create")
    data object EditPot : AppRoute("pots/edit/{potId}") {
        const val ARG_POT_ID = "potId"

        fun createRoute(potId: String) = "pots/edit/$potId"
    }
    data object Profile : AppRoute("profile")
}
