package nl.bryanderidder.byheart.card.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.persistence.PileRemoteDao

/**
 * Repository for local piles
 * @author Bryan de Ridder
 */
class CardRemoteRepository(private val dao: CardRemoteDao) {

    @WorkerThread
    fun insertAllAsync(pileId: String, cards: List<Card>): Deferred<Void> {
        return dao.insertAllAsync(pileId, cards)
    }

    @WorkerThread
    fun findAllForPileIdAsync(pileId: String): Deferred<List<Card>> {
        return dao.findAllForPileIdAsync(pileId)
    }

    @WorkerThread
    fun deleteAsync(remotePileId: String): Deferred<Void> {
        return dao.deleteAsync(remotePileId)
    }
}