package com.example.byheart.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.byheart.shared.CardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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