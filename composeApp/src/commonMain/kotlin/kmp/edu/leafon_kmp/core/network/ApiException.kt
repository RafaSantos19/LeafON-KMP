package kmp.edu.leafon_kmp.core.network

class ApiException(
    val statusCode: Int,
    message: String,
) : Exception(message)
