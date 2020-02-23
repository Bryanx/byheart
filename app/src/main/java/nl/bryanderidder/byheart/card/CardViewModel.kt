package nl.bryanderidder.byheart.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import nl.bryanderidder.byheart.shared.CoroutineProvider
import nl.bryanderidder.byheart.shared.database.CardDatabase
import kotlin.coroutines.CoroutineContext

/**
 * All cards in the main card fragment are stored in a LiveData property in this viewmodel.
 * The card fragment listens for changes on this property.
 * In the card viewmodel this property is updated by changes in the database or in the fragment.
 * @author Bryan de Ridder
 */
class CardViewModel(application: Application) : AndroidViewModel(application) {

    var coroutineProvider: CoroutineProvider = CoroutineProvider()
    private var parentJob: Job = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + coroutineProvider.Main
    private val scope = CoroutineScope(coroutineContext)
    private val repo: CardRepository
    val allCards: LiveData<List<Card>>

    init {
        val qaDao = CardDatabase.getDatabase(application, scope).cardDao()
        repo = CardRepository(qaDao)
        allCards = repo.allCards
    }

    fun getByPileId(id: MutableLiveData<Long>): LiveData<List<Card>> {
        return Transformations.map(allCards) { it.filter { card -> card.pileId == id.value } }
    }

    fun insert(card: Card) = scope.launch(Dispatchers.IO) {
        card.listIndex = repo.getCount(card.pileId)
        repo.insert(card)
    }

    fun insertAll(cards: List<Card>) = runBlocking(Dispatchers.Default) {
        cards.forEachIndexed { i, card -> card.listIndex = i }
        return@runBlocking repo.insertAll(cards)
    }

    fun update(card: Card) = scope.launch(Dispatchers.IO) {
        getCards(card.pileId).find { it.id == card.id }?.let {
            it.answer = card.answer
            it.question = card.question
            it.listIndex = card.listIndex
            it.amountCorrect = card.amountCorrect
            it.amountFalse = card.amountFalse
            repo.update(it)
        }
        repo.update(card)
    }

    fun updateAll(cards: List<Card>) = scope.launch(Dispatchers.IO) {
        repo.updateAll(cards)
    }

    fun delete(card: Card) = scope.launch(coroutineProvider.IO) {
        repo.delete(card)
        val cardsToUpdate = getCards(card.pileId).filter { it != card && it.listIndex > card.listIndex }
        cardsToUpdate.forEach { it.listIndex -= 1 }
        repo.updateAll(cardsToUpdate)
    }

    fun getCards(pileId: Long): List<Card> = allCards.value!!.filter { it.pileId == pileId }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}