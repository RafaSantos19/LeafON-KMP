package kmp.edu.leafon_kmp.core.auth

object SupabaseConfig {
    const val projectUrl: String = "https://cieqfhwerpxomfvelojq.supabase.co"

    const val publishableKey: String = "sb_publishable_1oh1a5eT_UphZA-UslxyDA_yKqNp4S6"
    const val anonKey: String = publishableKey

    fun configurationErrorOrNull(): String? {
        return when {
            publishableKey.isBlank() ->
                "Configure a publishableKey do Supabase antes de testar a autenticação."

            !publishableKey.startsWith("sb_publishable_") ->
                "A chave configurada não parece ser uma publishable key válida do Supabase."

            !projectUrl.startsWith("https://") ->
                "A projectUrl do Supabase precisa começar com https://"

            else -> null
        }
    }
}