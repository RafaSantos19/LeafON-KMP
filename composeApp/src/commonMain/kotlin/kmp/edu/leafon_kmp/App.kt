package kmp.edu.leafon_kmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.read
import kmp.edu.leafon_kmp.presentation.home.DashboardScreen
import kmp.edu.leafon_kmp.presentation.home.HomeAction
import kmp.edu.leafon_kmp.presentation.home.HomeViewModel
import kmp.edu.leafon_kmp.presentation.login.LoginAction
import kmp.edu.leafon_kmp.presentation.login.LoginScreen
import kmp.edu.leafon_kmp.presentation.login.LoginViewModel
import kmp.edu.leafon_kmp.presentation.navigation.AppRoute
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
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = AppRoute.Login.route
        ) {
            composable(AppRoute.Login.route) {
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
                        navController.navigate(AppRoute.Home.route) {
                            popUpTo(AppRoute.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onCreateAccountClick = {
                        navController.navigate(AppRoute.Register.route)
                    }
                )
            }

            composable(AppRoute.Register.route) {
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
                        navController.navigate(AppRoute.Home.route) {
                            popUpTo(AppRoute.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onBackToLoginClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(AppRoute.Home.route) {
                val homeViewModel = remember { HomeViewModel() }

                DashboardScreen(
                    state = homeViewModel.state,
                    onAction = homeViewModel::onAction,
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route)
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.Alerts.route)
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                    onLoggedOut = {
                        navController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(AppRoute.Pots.route) {
                val potListViewModel = remember { PotListViewModel() }

                PotListScreen(
                    viewModel = potListViewModel,
                    onAddPotClick = {
                        navController.navigate(AppRoute.CreatePot.route)
                    },
                    onNavigateToPotDetail = { potId ->
                        navController.navigate(AppRoute.PotDetail.createRoute(potId))
                    },
                    onNavigateToEditPot = { potId ->
                        navController.navigate(AppRoute.EditPot.createRoute(potId))
                    },
                    onDeletePot = {
                        // Delete confirmation/backend integration will be wired in a later phase.
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            popUpTo(AppRoute.Home.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.Alerts.route)
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(
                route = AppRoute.PotDetail.route,
                arguments = listOf(
                    navArgument(AppRoute.PotDetail.ARG_POT_ID) {
                        type = NavType.StringType
                    }
                ),
            ) { backStackEntry ->
                val potId = backStackEntry.arguments?.read {
                    getStringOrNull(AppRoute.PotDetail.ARG_POT_ID)
                }

                PotDetailScreen(
                    potId = potId.orEmpty(),
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.Pots.route)
                        }
                    },
                    onEditClick = { selectedPotId ->
                        navController.navigate(AppRoute.EditPot.createRoute(selectedPotId))
                    },
                    onViewRoutinesClick = { selectedPotId ->
                        navController.navigate(AppRoute.PotRoutines.createRoute(selectedPotId))
                    },
                    onViewAlertsClick = { selectedPotId ->
                        navController.navigate(AppRoute.PotAlerts.createRoute(selectedPotId))
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        if (!navController.popBackStack(AppRoute.Pots.route, inclusive = false)) {
                            navController.navigate(AppRoute.Pots.route)
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.PotAlerts.createRoute(potId.orEmpty()))
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(AppRoute.Alerts.route) {
                AlertListScreen(
                    potId = "1",
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.Home.route)
                        }
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {},
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(
                route = AppRoute.PotAlerts.route,
                arguments = listOf(
                    navArgument(AppRoute.PotAlerts.ARG_POT_ID) {
                        type = NavType.StringType
                    }
                ),
            ) { backStackEntry ->
                val potId = backStackEntry.arguments?.read {
                    getStringOrNull(AppRoute.PotAlerts.ARG_POT_ID)
                }.orEmpty()

                AlertListScreen(
                    potId = potId,
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.PotDetail.createRoute(potId))
                        }
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {},
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(
                route = AppRoute.PotRoutines.route,
                arguments = listOf(
                    navArgument(AppRoute.PotRoutines.ARG_POT_ID) {
                        type = NavType.StringType
                    }
                ),
            ) { backStackEntry ->
                val potId = backStackEntry.arguments?.read {
                    getStringOrNull(AppRoute.PotRoutines.ARG_POT_ID)
                }.orEmpty()

                RoutineListScreen(
                    potId = potId,
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.PotDetail.createRoute(potId))
                        }
                    },
                    onCreateRoutineClick = { selectedPotId ->
                        navController.navigate(AppRoute.CreateRoutine.createRoute(selectedPotId))
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.PotAlerts.createRoute(potId))
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(
                route = AppRoute.CreateRoutine.route,
                arguments = listOf(
                    navArgument(AppRoute.CreateRoutine.ARG_POT_ID) {
                        type = NavType.StringType
                    }
                ),
            ) { backStackEntry ->
                val potId = backStackEntry.arguments?.read {
                    getStringOrNull(AppRoute.CreateRoutine.ARG_POT_ID)
                }.orEmpty()

                CreateRoutineScreen(
                    potId = potId,
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.PotRoutines.createRoute(potId))
                        }
                    },
                    onRoutineCreated = {
                        if (!navController.popBackStack(AppRoute.PotRoutines.createRoute(potId), inclusive = false)) {
                            navController.navigate(AppRoute.PotRoutines.createRoute(potId))
                        }
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.PotAlerts.createRoute(potId))
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(
                route = AppRoute.EditPot.route,
                arguments = listOf(
                    navArgument(AppRoute.EditPot.ARG_POT_ID) {
                        type = NavType.StringType
                    }
                ),
            ) { backStackEntry ->
                val potId = backStackEntry.arguments?.read {
                    getStringOrNull(AppRoute.EditPot.ARG_POT_ID)
                }

                EditPotScreen(
                    potId = potId.orEmpty(),
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.Pots.route)
                        }
                    },
                    onPotUpdated = {
                        if (!navController.popBackStack(AppRoute.Pots.route, inclusive = false)) {
                            navController.navigate(AppRoute.Pots.route)
                        }
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.PotAlerts.createRoute(potId.orEmpty()))
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(AppRoute.CreatePot.route) {
                CreatePotScreen(
                    onBackClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(AppRoute.Pots.route)
                        }
                    },
                    onPotCreated = {
                        if (!navController.popBackStack(AppRoute.Pots.route, inclusive = false)) {
                            navController.navigate(AppRoute.Pots.route)
                        }
                    },
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.Alerts.route)
                    },
                    onProfileClick = {
                        navController.navigate(AppRoute.Profile.route)
                    },
                )
            }

            composable(AppRoute.Profile.route) {
                val profileViewModel = remember { ProfileViewModel() }

                ProfileRouteScreen(
                    viewModel = profileViewModel,
                    onHomeClick = {
                        navController.navigate(AppRoute.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    onPotsClick = {
                        navController.navigate(AppRoute.Pots.route) {
                            launchSingleTop = true
                        }
                    },
                    onAlertsClick = {
                        navController.navigate(AppRoute.Alerts.route) {
                            launchSingleTop = true
                        }
                    },
                    onLoggedOut = {
                        navController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Profile.route) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
        }
    }
}
