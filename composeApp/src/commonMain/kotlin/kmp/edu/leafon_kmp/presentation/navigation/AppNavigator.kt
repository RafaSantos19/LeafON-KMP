package kmp.edu.leafon_kmp.presentation.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList

internal class AppNavigator(
    private val backStack: SnapshotStateList<AppDestination>,
) {
    fun goBack(): Boolean {
        if (backStack.size <= 1) return false
        backStack.removeAt(backStack.lastIndex)
        return true
    }

    fun goToLogin() {
        replaceStack(AppDestination.Login)
    }

    fun goToRegister() {
        navigate(AppDestination.Register)
    }

    fun goToHome() {
        replaceStack(AppDestination.Home)
    }

    fun goToPots() {
        replaceStack(AppDestination.Home, AppDestination.Pots)
    }

    fun goToAlerts() {
        replaceStack(AppDestination.Home, AppDestination.Alerts)
    }

    fun goToProfile() {
        replaceStack(AppDestination.Home, AppDestination.Profile)
    }

    fun goToCreatePot() {
        navigate(AppDestination.CreatePot)
    }

    fun goToPotDetail(potId: String) {
        navigate(AppDestination.PotDetail(potId))
    }

    fun goToEditPot(potId: String) {
        navigate(AppDestination.EditPot(potId))
    }

    fun goToPotRoutines(potId: String) {
        navigate(AppDestination.PotRoutines(potId))
    }

    fun goToCreateRoutine(potId: String) {
        navigate(AppDestination.CreateRoutine(potId))
    }

    fun goToPotAlerts(potId: String) {
        if (potId.isBlank()) {
            goToAlerts()
            return
        }

        navigate(AppDestination.PotAlerts(potId))
    }

    fun goBackOrHome() {
        if (!goBack()) goToHome()
    }

    fun goBackOrLogin() {
        if (!goBack()) goToLogin()
    }

    fun goBackOrPots() {
        if (!goBack()) goToPots()
    }

    fun goBackOrPotDetail(potId: String) {
        if (!goBack()) goToPotDetail(potId)
    }

    fun goBackOrPotRoutines(potId: String) {
        if (!goBack()) goToPotRoutines(potId)
    }

    fun finishPotMutation() {
        goToPots()
    }

    fun finishRoutineMutation(potId: String) {
        val routines = AppDestination.PotRoutines(potId)
        if (!popTo(routines)) {
            navigate(routines)
        }
    }

    private fun navigate(destination: AppDestination) {
        if (backStack.lastOrNull() == destination) return
        if (popTo(destination)) return
        backStack.add(destination)
    }

    private fun popTo(destination: AppDestination): Boolean {
        val index = backStack.indexOfLast { it == destination }
        if (index < 0) return false

        while (backStack.lastIndex > index) {
            backStack.removeAt(backStack.lastIndex)
        }

        return true
    }

    private fun replaceStack(vararg destinations: AppDestination) {
        val nextStack = destinations.toList()
        if (backStack == nextStack) return

        backStack.clear()
        backStack.addAll(nextStack)
    }
}
