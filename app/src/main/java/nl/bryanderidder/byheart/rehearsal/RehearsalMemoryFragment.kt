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
                if (it == cardBtnCorrect) super.onCorrect()
                else super.onFalse()
                if (Preferences.REHEARSAL_PRONOUNCE) rehearsalCard.sayBackCard()
                rehearsalCard.turnToBack()
                var delay = rehearsalCard.backText!!.length*150.toLong()
                if (delay > 2000) delay = 2000
                handler.postDelayed({ nextQuestionWithButtons() }, delay)
            }
        }
        rehearsalCard.setOnClickListener {
            rehearsalCard.flipCard()
            setButtonsVisibility(VISIBLE)
        }
    }

    private fun nextQuestionWithButtons() = nextQuestion { /* on new card: */
        buttonsAreEnabled(true)
        setButtonsVisibility(GONE)
    }

    private fun setButtonsVisibility(visibility: Int) {
        cardBtnCorrect?.visibility = visibility
        cardBtnFalse?.visibility = visibility
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        cardBtnCorrect?.isEnabled = bool
        cardBtnFalse?.isEnabled = bool
    }

    override fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        return super.onRestart(true) {
            buttonsAreEnabled(true)
        }
    }
}
