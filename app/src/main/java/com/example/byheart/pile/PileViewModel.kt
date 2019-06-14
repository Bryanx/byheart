package com.example.byheart.pile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.byheart.shared.CardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PileViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val repo: PileRepository
    val allPiles: LiveData<List<Pile>>

    init {
        val pileDao = CardDatabase.getDatabase(application, scope).pileDao()
        repo = PileRepository(pileDao)
        allPiles = repo.allPiles
    }

    fun insert(pile: Pile) = scope.launch(Dispatchers.IO) {
        repo.insert(pile)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}