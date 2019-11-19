package nl.bryanderidder.byheart.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * ViewModel that contains all session information.
 * @author Bryan de Ridder
 */
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    var message: MutableLiveData<String> = MutableLiveData()
    var cardId: MutableLiveData<Long> = MutableLiveData()
    var cardCount: MutableLiveData<Int> = MutableLiveData()
    var cardColor: MutableLiveData<Int> = MutableLiveData()
    var pileId: MutableLiveData<Long> = MutableLiveData()
    var pileName: MutableLiveData<String> = MutableLiveData()

    fun findMessage(): String? {
        val msg = message.value
        message.value = null
        return msg
    }

}