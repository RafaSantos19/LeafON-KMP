package kmp.edu.leafon_kmp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoginViewModel {

    var state by mutableStateOf<LoginState>(LoginState())
        private set

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> {
                state = state.copy(email = action.email)
            }

            is LoginAction.OnPasswordChange -> {
                state = state.copy(password = action.password)
            }

            LoginAction.OnLoginClick -> {
                login()
            }
        }
    }

    private fun login() {
        state = state.copy(isLoading = true, error = null)
    }
}
