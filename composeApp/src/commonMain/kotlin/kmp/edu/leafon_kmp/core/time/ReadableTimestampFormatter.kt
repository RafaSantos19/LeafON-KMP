package kmp.edu.leafon_kmp.core.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

object ReadableTimestampFormatter {
    fun formatOrFallback(value: String?): String? {
        val rawValue = value?.trim()?.takeIf { it.isNotBlank() } ?: return null

        return runCatching {
            val localDateTime = Instant.parse(rawValue).toLocalDateTime(TimeZone.currentSystemDefault())
            buildString {
                append(localDateTime.date.day.toString().padStart(2, '0'))
                append('/')
                append(localDateTime.date.monthNumber.toString().padStart(2, '0'))
                append('/')
                append(localDateTime.year)
                append(' ')
                append(localDateTime.hour.toString().padStart(2, '0'))
                append(':')
                append(localDateTime.minute.toString().padStart(2, '0'))
            }
        }.getOrDefault(rawValue)
    }
}
