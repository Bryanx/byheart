package com.example.byheart.qa

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Manages queries and allows you to use multiple backends.
// Decides whether to fetch data from a network or use results cached in a local database.
class QaRepository(private val qaDao: QaDao) {

    val allQas: LiveData<List<Qa>> = qaDao.getAll()

    @WorkerThread
    suspend fun insert(qa: Qa) {
        qaDao.insert(qa)
    }
}