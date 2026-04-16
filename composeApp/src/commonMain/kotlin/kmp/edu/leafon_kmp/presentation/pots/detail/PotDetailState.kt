package kmp.edu.leafon_kmp.presentation.pots.detail

import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus

data class PotDetailState(
    val potId: String,
    val name: String = "",
    val plantName: String = "",
    val isOnline: Boolean = false,
    val status: PotStatus = PotStatus.OFFLINE,
    val humidityPercent: Int? = null,
    val temperatureCelsius: Int? = null,
    val lightLevel: Int? = null,
    val lastUpdateLabel: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
