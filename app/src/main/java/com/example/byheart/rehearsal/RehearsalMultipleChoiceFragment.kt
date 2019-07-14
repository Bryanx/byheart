package com.example.byheart.rehearsal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.shared.*
import com.example.byheart.shared.Preferences.REHEARSAL_REVERSE
import kotlinx.android.synthetic.main.content_rehearsal_multiple_choice.*


/**
 * Fragment that contains the rehearsal multiple choice mode.
 * Each time a card appears, 3 answers are randomly added to the buttons and 1 correct is answered
 * in a random position as well.
 * @author Bryan de Ridder
 */
class RehearsalMultipleChoiceFragment : RehearsalFragment() {

    private lateinit var buttons: MutableList<Button>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal_multiple_choice)
        super.onCreateView(inflater, container, savedInstanceState)
        return layout
    }

    override fun doAfterGetData() {
        buttons = mutableListOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4)
        resetViews()
        addEventHandlers()
    }

    private fun resetViews() {
        val tempCards = cards.toMutableList()
        tempCards.removeAt(cardIndex) // remove answer from custom list
        tempCards.shuffle()
        val tempButtons = buttons.toMutableList()
        val poppedBtn = tempButtons.removeAt((0..3).random()) // remove random button
        addAnswerToButton(cardIndex, poppedBtn, cards) // add correct answer
        tempButtons.forEachIndexed { i, btn ->
            addAnswerToButton(i, btn, tempCards) // add random answers
        }
        resetButtons()
    }

    private fun addAnswerToButton(i: Int, btn: Button, tempCards: List<Card>) {
        if (Preferences.read(REHEARSAL_REVERSE)) btn.text = tempCards[i].question
        else btn.text = tempCards[i].answer
    }

    private fun resetButtons() {
        buttonsAreEnabled(true)
        buttons.forEach {
            it.setTextColor(context?.getAttr(R.attr.mainTextColor)!!)
        }
    }

    override fun addEventHandlers() {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach { btn: Button ->
            btn.setOnClickListener {
                if (btn.string.equalsIgnoreCase(cardBack.string)) {
                    buttonsAreEnabled(false)
                    correctSound.start()
                    if (Preferences.read(Preferences.REHEARSAL_PRONOUNCE)) speakCard(cardBack, languageCardBack)
                    btn.setTxtColor(R.color.green)
                    flipCard()
                    handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_correct_duration).toLong())
                } else {
                    wrongSound.start()
                    btn.setTxtColor(R.color.red)
                    btn.isEnabled = false
                }
            }
        }
    }

    private fun nextQuestionWithButtons() {
        nextQuestion { resetViews() }
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach { it.isEnabled = bool }
    }

    override fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        return super.onRestart(true) {
            resetViews()
        }
    }
}
