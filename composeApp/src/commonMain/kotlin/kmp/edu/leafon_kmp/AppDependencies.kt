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
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryHttp
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryMemory

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
    val smartPotRepositoryMemory: SmartPotRepository by lazy { SmartPotRepositoryMemory() }
    val smartPotRepositoryHttp: SmartPotRepository by lazy {
        SmartPotRepositoryHttp(
            apiClient = apiClient,
        )
    }
    val remotoEmMemoria: RepositorioRemoto by lazy { RepositorioRemotoEmMemoria() }
    val remotoHttp: RepositorioRemoto by lazy {
        RepositorioRemotoHttp(
            smartPotRepository = smartPotRepositoryHttp,
            fallback = remotoEmMemoria,
        )
    }

    var usarRepositorioHttp: Boolean = true

    val smartPotRepository: SmartPotRepository
        get() = if (usarRepositorioHttp) smartPotRepositoryHttp else smartPotRepositoryMemory

    val repositorioRemoto: RepositorioRemoto
        get() = if (usarRepositorioHttp) remotoHttp else remotoEmMemoria

    fun atualizarBaseUrl(baseUrl: String) {
        apiConfig.baseUrl = baseUrl
    }
}
