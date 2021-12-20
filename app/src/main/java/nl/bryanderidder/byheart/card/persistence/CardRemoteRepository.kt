package nl.bryanderidder.byheart.card.persistence

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Deferred
import nl.bryanderidder.byheart.card.Card

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

    fun updateAllAsync(remoteId: String, cards: List<Card>): Deferred<Void> {
        return dao.insertAllAsync(remoteId, cards)
    }
}