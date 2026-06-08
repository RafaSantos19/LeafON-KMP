package kmp.edu.leafon_kmp.presentation.home

import kmp.edu.leafon_kmp.core.model.LatestTelemetryReading
import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.presentation.home.components.ChartRange

data class HomeState(
    val smartPots: List<SmartPot> = emptyList(),
    val selectedSmartPotId: String? = null,
    val latestTelemetry: LatestTelemetryReading? = null,
    val telemetryHistory: List<TelemetryReading> = emptyList(),
    val unreadAlertsCount: Int = 0,
    val isLoading: Boolean = false,
    val isTelemetryLoading: Boolean = false,
    val errorMessage: String? = null,
    val telemetryErrorMessage: String? = null,
    val selectedRange: ChartRange = ChartRange.H24,
)
