package com.example.byheart.card.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.shared.startFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CardEditFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var layout: View
    private lateinit var question: TextInputEditText
    private lateinit var questionLayout: TextInputLayout
    private lateinit var answer: TextInputEditText
    private lateinit var answerLayout: TextInputLayout
    private lateinit var btnSave: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card_edit, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        findViews()
        addEventHandlers()
        (activity as MainActivity).closeDrawer()
        (activity as MainActivity).setToolbarTitle("Add card")
        return layout
    }

    private fun addEventHandlers() {
        btnSave.setOnClickListener {
            val q = question.text.toString()
            val a = answer.text.toString()
            if (q.isEmpty()) questionLayout.error = "You need to enter a question"
            if (a.isEmpty()) answerLayout.error = "You need to enter an answer"
            if (q.isNotEmpty() && a.isNotEmpty()) {
                question.clearFocus()
                answer.clearFocus()
                cardViewModel.insert(Card(q, a, (activity as MainActivity).pileId.toLong()))
                fragmentManager?.startFragment(CardFragment())
            }
        }
    }

    private fun findViews() {
        question = layout.findViewById(R.id.card_edit_question)
        questionLayout = layout.findViewById(R.id.card_edit_text_input_question)
        answer = layout.findViewById(R.id.card_edit_answer)
        answerLayout = layout.findViewById(R.id.card_edit_text_input_answer)
        btnSave = layout.findViewById(R.id.card_edit_btn_save)
    }
}