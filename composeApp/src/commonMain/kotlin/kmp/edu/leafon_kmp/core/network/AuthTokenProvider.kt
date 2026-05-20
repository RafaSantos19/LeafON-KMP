package kmp.edu.leafon_kmp.core.network

import kotlinx.coroutines.flow.MutableStateFlow

interface AuthTokenProvider {
    suspend fun getAccessToken(): String?
}

interface MutableAuthTokenProvider : AuthTokenProvider {
    fun setAccessToken(value: String?)
}

class InMemoryAuthTokenProvider(
    initialToken: String? = null,
) : MutableAuthTokenProvider {
    private val tokenState = MutableStateFlow(initialToken)

    fun setToken(value: String?) {
        setAccessToken(value)
    }

    override fun setAccessToken(value: String?) {
        tokenState.value = value
    }

    override suspend fun getAccessToken(): String? = tokenState.value
}
