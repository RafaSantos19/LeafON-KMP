package kmp.edu.leafon_kmp.core.network

data class ApiConfig(
    var baseUrl: String = defaultApiBaseUrl(),
) {
    val normalizedBaseUrl: String
        get() = baseUrl.trimEnd('/')
}

expect fun defaultApiBaseUrl(): String
