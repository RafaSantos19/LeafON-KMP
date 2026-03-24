package kmp.edu.leafon_kmp.presentation.login

sealed class LoginAction {
    data class OnEmailChange(val email: String) : LoginAction()
    data class OnPasswordChange(val password: String) : LoginAction()
    object OnLoginClick : LoginAction()
}
