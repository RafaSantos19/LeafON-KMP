package kmp.edu.leafon_kmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria
import kmp.edu.leafon_kmp.presentation.home.DashboardScreen
import kmp.edu.leafon_kmp.presentation.home.HomeViewModel
import kmp.edu.leafon_kmp.presentation.login.LoginAction
import kmp.edu.leafon_kmp.presentation.login.LoginScreen
import kmp.edu.leafon_kmp.presentation.login.LoginViewModel
import kmp.edu.leafon_kmp.presentation.navigation.AppDestination
import kmp.edu.leafon_kmp.presentation.navigation.AppNavigator
import kmp.edu.leafon_kmp.presentation.pots.PotListAction
import kmp.edu.leafon_kmp.presentation.pots.PotListScreen
import kmp.edu.leafon_kmp.presentation.pots.PotListViewModel
import kmp.edu.leafon_kmp.presentation.pots.alerts.list.AlertListScreen
import kmp.edu.leafon_kmp.presentation.pots.create.CreatePotScreen
import kmp.edu.leafon_kmp.presentation.pots.detail.PotDetailScreen
import kmp.edu.leafon_kmp.presentation.pots.edit.EditPotScreen
import kmp.edu.leafon_kmp.presentation.pots.routines.create.CreateRoutineScreen
import kmp.edu.leafon_kmp.presentation.pots.routines.list.RoutineListScreen
import kmp.edu.leafon_kmp.presentation.profile.ProfileRouteScreen
import kmp.edu.leafon_kmp.presentation.profile.ProfileViewModel
import kmp.edu.leafon_kmp.presentation.register.RegisterAction
import kmp.edu.leafon_kmp.presentation.register.RegisterScreen
import kmp.edu.leafon_kmp.presentation.register.RegisterViewModel

@Composable
@Preview
fun App() {
    val backStack = remember { mutableStateListOf<AppDestination>(AppDestination.Login) }
    val navigator = remember(backStack) { AppNavigator(backStack) }
    val repositorioRemoto = remember { RepositorioRemotoEmMemoria() }
    val potListViewModel = remember(repositorioRemoto) {
        PotListViewModel(repositorio = repositorioRemoto)
    }

    MaterialTheme {
        // Fases 2-4: Navigation3 renders the typed app destination stack.
        NavDisplay(
            backStack = backStack,
            onBack = {
                navigator.goBack()
            },
            entryProvider = { destination ->
                when (destination) {
                    AppDestination.Login -> NavEntry(destination) {
                        val loginViewModel = remember { LoginViewModel() }

                        LoginScreen(
                            state = loginViewModel.state,
                            onEmailChange = {
                                loginViewModel.onAction(LoginAction.OnEmailChange(it))
                            },
                            onPasswordChange = {
                                loginViewModel.onAction(LoginAction.OnPasswordChange(it))
                            },
                            onLoginClick = {
                                loginViewModel.onAction(LoginAction.OnLoginClick)
                                navigator.goToHome()
                            },
                            onCreateAccountClick = navigator::goToRegister,
                        )
                    }

                    AppDestination.Register -> NavEntry(destination) {
                        val registerViewModel = remember { RegisterViewModel() }

                        RegisterScreen(
                            state = registerViewModel.state,
                            onNameChange = {
                                registerViewModel.onAction(RegisterAction.OnNameChange(it))
                            },
                            onEmailChange = {
                                registerViewModel.onAction(RegisterAction.OnEmailChange(it))
                            },
                            onPhoneChange = {
                                registerViewModel.onAction(RegisterAction.OnPhoneChange(it))
                            },
                            onPasswordChange = {
                                registerViewModel.onAction(RegisterAction.OnPasswordChange(it))
                            },
                            onConfirmPasswordChange = {
                                registerViewModel.onAction(RegisterAction.OnConfirmPasswordChange(it))
                            },
                            onRegisterClick = {
                                registerViewModel.onAction(RegisterAction.OnRegisterClick)
                                navigator.goToHome()
                            },
                            onBackToLoginClick = navigator::goBackOrLogin,
                        )
                    }

                    AppDestination.Home -> NavEntry(destination) {
                        val homeViewModel = remember { HomeViewModel() }

                        DashboardScreen(
                            state = homeViewModel.state,
                            onAction = homeViewModel::onAction,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = navigator::goToAlerts,
                            onProfileClick = navigator::goToProfile,
                            onLoggedOut = navigator::goToLogin,
                        )
                    }

                    AppDestination.Pots -> NavEntry(destination) {
                        PotListScreen(
                            viewModel = potListViewModel,
                            onAddPotClick = navigator::goToCreatePot,
                            onNavigateToPotDetail = navigator::goToPotDetail,
                            onNavigateToEditPot = navigator::goToEditPot,
                            onDeletePot = {
                                potListViewModel.onAction(PotListAction.OnRefresh)
                            },
                            onHomeClick = navigator::goToHome,
                            onAlertsClick = navigator::goToAlerts,
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    is AppDestination.PotDetail -> NavEntry(destination) {
                        val potId = destination.potId

                        PotDetailScreen(
                            potId = potId,
                            repositorio = repositorioRemoto,
                            onBackClick = navigator::goBackOrPots,
                            onEditClick = navigator::goToEditPot,
                            onViewRoutinesClick = navigator::goToPotRoutines,
                            onViewAlertsClick = navigator::goToPotAlerts,
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = {
                                navigator.goToPotAlerts(potId)
                            },
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    AppDestination.Alerts -> NavEntry(destination) {
                        AlertListScreen(
                            potId = "1",
                            onBackClick = navigator::goBackOrHome,
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = {},
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    is AppDestination.PotAlerts -> NavEntry(destination) {
                        val potId = destination.potId

                        AlertListScreen(
                            potId = potId,
                            onBackClick = {
                                navigator.goBackOrPotDetail(potId)
                            },
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = {},
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    is AppDestination.PotRoutines -> NavEntry(destination) {
                        val potId = destination.potId

                        RoutineListScreen(
                            potId = potId,
                            repositorio = repositorioRemoto,
                            onBackClick = {
                                navigator.goBackOrPotDetail(potId)
                            },
                            onCreateRoutineClick = navigator::goToCreateRoutine,
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = {
                                navigator.goToPotAlerts(potId)
                            },
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    is AppDestination.CreateRoutine -> NavEntry(destination) {
                        val potId = destination.potId

                        CreateRoutineScreen(
                            potId = potId,
                            repositorio = repositorioRemoto,
                            onBackClick = {
                                navigator.goBackOrPotRoutines(potId)
                            },
                            onRoutineCreated = {
                                navigator.finishRoutineMutation(potId)
                            },
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = {
                                navigator.goToPotAlerts(potId)
                            },
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    is AppDestination.EditPot -> NavEntry(destination) {
                        val potId = destination.potId

                        EditPotScreen(
                            potId = potId,
                            repositorio = repositorioRemoto,
                            onBackClick = navigator::goBackOrPots,
                            onPotUpdated = {
                                potListViewModel.onAction(PotListAction.OnRefresh)
                                navigator.finishPotMutation()
                            },
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = {
                                navigator.goToPotAlerts(potId)
                            },
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    AppDestination.CreatePot -> NavEntry(destination) {
                        CreatePotScreen(
                            repositorio = repositorioRemoto,
                            onBackClick = navigator::goBackOrPots,
                            onPotCreated = {
                                potListViewModel.onAction(PotListAction.OnRefresh)
                                navigator.finishPotMutation()
                            },
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = navigator::goToAlerts,
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    AppDestination.Profile -> NavEntry(destination) {
                        val profileViewModel = remember { ProfileViewModel() }

                        ProfileRouteScreen(
                            viewModel = profileViewModel,
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = navigator::goToAlerts,
                            onLoggedOut = navigator::goToLogin,
                        )
                    }
                }
            },
        )
    }
}
