package nl.bryanderidder.byheart.pile.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.persistence.PileRemoteDao

/**
 * Repository for local piles
 * @author Bryan de Ridder
 */
class PileRemoteRepository(private val dao: PileRemoteDao) {

    val allPiles: LiveData<List<Pile>> = dao.getAll()

    @WorkerThread
    fun insert(pile: Pile): String = dao.insert(pile)

    @WorkerThread
    fun findAsync(remotePileId: String): Deferred<Pile> = GlobalScope.async {
        dao.findAsync(remotePileId).await()
    }

    @WorkerThread
    fun deleteAsync(remotePileId: String) = GlobalScope.async {
        dao.deleteAsync(remotePileId).await()
    }
}