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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetViews()
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
        rehearsalCard.flipCard()
        if (etInput.string.equalsIgnoreCase(rehearsalCard.backText!!)) onCorrect()
        else onFalse()
        if (Preferences.REHEARSAL_PRONOUNCE) rehearsalCard.sayBackCard()
    }

    override fun onCorrect() {
        super.onCorrect()
        etInput.setLineColor(requireContext().color(R.color.green))
        btnGo.setBackgroundTint(requireContext().color(R.color.green))
        handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_correct_duration).toLong())
    }

    override fun onFalse() {
        super.onFalse()
        etInput.setLineColor(requireContext().color(R.color.red))
        btnGo.setBackgroundTint(requireContext().color(R.color.red))
        handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_false_duration).toLong())
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
        etInput.setLineColor(pileColor)
        btnGo.setBackgroundTint(pileColor)
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
