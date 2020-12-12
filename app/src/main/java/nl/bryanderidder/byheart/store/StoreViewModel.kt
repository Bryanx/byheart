package nl.bryanderidder.byheart.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView.NO_ID
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.persistence.PileLocalRepository
import nl.bryanderidder.byheart.store.persistence.PileRemoteRepository

/**
 * ViewModel that contains all store information.
 * @author Bryan de Ridder
 */
class StoreViewModel(application: Application, private val repo: PileRemoteRepository) : AndroidViewModel(application) {

//    var coroutineProvider: CoroutineProvider = CoroutineProvider()
//    private var parentJob: Job = Job()
//    private val coroutineContext: CoroutineContext
//        get() = parentJob + coroutineProvider.Main
//    private val scope: CoroutineScope = CoroutineScope(coroutineContext)

    val allPiles: LiveData<List<Pile>> = repo.allPiles

    fun insertPile(pile: Pile?, cards: MutableList<Card>) {
        pile?.cards = cards
        pile?.let { repo.insert(it) }
    }

}