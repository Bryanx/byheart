package com.example.byheart.rehearsal

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.byheart.R
import com.example.byheart.shared.*
import kotlinx.android.synthetic.main.content_rehearsal_typed.*


class RehearsalTypedFragment : RehearsalFragment() {

    private var enterCounter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal_typed)
        super.onCreateView(inflater, container, savedInstanceState)
        return layout
    }

    override fun addEventHandlers() {
        etRehearsalInput.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                    if (etRehearsalInput.text.isEmpty() || enterCounter > 1) return false
                    else if (enterCounter == 1) {
                        enterCounter += 1
                        skipWaitingGoToNextQuestion()
                        return false
                    } else if (enterCounter == 0) {
                        enterCounter += 1
                        onEnter()
                        return true
                    }
                }
                return false
            }
        })
        btnGo.setOnClickListener {
            when {
                etRehearsalInput.text.isEmpty() || enterCounter > 1 -> return@setOnClickListener
                (it as Button).string.isEmpty() -> skipWaitingGoToNextQuestion()
                else -> onEnter()
            }
        }
    }

    private fun onEnter() {
        btnGo.text = ""
        btnGo.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_forward_white_24dp, 0, 0, 0)
        flipCard()
        if (etRehearsalInput.string.equalsIgnoreCase(cardBack.string)) {
            correctSound.start()
            context?.let { etRehearsalInput.setLineColor(it, R.color.green) }
            handler.postDelayed({ nextQuestionWithButtons() }, 1000)
        } else {
            wrongSound.start()
            context?.let { etRehearsalInput.setLineColor(it, R.color.red) }
            handler.postDelayed({ nextQuestionWithButtons() }, 5000)
        }
        if (Preferences.read(Preferences.REHEARSAL_PRONOUNCE)) pronounceAnswer()
    }

    private fun skipWaitingGoToNextQuestion() {
        buttonsAreEnabled(false)
        handler.removeMessages(0)
        nextQuestionWithButtons()
    }

    private fun nextQuestionWithButtons() {
        nextQuestion {
            btnGo.text = "GO"
            btnGo.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0)
            etRehearsalInput.text = null
            context?.let { etRehearsalInput.setLineColor(it, R.color.colorPrimary) }
            enterCounter = 0
            buttonsAreEnabled(true)
        }
    }

    private fun buttonsAreEnabled(bool: Boolean) {
        btnGo.isEnabled = bool
    }

    override fun onDestroyView() {
        activity.hideKeyboard()
        super.onDestroyView()
    }
}
