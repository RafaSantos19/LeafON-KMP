package kmp.edu.leafon_kmp.presentation.pots

import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.model.potPreviewItems

data class PotListState(
    val pots: List<PotUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

fun potListPreviewState() = PotListState(
    pots = potPreviewItems(),
)
