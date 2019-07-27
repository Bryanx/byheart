package nl.bryanderidder.byheart.rehearsal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_rehearsal_memory.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.inflate

/**
 * Fragment that contains the rehearsal memory mode.
 * @author Bryan de Ridder
 */
class RehearsalMemoryFragment : RehearsalFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal_memory)
        super.onCreateView(inflater, container, savedInstanceState)
        return layout
    }

    override fun addEventHandlers() {
        listOf(cardBtnFalse, cardBtnCorrect).forEach { button ->
            button.setOnClickListener {
                buttonsAreEnabled(false)
                if (it == cardBtnCorrect) correctSound.start()
                else wrongSound.start()
                if (Preferences.REHEARSAL_PRONOUNCE) rehearsalCard.sayBackCard()
                rehearsalCard.turnToBack()
                val delay = rehearsalCard.backText!!.length*150.toLong()
                if (button == cardBtnCorrect) {
                    handler.postDelayed({ nextQuestionWithButtons() }, delay)
                } else {
                    btnContinue.visibility = VISIBLE
                    handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_false_duration).toLong())
                }
            }
        }
        btnContinue.setOnClickListener {
            handler.removeMessages(0)
            nextQuestionWithButtons()
        }
        rehearsalCard.setOnClickListener {
            if (!rehearsalCard.backIsVisible) {
                rehearsalCard.flipCard()
                rehearsalCard.cardFront.isEnabled = false
            }
        }
    }

    private fun nextQuestionWithButtons() {
        btnContinue.visibility = GONE
        nextQuestion { /* on new card: */
            rehearsalCard.cardFront.isEnabled = true
            buttonsAreEnabled(true)
        }
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        cardBtnCorrect.isEnabled = bool
        cardBtnFalse.isEnabled = bool
    }

    override fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        btnContinue.visibility = GONE
        return super.onRestart(true) {
            rehearsalCard.cardFront.isEnabled = true
            buttonsAreEnabled(true)
        }
    }
}
