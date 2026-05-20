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
        return httpClient.get(url("/smart-pots")).body()
    }

    suspend fun getSmartPotById(id: String): SmartPotResponseDto {
        return httpClient.get(url("/smart-pots/$id")).body()
    }

    suspend fun createSmartPot(request: CreateSmartPotRequestDto): SmartPotResponseDto {
        return httpClient.post(url("/smart-pots")) {
            setBody(request)
        }.body()
    }

    suspend fun updateSmartPot(
        id: String,
        request: UpdateSmartPotRequestDto,
    ): SmartPotResponseDto {
        return httpClient.put(url("/smart-pots/$id")) {
            setBody(request)
        }.body()
    }

    suspend fun deleteSmartPot(id: String) {
        httpClient.delete(url("/smart-pots/$id"))
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
}
