package kmp.edu.leafon_kmp.core.network

import kotlinx.browser.window

actual fun defaultApiBaseUrl(): String {
    val location = window.location
    return "${location.protocol}//${location.hostname}:8080"
}
