package com.example.byheart.pile

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Manages queries and allows you to use multiple backends.
// Decides whether to fetch data from a network or use results cached in a local database.
class PileRepository(private val pileDao: PileDao) {

    val allPiles: LiveData<List<Pile>> = pileDao.getAll()

    @WorkerThread
    suspend fun insert(pile: Pile): Long {
        return pileDao.insert(pile)
    }

    @WorkerThread
    suspend fun update(pile: Pile) {
        pileDao.update(pile)
    }

    @WorkerThread
    suspend fun delete(pile: Pile) {
        pileDao.delete(pile)
    }
}