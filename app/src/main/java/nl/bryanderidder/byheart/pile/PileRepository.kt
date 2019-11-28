package nl.bryanderidder.byheart.pile

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/**
 * Manages queries and allows you to use multiple backend's.
 * Decides whether to fetch data from a network or use results cached in a local database.
 * @author Bryan de Ridder
 */
class PileRepository(private val dataSource: DataSource) {

    val allPiles: LiveData<List<Pile>> = dataSource.getAll()

    @WorkerThread
    fun insert(pile: Pile): Long = dataSource.insert(pile)

    @WorkerThread
    fun update(pile: Pile) = dataSource.update(pile)

    @WorkerThread
    fun updateAll(piles: List<Pile>) = dataSource.updateAll(piles)

    @WorkerThread
    fun delete(pile: Pile) = dataSource.delete(pile)

    @WorkerThread
    fun getCount(): Int = dataSource.getCount()

}