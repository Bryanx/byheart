package nl.bryanderidder.byheart.rehearsal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_rehearsal_memory.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.*

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
                if (it == cardBtnCorrect) {
                    button.setIconColor(requireContext().color(R.color.white))
                    button.animateBgColor(requireContext().getAttr(R.attr.colorGreen))
                    super.onCorrect()
                }
                else {
                    button.setIconColor(requireContext().color(R.color.white))
                    button.animateBgColor(requireContext().color(R.color.red))
                    super.onFalse()
                }
                if (Preferences.REHEARSAL_PRONOUNCE) rehearsalCard.sayBackCard()
                val delay = Preferences.REHEARSAL_DELAY_TIME.toLong()
                handler.postDelayed({ nextQuestionWithButtons() }, delay)
            }
        }
        rehearsalLayout.setOnClickListener {
            rehearsalCard.flipCard()
            setButtonsVisibility(VISIBLE)
        }
    }

    private fun nextQuestionWithButtons() = nextQuestion { /* on new card: */
        buttonsAreEnabled(true)
        setButtonsVisibility(GONE)
        cardBtnCorrect.setIconColor(requireContext().color(R.color.green))
        cardBtnCorrect.animateBgColor(requireContext().getAttr(R.attr.mainBackgroundColorLighter))
        cardBtnFalse.setIconColor(requireContext().color(R.color.red))
        cardBtnFalse.animateBgColor(requireContext().getAttr(R.attr.mainBackgroundColorLighter))
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
