package com.example.byheart.pile

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Manages queries and allows you to use multiple backends.
// Decides whether to fetch data from a network or use results cached in a local database.
class PileRepository(private val pileDao: PileDao) {

    val allPiles: LiveData<List<Pile>> = pileDao.getAll()

    @WorkerThread
    suspend fun insert(pile: Pile) {
        pileDao.insert(pile)
    }
}