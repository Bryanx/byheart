package nl.bryanderidder.byheart.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import nl.bryanderidder.byheart.shared.database.CardDatabase
import kotlin.coroutines.CoroutineContext

/**
 * All cards in the main card fragment are stored in a LiveData property in this viewmodel.
 * The card fragment listens for changes on this property.
 * In the card viewmodel this property is updated by changes in the database or in the fragment.
 * @author Bryan de Ridder
 */
class CardViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val repo: CardRepository
    val allCards: LiveData<List<Card>>

    init {
        val qaDao = CardDatabase.getDatabase(application, scope).cardDao()
        repo = CardRepository(qaDao)
        allCards = repo.allCards
    }

    fun insert(card: Card) = scope.launch(Dispatchers.IO) {
        repo.insert(card)
    }

    fun insertAll(cards: List<Card>) = runBlocking(Dispatchers.Default) {
        return@runBlocking repo.insertAll(cards)
    }

    fun update(card: Card) = scope.launch(Dispatchers.IO) {
        repo.update(card)
    }

    fun delete(card: Card) = scope.launch(Dispatchers.IO) {
        repo.delete(card)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}