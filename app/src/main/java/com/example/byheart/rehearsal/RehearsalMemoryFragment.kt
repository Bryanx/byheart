package com.example.byheart.rehearsal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.byheart.R
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.REHEARSAL_PRONOUNCE
import com.example.byheart.shared.inflate
import kotlinx.android.synthetic.main.content_rehearsal_memory.*


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
                if (Preferences.read(REHEARSAL_PRONOUNCE)) pronounceAnswer()
                btnContinue.visibility = VISIBLE
                flipCard()
                handler.postDelayed({ nextQuestionWithButtons() }, 5000)
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
