package kmp.edu.leafon_kmp.presentation.pots

sealed interface PotListAction {
    data class OnPotClick(val id: String) : PotListAction
    data class OnEditPotClick(val id: String) : PotListAction
    data class OnDeletePotClick(val id: String) : PotListAction
    data object OnAddPotClick : PotListAction
    data object OnRefresh : PotListAction
}
