package nl.bryanderidder.byheart.store.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import nl.bryanderidder.byheart.pile.Pile

/**
 * Repository for local piles
 * @author Bryan de Ridder
 */
class PileRemoteRepository(private val dao: PileRemoteDao) {

    val allPiles: LiveData<List<Pile>> = dao.getAll()

    @WorkerThread
    fun insert(pile: Pile): String = dao.insert(pile)

    @WorkerThread
    fun update(pile: Pile) = dao.update(pile)

    @WorkerThread
    fun updateAll(piles: List<Pile>) = dao.updateAll(piles)

    @WorkerThread
    fun delete(pile: Pile) = dao.delete(pile)

    @WorkerThread
    fun getCount(): Int = dao.getCount()
}