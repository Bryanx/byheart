package nl.bryanderidder.byheart.pile.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
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
    fun findAsync(remotePileId: String): Deferred<Pile> {
        return dao.findAsync(remotePileId)
    }

    @WorkerThread
    fun deleteAsync(remotePileId: String): Deferred<Void> {
        return dao.deleteAsync(remotePileId)
    }
}