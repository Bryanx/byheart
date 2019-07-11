package com.example.byheart.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.byheart.shared.CardDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel that contains all session information.
 * @author Bryan de Ridder
 */
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    var cardId: MutableLiveData<Long> = MutableLiveData()
    var pileId: MutableLiveData<Long> = MutableLiveData()

}