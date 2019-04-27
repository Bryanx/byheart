package com.example.byheart.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.byheart.model.Qa
import com.example.byheart.persistence.QaDatabase
import com.example.byheart.persistence.QaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class QaViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val repo: QaRepository
    val allQas: LiveData<List<Qa>>

    init {
        val qaDao = QaDatabase.getDatabase(application).qaDao()
        repo = QaRepository(qaDao)
        allQas = repo.allQas
    }

    fun insert(qa: Qa) = scope.launch(Dispatchers.IO) {
        repo.insert(qa)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}