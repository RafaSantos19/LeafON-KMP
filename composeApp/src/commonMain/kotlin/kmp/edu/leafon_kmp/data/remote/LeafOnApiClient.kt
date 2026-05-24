package kmp.edu.leafon_kmp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kmp.edu.leafon_kmp.core.network.ApiConfig
import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.CreateSmartPotRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.CreateTelemetryRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.CreateUserRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.SmartPotResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.TelemetryResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.UpdateSmartPotRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.UpdateUserRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.UserResponseDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json

class LeafOnApiClient(
    private val httpClient: HttpClient,
    private val apiConfig: ApiConfig,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    suspend fun getMe(): UserResponseDto {
        val requestUrl = url("/users/me")
        println("LeafOnApiClient.getMe -> start url=$requestUrl")

        val response = try {
            withTimeout(15_000) {
                httpClient.get(requestUrl)
            }.also {
                println("LeafOnApiClient.getMe -> response headers received status=${it.status}")
            }
        } catch (exception: TimeoutCancellationException) {
            println("LeafOnApiClient.getMe -> timeout: ${exception.message}")
            throw exception
        } catch (exception: CancellationException) {
            println("LeafOnApiClient.getMe -> cancelled: ${exception.message}")
            throw exception
        } catch (exception: HttpRequestTimeoutException) {
            println("LeafOnApiClient.getMe -> timeout: ${exception.message}")
            throw exception
        } catch (exception: ResponseException) {
            println(
                "LeafOnApiClient.getMe -> response exception status=${exception.response.status} " +
                    "message=${exception.message}"
            )
            throw exception
        } catch (exception: Throwable) {
            println(
                "LeafOnApiClient.getMe -> request failed: ${exception::class.simpleName} " +
                    "message=${exception.message}"
            )
            throw exception
        }

        val rawBody = try {
            response.bodyAsText().also {
                println("LeafOnApiClient.getMe -> rawBody=$it")
            }
        } catch (exception: Throwable) {
            println(
                "LeafOnApiClient.getMe -> body read failed: ${exception::class.simpleName} " +
                    "message=${exception.message}"
            )
            throw exception
        }

        if (!response.status.isSuccess()) {
            throw ApiException(
                statusCode = response.status.value,
                message = rawBody.ifBlank { "Erro ao buscar perfil" },
            )
        }

        return try {
            json.decodeFromString<UserResponseDto>(rawBody).also { decoded ->
                println("LeafOnApiClient.getMe -> result=$decoded")
            }
        } catch (exception: Exception) {
            println("LeafOnApiClient.getMe -> decode error=${exception.message}")
            throw exception
        }
    }

    suspend fun createUser(request: CreateUserRequestDto): UserResponseDto {
        return httpClient.post(url("/users")) {
            setBody(request)
        }.body()
    }

    suspend fun updateMe(request: UpdateUserRequestDto): UserResponseDto {
        return httpClient.put(url("/users/me")) {
            setBody(request)
        }.body()
    }

    suspend fun getSmartPots(): List<SmartPotResponseDto> {
        return requestJson(
            method = "GET",
            path = "/smart-pots",
            execute = { httpClient.get(url("/smart-pots")) },
            decode = { rawBody -> json.decodeFromString<List<SmartPotResponseDto>>(rawBody) },
        )
    }

    suspend fun getSmartPotById(id: String): SmartPotResponseDto {
        return requestJson(
            method = "GET",
            path = "/smart-pots/$id",
            execute = { httpClient.get(url("/smart-pots/$id")) },
            decode = { rawBody -> json.decodeFromString<SmartPotResponseDto>(rawBody) },
        )
    }

    suspend fun createSmartPot(request: CreateSmartPotRequestDto): SmartPotResponseDto {
        return requestJson(
            method = "POST",
            path = "/smart-pots",
            execute = {
                httpClient.post(url("/smart-pots")) {
                    setBody(request)
                }
            },
            decode = { rawBody -> json.decodeFromString<SmartPotResponseDto>(rawBody) },
        )
    }

    suspend fun updateSmartPot(
        id: String,
        request: UpdateSmartPotRequestDto,
    ): SmartPotResponseDto {
        return requestJson(
            method = "PUT",
            path = "/smart-pots/$id",
            execute = {
                httpClient.put(url("/smart-pots/$id")) {
                    setBody(request)
                }
            },
            decode = { rawBody -> json.decodeFromString<SmartPotResponseDto>(rawBody) },
        )
    }

    suspend fun deleteSmartPot(id: String) {
        requestUnit(
            method = "DELETE",
            path = "/smart-pots/$id",
            execute = { httpClient.delete(url("/smart-pots/$id")) },
        )
    }

    suspend fun createTelemetry(request: CreateTelemetryRequestDto): TelemetryResponseDto {
        return httpClient.post(url("/telemetry")) {
            setBody(request)
        }.body()
    }

    suspend fun getTelemetry(smartPotId: String): List<TelemetryResponseDto> {
        return httpClient.get(url("/telemetry")) {
            parameter("smartPotId", smartPotId)
        }.body()
    }

    suspend fun getLatestTelemetry(smartPotId: String): TelemetryResponseDto? {
        return try {
            httpClient.get(url("/telemetry/latest")) {
                parameter("smartPotId", smartPotId)
            }.body()
        } catch (exception: ApiException) {
            if (exception.statusCode == 404) {
                null
            } else {
                throw exception
            }
        }
    }

    private fun url(path: String): String {
        return "${apiConfig.normalizedBaseUrl}$path"
    }

    private suspend fun <T> requestJson(
        method: String,
        path: String,
        execute: suspend () -> HttpResponse,
        decode: (String) -> T,
    ): T {
        return try {
            val response = execute()
            val rawBody = response.bodyAsText()

            println(
                "LeafOnApiClient.request -> method=$method endpoint=$path " +
                    "status=${response.status.value} bodyLength=${rawBody.length}"
            )

            if (!response.status.isSuccess()) {
                throw ApiException(
                    statusCode = response.status.value,
                    message = rawBody.ifBlank { "Erro ao acessar $path" },
                )
            }

            if (rawBody.isBlank()) {
                throw ApiException(
                    statusCode = -1,
                    message = "Resposta vazia ao acessar $path.",
                )
            }

            decode(rawBody)
        } catch (exception: CancellationException) {
            println("LeafOnApiClient.request -> cancelled method=$method endpoint=$path")
            throw exception
        } catch (exception: HttpRequestTimeoutException) {
            println("LeafOnApiClient.request -> timeout method=$method endpoint=$path")
            throw exception
        } catch (exception: ApiException) {
            println(
                "LeafOnApiClient.request -> error method=$method endpoint=$path " +
                    "status=${exception.statusCode} bodyLength=${exception.message?.length ?: 0}"
            )
            throw exception
        } catch (exception: ResponseException) {
            println(
                "LeafOnApiClient.request -> response exception method=$method endpoint=$path " +
                    "status=${exception.response.status.value} message=${exception.message}"
            )
            throw exception
        } catch (exception: Throwable) {
            println(
                "LeafOnApiClient.request -> failure method=$method endpoint=$path " +
                    "type=${exception::class.simpleName} message=${exception.message}"
            )
            throw exception
        }
    }

    private suspend fun requestUnit(
        method: String,
        path: String,
        execute: suspend () -> HttpResponse,
    ) {
        try {
            val response = execute()
            val rawBody = runCatching { response.bodyAsText() }.getOrDefault("")

            println(
                "LeafOnApiClient.request -> method=$method endpoint=$path " +
                    "status=${response.status.value} bodyLength=${rawBody.length}"
            )

            if (!response.status.isSuccess()) {
                throw ApiException(
                    statusCode = response.status.value,
                    message = rawBody.ifBlank { "Erro ao acessar $path" },
                )
            }
        } catch (exception: CancellationException) {
            println("LeafOnApiClient.request -> cancelled method=$method endpoint=$path")
            throw exception
        } catch (exception: HttpRequestTimeoutException) {
            println("LeafOnApiClient.request -> timeout method=$method endpoint=$path")
            throw exception
        } catch (exception: ApiException) {
            println(
                "LeafOnApiClient.request -> error method=$method endpoint=$path " +
                    "status=${exception.statusCode} bodyLength=${exception.message?.length ?: 0}"
            )
            throw exception
        } catch (exception: ResponseException) {
            println(
                "LeafOnApiClient.request -> response exception method=$method endpoint=$path " +
                    "status=${exception.response.status.value} message=${exception.message}"
            )
            throw exception
        } catch (exception: Throwable) {
            println(
                "LeafOnApiClient.request -> failure method=$method endpoint=$path " +
                    "type=${exception::class.simpleName} message=${exception.message}"
            )
            throw exception
        }
    }
}
