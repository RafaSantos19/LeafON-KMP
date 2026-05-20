package kmp.edu.leafon_kmp.data

import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

class RepositorioRemotoEmMemoria : RepositorioRemoto {

    private val pots = mutableListOf<PotUi>()

    private val rotinasPorPot = mutableMapOf<String, MutableList<RoutineUi>>()

    override fun getPots(): List<PotUi> {
        return pots.toList()
    }

    override fun getPotById(id: String): PotUi? {
        return pots.firstOrNull { it.id == id }
    }

    override fun postPot(pot: PotUi) {
        pots.removeAll { it.id == pot.id }
        pots.add(pot)
    }

    override fun putPot(pot: PotUi) {
        val index = pots.indexOfFirst { it.id == pot.id }

        if (index >= 0) {
            pots[index] = pot
        } else {
            pots.add(pot)
        }
    }

    override fun deletePot(id: String) {
        pots.removeAll { it.id == id }
        rotinasPorPot.remove(id)
    }

    override fun listarRotinas(potId: String): List<RoutineUi> {
        return rotinasPorPot[potId]?.toList().orEmpty()
    }

    override fun criarRotina(potId: String, rotina: RoutineUi) {
        val rotinas = rotinasPorPot.getOrPut(potId) { mutableListOf() }
        rotinas.removeAll { it.id == rotina.id }
        rotinas.add(rotina)
    }

    override fun atualizarRotina(potId: String, rotina: RoutineUi) {
        val rotinas = rotinasPorPot.getOrPut(potId) { mutableListOf() }
        val index = rotinas.indexOfFirst { it.id == rotina.id }

        if (index >= 0) {
            rotinas[index] = rotina
        } else {
            rotinas.add(rotina)
        }
    }
}
