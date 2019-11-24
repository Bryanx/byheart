package nl.bryanderidder.byheart.rehearsal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_rehearsal_multiple_choice.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.rehearsal.views.MultipleChoiceAnswer
import nl.bryanderidder.byheart.shared.*


/**
 * Fragment that contains the rehearsal multiple choice mode.
 * Each time a card appears, 3 answers are randomly added to the buttons and 1 correct is answered
 * in a random position as well.
 * @author Bryan de Ridder
 */
class RehearsalMultipleChoiceFragment : RehearsalFragment() {

    private var falseAnswer: Boolean = false
    private lateinit var buttons: MutableList<MultipleChoiceAnswer>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal_multiple_choice)
        super.onCreateView(inflater, container, savedInstanceState)
        return layout
    }

    override fun doAfterGetData() {
        if (this::buttons.isInitialized) return
        buttons = mutableListOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4)
        resetViews()
        addEventHandlers()
    }

    private fun resetViews() {
        falseAnswer = false
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

    private fun addAnswerToButton(i: Int, btn: MultipleChoiceAnswer, tempCards: List<Card>) {
        if (Preferences.REHEARSAL_REVERSE) btn.text = tempCards[i].question!!
        else btn.text = tempCards[i].answer!!
    }

    private fun resetButtons() {
        buttonsAreEnabled(true)
        buttons.forEach {
            it.textColor = context?.getAttr(R.attr.mainTextColor)!!
            it.bgColor = context?.getAttr(R.attr.mainBackgroundColorLighter)!!
        }
    }

    override fun addEventHandlers() {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach { btn: MultipleChoiceAnswer ->
            btn.setOnClickListener {
                if (btn.text.equalsIgnoreCase(rehearsalCard.backText!!)) {
                    buttonsAreEnabled(false)
                    super.onCorrect()
                    if (Preferences.REHEARSAL_PRONOUNCE) rehearsalCard.sayBackCard()
                    btn.textColor = context!!.color(R.color.white)
                    btn.bgColor = context!!.getAttr(R.attr.colorGreen)
                    rehearsalCard.turnToBack()
                    handler.postDelayed({ nextQuestionWithButtons() }, resources.getInteger(R.integer.rehearsal_correct_duration).toLong())
                } else {
                    if (!falseAnswer) super.onFalse().also { falseAnswer = true }
                    else wrongSound.start() // don't count each consecutive wrong answer
                    btn.textColor = context!!.color(R.color.white)
                    btn.bgColor = context!!.color(R.color.red)
                    btn.isEnabled = false
                }
            }
        }
    }

    private fun nextQuestionWithButtons() = nextQuestion { resetViews() }

    private fun buttonsAreEnabled(bool: Boolean) {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach { it?.let { it.isEnabled = bool } }
    }

    override fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        return super.onRestart(true) {
            resetViews()
        }
    }
}
