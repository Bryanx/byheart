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
import nl.bryanderidder.byheart.shared.Preferences.REHEARSAL_PRONOUNCE
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
                if (Preferences.read(REHEARSAL_PRONOUNCE)) speakCard(cardBack, languageCardBack)
                flipCard()
                if (button == cardBtnCorrect) {
                    handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_correct_duration).toLong())
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
    }

    private fun nextQuestionWithButtons() {
        btnContinue.visibility = GONE
        nextQuestion { buttonsAreEnabled(true) }
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        cardBtnCorrect.isEnabled = bool
        cardBtnFalse.isEnabled = bool
    }

    override fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        btnContinue.visibility = GONE
        return super.onRestart(true) {
            buttonsAreEnabled(true)
        }
    }
}
