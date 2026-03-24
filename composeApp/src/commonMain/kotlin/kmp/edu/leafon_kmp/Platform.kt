package kmp.edu.leafon_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform