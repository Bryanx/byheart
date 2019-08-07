package nl.bryanderidder.byheart.pile

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/**
 * Manages queries and allows you to use multiple backend's.
 * Decides whether to fetch data from a network or use results cached in a local database.
 * @author Bryan de Ridder
 */
class PileRepository(private val pileDao: PileDao) {

    val allPiles: LiveData<List<Pile>> = pileDao.getAll()

    @WorkerThread
    fun insert(pile: Pile): Long {
        return pileDao.insert(pile)
    }

    @WorkerThread
    fun update(pile: Pile) {
        pileDao.update(pile)
    }

    @WorkerThread
    fun updateAll(piles: List<Pile>) {
        pileDao.updateAll(piles)
    }

    @WorkerThread
    fun delete(pile: Pile) {
        pileDao.delete(pile)
    }
}