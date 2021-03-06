package nl.bryanderidder.byheart.pile.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import nl.bryanderidder.byheart.pile.Pile

/**
 * Repository for local piles
 * @author Bryan de Ridder
 */
class PileLocalRepository(private val dao: PileLocalDao) {

    val allPiles: LiveData<List<Pile>> = dao.getAll()

    @WorkerThread
    fun insert(pile: Pile): Long = dao.insert(pile)

    @WorkerThread
    fun update(pile: Pile) = dao.update(pile)

    @WorkerThread
    fun updateAll(piles: List<Pile>) = dao.updateAll(piles)

    @WorkerThread
    fun delete(pile: Pile) = dao.delete(pile)

    @WorkerThread
    fun getCount(): Int = dao.getCount()
}