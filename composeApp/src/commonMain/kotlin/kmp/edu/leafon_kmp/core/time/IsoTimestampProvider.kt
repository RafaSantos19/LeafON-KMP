package kmp.edu.leafon_kmp.core.time

import kotlin.time.Clock

object IsoTimestampProvider {
    fun nowUtc(): String = Clock.System.now().toString()
}
