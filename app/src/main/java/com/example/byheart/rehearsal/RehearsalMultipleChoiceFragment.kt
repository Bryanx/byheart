package com.example.byheart.rehearsal

import android.content.res.ColorStateList
import android.os.Build
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


class RehearsalMultipleChoiceFragment : RehearsalFragment() {

    private lateinit var buttons: MutableList<Button>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal_multiple_choice)
        super.onCreateView(inflater, container, savedInstanceState)
        return layout
    }

    override fun doAfterGetData() {
        buttons = mutableListOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4)
        fillButtons()
        addEventHandlers()
    }

    private fun fillButtons() {
        val tempCards = cards.toMutableList()
        tempCards.removeAt(cardIndex) // remove answer from custom list
        tempCards.shuffle()
        val tempButtons = buttons.toMutableList()
        val poppedBtn = tempButtons.removeAt((0..3).random()) // remove random button
        addAnswerToButton(cardIndex, poppedBtn, cards) // add correct answer
        tempButtons.forEachIndexed { i, btn ->
            addAnswerToButton(i, btn, tempCards) // add random answers
        }
    }

    private fun addAnswerToButton(i: Int, btn: Button, tempCards: List<Card>) {
        if (Preferences.read(REHEARSAL_REVERSE)) btn.text = tempCards[i].question
        else btn.text = tempCards[i].answer
    }

    override fun addEventHandlers() {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach { btn: Button ->
            btn.setOnClickListener {
                if (btn.string.equalsIgnoreCase(cardBack.string)) {
                    buttonsAreEnabled(false)
                    correctSound.start()
                    if (Preferences.read(Preferences.REHEARSAL_PRONOUNCE)) pronounceAnswer()
                    btn.setButtonColors(R.color.green_600, R.color.white)
                    flipCard()
                    handler.postDelayed({ nextQuestionWithButtons() }, 5000)
                } else {
                    wrongSound.start()
                    btn.setButtonColors(R.color.red, R.color.white)
                    //TODO: second try? unlimited tries?
                }
            }
        }
    }

    private fun nextQuestionWithButtons() {
        nextQuestion {
            buttonsAreEnabled(true)
            fillButtons()
            buttons.forEach {
                it.setButtonColorsRaw(
                    context?.getAttr(R.attr.mainBackgroundColorLighter)!!,
                    context?.getAttr(R.attr.mainTextColor)!!
                )
            }
        }
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        listOf(btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4).forEach { it.isEnabled = bool }
    }

    private fun Button.setButtonColors(backgroundColor: Int, textColor: Int) {
        this.setBackgroundTint(backgroundColor)
        this.setTxtColor(textColor)
    }

    private fun Button.setButtonColorsRaw(backgroundColor: Int, textColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.backgroundTintList = ColorStateList.valueOf(backgroundColor)
        }
        this.setTextColor(textColor)
    }
}
