package nl.bryanderidder.byheart.pile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.bryanderidder.byheart.shared.CoroutineProvider
import nl.bryanderidder.byheart.shared.database.CardDatabase
import kotlin.coroutines.CoroutineContext

/**
 * Viewmodel that contains all the piles in a LiveData object.
 * The object is updating on backend or frontend changes.
 * @author Bryan de Ridder
 */
class PileViewModel(application: Application, private val repo: PileRepository) : AndroidViewModel(application) {

    var coroutineProvider: CoroutineProvider = CoroutineProvider()
    private var parentJob: Job = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + coroutineProvider.Main
    private val scope: CoroutineScope = CoroutineScope(coroutineContext)
    val allPiles: LiveData<List<Pile>> = repo.allPiles

    suspend fun insert(pile: Pile): Long = withContext(coroutineProvider.Default) {
        pile.listIndex = repo.getCount()
        repo.insert(pile)
    }

    fun update(pile: Pile) = scope.launch(coroutineProvider.IO) {
        allPiles.value!!.find { it.id == pile.id }?.let {
            it.name = pile.name
            it.color = pile.color
            it.languageCardFront = pile.languageCardFront
            it.languageCardBack = pile.languageCardBack
            it.listIndex = pile.listIndex
            repo.update(it)
        }
    }

    fun updateAll(piles: List<Pile>) = scope.launch(coroutineProvider.IO) {
        repo.updateAll(piles)
    }

    fun delete(id: Long?) = scope.launch(coroutineProvider.IO) {
        val pile = allPiles.value!!.find { it.id == id };
        pile?.let { repo.delete(it) }
        val pilesToUpdate = allPiles.value!!.filter { it != pile && it.listIndex > pile?.listIndex!! }
        pilesToUpdate.forEach { it.listIndex -= 1 }
        repo.updateAll(pilesToUpdate)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}