package kmp.edu.leafon_kmp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.auth.AuthRepository
import kmp.edu.leafon_kmp.core.auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val onLoginSuccess: () -> Unit,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf<LoginState>(LoginState())
        private set

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> {
                state = state.copy(email = action.email, error = null)
            }

            is LoginAction.OnPasswordChange -> {
                state = state.copy(password = action.password, error = null)
            }

            LoginAction.OnLoginClick -> {
                login()
            }
        }
    }

    private fun login() {
        val email = state.email.trim()
        val password = state.password

        when {
            email.isBlank() -> {
                state = state.copy(error = "Informe seu email.")
                return
            }

            password.isBlank() -> {
                state = state.copy(error = "Informe sua senha.")
                return
            }
        }

        state = state.copy(isLoading = true, error = null, email = email)

        scope.launch {
            val result = authRepository.signIn(
                email = email,
                password = password,
            )

            when (result) {
                is AuthResult.Success -> {
                    state = state.copy(isLoading = false, error = null)
                    onLoginSuccess()
                }

                is AuthResult.EmailConfirmationRequired -> {
                    state = state.copy(
                        isLoading = false,
                        error = "Confirme seu email antes de entrar.",
                    )
                }

                is AuthResult.Error -> {
                    state = state.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}
