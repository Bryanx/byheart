package com.example.byheart.qa

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class QaRepository(private val qaDao: QaDao) {

    val allQas: LiveData<List<Qa>> = qaDao.getAll()

    @WorkerThread
    suspend fun insert(qa: Qa) {
        qaDao.insert(qa)
    }
}