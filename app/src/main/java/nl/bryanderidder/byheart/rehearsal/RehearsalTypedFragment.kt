package nl.bryanderidder.byheart.rehearsal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.content_rehearsal_typed.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.*

/**
 * Fragment that contains the rehearsal typed mode.
 * Some extra logic was added so a user can press enter twice to quickly switch to the next question.
 * @author Bryan de Ridder
 */
class RehearsalTypedFragment : RehearsalFragment() {

    private var enterCounter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal_typed)
        super.onCreateView(inflater, container, savedInstanceState)
        return layout
    }

    override fun addEventHandlers() {
        etInput.onEnter {
            return@onEnter when {
                etInput.text.isEmpty() || enterCounter > 1 -> false
                enterCounter == 1 -> {
                    enterCounter += 1
                    skipWaitingGoToNextCard().run { false }
                }
                enterCounter == 0 -> {
                    enterCounter += 1
                    onEnter().run { true }
                }
                else -> false
            }
        }
        btnGo.setOnClickListener {
            when {
                etInput.text.isEmpty() || enterCounter > 1 -> return@setOnClickListener
                (it as Button).string.isEmpty() -> skipWaitingGoToNextCard()
                else -> onEnter()
            }
        }
    }

    private fun onEnter() {
        btnGo.text = ""
        btnGo.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_forward_white_24dp, 0, 0, 0)
        flipCard()
        if (etInput.string.equalsIgnoreCase(cardBack.string)) {
            correctSound.start()
            etInput.setLineColor(R.color.green)
            handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_correct_duration).toLong())
        } else {
            wrongSound.start()
            etInput.setLineColor(R.color.red)
            handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_false_duration).toLong())
        }
        if (Preferences.read(Preferences.REHEARSAL_PRONOUNCE)) speakCard(cardBack, languageCardBack)
    }

    private fun skipWaitingGoToNextCard() {
        buttonsAreEnabled(false)
        handler.removeMessages(0)
        nextQuestionWithButtons()
    }

    private fun nextQuestionWithButtons() {
        nextQuestion {
            resetViews()
            buttonsAreEnabled(true)
        }
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        btnGo.isEnabled = bool
    }

    private fun resetViews() {
        btnGo.text = resources.getString(R.string.go)
        btnGo.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0)
        etInput.text = null
        etInput.setLineColor(R.color.colorPrimary)
        enterCounter = 0
    }

    override fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        resetViews()
        return super.onRestart(true) {
            buttonsAreEnabled(true)
        }
    }

    override fun onDestroyView() {
        activity.hideKeyboard()
        super.onDestroyView()
    }
}