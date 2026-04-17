package kmp.edu.leafon_kmp.data

import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

interface RepositorioRemoto {
    fun getPots(): List<PotUi>

    fun getPotById(id: String): PotUi?

    fun postPot(pot: PotUi)

    fun putPot(pot: PotUi)

    fun deletePot(id: String)

    fun listarPots(): List<PotUi> = getPots()
    fun buscarPotPorId(id: String): PotUi? = getPotById(id)
    fun criarPot(pot: PotUi) = postPot(pot)
    fun atualizarPot(pot: PotUi) = putPot(pot)
    fun excluirPot(id: String) = deletePot(id)

    fun listarRotinas(potId: String): List<RoutineUi>
    fun criarRotina(potId: String, rotina: RoutineUi)
    fun atualizarRotina(potId: String, rotina: RoutineUi)
}
