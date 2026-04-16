package kmp.edu.leafon_kmp.presentation.pots.alerts.model

data class AlertUi(
    val id: String,
    val title: String,
    val description: String,
    val severity: AlertSeverity,
    val status: AlertStatus,
    val createdAtLabel: String,
)

enum class AlertSeverity {
    INFO,
    WARNING,
    CRITICAL,
}

enum class AlertStatus {
    ACTIVE,
    RESOLVED,
}
