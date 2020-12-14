package nl.bryanderidder.byheart.card.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.persistence.PileRemoteDao

/**
 * Repository for local piles
 * @author Bryan de Ridder
 */
class CardRemoteRepository(private val dao: CardRemoteDao) {

    @WorkerThread
    fun insertAllAsync(pileId: String, cards: List<Card>) = GlobalScope.async {
        dao.insertAllAsync(pileId, cards).await()
    }

    fun findAllForPileIdAsync(pileId: String): Deferred<List<Card>> = GlobalScope.async {
        dao.findAllForPileIdAsync(pileId).await()
    }

    fun deleteAsync(remotePileId: String) = GlobalScope.async {
        dao.deleteAsync(remotePileId).await()
    }
}