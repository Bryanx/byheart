package nl.bryanderidder.byheart.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileRepository

/**
 * ViewModel that contains all store information.
 * @author Bryan de Ridder
 */
class StoreViewModel(application: Application) : AndroidViewModel(application) {

//    var coroutineProvider: CoroutineProvider = CoroutineProvider()
//    private var parentJob: Job = Job()
//    private val coroutineContext: CoroutineContext
//        get() = parentJob + coroutineProvider.Main
//    private val scope: CoroutineScope = CoroutineScope(coroutineContext)
    private val repo: PileRepository = PileRepository(StoreDao())

    fun getPiles() : LiveData<List<Pile>> = repo.allPiles

}