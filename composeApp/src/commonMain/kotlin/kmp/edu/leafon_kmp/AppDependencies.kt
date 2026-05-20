package kmp.edu.leafon_kmp

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import kmp.edu.leafon_kmp.core.auth.AuthRepository
import kmp.edu.leafon_kmp.core.auth.SupabaseConfig
import kmp.edu.leafon_kmp.core.network.ApiConfig
import kmp.edu.leafon_kmp.core.network.HttpClientFactory
import kmp.edu.leafon_kmp.core.network.InMemoryAuthTokenProvider
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria
import kmp.edu.leafon_kmp.data.RepositorioRemotoHttp
import kmp.edu.leafon_kmp.data.auth.SupabaseAuthRepository
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient

object AppDependencies {
    val apiConfig = ApiConfig()
    val tokenProvider = InMemoryAuthTokenProvider()
    val supabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SupabaseConfig.projectUrl,
            supabaseKey = SupabaseConfig.anonKey,
        ) {
            install(Auth)
        }
    }
    val authRepository: AuthRepository by lazy {
        SupabaseAuthRepository(
            supabaseClient = supabaseClient,
            tokenProvider = tokenProvider,
        )
    }
    val httpClient by lazy {
        HttpClientFactory.create(
            tokenProvider = tokenProvider,
            enableLogging = false,
        )
    }
    val apiClient by lazy {
        LeafOnApiClient(
            httpClient = httpClient,
            apiConfig = apiConfig,
        )
    }
    val remotoEmMemoria: RepositorioRemoto by lazy { RepositorioRemotoEmMemoria() }
    val remotoHttp: RepositorioRemoto by lazy {
        RepositorioRemotoHttp(
            apiClient = apiClient,
            fallback = remotoEmMemoria,
        )
    }

    var usarRepositorioHttp: Boolean = false

    val repositorioRemoto: RepositorioRemoto
        get() = if (usarRepositorioHttp) remotoHttp else remotoEmMemoria

    fun atualizarBaseUrl(baseUrl: String) {
        apiConfig.baseUrl = baseUrl
    }
}
