package nl.bryanderidder.byheart.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import nl.bryanderidder.byheart.card.persistence.CardRepository
import nl.bryanderidder.byheart.shared.CoroutineProvider
import kotlin.coroutines.CoroutineContext

/**
 * All cards in the main card fragment are stored in a LiveData property in this viewmodel.
 * The card fragment listens for changes on this property.
 * In the card viewmodel this property is updated by changes in the database or in the fragment.
 * @author Bryan de Ridder
 */
class CardViewModel(application: Application, private val repo: CardRepository) : AndroidViewModel(application) {

    var coroutineProvider: CoroutineProvider = CoroutineProvider()
    private var parentJob: Job = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + coroutineProvider.Main
    private val scope = CoroutineScope(coroutineContext)
    val allCards: LiveData<List<Card>> = repo.allCards

    fun getByPileId(id: MutableLiveData<Long>): LiveData<List<Card>> {
        return Transformations.map(allCards) { it.filter { card -> card.pileId == id.value } }
    }

    fun insertAsync(card: Card) = scope.async(coroutineProvider.Default) {
        card.listIndex = repo.getCount(card.pileId)
        repo.insert(card)
    }

    fun insertAtIndexAsync(card: Card, listIndex: Int) = scope.async(coroutineProvider.Default) {
        val cards = withContext(coroutineProvider.Default) { repo.allCards }.value!!
        if (listIndex < 0  || listIndex > cards.size) return@async
        card.listIndex = listIndex
        cards.filter { it.pileId == card.pileId }
            .filter { it != card }
            .filter { it.listIndex >= card.listIndex }
            .onEach { it.listIndex += 1 }
            .also { repo.updateAll(it) }
        repo.insert(card)
    }

    fun insertAllAsync(cards: List<Card>) = scope.async(coroutineProvider.Default) {
        cards.forEachIndexed { i, card -> card.listIndex = i }
        repo.insertAll(cards)
    }

    fun updateAsync(card: Card) = scope.async(coroutineProvider.IO) {
        val cards = withContext(coroutineProvider.Default) { allCards }
        cards.value!!.find { it.id == card.id }?.let {
            it.answer = card.answer
            it.question = card.question
            it.listIndex = card.listIndex
            it.amountCorrect = card.amountCorrect
            it.amountFalse = card.amountFalse
            repo.update(it)
        }
        repo.update(card)
    }

    fun updateAll(cards: List<Card>) = scope.launch(coroutineProvider.IO) {
        repo.updateAll(cards)
    }

    fun deleteAsync(card: Card) = scope.async(coroutineProvider.IO) {
        repo.delete(card)
        val cards = withContext(coroutineProvider.Default) { repo.allCards }.value!!
        cards.filter { it.pileId == card.pileId }
            .filter { it != card }
            .filter { it.listIndex > card.listIndex }
            .onEach { it.listIndex -= 1 }
            .also { repo.updateAll(it) }
    }

    fun deleteByIdAsync(id: Long) = scope.async(coroutineProvider.IO) {
        val card = allCards.value?.find { card -> card.id == id }
        card?.let { deleteAsync(it).await() }
    }

    fun getCards(pileId: Long): List<Card> = allCards.value!!.filter { it.pileId == pileId }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}