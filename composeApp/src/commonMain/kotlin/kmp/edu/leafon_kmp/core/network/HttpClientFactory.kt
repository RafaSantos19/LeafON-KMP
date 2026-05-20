package kmp.edu.leafon_kmp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(
        tokenProvider: AuthTokenProvider,
        enableLogging: Boolean = false,
    ): HttpClient {
        return HttpClient {
            expectSuccess = false

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        explicitNulls = false
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 10_000
            }

            if (enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    level = LogLevel.HEADERS
                }
            }

            defaultRequest {
                headers.append(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString(),
                )
                headers.append(
                    HttpHeaders.Accept,
                    ContentType.Application.Json.toString(),
                )
            }

            install(AuthTokenPlugin) {
                this.tokenProvider = tokenProvider
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val errorBody = runCatching { response.bodyAsText() }.getOrDefault("")
                        val message = errorBody.ifBlank {
                            "HTTP ${response.status.value} ao acessar ${response.call.request.url}"
                        }
                        throw ApiException(response.status.value, message)
                    }
                }

                handleResponseExceptionWithRequest { cause, _ ->
                    if (cause is SerializationException) {
                        throw ApiException(
                            statusCode = -1,
                            message = "Falha ao serializar ou desserializar a resposta da API.",
                        )
                    }
                }
            }
        }
    }
}

private class AuthTokenPluginConfig {
    lateinit var tokenProvider: AuthTokenProvider
}

private val AuthTokenPlugin = createClientPlugin(
    name = "AuthTokenPlugin",
    createConfiguration = ::AuthTokenPluginConfig,
) {
    val tokenProvider = pluginConfig.tokenProvider

    onRequest { request, _ ->
        val token = tokenProvider.getAccessToken()

        if (!token.isNullOrBlank()) {
            request.headers.remove(HttpHeaders.Authorization)
            request.headers.append(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}
