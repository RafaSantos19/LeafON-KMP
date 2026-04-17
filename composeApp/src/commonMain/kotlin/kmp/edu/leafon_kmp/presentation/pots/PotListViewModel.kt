package kmp.edu.leafon_kmp.presentation.pots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria

class PotListViewModel(
    private val repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
) {

    var state by mutableStateOf(PotListState())
        private set

    init {
        refresh()
    }

    fun onAction(action: PotListAction) {
        when (action) {
            is PotListAction.OnPotClick -> Unit
            is PotListAction.OnEditPotClick -> Unit
            is PotListAction.OnDeletePotClick -> deletePot(action.id)
            is PotListAction.OnAddPotClick -> Unit
            is PotListAction.OnRefresh -> refresh()
        }
    }

    private fun refresh() {
        state = state.copy(
            pots = repositorio.listarPots(),
            isLoading = false,
            errorMessage = null,
        )
    }

    private fun deletePot(id: String) {
        repositorio.excluirPot(id)
        refresh()
    }
}
