package nl.bryanderidder.byheart.rehearsal

import android.app.Application
import android.content.Context
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.scheduleAtFixedRate
import nl.bryanderidder.byheart.shared.utils.fractionToPercentage
import nl.bryanderidder.byheart.shared.utils.secondsToTimeFormat
import java.util.*

/**
 * ViewModel that contains all rehearsal information.
 * @author Bryan de Ridder
 */
class RehearsalViewModel(application: Application) : AndroidViewModel(application) {

    var amountCorrect: MutableLiveData<Int> = MutableLiveData()
    var amountFalse: MutableLiveData<Int> = MutableLiveData()
    private var mElapsedTime: MutableLiveData<Int> = MutableLiveData()

    private val ONE_SECOND: Long = 1000
    private var initialTime: Int = 0
    private val timer: Timer = Timer()

    private val amountOfAnswers: Int get() = amountCorrect.value?.plus(amountFalse.value ?: 0) ?: 0
    val percentageCorrect: Int get() = fractionToPercentage(amountCorrect, amountOfAnswers)
    val elapsedTime: String get() = secondsToTimeFormat(mElapsedTime)
    val score: String get() = "${amountCorrect.value}/${amountOfAnswers}"

    fun reset() {
        startTimer()
        amountCorrect.value = 0
        amountFalse.value = 0
    }

    private fun startTimer() {
        initialTime = SystemClock.elapsedRealtime().toInt()
        timer.scheduleAtFixedRate(ONE_SECOND, ONE_SECOND) { // on timer tick:
            val newValue: Int = (SystemClock.elapsedRealtime().toInt() - initialTime) / ONE_SECOND.toInt()
            mElapsedTime.postValue(newValue)
        }
    }

    fun incrementAmountCorrect() {
        amountCorrect.value = amountCorrect.value?.plus(1)
    }

    fun incrementAmountFalse() {
        amountFalse.value = amountFalse.value?.plus(1)
    }

    fun getMedal(): Int {
        val percentage = percentageCorrect
        return when {
            percentage > 90 -> R.drawable.ic_medal_gold
            percentage > 80 -> R.drawable.ic_medal_silver
            else -> R.drawable.ic_medal_bronze
        }
    }

    fun getTitle(context: Context): String {
        val percentage = percentageCorrect
        return when {
            percentage == 100 -> context.getString(R.string.perfection)
            percentage > 95 -> context.getString(R.string.amazing)
            percentage > 90 -> context.getString(R.string.well_done)
            percentage > 85 -> context.getString(R.string.keep_going)
            percentage > 80 -> context.getString(R.string.nice_work)
            percentage > 75 -> context.getString(R.string.not_bad)
            percentage > 70 -> context.getString(R.string.keep_learning)
            percentage > 65 -> context.getString(R.string.hang_in_there)
            percentage > 50 -> context.getString(R.string.stay_focused)
            percentage > 25 -> context.getString(R.string.you_can_do_it)
            else -> context.getString(R.string.pretty_bad)
        }
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }
}
