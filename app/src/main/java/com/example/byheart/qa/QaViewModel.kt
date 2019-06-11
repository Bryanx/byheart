package com.example.byheart.qa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.byheart.persistence.QaDatabase
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
        val qaDao = QaDatabase.getDatabase(application, scope).qaDao()
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