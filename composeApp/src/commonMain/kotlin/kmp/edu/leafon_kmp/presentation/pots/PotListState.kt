package kmp.edu.leafon_kmp.presentation.pots

import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.presentation.pots.model.smartPotPreviewItems

data class PotListState(
    val pots: List<SmartPot> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

fun potListPreviewState() = PotListState(
    pots = smartPotPreviewItems(),
)
