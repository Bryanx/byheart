package nl.bryanderidder.byheart.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.persistence.CardRemoteRepository
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.persistence.PileRemoteRepository

/**
 * ViewModel that contains all store information.
 * @author Bryan de Ridder
 */
class StoreViewModel(
    application: Application,
    private val pileRepo: PileRemoteRepository,
    private val cardRepo: CardRemoteRepository
) : AndroidViewModel(application) {

    val allPiles: LiveData<List<Pile>> = pileRepo.allPiles

    fun insertPileAsync(pile: Pile, cards: List<Card>): Deferred<String> = GlobalScope.async {
        pile.cardCount = cards.size
        val id = pileRepo.insert(pile)
        cardRepo.insertAllAsync(id, cards).await()
        id
    }

    fun getPileAsync(remotePileId: String): Deferred<Pile> = GlobalScope.async {
        val pile = pileRepo.findAsync(remotePileId).await()
        pile.cards = cardRepo.findAllForPileIdAsync(remotePileId).await().toMutableList()
        pile
    }

    fun deleteAsync(remotePileId: String) = GlobalScope.async {
        pileRepo.deleteAsync(remotePileId).await()
        cardRepo.deleteAsync(remotePileId).await()
    }
}