package kmp.edu.leafon_kmp.data

import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

interface RepositorioRemoto {
    fun listarPots(): List<PotUi>
    fun buscarPotPorId(id: String): PotUi?
    fun criarPot(pot: PotUi)
    fun atualizarPot(pot: PotUi)
    fun excluirPot(id: String)

    fun listarRotinas(potId: String): List<RoutineUi>
    fun criarRotina(potId: String, rotina: RoutineUi)
    fun atualizarRotina(potId: String, rotina: RoutineUi)
}
