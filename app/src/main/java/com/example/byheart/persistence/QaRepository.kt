package com.example.byheart.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.byheart.model.Qa

class QaRepository(private val qaDao: QaDao) {

    val allQas: LiveData<List<Qa>> = qaDao.getAll()

    @WorkerThread
    suspend fun insert(qa: Qa) {
        qaDao.insertAll(qa)
    }
}