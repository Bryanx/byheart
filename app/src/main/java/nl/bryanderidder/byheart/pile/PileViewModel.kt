package nl.bryanderidder.byheart.pile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import nl.bryanderidder.byheart.shared.database.CardDatabase
import kotlin.coroutines.CoroutineContext

/**
 * Viewmodel that contains all the piles in a LiveData object.
 * The object is updating on backend or frontend changes.
 * @author Bryan de Ridder
 */
class PileViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob: Job = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope: CoroutineScope = CoroutineScope(coroutineContext)
    private val repo: PileRepository
    val allPiles: LiveData<List<Pile>>

    init {
        val pileDao = CardDatabase.getDatabase(application, scope).pileDao()
        repo = PileRepository(pileDao)
        allPiles = repo.allPiles
    }

    suspend fun insert(pile: Pile): Long = withContext(Dispatchers.Default) {
        pile.listIndex = repo.getCount()
        repo.insert(pile)
    }

    fun update(pile: Pile) = scope.launch(Dispatchers.IO) {
        allPiles.value!!.find { it.id == pile.id }?.let {
            it.name = pile.name
            it.color = pile.color
            it.languageCardFront = pile.languageCardFront
            it.languageCardBack = pile.languageCardBack
            it.listIndex = pile.listIndex
            repo.update(it)
        }
    }

    fun updateAll(piles: List<Pile>) = scope.launch(Dispatchers.IO) {
        repo.updateAll(piles)
    }

    fun delete(id: Long?) = scope.launch(Dispatchers.IO) {
        allPiles.value!!.find { it.id == id }?.let { pileToRemove ->
            allPiles.value!!.filter { it.listIndex > pileToRemove.listIndex }.forEach {
                it.listIndex -= 1
                repo.update(it)
            }
            repo.delete(pileToRemove)
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}