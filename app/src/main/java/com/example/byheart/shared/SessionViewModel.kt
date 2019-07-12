package com.example.byheart.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * ViewModel that contains all session information.
 * @author Bryan de Ridder
 */
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    var cardId: MutableLiveData<Long> = MutableLiveData()
    var pileId: MutableLiveData<Long> = MutableLiveData()
    var pileName: MutableLiveData<String> = MutableLiveData()

}