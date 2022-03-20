package nl.bryanderidder.byheart.pile.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
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
    fun findAsync(remotePileId: String): Deferred<Pile> {
        return dao.findAsync(remotePileId)
    }

    @WorkerThread
    fun deleteAsync(remotePileId: String): Deferred<Void> {
        return dao.deleteAsync(remotePileId)
    }

    fun updateAsync(pile: Pile): Deferred<Void> {
        return dao.updateAsync(pile)
    }
}