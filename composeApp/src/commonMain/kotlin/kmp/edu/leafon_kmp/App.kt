package kmp.edu.leafon_kmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import kmp.edu.leafon_kmp.core.auth.AuthRepository
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
    val authRepository = AppDependencies.authRepository
    val smartPotRepository = AppDependencies.smartPotRepository
    val repositorioRemoto = AppDependencies.repositorioRemoto
    val potListViewModel = remember {
        PotListViewModel(smartPotRepository = smartPotRepository)
    }
    var profileViewModel by remember { mutableStateOf<ProfileViewModel?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            potListViewModel.onCleared()
            profileViewModel?.onCleared()
            profileViewModel = null
        }
    }

    LaunchedEffect(authRepository) {
        restoreSessionIfPossible(
            authRepository = authRepository,
            navigator = navigator,
            backStack = backStack,
        )
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
                        val loginViewModel = remember {
                            LoginViewModel(
                                authRepository = AppDependencies.authRepository,
                                onLoginSuccess = navigator::goToHome,
                            )
                        }

                        DisposableEffect(loginViewModel) {
                            onDispose { loginViewModel.onCleared() }
                        }

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
                            },
                            onCreateAccountClick = navigator::goToRegister,
                        )
                    }

                    AppDestination.Register -> NavEntry(destination) {
                        val registerViewModel = remember {
                            RegisterViewModel(
                                authRepository = AppDependencies.authRepository,
                                apiClient = AppDependencies.apiClient,
                                onRegisterSuccess = navigator::goToLogin,
                            )
                        }

                        DisposableEffect(registerViewModel) {
                            onDispose { registerViewModel.onCleared() }
                        }

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
                            onHomeClick = navigator::goToHome,
                            onAlertsClick = navigator::goToAlerts,
                            onProfileClick = navigator::goToProfile,
                        )
                    }

                    is AppDestination.PotDetail -> NavEntry(destination) {
                        val potId = destination.potId

                        PotDetailScreen(
                            potId = potId,
                            smartPotRepository = smartPotRepository,
                            onBackClick = navigator::goBackOrPots,
                            onEditClick = navigator::goToEditPot,
                            onDeleteSuccess = {
                                potListViewModel.onAction(PotListAction.OnRefresh)
                                navigator.goToPots()
                            },
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
                            smartPotRepository = smartPotRepository,
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
                            smartPotRepository = smartPotRepository,
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
                        val resolvedProfileViewModel = profileViewModel ?: ProfileViewModel(
                            authRepository = AppDependencies.authRepository,
                            apiClient = AppDependencies.apiClient,
                        ).also { createdViewModel ->
                            profileViewModel = createdViewModel
                        }

                        ProfileRouteScreen(
                            viewModel = resolvedProfileViewModel,
                            onHomeClick = navigator::goToHome,
                            onPotsClick = navigator::goToPots,
                            onAlertsClick = navigator::goToAlerts,
                            onLoggedOut = {
                                profileViewModel?.onCleared()
                                profileViewModel = null
                                navigator.goToLogin()
                            },
                        )
                    }
                }
            },
        )
    }
}

private suspend fun restoreSessionIfPossible(
    authRepository: AuthRepository,
    navigator: AppNavigator,
    backStack: List<AppDestination>,
) {
    val session = authRepository.getCurrentSession() ?: return
    val loginIsOnTop = backStack.lastOrNull() == AppDestination.Login

    if (!loginIsOnTop || session.accessToken.isBlank()) {
        return
    }

    navigator.goToHome()
}
