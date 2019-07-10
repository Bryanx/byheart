package com.example.byheart.pile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.byheart.shared.CardDatabase
import kotlinx.coroutines.*
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

    suspend fun insert(pile: Pile): Deferred<Long> = GlobalScope.async {
        repo.insert(pile)
    }

    fun update(pile: Pile) = scope.launch(Dispatchers.IO) {
        repo.update(pile)
    }

    fun delete(pile: Pile) = scope.launch(Dispatchers.IO) {
        repo.delete(pile)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}