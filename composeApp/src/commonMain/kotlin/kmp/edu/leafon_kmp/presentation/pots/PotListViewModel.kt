package kmp.edu.leafon_kmp.presentation.pots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PotListViewModel {

    var state by mutableStateOf(potListPreviewState())
        private set

    fun onAction(action: PotListAction) {
        when (action) {
            is PotListAction.OnPotClick -> Unit
            is PotListAction.OnEditPotClick -> Unit
            is PotListAction.OnDeletePotClick -> Unit
            is PotListAction.OnAddPotClick -> Unit
            is PotListAction.OnRefresh -> refresh()
        }
    }

    private fun refresh() {
        state = potListPreviewState()
    }
}
