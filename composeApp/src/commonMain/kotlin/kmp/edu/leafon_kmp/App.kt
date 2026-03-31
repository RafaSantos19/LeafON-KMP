package kmp.edu.leafon_kmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kmp.edu.leafon_kmp.presentation.home.DashboardScreen
import kmp.edu.leafon_kmp.presentation.home.HomeAction
import kmp.edu.leafon_kmp.presentation.home.HomeViewModel
import kmp.edu.leafon_kmp.presentation.login.LoginAction
import kmp.edu.leafon_kmp.presentation.login.LoginScreen
import kmp.edu.leafon_kmp.presentation.login.LoginViewModel
import kmp.edu.leafon_kmp.presentation.navigation.AppRoute
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
                    onLoggedOut = {
                        navController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}
