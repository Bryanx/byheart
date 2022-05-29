package nl.bryanderidder.byheart.shared

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
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
    var pileId: MutableLiveData<Long> = MutableLiveData()
    var pileName: MutableLiveData<String> = MutableLiveData()
    var pileColor: MutableLiveData<Int> = MutableLiveData()
    var cardListState: MutableLiveData<MutableMap<Long, Parcelable>> = MutableLiveData(mutableMapOf())

    fun pushCardListState(onSaveInstanceState: Parcelable?) {
        onSaveInstanceState?.let { state ->
            pileId.value?.let { id ->
                cardListState.value?.set(id, state)
            }
        }
    }

    fun findMessage(): String? {
        val msg = message.value
        message.value = null
        return msg
    }

}